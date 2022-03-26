/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Filter;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class CustomAlgorithmImpl {

    private static final String SECRET = "My secret";


     public Algorithm getAlgorith(){
         return Algorithm.HMAC256(SECRET.getBytes());
     }
}
