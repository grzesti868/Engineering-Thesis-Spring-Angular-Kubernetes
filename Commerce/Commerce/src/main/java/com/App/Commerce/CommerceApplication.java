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
//todo: powinien usunac adres ale nie person; ogarnac casade typ
//todo: impl debug logów jak w product service
//todo: test na dodawanie duplikatow itp
//todo: zamiast get all niech zwraca lista x produktow (stronnicowawnie)
//todo: dodac wiecej rodzajow errorow?
//todo: zwiekszania/zmniejszenie liczby prodkutu w koszyku zimplementowac w front-end
//todo: impl interceptory???
//todo: make all ids unique (np order i order details moga miec takie samo id!)
@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)
public class CommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}
}
