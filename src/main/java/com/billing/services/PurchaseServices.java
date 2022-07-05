package com.billing.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.PurchaseBean;
import com.billing.beans.PurchaseIdentifier;
import com.billing.repositories.PurchaseRepository;

@Service
public class PurchaseServices {
	
	@Autowired 
	private PurchaseRepository purchaseRepository;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public boolean addPurchase(PurchaseBean purchaseBean) throws Exception {
		PurchaseIdentifier purchaseIdentifier = extractKey(purchaseBean);
		if(purchaseRepository.existsById(purchaseIdentifier)) {
			return false;
		}else {
			purchaseRepository.save(purchaseBean);
		}
		return true;
	}
	
	public  PurchaseBean findPurchaseById(PurchaseIdentifier purchaseIdentifier) throws Exception {
		Optional<PurchaseBean> op = purchaseRepository.findById(purchaseIdentifier);
		PurchaseBean purchaseBean = op.get();
		return purchaseBean;		
	}
	
	public void updatePurchase(PurchaseBean purchaseBean) throws Exception {
		purchaseRepository.save(purchaseBean);
	}
	
	public void deletePurchase(PurchaseBean purchaseBean) throws Exception {
		PurchaseIdentifier purchaseIdentifier = extractKey(purchaseBean);
		purchaseRepository.deleteById(purchaseIdentifier);
	}
	
	public List<PurchaseBean> getAllPurchase() throws Exception {
		return purchaseRepository.findAll();
	}
	
	public List<PurchaseBean> searchPurchaseByInvoiceNumber(String invoiceNumber) throws Exception {
		List<PurchaseBean> fullList = purchaseRepository.findAll();
		List<PurchaseBean> filteredList = new ArrayList<PurchaseBean>();
		fullList.forEach(a->{
			if(a.getPurchaseInvoiceNumber().equals(invoiceNumber)) {
				filteredList.add(a);
			}
		});
		return filteredList;
	}
	
	public List<PurchaseBean> searchPurchaseByCompanyName(String companyName) throws Exception {
		List<PurchaseBean> fullList = purchaseRepository.findAll();
		List<PurchaseBean> filteredList = new ArrayList<PurchaseBean>();
		fullList.forEach(a->{
			if(a.getPurchaseCompanyName().equals(companyName)) {
				filteredList.add(a);
			}
		});
		return filteredList;
	}
	
	private PurchaseIdentifier extractKey(PurchaseBean purchaseBean) throws Exception {
		return new PurchaseIdentifier(purchaseBean.getSerialNumber(),purchaseBean.getFinancialYear());
	}
	
	public List<PurchaseBean> getAllPurchase(String financialYear) throws Exception {
		List<PurchaseBean> list =purchaseRepository.findAll();
		List<PurchaseBean> newList = new ArrayList<PurchaseBean>();
		for(PurchaseBean bean: list) {
			if(bean.getFinancialYear().equals(financialYear)) {
				newList.add(bean);
			}
		}
		return newList;
	}
	public int getPurchaseSerialNumber(String financialYear) throws Exception {
		
		return getAllPurchase(financialYear).size()+1;
	}
	
	public void addAllPurchases(List<PurchaseBean> list) throws Exception {
		purchaseRepository.saveAll(list);
	}
	
	public String toStringForBackup(PurchaseBean purchaseBean) {
		String s="";
		s+=String.valueOf(purchaseBean.getSerialNumber())+"{}"+purchaseBean.getFinancialYear()+"{}";
		
		if(purchaseBean.getPurchaseInvoiceNumber()!=null && !purchaseBean.getPurchaseInvoiceNumber().isEmpty()) {
			s+=purchaseBean.getPurchaseInvoiceNumber()+"{}";
		}else {s+="*{}";}
		if(purchaseBean.getPurchaseCompanyName()!=null && !purchaseBean.getPurchaseCompanyName().isEmpty()) {
			s+=purchaseBean.getPurchaseCompanyName()+"{}";
		}else {s+="*{}";}
		if(purchaseBean.getPurchaseItemDescription()!=null && !purchaseBean.getPurchaseItemDescription().isEmpty()) {
			s+=purchaseBean.getPurchaseItemDescription()+"{}";
		}else {s+="*{}";}
		s+=+purchaseBean.getPurchaseGrandTotal()+"{}";
		if(purchaseBean.getPurchaseInvoiceDate()!=null) {
			
			s+=purchaseBean.getPurchaseInvoiceDate().format(dateTimeFormatter);
		}else {s+="*";}
		s+="#";
		
		return s;
	}
	
	public List<PurchaseBean> toBeanForRestore(String s) throws Exception {
		List<PurchaseBean> list = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(s, "#");
		
		while(stringTokenizer.hasMoreTokens()) {
			String[] parameters = new String[7];
			int i=0;
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().toString(),"{}");
			while(stringTokenizer2.hasMoreTokens()) {
				parameters[i]=stringTokenizer2.nextToken();
				i++;
			}
			PurchaseBean purchaseBean = new PurchaseBean();
			purchaseBean.setSerialNumber(Integer.valueOf(parameters[0]));
			purchaseBean.setFinancialYear(parameters[1]);
			if(parameters[2].equals("*")) {
				purchaseBean.setPurchaseInvoiceNumber(null);
			}else {purchaseBean.setPurchaseInvoiceNumber(parameters[2]);}
			if(parameters[3].equals("*")) {
				purchaseBean.setPurchaseCompanyName(null);;
			}else {purchaseBean.setPurchaseCompanyName(parameters[3]);}
			if(parameters[4].equals("*")) {
				purchaseBean.setPurchaseItemDescription(null);
			}else {purchaseBean.setPurchaseItemDescription(parameters[4]);}
			purchaseBean.setPurchaseGrandTotal(Double.valueOf(parameters[5]));
			if(parameters[6].equals("*")) {
				purchaseBean.setPurchaseInvoiceDate(null);
			}else {purchaseBean.setPurchaseInvoiceDate(LocalDate.parse(parameters[6], dateTimeFormatter));}			
			list.add(purchaseBean);
		}
		return list;
	}

}
