package com.study.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}

}
