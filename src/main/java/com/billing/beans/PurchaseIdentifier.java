package com.billing.beans;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class PurchaseIdentifier implements Serializable{
	
	private int SerialNumber;
	private String FinancialYear;
	
	public PurchaseIdentifier() {
		SerialNumber=0;
		FinancialYear=null;
	}
	
	public PurchaseIdentifier(int serialNumber, String financialYear) {
		super();
		SerialNumber = serialNumber;
		FinancialYear = financialYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(FinancialYear, SerialNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseIdentifier other = (PurchaseIdentifier) obj;
		return Objects.equals(FinancialYear, other.FinancialYear) && SerialNumber == other.SerialNumber;
	}

}
