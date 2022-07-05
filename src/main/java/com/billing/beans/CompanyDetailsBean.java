package com.billing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="company")
public class CompanyDetailsBean {
	@Id
	@Column(name="companyname")
	private String CompanyName;
	@Column(name="companygst")
	private String CompanyGST;
	@Column(name="companyaddress")
	private String CompanyAddress;
	@Column(name="companybankdetails")
	private String CompanyBankDetails;
	@Column(name="declaration")
	private String Declaration;
	
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getCompanyGST() {
		return CompanyGST;
	}
	public void setCompanyGST(String companyGST) {
		CompanyGST = companyGST;
	}
	public String getCompanyAddress() {
		return CompanyAddress;
	}
	public void setCompanyAddress(String companyDetails) {
		CompanyAddress = companyDetails;
	}
	public String getCompanyBankDetails() {
		return CompanyBankDetails;
	}
	public void setCompanyBankDetails(String companyBankDetails) {
		CompanyBankDetails = companyBankDetails;
	}
	public String getDeclaration() {
		return Declaration;
	}
	public void setDeclaration(String declaration) {
		Declaration = declaration;
	}

}
