package com.App.Commerce.Security;

/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

import com.App.Commerce.Filter.CustomAuthenticationFilter;
import com.App.Commerce.Models.AppUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers("/login").permitAll()
                .and()
                    .authorizeRequests()
                        .anyRequest().authenticated()
                .and()
                    .authorizeRequests()
                        .antMatchers(GET, "/api/v1/users/**").hasAnyAuthority("ROLE_USER")
                .and()
                    .authorizeRequests()
                        .antMatchers(GET, "/api/v1/users/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                        .antMatchers(POST, "/api/v1/users/save/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                    .addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
