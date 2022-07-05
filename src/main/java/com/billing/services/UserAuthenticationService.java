package com.billing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
	
	 @Autowired
	 PasswordServices passwordServices;
	
	public String UserName="Admin";
	
	public boolean authenticateUser(String userName, String password) throws Exception {
		
		if(userName.equals(UserName) && password.equals(passwordServices.getPassword().getPassword())) {
			return true;
		}
		return false;
	}

}
