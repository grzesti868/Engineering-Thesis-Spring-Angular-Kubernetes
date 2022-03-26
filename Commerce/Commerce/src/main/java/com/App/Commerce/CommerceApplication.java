package com.App.Commerce;

import com.App.Commerce.Configs.JwtConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//TODO: uzywac hhtpsecurity ale sprawdzic czymu preauthorize nie dziala (przepuszcza wszystko)
//TODO: poczyscic kod
//TODO: ogarnac endpointy
//TODO: przechwycic wyjatek jak bledne dane przez controller (default: 400 + err msg)?
//TODO: ogarnac logi (dodac + zmienic na debug)
@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)
public class CommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}
}
