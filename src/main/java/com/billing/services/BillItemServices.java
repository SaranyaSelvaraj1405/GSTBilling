package com.billing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.BillItemBean;
import com.billing.beans.BillItemIdentifier;
import com.billing.repositories.BillItemRepository;


@Service
public class BillItemServices {
	
	@Autowired
	private BillItemRepository billItemRepository;
	
	public boolean addBillItem(BillItemBean billItemBean) throws Exception {
		BillItemIdentifier billItemIdentifier=extractKey(billItemBean);
		if(billItemRepository.existsById(billItemIdentifier)) {
			return false;
		}else {
			billItemRepository.save(billItemBean);
		}
		return true;
	}
	
	public BillItemBean findBillItemById(BillItemIdentifier billItemIdentifier) throws Exception {
		Optional<BillItemBean> op=billItemRepository.findById(billItemIdentifier);
		BillItemBean billItemBean = op.get();
		return billItemBean;
	}
	
	public void updateBillItem(BillItemBean billItemBean) throws Exception {
		billItemRepository.save(billItemBean);
	}
	
	public void deleteBillItem(BillItemBean billItemBean) throws Exception{
		BillItemIdentifier billItemIdentifier = extractKey(billItemBean);
		billItemRepository.deleteById(billItemIdentifier);
	}
	
	public void deleteBillItemWithInvoice(int invoiceNumber,String financialYear) throws Exception
	{
		List<BillItemBean> list=getAllBillItemforInvoice(invoiceNumber, financialYear);
		billItemRepository.deleteAll(list);
	}
	
	public List<BillItemBean> getAllBillItemforInvoice(int invoiceNumber, String finacialYear) throws Exception{
		List<BillItemBean> list=billItemRepository.findAll();
		List<BillItemBean> filteredList = new ArrayList<>();
		for(BillItemBean bean:list) {
			if(bean.getFinancialYear().equals(finacialYear)&&(bean.getInvoiceNumber()==invoiceNumber)) {
				filteredList.add(bean);
			}
		}
		return filteredList;
	}
	
	public int getBillItemserialNumber(int invoiceNumber, String finacialYear) throws Exception  {
		return getAllBillItemforInvoice(invoiceNumber, finacialYear).size()+1;
	}
	
	
	private BillItemIdentifier extractKey(BillItemBean billItemBean) throws Exception{
		return new BillItemIdentifier(billItemBean.getSerialNumber(), billItemBean.getInvoiceNumber(),billItemBean.getFinancialYear());
	}
	
	
	public double calculateCGST(BillItemBean billItemBean) throws Exception{
		billItemBean.setCGSTAmount((billItemBean.getItemAmount()/100)* billItemBean.getCGSTPercentage());
		return billItemBean.getCGSTAmount();
	}
	public double calculateSGST(BillItemBean billItemBean) throws Exception {
		billItemBean.setSGSTAmount((billItemBean.getItemAmount()/100)* billItemBean.getSGSTPercentage());
		return billItemBean.getSGSTAmount();
	}
	public double calculateIGST(BillItemBean billItemBean) throws Exception {
		billItemBean.setIGSTAmount((billItemBean.getItemAmount()/100)* billItemBean.getIGSTPercentage());
		return billItemBean.getIGSTAmount();
	}
	public double calculateTotalItemAmount(BillItemBean billItemBean) throws Exception {
		billItemBean.setTotalItemAmount(billItemBean.getItemAmount()+billItemBean.getCGSTAmount()+billItemBean.getSGSTAmount()+billItemBean.getIGSTAmount());
		return billItemBean.getTotalItemAmount();
	}
	public double calculateTotalWeight(BillItemBean billItemBean) throws Exception {
		billItemBean.setTotalWeight(billItemBean.getNoOfPackages()*billItemBean.getWeightPerPackage());
		return billItemBean.getTotalWeight();
	}
	public double calculateItemAmount(BillItemBean billItemBean) throws Exception {
		
		billItemBean.setItemAmount((billItemBean.getTotalWeight()/billItemBean.getPortionOfUnit())*billItemBean.getRatePerUnit());
		return billItemBean.getItemAmount();
		
	}
	
