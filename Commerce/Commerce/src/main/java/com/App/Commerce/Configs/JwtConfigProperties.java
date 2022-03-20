/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfigProperties {
    private String prefix;
    private String claimName;
    private Integer accessTokenExpiration;
    private Integer refreshTokenExpiration;
}