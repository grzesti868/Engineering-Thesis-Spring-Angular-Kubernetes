/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Security;

import com.App.Commerce.Configs.JwtConfigProperties;
import com.App.Commerce.Filter.CustomAlgorithmImpl;
import com.App.Commerce.Filter.CustomAuthenticationFilter;
import com.App.Commerce.Filter.CustomAuthorizationFilter;
import com.App.Commerce.Models.AppUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAlgorithmImpl customAlgorithm;
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), customAlgorithm, jwtConfigProperties);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers("/api/login/**", "/api/users/token/refresh/**").permitAll()
                .and()
                    .authorizeRequests()
                        .antMatchers(GET, "/api/users/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                        .antMatchers(POST, "/api/users/add/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                        .antMatchers(POST, "/api/users/role/**").hasAnyAuthority("ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .addFilter(customAuthenticationFilter)
                    .addFilterBefore(new CustomAuthorizationFilter(customAlgorithm, jwtConfigProperties), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
