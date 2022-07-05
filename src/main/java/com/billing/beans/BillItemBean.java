package com.billing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(BillItemIdentifier.class)
@Table(name="billitem")
public class BillItemBean {
	
	@Id
	@Column(name="serialnumber", nullable=false)
	private int SerialNumber;
	@Id
	@Column(name="invoicenumber", nullable=false)
	private int InvoiceNumber;
	@Id
	@Column(name="financialyear")
	private String FinancialYear;
	@Column(name="itemname")
	private String ItemName;
	@Column(name="itemcode")
	private String ItemCode;
	@Column(name="itemunit")
	private String ItemUnit;
	@Column(name="weightperpackage")
	private double WeightPerPackage;
	@Column(name="noofpackages")
	private int NoOfPackages;
	@Column(name="totalweight")
	private double TotalWeight;
	@Column(name="rateperunit")
	private double RatePerUnit;
	@Column(name="portionofunit")
	private double PortionOfUnit;
	@Column(name="itemamount")
	private double ItemAmount;
	@Column(name="igstpercentage")
	private double IGSTPercentage;
	@Column(name="cgstpercentage")
	private double CGSTPercentage;
	@Column(name="sgstpercentage")
	private double SGSTPercentage;
	@Column(name="igstamount")
	private double IGSTAmount;
	@Column(name="cgstamount")
	private double CGSTAmount;
	@Column(name="sgstamount")
	private double SGSTAmount;
	@Column(name="totalitemamount")
	private double TotalItemAmount;
	@Column(name="isigstselected")
	private boolean isIGSTSelected;
	
	public int getSerialNumber() {
		return SerialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		SerialNumber = serialNumber;
	}
	public int getInvoiceNumber() {
		return InvoiceNumber;
	}
	public void setInvoiceNumber(int invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemDescription) {
		ItemName = itemDescription;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public String getItemUnit() {
		return ItemUnit;
	}
	public void setItemUnit(String itemUnit) {
		ItemUnit = itemUnit;
	}
	public double getWeightPerPackage() {
		return WeightPerPackage;
	}
	public void setWeightPerPackage(double weightPerPackage) {
		WeightPerPackage = weightPerPackage;
	}
	public int getNoOfPackages() {
		return NoOfPackages;
	}
	public void setNoOfPackages(int noOfPackages) {
		NoOfPackages = noOfPackages;
	}
	public double getTotalWeight() {
		return TotalWeight;
	}
	public void setTotalWeight(double totalWeight) {
		TotalWeight = totalWeight;
	}
	public double getRatePerUnit() {
		return RatePerUnit;
	}
	public void setRatePerUnit(double ratePerUnit) {
		RatePerUnit = ratePerUnit;
	}
	public double getItemAmount() {
		return ItemAmount;
	}
	public void setItemAmount(double itemAmount) {
		ItemAmount = itemAmount;
	}
	public double getIGSTPercentage() {
		return IGSTPercentage;
	}
	public void setIGSTPercentage(double iGSTPercentage) {
		IGSTPercentage = iGSTPercentage;
	}
	public double getCGSTPercentage() {
		return CGSTPercentage;
	}
	public void setCGSTPercentage(double cGSTPercentage) {
		CGSTPercentage = cGSTPercentage;
	}
	public double getSGSTPercentage() {
		return SGSTPercentage;
	}
	public void setSGSTPercentage(double sGCTPercentage) {
		SGSTPercentage = sGCTPercentage;
	}
	public double getIGSTAmount() {
		return IGSTAmount;
	}
	public void setIGSTAmount(double iGSTAmount) {
		IGSTAmount = iGSTAmount;
	}
	public double getCGSTAmount() {
		return CGSTAmount;
	}
	public void setCGSTAmount(double cGSTAmount) {
		CGSTAmount = cGSTAmount;
	}
	public double getSGSTAmount() {
		return SGSTAmount;
	}
	public void setSGSTAmount(double sGSTAmount) {
		SGSTAmount = sGSTAmount;
	}
	public double getPortionOfUnit() {
		return PortionOfUnit;
	}
	public void setPortionOfUnit(double portionOfUnit) {
		PortionOfUnit = portionOfUnit;
	}
	
	public double getTotalItemAmount() {
		return TotalItemAmount;
	}
	public void setTotalItemAmount(double totalItemAmount) {
		TotalItemAmount = totalItemAmount;
	}
	
	public String getFinancialYear() {
		return FinancialYear;
	}
	public void setFinancialYear(String financialYear) {
		FinancialYear = financialYear;
	}
	
	public boolean getIsIGSTSelected() {
		return isIGSTSelected;
	}
	public void setIsIGSTSelected(boolean isIGSTSelected) {
		this.isIGSTSelected = isIGSTSelected;
	}

}
