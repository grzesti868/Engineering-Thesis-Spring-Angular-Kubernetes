package com.App.Commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO: dodac public getAge do person Period.between(birth, now).getYears();
//TODO: w userze email nie moze juz istnieć (dodac w service wymóg)
//TODO: czy dodac RESTMODEL czy olac? (raczej olac)
//TODO: put mappping email nie moze juz byc zajety
//TODO: put mapping username zeby nie byl zajety/ten sam(!Object.equals(user.getName(),newName))  && username > 0

@SpringBootApplication
public class CommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}
}
