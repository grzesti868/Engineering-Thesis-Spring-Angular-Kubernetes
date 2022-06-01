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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

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
        //CorsConfigurationSource corsConfigurationSource = corsConfigurationSource();
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http
                    .cors()
                .and()
                    .csrf().disable()
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
                        .antMatchers(GET, "/api/products/**").hasAnyAuthority("ROLE_CUSTOMER","ROLE_MANAGER", "ROLE_ADMIN")
                .and()
                     .authorizeRequests()
                        .antMatchers(POST, "/api/products/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                        .antMatchers(PUT, "/api/products/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
                .and()
                    .authorizeRequests()
                        .antMatchers(DELETE, "/api/products/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN")
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


    @Bean("corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
