package com.jark.TMS.config;

import com.jark.TMS.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    //@Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/about", "/", "/registration").permitAll()
                        .requestMatchers("/tasks/**","/reports/**", "/api", "/sendEmail").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin/**","/teams/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .permitAll());
        logger.info("This is an informational message.");
        logger.error("This is an error message with an exception.");
        return httpSecurity.build();


    }
//4 debug
   /* @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                        .username("user")
                        .passwordEncoder(passwordEncoder::encode)
                        .password("u")
                        .roles("USER")
                        .build();
        UserDetails admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("a"))
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user, admin);
    }*/


}