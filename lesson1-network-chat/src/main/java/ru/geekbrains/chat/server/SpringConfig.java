package ru.geekbrains.chat.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class SpringConfig {

    @Bean
    public Server chatServer(DatabaseHandler dbHandler) {
        return new Server(dbHandler);
    }

    @Bean
    public DatabaseHandler dbHandler(DataSource dataSource) throws SQLException {
        return new DatabaseHandler(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dmds.setUsername("chat-server");
        dmds.setPassword("XgVbEF4vTzP!R");
        dmds.setUrl("jdbc:mysql://localhost/chat_users?serverTimezone=Europe/Moscow");
        return dmds;
    }

}
