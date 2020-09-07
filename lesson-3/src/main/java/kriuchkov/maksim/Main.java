package kriuchkov.maksim;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        EntityManager em = emFactory.createEntityManager();
//
//        Customer customer1 = new Customer(null, "customer1");
//        Customer customer2 = new Customer(null, "customer2");
//        Product product1 = new Product(null, "product1", new BigDecimal("999.99"));
//        Product product2 = new Product(null, "product2", new BigDecimal("99.99"));
//        Product product3 = new Product(null, "product3", new BigDecimal("0.99"));
//
//        List<Product> products1 = new ArrayList<>();
//        products1.add(product1);
//        products1.add(product2);
//        customer1.setProducts(products1);
//
//        List<Product> products2 = new ArrayList<>();
//        products2.add(product2);
//        products2.add(product3);
//        customer2.setProducts(products2);
//
//        em.getTransaction().begin();
//        em.persist(customer1);
//        em.persist(customer2);
//        em.persist(product1);
//        em.persist(product2);
//        em.persist(product3);
//        em.getTransaction().commit();

        Customer customer3 = new Customer(null, "customer3");
        em.getTransaction().begin();
        em.persist(customer3);
        em.getTransaction().commit();


        Scanner scanner = new Scanner(System.in);
        String line;
        String inputPrompt = "Enter number to choose: \n" +
                "\t1: Show all customers \n" +
                "\t2: Show all products \n" +
                "\t3: Show products bought by a customer \n" +
                "\t4: Show all customers that bought a product \n" +
                "\t5: Delete customer \n" +
                "\t6: Delete product \n" +
                "\tEmpty line to exit \n" +
                "Your choice? -> ";
        System.out.print(inputPrompt);
        while (!(line = scanner.nextLine()).isEmpty()) {
            int choice = Integer.parseInt(line);
            switch (choice) {
                case 1: // вывести всех клиентов на экран
                {
                    List<Customer> customerList = em.createQuery("from Customer", Customer.class).getResultList();
                    for (Customer c : customerList) {
                        System.out.println("Customer " + c.getId());
                        System.out.println("\tName: " + c.getName());
                    }
                    break;
                }
                case 2: // вывести все товары на экран
                {
                    List<Product> productList = em.createQuery("from Product ", Product.class).getResultList();
                    for (Product p : productList) {
                        printProductInfo(p);
                    }
                    break;
                }
                case 3: // какие товары купил клиент
                {
                    System.out.print("Customer id? -> ");
                    long customerId = Long.parseLong(scanner.nextLine());
                    Customer customer = em.find(Customer.class, customerId);
                    if (customer == null) {
                        System.out.println("No such customer with id " + customerId + "!");
                        break;
                    }
                    printCustomerInfo(customer);
                    System.out.println("Bought " + customer.getProducts().size() + " product(s):");
                    for (Product p : customer.getProducts()) {
                        printProductInfo(p);
                    }
                    break;
                }
                case 4: // какие клиенты купили товар
                {
                    System.out.print("Product id? -> ");
                    long productId = Long.parseLong(scanner.nextLine());
                    Product product = em.find(Product.class, productId);
                    if (product == null) {
                        System.out.println("No such product with id " + productId + "!");
                        break;
                    }
                    printProductInfo(product);
                    System.out.println("Bought by " + product.getCustomers().size() + " customer(s):");
                    for (Customer c : product.getCustomers()) {
                        printCustomerInfo(c);
                    }
                    break;
                }
                case 5: // удалить клиента
                {
                    System.out.print("Customer id? -> ");
                    long customerId = Long.parseLong(scanner.nextLine());
                    Customer customer = em.find(Customer.class, customerId);
                    if (customer == null) {
                        System.out.println("No such customer with id " + customerId + "!");
                        break;
                    }
                    em.getTransaction().begin();
                    em.remove(customer);
                    em.getTransaction().commit();
                    printCustomerInfo(customer);
                    System.out.println("Customer deleted.");
                    break;
                }
                case 6: // удалить товар
                {
                    System.out.print("Product id? -> ");
                    long productId = Long.parseLong(scanner.nextLine());
                    Product product = em.find(Product.class, productId);
                    if (product == null) {
                        System.out.println("No such product with id " + productId + "!");
                        break;
                    }
                    em.getTransaction().begin();
                    em.remove(product);
                    em.getTransaction().commit();
                    printProductInfo(product);
                    System.out.println("Product deleted.");
                }
            }
            System.out.println();
            System.out.print(inputPrompt);
        }
    }

    private static void printProductInfo(Product p) {
        System.out.println("Product " + p.getId());
        System.out.println("\tName: " + p.getName());
        System.out.println("\tPrice: " + p.getPrice());
    }

    private static void printCustomerInfo(Customer c) {
        System.out.println("Customer " + c.getId());
        System.out.println("\tName: " + c.getName());
    }
}
