package com.billing.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.billing.beans.BillItemBean;
import com.billing.beans.CompanyDetailsBean;
import com.billing.beans.CustomerBean;
import com.billing.beans.FinancialYearBean;
import com.billing.beans.InvoiceBean;
import com.billing.beans.ItemBean;
import com.billing.beans.PasswordBean;
import com.billing.beans.PurchaseBean;
import com.billing.beans.UserBean;
import com.billing.mains.ViewSceneChanger;

@Configuration
@EnableJpaRepositories
public class ApplicationConfiguration {
	
	@Bean
	public ViewSceneChanger viewSceneChanger() {
		return new ViewSceneChanger();
	}
	
	@Bean
	public CustomerBean customerBean() {
		return new CustomerBean();
	}
	
	@Bean
	public ItemBean itemBean() {
		return new ItemBean();
	}
	
	@Bean
	public BillItemBean billItemBean() {
		return new BillItemBean();
	}
	
	@Bean
	public InvoiceBean invoiceBean() {
		return new InvoiceBean();
	}
	
	@Bean
	public FinancialYearBean financialYearBean() {
		return new FinancialYearBean();
	}
	
	@Bean
	public CompanyDetailsBean companyBean() {
		return new CompanyDetailsBean();
	}
	
	@Bean
	public UserBean userBean() {
		return new UserBean();
	}
	
	@Bean
	public PurchaseBean purchaseBean() {
		return new PurchaseBean();
	}
	
	@Bean
	public PasswordBean passwordBean() {
		return new PasswordBean();
	}
	
}
