package com.billing.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.BillItemBean;
import com.billing.beans.CustomerBean;
import com.billing.beans.InvoiceBean;
import com.billing.beans.ItemBean;
import com.billing.beans.PurchaseBean;

@Service
public class BackUpServices {
	
	String CustomerBackUpFileName="CustomerBackup.txt";
	String ItemBackUpFileName="ItemBackUp.txt";
	String PurchaseBackUpFileName="PurchaseBackUp.txt";
	String BillItemBackUpFileName="BillItemBackUp.txt";
	String InvoiceBackUpFileName="InvoiceBackUp.txt";
	
	@Autowired
	CustomerServices customerServices;
	@Autowired
	ItemServices itemServices;
	@Autowired
	PurchaseServices purchaseServices;
	@Autowired
	BillItemServices billItemServices;
	@Autowired
	InvoiceServices invoiceServices;
	
	
	public void backup(File file) throws Exception {
		
		 List<CustomerBean> customerList = customerServices.getAllCustomers();
	      FileWriter fileWriter=new FileWriter(file.getPath()+"/"+CustomerBackUpFileName);
	      String input ="";
	      for(CustomerBean bean: customerList) {
	    	  input = input+customerServices.toStringForBackup(bean);
	    	  }
	      String cipherText = encrypt(input);
	      fileWriter.write(cipherText);
	      
	    fileWriter.close();
	    
	    //Item
	    List<ItemBean> itemList = itemServices.getAllItems();
	      
	      fileWriter=new FileWriter(file.getPath()+"/"+ItemBackUpFileName);
	      input ="";
	      for(ItemBean bean: itemList) {
	    	  input = input+itemServices.toStringForBackup(bean);
	    	  }
	      cipherText = encrypt(input);
	      fileWriter.write(cipherText);
	      
	    fileWriter.close();

	    ////Purchase
	    
	    List<PurchaseBean> purchaseList = purchaseServices.getAllPurchase();
	      
	      fileWriter=new FileWriter(file.getPath()+"/"+PurchaseBackUpFileName);
	      input ="";
	      for(PurchaseBean bean: purchaseList) {
	    	  input = input+purchaseServices.toStringForBackup(bean);
	    	  }
	      cipherText = encrypt(input);
	      fileWriter.write(cipherText);
	      
	    fileWriter.close();
	    
	   ////BillItem
	    
	    List<BillItemBean> billItemList = billItemServices.getAllBillItems();
	      
	      fileWriter=new FileWriter(file.getPath()+"/"+BillItemBackUpFileName);
	      input ="";
	      for(BillItemBean bean: billItemList) {
	    	  input = input+billItemServices.toStringForBackup(bean);
	    	  }
	      cipherText = encrypt(input);
	      fileWriter.write(cipherText);
	      
	    fileWriter.close();
	    
	    ////Invoice
	    
	    List<InvoiceBean> invoiceList = invoiceServices.getAllInvoice();
	      
	      fileWriter=new FileWriter(file.getPath()+"/"+InvoiceBackUpFileName);
	      input ="";
	      for(InvoiceBean bean: invoiceList) {
	    	  input = input+invoiceServices.toStringForBackup(bean);
	    	  }
	      cipherText = encrypt(input);
	      fileWriter.write(cipherText);
	      
	    fileWriter.close();	    
	}
	
	
	public void restore(File file) throws Exception {
		FileReader fileReader = new FileReader(file.getPath()+"/"+CustomerBackUpFileName);
	    String inputCipherText="";
	    int temp;
	    do {
	    	temp=fileReader.read();
	    	if(temp!=-1)
	    		inputCipherText=inputCipherText+(char)temp;
	    }while(temp!=-1);
	    String output=decrypt(inputCipherText);
	    
	    fileReader.close();
	    List<CustomerBean> CustomerList =customerServices.toBeanForRestore(output);
	    customerServices.addAllCustomers(CustomerList);
	    
	    /////Item
	    
	    fileReader = new FileReader(file.getPath()+"/"+ItemBackUpFileName);
	    inputCipherText="";
	    do {
	    	temp=fileReader.read();
	    	if(temp!=-1)
	    		inputCipherText=inputCipherText+(char)temp;
	    }while(temp!=-1);
	    output=decrypt(inputCipherText);
	    fileReader.close();
	    
	    List<ItemBean> itemList =itemServices.toBeanForRestore(output);
	    itemServices.addAllItems(itemList);
	    
	    
	    ////Purchase
	    
	    fileReader = new FileReader(file.getPath()+"/"+PurchaseBackUpFileName);
	    inputCipherText="";
	    do {
	    	temp=fileReader.read();
	    	if(temp!=-1)
	    		inputCipherText=inputCipherText+(char)temp;
	    }while(temp!=-1);
	    output=decrypt(inputCipherText);
	    fileReader.close();
	    
	    List<PurchaseBean> purchaseList =purchaseServices.toBeanForRestore(output);
	    purchaseServices.addAllPurchases(purchaseList);
	    
	    
	    ////BillItem
	    
	    fileReader = new FileReader(file.getPath()+"/"+BillItemBackUpFileName);
	    inputCipherText="";
	    do {
	    	temp=fileReader.read();
	    	if(temp!=-1)
	    		inputCipherText=inputCipherText+(char)temp;
	    }while(temp!=-1);
	    output=decrypt(inputCipherText);
	    fileReader.close();
	    
	    List<BillItemBean> billItemList = billItemServices.toBeanForRestore(output);
	    billItemServices.addAllBillItem(billItemList);
	    
	    ////Invoice
	    
	    fileReader = new FileReader(file.getPath()+"/"+InvoiceBackUpFileName);
	    inputCipherText="";
	    do {
	    	temp=fileReader.read();
	    	if(temp!=-1)
	    		inputCipherText=inputCipherText+(char)temp;
	    }while(temp!=-1);
	    output=decrypt(inputCipherText);
	    fileReader.close();
	    
	    List<InvoiceBean> invoiceList = invoiceServices.toBeanForRestore(output);
	    invoiceServices.addAllInvoice(invoiceList);
	   
	    
	}
	
	private String encrypt(String input) throws Exception {
		String output ="";
		StringBuffer stringBuffer = new StringBuffer(input);
		input = new String(stringBuffer.reverse());
		for(int i=0;i<input.length();i++) {
			output+=(char)(input.charAt(i)+1);
		}
		return output;
	}
	
	private String decrypt(String input) throws Exception{
		String output ="";
		
		for(int i=0;i<input.length();i++) {
			output+=(char)(input.charAt(i)-1);
		}
		StringBuffer stringBuffer = new StringBuffer(output);
		output = new String(stringBuffer.reverse());
		
		return output;
	}

}
