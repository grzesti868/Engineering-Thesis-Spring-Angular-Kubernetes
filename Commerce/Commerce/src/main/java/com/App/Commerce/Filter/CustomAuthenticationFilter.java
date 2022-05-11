/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Filter;

import com.App.Commerce.Configs.JwtConfigProperties;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Credentials.Credentials;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final CustomAlgorithmImpl customAlgorithm;
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication started");
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        Credentials credentials;
        String username;
        String password;
        log.debug("user-agent"+request.getHeader("user-agent"));
        log.debug("Content-Type"+request.getHeader("Content-Type"));
        log.debug("Access-Control-Allow-Origin"+request.getHeader("Access-Control-Allow-Origin"));
        log.debug("Access-Control-Allow-Headers"+request.getHeader("Access-Control-Allow-Headers"));
        log.debug("Access-Control-Allow-Methods"+request.getHeader("GET,POST,OPTIONS,DELETE,PUT"));
        try {
            json = request.getReader().lines().collect(Collectors.joining());
            log.debug("JSON: " + json);
            credentials = objectMapper.readValue(json, Credentials.class);

            username=credentials.getUsername();
            password=credentials.getPassword();
            log.info("User is: {}, ", username);
        } catch (IOException e) {
            throw new ApiRequestException("Provided credentials are invalid!");
        }
        log.info("User is: {}, ", username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = customAlgorithm.getAlgorith();
        String claim = jwtConfigProperties.getClaimName();
        Integer refreshTokenExp = jwtConfigProperties.getRefreshTokenExpiration();
        Integer accessTokenExp = jwtConfigProperties.getAccessTokenExpiration();

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExp))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(claim, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExp))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        /*RETURN VALUES IN HEADER
        response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);*/
        //RETURN AS A BODY
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
