package com.billing.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.FinancialYearBean;
import com.billing.repositories.FinancialYearRepository;

@Service
public class FinancialYearService {
	
	private String CurrentFinancialYear;
	private List<String> ListOfValidFinancialYears;
	

	private int StartYear=2020;
	
	@Autowired
	private FinancialYearBean financialYearBean;
	@Autowired
	private FinancialYearRepository financialYearRepository;
	
	public void calculateCurrentFinancialYear() throws Exception {
		
		LocalDate localDate = LocalDate.now();
		
		int Year=localDate.getYear();
		int Month=localDate.getMonthValue();
		
		if(Month>3) {
			CurrentFinancialYear=Year+"-"+(Year+1);
		}else {
			CurrentFinancialYear=(Year-1)+"-"+Year;
		}
	}
	
	public List<String> calculateListOfValidFinancilaYears() throws Exception{
		
		LocalDate localDate = LocalDate.now();
		
		int Year=localDate.getYear();
		int Month=localDate.getMonthValue();
		ListOfValidFinancialYears = new ArrayList<String>();
		
		for(int i=StartYear;i<=Year;i++) {
			String FinancialYear;
			if(i==Year) {
				if(Month>3) {
					FinancialYear=Year+"-"+(Year+1);
					ListOfValidFinancialYears.add(FinancialYear);
				}
			}else {
				FinancialYear=i+"-"+(i+1);
				ListOfValidFinancialYears.add(FinancialYear);
			}
		}
		return ListOfValidFinancialYears;
	}
	
	public void setFinancialYeartoCurrent()  throws Exception{
		calculateCurrentFinancialYear();
		financialYearBean.setSelectedFinancialYear(CurrentFinancialYear);
		financialYearRepository.deleteAll();
		financialYearRepository.save(financialYearBean);
	}
	
	public void setFinalcialYear(String FinancialYear) throws Exception{
		financialYearRepository.deleteAll();
		financialYearBean.setSelectedFinancialYear(FinancialYear);
		financialYearRepository.save(financialYearBean);
	}
	
	public String getFinancialYear() throws Exception{
		List<FinancialYearBean> financialYearList = financialYearRepository.findAll();
		return financialYearList.get(0).getSelectedFinancialYear();
	}
	
	public List<String> getListOfValidFinancialYears() throws Exception{
		ListOfValidFinancialYears= new ArrayList<String>();
		ListOfValidFinancialYears.addAll(calculateListOfValidFinancilaYears());
		return ListOfValidFinancialYears;
	}
	

}
