package com.billing.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.PasswordBean;
import com.billing.repositories.PasswordRepository;

@Service
public class PasswordServices {
	
	@Autowired
	PasswordRepository passwordRepository;
	
	public void savePassword(PasswordBean passwordBean) {
		passwordRepository.deleteAll();
		passwordRepository.save(passwordBean);
	}
	
	public boolean changePassword(PasswordBean passwordBean) throws Exception {
		try {
		passwordRepository.deleteAll();
		passwordRepository.save(passwordBean);
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public PasswordBean getPassword() throws Exception {
		List<PasswordBean> list = passwordRepository.findAll();
		return list.get(0);
	}
	
	public boolean isPasswordSaved() throws Exception {
		if(passwordRepository.count()>0) {
			return true;
		}
		return false;
	}
}
