package com.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.billing.mains.ViewSceneChanger;

import javafx.application.Application;
import javafx.stage.Stage;


public class JavaFxApplication extends Application {

	private ConfigurableApplicationContext applicationContext;
	
	@Autowired
	private ViewSceneChanger viewSceneChanger;
	
	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(new String[0]);
		this.applicationContext = new SpringApplicationBuilder().sources(SkvBillingApplication.class).run(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		viewSceneChanger=applicationContext.getBean(ViewSceneChanger.class);
		viewSceneChanger.initialSceneLoader(this.applicationContext, primaryStage);		
	}

}
