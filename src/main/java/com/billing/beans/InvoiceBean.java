package com.billing.beans;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@IdClass(InvoiceIdentifier.class)
@Table(name="invoice")
public class InvoiceBean {
	
	@Id
	@Column(name="invoicenumber")
	private int InvoiceNumber;
	@Column(name="invoicedate")
	private LocalDate InvoiceDate;
	@Column(name="customername")
	private String CustomerName;
	@Column(name="customergst")
	private String CustomerGST;
	@Column(name="customeraddress")
	private String CustomerAddress;
	@Column(name="shippingaddress")
	private String ShippingAddress;
	@Column(name="ewaybillnumber")
	private String EWayBillNumber;
	
	@Id
	@Column(name="financialyear")
	private String FinancialYear;
	
	@Column(name="lrnumber")
	private String LRNumber;
	@Column(name="lrdate")
	private LocalDate LRDate;
	@Column(name="dispatchthrough")
	private String DispatchThrough;
	@Column(name="destination")
	private String Destination;
	@Column(name="termsofdelivery")
	private String TermsOfDelivery;
	
	@Transient
	private List<BillItemBean> Items;
	
	@Column(name="totalamount")
	private double TotalAmount;
	@Column(name="totaltaxamount")
	private double TotalTaxAmount;
	@Column(name="roundoff")
	private double Roundoff;
	@Column(name="grandtotal")
	private double GrandTotal;
		
	public int getInvoiceNumber() {
		return InvoiceNumber;
	}
	public void setInvoiceNumber(int invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}
	public LocalDate getInvoiceDate() {
		return InvoiceDate;
	}
	public void setInvoiceDate(LocalDate invoiceDate) {
		InvoiceDate = invoiceDate;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getCustomerGST() {
		return CustomerGST;
	}
	public void setCustomerGST(String customerGST) {
		CustomerGST = customerGST;
	}
	public String getCustomerAddress() {
		return CustomerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		CustomerAddress = customerAddress;
	}
	
	public String getShippingAddress() {
		return ShippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		ShippingAddress = shippingAddress;
	}
	
	
	public String getLRNumber() {
		return LRNumber;
	}
	public void setLRNumber(String lRNumber) {
		LRNumber = lRNumber;
	}
	public LocalDate getLRDate() {
		return LRDate;
	}
	public void setLRDate(LocalDate lRDate) {
		LRDate = lRDate;
	}
	public String getDispatchThrough() {
		return DispatchThrough;
	}
	public void setDispatchThrough(String dispatchThrough) {
		DispatchThrough = dispatchThrough;
	}
	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getTermsOfDelivery() {
		return TermsOfDelivery;
	}
	public void setTermsOfDelivery(String termsOfDelivery) {
		TermsOfDelivery = termsOfDelivery;
	}
	
	
	public List<BillItemBean> getItems() {
		return Items;
	}
	public void setItems(List<BillItemBean> items) {
		Items=items;
	}
	
	
	public double getTotalAmount() {
		return TotalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}
	
	public double getTotalTaxAmount() {
		return TotalTaxAmount;
	}
	public void setTotalTaxAmount(double totalTaxAmount) {
		TotalTaxAmount = totalTaxAmount;
	}
	public double getRoundoff() {
		return Roundoff;
	}
	public void setRoundoff(double roundoff) {
		Roundoff = roundoff;
	}
	public double getGrandTotal() {
		return GrandTotal;
	}
	public void setGrandTotal(double grandTotal) {
		GrandTotal = grandTotal;
	}	
	public String getEWayBillNumber() {
		return EWayBillNumber;
	}
	public void setEWayBillNumber(String eWayBillNumber) {
		EWayBillNumber = eWayBillNumber;
	}
	public String getFinancialYear() {
		return FinancialYear;
	}
	public void setFinancialYear(String financialYear) {
		FinancialYear = financialYear;
	}

}
