package com.billing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="customer",uniqueConstraints= {@UniqueConstraint(columnNames= {"customergst"})})

public class CustomerBean {
	
	@Column(name="customername")
	private String customerName;
	@Id
	@Column(name="customergst")
	private String customerGST;
	@Column(name="customeraddress")
	private String CustomerAddress;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerGST() {
		return customerGST;
	}
	public void setCustomerGST(String customerGST) {
		this.customerGST = customerGST;
	}
	public String getCustomerAddress() {
		return CustomerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		CustomerAddress = customerAddress;
	}	
	

}
