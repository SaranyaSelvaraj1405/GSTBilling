package com.billing.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.BillItemBean;
import com.billing.beans.InvoiceBean;
import com.billing.beans.InvoiceIdentifier;
import com.billing.repositories.InvoiceRepository;

@Service
public class InvoiceServices {
	
	@Autowired
	private InvoiceRepository invoiceRepository; 
	
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public boolean addInvoice(InvoiceBean invoiceBean) throws Exception{
		InvoiceIdentifier invoiceIdentifier=extractkey(invoiceBean);
		if(invoiceRepository.existsById(invoiceIdentifier)) {
			return false;
		}else {
			invoiceRepository.save(invoiceBean);
		}
		return true;
	}
	
	public void updateInvoice(InvoiceBean invoiceBean) throws Exception{
		invoiceRepository.save(invoiceBean);
	}
	
	public InvoiceBean findinvoiceById(InvoiceIdentifier invoiceIdentifier) throws Exception {
		Optional<InvoiceBean> op=invoiceRepository.findById(invoiceIdentifier);
		InvoiceBean invoiceBean = op.get();
		return invoiceBean;
	}
	
	public boolean isInvoiceExists(int invoiceNumber, String financialYear) throws Exception {
		InvoiceIdentifier invoiceIdentifier = new InvoiceIdentifier(invoiceNumber, financialYear);
		boolean flag=invoiceRepository.existsById(invoiceIdentifier);
		return flag;
	}
	
	public void deleteInvoice(InvoiceBean invoiceBean) throws Exception {
		InvoiceIdentifier invoiceIdentifier=extractkey(invoiceBean);
		invoiceRepository.deleteById(invoiceIdentifier);
	}
	
	public List<InvoiceBean> searchInvoiceByInvoiceNumber(String invoiceNumber,String financialYear) throws Exception {

		List<InvoiceBean> fullList = getAllInvoice(financialYear);
		List<InvoiceBean> filteredList=new ArrayList<InvoiceBean>();
		for(InvoiceBean invoice: fullList) {
			if(invoice.getInvoiceNumber()==Integer.valueOf(invoiceNumber)) {
				filteredList.add(invoice);
			}
		}
		return filteredList;
	}
	
	public List<InvoiceBean> searchInvoiceByCustomerName(String customerName,String financialYear) throws Exception {
		List<InvoiceBean> fullList = getAllInvoice(financialYear);
		List<InvoiceBean> filteredList=new ArrayList<InvoiceBean>();
		for(InvoiceBean invoice: fullList) {
			if(invoice.getCustomerName().equalsIgnoreCase(customerName)) {
				filteredList.add(invoice);
			}
		}
		return filteredList;
	}
	
	public List<InvoiceBean> getAllInvoice(String financialYear) throws Exception {
		List<InvoiceBean> list =invoiceRepository.findAll();
		List<InvoiceBean> newList = new ArrayList<InvoiceBean>();
		for(InvoiceBean bean: list) {
			if(bean.getFinancialYear().equals(financialYear)) {
				newList.add(bean);
			}
		}
		return newList;
	}
	
	
	private InvoiceIdentifier extractkey(InvoiceBean invoiceBean) throws Exception {
		return new InvoiceIdentifier(invoiceBean.getInvoiceNumber(),invoiceBean.getFinancialYear());
	}
	
	
	
	public int getInvoiceNumber(String financialYear) throws Exception {
		
		return getAllInvoice(financialYear).size()+1;
	}
	
	public double calculateTotalAmount(List<BillItemBean> list) throws Exception {
		double total=0;
		for(BillItemBean b: list) {
			total+=b.getTotalItemAmount();
		}
		return total;
	}
	
	public double calculateTotalTaxAmount(List<BillItemBean> list) throws Exception {
		double total=0;
		for(BillItemBean b: list) {
			total+=b.getIGSTAmount()+b.getCGSTAmount()+b.getSGSTAmount();
		}
		return total;
	}
	
	public double calculateRoundOff(String d) throws Exception {
		return -(Double.valueOf(d)-Math.round(Double.valueOf(d)));
	}
	
	public double calculateGrandTotal(Double d1,Double d2) throws Exception {
		return d1-d2;
	}
	
	public List<InvoiceBean> getAllInvoice() throws Exception {
		return invoiceRepository.findAll();
	}
	
	public void addAllInvoice(List<InvoiceBean> list) {
		invoiceRepository.saveAll(list);
	}
	
