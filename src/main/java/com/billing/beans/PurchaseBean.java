package com.billing.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(PurchaseIdentifier.class)
@Table(name="purchase")
public class PurchaseBean {
	
	@Column(name= "serialnumber")
	@Id
	private int SerialNumber;
	@Column(name= "financialYear")
	@Id
	private String FinancialYear;
	@Column(name="purchaseinvoicenumber")
	private String PurchaseInvoiceNumber;
	@Column(name="purchaseinvoicedate")
	private LocalDate PurchaseInvoiceDate;
	@Column(name="purchasecompanyname")
	private String PurchaseCompanyName;
	@Column(name="purchaseitemdescription")
	private String PurchaseItemDescription;
	@Column(name="purchasegrandtotal")
	private Double PurchaseGrandTotal;
	
	public int getSerialNumber() {
		return SerialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		SerialNumber = serialNumber;
	}
	public String getFinancialYear() {
		return FinancialYear;
	}
	public void setFinancialYear(String financialYear) {
		FinancialYear = financialYear;
	}
	public String getPurchaseInvoiceNumber() {
		return PurchaseInvoiceNumber;
	}
	public void setPurchaseInvoiceNumber(String purchaseInvoiceNumber) {
		PurchaseInvoiceNumber = purchaseInvoiceNumber;
	}
	public LocalDate getPurchaseInvoiceDate() {
		return PurchaseInvoiceDate;
	}
	public void setPurchaseInvoiceDate(LocalDate purchaseInvoiceDate) {
		PurchaseInvoiceDate = purchaseInvoiceDate;
	}
	public String getPurchaseCompanyName() {
		return PurchaseCompanyName;
	}
	public void setPurchaseCompanyName(String purchaseCompanyName) {
		PurchaseCompanyName = purchaseCompanyName;
	}
	public String getPurchaseItemDescription() {
		return PurchaseItemDescription;
	}
	public void setPurchaseItemDescription(String purchaseItemDescription) {
		PurchaseItemDescription = purchaseItemDescription;
	}
	public Double getPurchaseGrandTotal() {
		return PurchaseGrandTotal;
	}
	public void setPurchaseGrandTotal(Double purchaseGrandTotal) {
		PurchaseGrandTotal = purchaseGrandTotal;
	}

}
