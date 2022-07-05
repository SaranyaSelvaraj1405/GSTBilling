package com.billing.mains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import com.billing.SkvBillingApplication;
import com.billing.controllers.LoginController;
import com.billing.controllers.MainPageController;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.rgielen.fxweaver.core.FxWeaver;

public class ViewSceneChanger {
	
	private static ConfigurableApplicationContext staticApplicationContext;
	private double xOffset = 0;
	private double yOffset = 0;
	@Autowired
	private MainPageController mainPageController;
	
	
	public void initialSceneLoader(ConfigurableApplicationContext applicationContext,Stage primaryStage) {
		
		SkvBillingApplication.logger.info("Loading Login Scene");
		staticApplicationContext=applicationContext;
		FxWeaver fxWeaver= applicationContext.getBean(FxWeaver.class);
		Parent root = (Parent)fxWeaver.loadView(LoginController.class);
		Scene scene = new Scene(root,1280,680);
		
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
		      @Override
		      public void handle(MouseEvent event) {
		        xOffset = event.getSceneX();
		        yOffset = event.getSceneY();
		      }
		    });
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
		      @Override
		      public void handle(MouseEvent event) {
		        primaryStage.setX(event.getScreenX() - xOffset);
		        primaryStage.setY(event.getScreenY() - yOffset);
		      }
		    });
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			primaryStage.setWidth((double)newVal);
		});

		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			primaryStage.setHeight((double)newVal);
		});
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		primaryStage.show();
		
	}
	
	public void successfullLoginSceneChanger(Stage stage) {
		
		FxWeaver fxWeaver= staticApplicationContext.getBean(FxWeaver.class);
		
		Parent root = (Parent)fxWeaver.loadView(MainPageController.class);
		stage.setResizable(true);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent e) {
			    	mainPageController.exit();
			     Platform.exit();
			     System.exit(0);
			    }
			  });
		
		Scene scene = new Scene(root,1280,680);
		stage.setScene(scene);
		
		stage.setMaximized(true);
		stage.show();
		
	}
	
	
	public void successfullLogoutSceneChanger(Stage stage) {
		FxWeaver fxWeaver= staticApplicationContext.getBean(FxWeaver.class);
		Parent root = (Parent)fxWeaver.loadView(LoginController.class);
		Scene scene = new Scene(root);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

}