	public String toStringForBackup(InvoiceBean invoiceBean) throws Exception {
		String s="";
		s+=invoiceBean.getInvoiceNumber()+"{}"+invoiceBean.getFinancialYear()+"{}"+invoiceBean.getCustomerName()+"{}"+invoiceBean.getCustomerGST()+"{}";
		
		if(invoiceBean.getCustomerAddress()!=null && !invoiceBean.getCustomerAddress().isEmpty()) {
			s+=invoiceBean.getCustomerAddress()+"{}";
		}else {s+="*{}";}
		if(invoiceBean.getLRNumber()!=null && !invoiceBean.getLRNumber().isEmpty()) {
			s+=invoiceBean.getLRNumber()+"{}";
		}else {s+="*{}";}
		if(invoiceBean.getDispatchThrough()!=null && !invoiceBean.getDispatchThrough().isEmpty()) {
			s+=invoiceBean.getDispatchThrough()+"{}";
		}else {s+="*{}";}
		if(invoiceBean.getDestination()!=null && !invoiceBean.getDestination().isEmpty()) {
			s+=invoiceBean.getDestination()+"{}";
		}else {s+="*{}";}
		if(invoiceBean.getShippingAddress()!=null && !invoiceBean.getShippingAddress().isEmpty()) {
			s+=invoiceBean.getShippingAddress()+"{}";
		}else {s+="*{}";}
		if(invoiceBean.getTermsOfDelivery()!=null && !invoiceBean.getTermsOfDelivery().isEmpty()) {
			s+=invoiceBean.getTermsOfDelivery()+"{}";
		}else {s+="*{}";}
						
		s+=invoiceBean.getTotalAmount()+"{}"+invoiceBean.getTotalTaxAmount()+"{}"+invoiceBean.getRoundoff()+"{}"+invoiceBean.getGrandTotal()+"{}";
		
		if(invoiceBean.getInvoiceDate()==null) {
			s+="*{}";
		}else {s+=invoiceBean.getInvoiceDate().format(dateTimeFormatter)+"{}";}
		if(invoiceBean.getLRDate()==null) {
			s+="*";
		}else {s+=invoiceBean.getLRDate().format(dateTimeFormatter);}
		s+="#";
		
		return s;
	}
	
	public List<InvoiceBean> toBeanForRestore(String s) throws Exception {
		List<InvoiceBean> list = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(s, "#");
		while(stringTokenizer.hasMoreTokens()) {
			String[] parameters = new String[16];
			int i=0;
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().toString(),"{}");
			while(stringTokenizer2.hasMoreTokens()) {
				parameters[i]=stringTokenizer2.nextToken();
				i++;
			}
			InvoiceBean invoiceBean = new InvoiceBean();
			invoiceBean.setInvoiceNumber(Integer.valueOf(parameters[0]));
			invoiceBean.setFinancialYear(parameters[1]);
			invoiceBean.setCustomerName(parameters[2]);
			invoiceBean.setCustomerGST(parameters[3]);
			if(parameters[4].equals("*")) {
				invoiceBean.setCustomerAddress(null);
			}else {invoiceBean.setCustomerAddress(parameters[4]);}
			if(parameters[5].equals("*")) {
				invoiceBean.setLRNumber(null);
			}else {invoiceBean.setLRNumber(parameters[5]);}
			if(parameters[6].equals("*")) {
				invoiceBean.setDispatchThrough(null);
			}else {invoiceBean.setDispatchThrough(parameters[6]);}
			if(parameters[7].equals("*")) {
				invoiceBean.setDestination(null);
			}else {invoiceBean.setDestination(parameters[7]);}
			if(parameters[8].equals("*")) {
				invoiceBean.setShippingAddress(null);
			}else {invoiceBean.setShippingAddress(parameters[8]);}
			if(parameters[9].equals("*")) {
				invoiceBean.setTermsOfDelivery(null);
			}else {invoiceBean.setTermsOfDelivery(parameters[9]);}
			invoiceBean.setTotalAmount(Double.valueOf(parameters[10]));
			invoiceBean.setTotalTaxAmount(Double.valueOf(parameters[11]));
			invoiceBean.setRoundoff(Double.valueOf(parameters[12]));
			invoiceBean.setGrandTotal(Double.valueOf(parameters[13]));
			
			if(parameters[14].equals("*")) {
				invoiceBean.setInvoiceDate(null);
			}else {invoiceBean.setInvoiceDate(LocalDate.parse(parameters[14],dateTimeFormatter));}
			if(parameters[15].equals("*")) {
				invoiceBean.setLRDate(null);
			}else {invoiceBean.setLRDate(LocalDate.parse(parameters[15],dateTimeFormatter));}
			
			list.add(invoiceBean);
		}
		return list;
	}

}
