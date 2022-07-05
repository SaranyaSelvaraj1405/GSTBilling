package com.billing.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.CompanyDetailsBean;
import com.billing.repositories.CompanyDetailsRepository;

@Service
public class CompanyDetailsServices {
	
	@Autowired
	CompanyDetailsRepository companyDetailsRepository;

	public void saveCompanyDetails(CompanyDetailsBean companyDetailsBean) throws Exception {
		companyDetailsRepository.deleteAll();
		companyDetailsRepository.save(companyDetailsBean);
	}
	
	public CompanyDetailsBean getCompanyDetails() throws Exception {
		List<CompanyDetailsBean> list = companyDetailsRepository.findAll();
		return list.get(0);
	}
	
	public boolean isCompanyDetailsEntered() throws Exception {
		if(companyDetailsRepository.count()>0) {
			return true;
		}
		return false;
	}
	
}
