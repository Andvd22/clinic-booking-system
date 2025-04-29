package com.mediclinic.appointment_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// disable security
// @SpringBootApplication(exclude = {
// org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })
@EnableAsync
public class AppointmentSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentSchedulerApplication.class, args);
		System.out.println("hello Do Van An");
	}

}
