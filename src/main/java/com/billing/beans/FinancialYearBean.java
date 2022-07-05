package com.billing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="financialYear")
public class FinancialYearBean {

	@Id
	@Column(name="selectedFinancialYear")
	private String SelectedFinancialYear;

	public String getSelectedFinancialYear() {
		return SelectedFinancialYear;
	}

	public void setSelectedFinancialYear(String selectedFinancialYear) {
		SelectedFinancialYear = selectedFinancialYear;
	}
	
}
