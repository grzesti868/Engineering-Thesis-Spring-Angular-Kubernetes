/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Filter;

import com.App.Commerce.Configs.JwtConfigProperties;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
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
       String username = request.getParameter("username");
       String password = request.getParameter("password");
       log.info("User is: {}, ",username);
       log.info("Password is: {}, ",password);
       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
       //todo: 1:08:08 oraz 1:23:10 oraz 1:25:00 (uzyc Mapper by sparsowac json object z body, request) wziac z request body i zmpaowac zamiast z repsonse?
       return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = customAlgorithm.getAlgorith() ;
        String prefix=jwtConfigProperties.getPrefix();
        String claim = jwtConfigProperties.getClaimName();
        Integer refreshTokenExp = jwtConfigProperties.getRefreshTokenExpiration();
        Integer accessTokenExp = jwtConfigProperties.getAccessTokenExpiration();

        System.out.println("*** CustomAuthenticationFilter ***");
        System.out.println("Prefix: "+prefix);
        System.out.println("claim: "+claim);
        System.out.println("refreshTokenExp: "+refreshTokenExp);
        System.out.println("accessTokenExp: "+accessTokenExp);
    //TODO: access i refresh sie duzo duplikuje w klasach -> refaktor do osobnej klasy
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                //todo: czas tokena wrzucic do app.prop
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExp ))
                .withIssuer(request.getRequestURL().toString())
                //todo: roles to app.prop
                .withClaim(claim, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                //todo: czas tokena wrzucic do app.prop
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExp ))
                .withIssuer(request.getRequestURL().toString())
                //todo: "roles" to app.prop
                //.withClaim(claim, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        /* TODO: RETURN VALUES IN HEADER
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
