package kriuchkov.maksim.lesson4.security;

import kriuchkov.maksim.lesson4.persist.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    public void authConfigure(AuthenticationManagerBuilder auth,
                              UserDetailsService userDetailsService,
                              PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);

        auth.inMemoryAuthentication()
                .withUser("mem_user")
                .password("$2y$12$akMK9tlTR3uo/l4XUzQJieZQNVRO4M8GjPxWNl4j5yBDOkM97ccj2")
                .roles("ADMIN");
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return new UserAuthService(repository);
    }

    @Configuration
    public static class UIWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                        .antMatchers("/").anonymous()
                        .antMatchers("/product/**").hasRole("ADMIN")
                        .antMatchers("/product/**").hasRole("MANAGER")
                        .antMatchers("/user/**").hasRole("ADMIN")
                    .and()
                    .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/authUser");
        }
    }

}
