package com.App.Commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//TODO: dodac public getAge do person Period.between(birth, now).getYears();
//TODO: w userze email nie moze juz istnieć (dodac w service wymóg)
//TODO: put mappping email nie moze juz byc zajety
//TODO: put mapping username zeby nie byl zajety/ten sam(!Object.equals(user.getName(),newName))  && username > 0
//TODO: co to jest to transactional
//TODO: save role z app user to Role pakietu/
//TODO: haslo dla super admina dodac w app.properties i w config je wywolac (dodac jako pole)
@SpringBootApplication
public class CommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}
}
