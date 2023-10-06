package com.gfinance.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
// security configuration for application that secures the application using spring security dependency
@Configuration
public class SecurityConfig {

    // method that tells application where to find user login details in database
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        theUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from user where username=?");
        theUserDetailsManager.setAuthoritiesByUsernameQuery("select username, rolename FROM role as r JOIN users_roles as ur ON r.id=ur.role_id JOIN user as u ON ur.user_id=u.id WHERE username=?");
        return  theUserDetailsManager;
    }

    // method that facilitates bycrypt encryption between database and application layer
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // method that secures desired endpoints and login and logout logic.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers("/registration/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/gfinance/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/gfinance/main", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/showMyLoginPage")
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
        return http.build();
    }
}