	public List<BillItemBean> getAllBillItems() throws Exception {
		return billItemRepository.findAll();
	}
	
	public void addAllBillItem(List<BillItemBean> list) throws Exception {
		billItemRepository.saveAll(list);
	}
	
	public String toStringForBackup(BillItemBean billItemBean) throws Exception {
		String s= billItemBean.getSerialNumber()+"{}"+billItemBean.getInvoiceNumber()+"{}"+billItemBean.getFinancialYear()+"{}";
		
		if(billItemBean.getItemName()!=null && !billItemBean.getItemName().isEmpty()) {
			s+=billItemBean.getItemName()+"{}";
		}else {s+="*{}";}
		if(billItemBean.getItemCode()!=null && !billItemBean.getItemCode().isEmpty()) {
			s+=billItemBean.getItemCode()+"{}";
		}else {s+="*{}";}
		if(billItemBean.getItemUnit()!=null && !billItemBean.getItemUnit().isEmpty()) {
			s+=billItemBean.getItemUnit()+"{}";
		}else {s+="*{}";}
		
		s+=billItemBean.getRatePerUnit()+"{}"+billItemBean.getPortionOfUnit()+"{}"+billItemBean.getWeightPerPackage()+"{}"+billItemBean.getNoOfPackages()
		+"{}"+billItemBean.getTotalWeight()+"{}"+billItemBean.getItemAmount()+"{}"+billItemBean.getIsIGSTSelected()+"{}"
		+billItemBean.getIGSTPercentage()+"{}"+billItemBean.getIGSTAmount()+"{}"+billItemBean.getCGSTPercentage()+"{}"+billItemBean.getCGSTAmount()
		+"{}"+billItemBean.getSGSTPercentage()+"{}"+billItemBean.getSGSTAmount()+"{}"+billItemBean.getTotalItemAmount()+"#";
		return s;
	}
	
	public List<BillItemBean> toBeanForRestore(String s) throws Exception {
		List<BillItemBean> list = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(s, "#");
		while(stringTokenizer.hasMoreTokens()) {
			String[] parameters = new String[20];
			int i=0;
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().toString(),"{}");
			while(stringTokenizer2.hasMoreTokens()) {
				parameters[i]=stringTokenizer2.nextToken();
				i++;
			}
			BillItemBean billItemBean = new BillItemBean();
			billItemBean.setSerialNumber(Integer.valueOf(parameters[0]));
			billItemBean.setInvoiceNumber(Integer.valueOf(parameters[1]));
			billItemBean.setFinancialYear(parameters[2]);
			if(parameters[3].equals("*")) {
				billItemBean.setItemName(null);
			}else {billItemBean.setItemName(parameters[3]);}
			if(parameters[4].equals("*")) {
				billItemBean.setItemCode(null);
			}else {billItemBean.setItemCode(parameters[4]);}
			if(parameters[5].equals("*")) {
				billItemBean.setItemUnit(null);
			}else {billItemBean.setItemUnit(parameters[5]);}
			billItemBean.setRatePerUnit(Double.valueOf(parameters[6]));
			billItemBean.setPortionOfUnit(Double.valueOf(parameters[7]));
			billItemBean.setWeightPerPackage(Double.valueOf(parameters[8]));
			billItemBean.setNoOfPackages(Integer.valueOf(parameters[9]));
			billItemBean.setTotalWeight(Double.valueOf(parameters[10]));
			billItemBean.setItemAmount(Double.valueOf(parameters[11]));
			billItemBean.setIsIGSTSelected(Boolean.valueOf(parameters[12]));
			billItemBean.setIGSTPercentage(Double.valueOf(parameters[13]));
			billItemBean.setIGSTAmount(Double.valueOf(parameters[14]));
			billItemBean.setCGSTPercentage(Double.valueOf(parameters[15]));
			billItemBean.setCGSTAmount(Double.valueOf(parameters[16]));
			billItemBean.setSGSTPercentage(Double.valueOf(parameters[17]));
			billItemBean.setSGSTAmount(Double.valueOf(parameters[18]));
			billItemBean.setTotalItemAmount(Double.valueOf(parameters[19]));
			list.add(billItemBean);
		}
		return list;
	}

}
