/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Filter;

import com.App.Commerce.Configs.JwtConfigProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final CustomAlgorithmImpl customAlgorithm;
    private final JwtConfigProperties jwtConfigProperties;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request, response);
        } else {
            String prefix=jwtConfigProperties.getPrefix();
            String prefix2="Bearer ";
            String claim = jwtConfigProperties.getClaimName();
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            System.out.println("*** CustomAuthorizationFilter ***");
            System.out.println("Prefix: "+prefix);
            System.out.println("claim: "+claim);
            System.out.println("authorizationHeader: "+authorizationHeader);
            if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
                try {
                    String token = authorizationHeader.substring(prefix.length());
                    Algorithm algorithm = customAlgorithm.getAlgorith();
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    //todo: roles to app.prop
                    String[] roles = decodedJWT.getClaim(claim).asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception err) {
                    log.error("Error with token access authorization:" + err.getMessage());
                    response.setHeader("error", err.getMessage());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", err.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(FORBIDDEN.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
