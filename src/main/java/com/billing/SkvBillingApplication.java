package com.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javafx.application.Application;



@SpringBootApplication
@EnableJpaRepositories("com.billing.repositories")
public class SkvBillingApplication {

	public static final Logger logger = LoggerFactory.getLogger(SkvBillingApplication.class);
	 
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		Application.launch(JavaFxApplication.class,args);
	}

}
