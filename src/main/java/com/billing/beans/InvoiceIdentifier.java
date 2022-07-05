package com.billing.beans;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class InvoiceIdentifier implements Serializable{
	
	private int InvoiceNumber;
	private String FinancialYear;
	
	public InvoiceIdentifier() {
		InvoiceNumber=0;
		FinancialYear=null;
	}
	
	public InvoiceIdentifier(int invoiceNumber, String financialYear) {
		super();
		InvoiceNumber = invoiceNumber;
		FinancialYear = financialYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(FinancialYear, InvoiceNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceIdentifier other = (InvoiceIdentifier) obj;
		return Objects.equals(FinancialYear, other.FinancialYear) && InvoiceNumber == other.InvoiceNumber;
	}
	
	
	
	

}
