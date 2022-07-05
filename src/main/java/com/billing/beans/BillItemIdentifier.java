package com.billing.beans;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class BillItemIdentifier implements Serializable{

	private int SerialNumber;
	private int InvoiceNumber;
	private String FinancialYear;
	
	public BillItemIdentifier() {
		SerialNumber=0;
		InvoiceNumber=0;
		FinancialYear="";
	}
	public BillItemIdentifier(int serialNumber, int invoiceNumber, String financialYear) {
		SerialNumber = serialNumber;
		InvoiceNumber = invoiceNumber;
		FinancialYear = financialYear;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillItemIdentifier other = (BillItemIdentifier) obj;
		return Objects.equals(FinancialYear, other.FinancialYear) && InvoiceNumber == other.InvoiceNumber
				&& SerialNumber == other.SerialNumber;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(FinancialYear, InvoiceNumber, SerialNumber);
	}
}
