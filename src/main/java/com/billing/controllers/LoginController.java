package com.billing.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import com.billing.SkvBillingApplication;
import com.billing.beans.PasswordBean;
import com.billing.mains.ViewSceneChanger;
import com.billing.services.PasswordServices;
import com.billing.services.UserAuthenticationService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;

@Controller
@FxmlView("/fxmlPages/Login.fxml")
public class LoginController implements Initializable{

  @FXML
  private Button LoginButton;
  @FXML
  private TextField UserNameTextField;
  @FXML
  private PasswordField PasswordPasswordField;
  @FXML
  private Text MessageText;
  
  @Autowired
  ViewSceneChanger viewSceneChanger;
  @Autowired
  UserAuthenticationService userAuthenticationService;
  
  @Autowired
	private PasswordServices passwordServices;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private	PasswordBean passwordBean;
	
	private DateTimeFormatter dateTimeFormatterForFile = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss");

	
	
	  
	  public void loginCheckEvent(ActionEvent actionEvent) {
		 
		  Stage stage = (Stage) LoginButton.getScene().getWindow();
		 
		  try {
			  
			  	  
		 if(userAuthenticationService.authenticateUser(this.UserNameTextField.getText(), this.PasswordPasswordField.getText()))
		  {
			  viewSceneChanger.successfullLoginSceneChanger(stage);
		  }
		  else
		  {
			 MessageText.setText("Login Failed!\nInvalid Credentials");
		  }
		 
		  }catch(Exception e) {
			  SkvBillingApplication.logger.error("Exception During Credentials Validation", e);
			  e.printStackTrace();
			  log(e);
		  }
		  
	  }



	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			if(!passwordServices.isPasswordSaved()) {
				 Resource resource = resourceLoader.getResource("classpath:/Auth/Authentication.txt");
				    InputStream inputStream = resource.getInputStream();
				    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				    String password=bufferReader.readLine();
				    passwordBean.setPassword(password);
				    passwordServices.savePassword(passwordBean);
				    bufferReader.close();
				    inputStream.close();
			}
		} catch (IOException e) {
			SkvBillingApplication.logger.error("Exception During Credentials Validation", e);
			log(e);
			e.printStackTrace();
		} catch (Exception e) {
			SkvBillingApplication.logger.error("Exception During Credentials Validation", e);
			log(e);
			e.printStackTrace();
		}
		
		UserNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PasswordPasswordField.requestFocus();
				}
			}
		});
		PasswordPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					LoginButton.requestFocus();
				}
			}
		});
		
	}

	private void  log(Exception e) {
		LocalDateTime localDateTime = LocalDateTime.now();
		String filename="D:\\Invoice\\Logs\\"+dateTimeFormatterForFile.format(localDateTime)+".txt";
		System.out.println(filename);
		try (Writer fileWriter = new FileWriter(new File(filename))){
			PrintWriter printWriter = new PrintWriter(fileWriter);
	      
	      e.printStackTrace(printWriter);
	      
	      printWriter.close();
		} catch (IOException e2) {
			exceptionAlert("Logging Failed");
			e2.printStackTrace();
		}
	}
	
	public void exceptionAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Occured");
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/Stylesheets/DialogBox.css").toExternalForm());
		dialogPane.getStyleClass().add("dialogBox");
		dialogPane.applyCss();
		alert.showAndWait();
	}

}




