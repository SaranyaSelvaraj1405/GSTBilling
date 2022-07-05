package com.billing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.CustomerBean;
import com.billing.repositories.CustomerRepository;

@Service
public class CustomerServices {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public boolean addCustomer(CustomerBean customerBean) throws Exception {
		if(customerRepository.existsById(customerBean.getCustomerGST())) {
		return false;
		}else {
		customerRepository.save(customerBean);
		}
		return true;
	}
	
	public void updateCustomer(CustomerBean customerBean) throws Exception {
		customerRepository.save(customerBean);
	}
	
	public void deleteCustomer(CustomerBean customerBean) throws Exception {
		customerRepository.deleteById(customerBean.getCustomerGST());
	}
	
	public List<CustomerBean> searchCustomerByName(String name) throws Exception {
		List<CustomerBean> fullList =customerRepository.findAll();
		List<CustomerBean> filteredList=new ArrayList<CustomerBean>();
		for(CustomerBean customer: fullList) {
			String s=customer.getCustomerName();
			if(s.toLowerCase().contains(name)) {
				filteredList.add(customer);
			}
		}
 		return filteredList;
	}
	
	public List<CustomerBean> searchCustomerByGST(String gst) throws Exception {
		List<CustomerBean> fullList =customerRepository.findAll();
		List<CustomerBean> filteredList=new ArrayList<CustomerBean>();
		for(CustomerBean customer: fullList) {
			String s=customer.getCustomerGST();
			if(s.toLowerCase().contains(gst)) {
				filteredList.add(customer);
			}
		}
 		return filteredList;
	}
	
	public CustomerBean findCustomerByName(String Name) throws Exception {
		List<CustomerBean> fullList =customerRepository.findAll();
		
		for(CustomerBean customer:fullList) {
			String s=customer.getCustomerName();
			if(s.equalsIgnoreCase(Name)) {
				return customer;
			}
		}
		return null;
	}
	
	public CustomerBean findCustomerByGST(String gst) throws Exception {
		List<CustomerBean> fullList =customerRepository.findAll();
		
		for(CustomerBean customer:fullList) {
			String s=customer.getCustomerGST();
			if(s.equalsIgnoreCase(gst)) {
				return customer;
			}
		}
		return null;
	}
	
	public List<CustomerBean> getAllCustomers() throws Exception {
		return customerRepository.findAll();
	}
	
	public void addAllCustomers(List<CustomerBean> list) throws Exception {
		customerRepository.saveAll(list);
	}
	
	public String toStringForBackup(CustomerBean customerBean) {
		String s="";
		s+=customerBean.getCustomerName()+"{}" +customerBean.getCustomerGST() + "{}";
		
		if(customerBean.getCustomerAddress()!=null) {
			s+=customerBean.getCustomerAddress();
		}else {s+="*";}
		s+="#";
		return s;
	}
	
	public List<CustomerBean> toBeanForRestore(String s) throws Exception {
		List<CustomerBean> list = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(s, "#");
		while(stringTokenizer.hasMoreTokens()) {
			String[] parameters = new String[3];
			int i=0;
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().toString(),"{}");
			while(stringTokenizer2.hasMoreTokens()) {
				parameters[i]=stringTokenizer2.nextToken();
				i++;
			}
			CustomerBean customerBean = new CustomerBean();
			customerBean.setCustomerName(parameters[0]);
			customerBean.setCustomerGST(parameters[1]);
			if(parameters[2].equals("*")) {
				customerBean.setCustomerAddress(null);
			}else {customerBean.setCustomerAddress(parameters[2]);}
			list.add(customerBean);
		}
		return list;
	}

}
