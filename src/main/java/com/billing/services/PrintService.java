package com.billing.services;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.billing.beans.BillItemBean;
import com.billing.beans.CompanyDetailsBean;
import com.billing.beans.InvoiceBean;
import com.billing.controllers.MainPageController;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Table;


@Service
public class PrintService {
	
	@Autowired
	CompanyDetailsServices companyDetailsServices;
	@Autowired
	CompanyDetailsBean companyDetailsBean;
	private MainPageController mainPageController = new MainPageController();
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	@Autowired
	ResourceLoader resourceLoader;
	
	public void createInvoice(InvoiceBean invoiceBean) throws Exception {
		
		Document document = new Document();
		
		
		
		try {
			companyDetailsBean = companyDetailsServices.getCompanyDetails();
		} catch (Exception e) {
			throw e;
			
		}
		
		List<BillItemBean> list = invoiceBean.getItems();
		boolean isIGSTSelected=invoiceBean.getItems().get(0).getIsIGSTSelected();
		
		if(isIGSTSelected) {
			Resource resource = resourceLoader.getResource("classpath:/Invoice/IGSTInvoiceTemplate.dotx");
		    InputStream inputStream = resource.getInputStream();
		    document.loadFromStream(inputStream, FileFormat.Dotx);
			
			double totalIGST=list.stream().map(ob->ob.getIGSTAmount()).reduce(0.0, (a,b)->a+b);
			document.replace("#TIA", String.valueOf(totalIGST),true,true);
			
			document.replace("#IP", String.valueOf(invoiceBean.getItems().get(0).getIGSTPercentage()),true,true);
			document.replace("#IA", String.valueOf(invoiceBean.getItems().get(0).getIGSTAmount()),true,true);
			
		}else {
			Resource resource = resourceLoader.getResource("classpath:/Invoice/CGSTInvoiceTemplate.dotx");
		    InputStream inputStream = resource.getInputStream();
		    document.loadFromStream(inputStream, FileFormat.Dotx);
			
			double totalCGST=list.stream().map(ob->ob.getCGSTAmount()).reduce(0.0, (a,b)->a+b);
			double totalSGST=list.stream().map(ob->ob.getSGSTAmount()).reduce(0.0, (a,b)->a+b);
			document.replace("#TSA", String.valueOf(totalSGST),true,true);
			document.replace("#TCA", String.valueOf(totalCGST),true,true);
			
			document.replace("#CP", String.valueOf(invoiceBean.getItems().get(0).getCGSTPercentage()),true,true);
			document.replace("#SP", String.valueOf(invoiceBean.getItems().get(0).getSGSTPercentage()),true,true);
			document.replace("#CA", String.valueOf(invoiceBean.getItems().get(0).getCGSTAmount()),true,true);
			document.replace("#SA", String.valueOf(invoiceBean.getItems().get(0).getSGSTAmount()),true,true);
			
			
		}
		
		document.replace("#Invoice_Number", String.valueOf(invoiceBean.getInvoiceNumber()), true, true);
		document.replace("#Invoice_Date",invoiceBean.getInvoiceDate().toString(),true,true);
		document.replace("#Company_Name",companyDetailsBean.getCompanyName(),true,true);
		document.replace("#Company_Address",companyDetailsBean.getCompanyAddress(),true,true);
		document.replace("#Company_GST",companyDetailsBean.getCompanyGST(),true,true);
		document.replace("#Customer_Name",invoiceBean.getCustomerName(),true,true);
		document.replace("#Billing_Address",invoiceBean.getCustomerAddress(),true,true);
		document.replace("#Customer_GST",invoiceBean.getCustomerGST(),true,true);
		document.replace("#Shipping_Address",invoiceBean.getShippingAddress(),true,true);
		
		document.replace("#LR_Number",invoiceBean.getLRNumber(),true,true);
		if(invoiceBean.getLRDate()!=null) {
		document.replace("#LR_Date",invoiceBean.getLRDate().toString(),true,true);
		}
		else {
			document.replace("#LR_Date"," ",true,true);
		}
		document.replace("#Delivery_Through",invoiceBean.getDispatchThrough(),true,true);
		document.replace("#Destination",invoiceBean.getDestination(),true,true);
		document.replace("#Terms_of_Delivery",invoiceBean.getTermsOfDelivery(),true,true);
		
		
		document.replace("#Roundoff", String.valueOf(decimalFormat.format(invoiceBean.getRoundoff())),true,true);
		document.replace("#GTotal", String.valueOf(invoiceBean.getGrandTotal()),true,true);
		document.replace("#Amount_In_Words", AmountToWordService.convert(invoiceBean.getGrandTotal()),true,true);
		
		document.replace("#TA", String.valueOf(invoiceBean.getTotalAmount()),true,true);
		document.replace("#TTA", String.valueOf(invoiceBean.getTotalTaxAmount()),true,true);
		
		document.replace("#Declaration", companyDetailsBean.getDeclaration(),true,true);
		document.replace("#Company_Details",companyDetailsBean.getCompanyBankDetails(),true,true);
				
		
		
		String [][] PurchaseData = new String[list.size()][6];
		String [][] CGSTData = new String[list.size()][7];
		String [][] IGSTData = new String[list.size()][5];
		
		for(int i=0;i<list.size();i++) {
			BillItemBean billItemBean=list.get(i);
			String s[] = new String[] {String.valueOf(billItemBean.getSerialNumber()),billItemBean.getItemName(),billItemBean.getItemCode(),
					String.valueOf(billItemBean.getRatePerUnit()),String.valueOf(billItemBean.getTotalWeight()),String.valueOf(billItemBean.getItemAmount())};
			PurchaseData[i]=s;
			if(!isIGSTSelected) {
			String s2 [] = new String[] {billItemBean.getItemCode(),String.valueOf(billItemBean.getItemAmount()),
					String.valueOf(billItemBean.getCGSTPercentage()),String.valueOf(billItemBean.getCGSTAmount()),
					String.valueOf(billItemBean.getSGSTPercentage()),String.valueOf(billItemBean.getSGSTAmount()),
					String.valueOf(billItemBean.getCGSTAmount()+billItemBean.getSGSTAmount())};
			
			CGSTData[i]=s2;
			}else {
				String s2 [] = new String[] {billItemBean.getItemCode(),String.valueOf(billItemBean.getItemAmount()),
						String.valueOf(billItemBean.getIGSTPercentage()),String.valueOf(billItemBean.getIGSTAmount()),
						String.valueOf(billItemBean.getIGSTAmount())};
				
				IGSTData[i]=s2;
			}
		}
		
		if(isIGSTSelected) {
			writeDataToDocument(document, PurchaseData, IGSTData,isIGSTSelected);
		}else {
			writeDataToDocument(document, PurchaseData, CGSTData,isIGSTSelected);			
		}
		

				
		document.isUpdateFields(true);
		document.saveToFile("D:\\Invoice\\Invoice.pdf", FileFormat.PDF);
		File file = new File("D:\\Invoice\\Invoice.pdf");
		printPreview(file);
		
		}
	
	private static void addRows(Table table,int n) {
		for(int i=0;i<n-1;i++) {
			table.getRows().insert(6+i, table.getRows().get(6).deepClone());
			
		}
	}
	
	private static void addRowsCGST(Table table,int n) {
		for(int i=0;i<n-1;i++) {
			table.getRows().insert(7+i, table.getRows().get(7).deepClone());
			
		}
	}
	
	private static void addRowsIGST(Table table,int n) {
		for(int i=0;i<n-1;i++) {
			table.getRows().insert(5+i, table.getRows().get(5).deepClone());
			
		}
	}
	
	private static void fillTableWithData(Table table,String[][] data, int n) {
		
		for(int r=0;r<data.length;r++) {
			for(int c=0;c<data[r].length;c++) {
				table.getRows().get(r+n).getCells().get(c).getParagraphs().get(0).setText(data[r][c]);
				
			}
		}
		
	}

	private static void writeDataToDocument(Document document, String[][] PurchaseData, String[][] GSTData, boolean flag) {
		
		//Page 1
		Table table = document.getSections().get(0).getTables().get(1);
		if(PurchaseData.length>1) {
			addRows(table,PurchaseData.length-1);
		}
		fillTableWithData(table, PurchaseData,1);
		
		if(flag) {
		Table table2 = document.getSections().get(0).getTables().get(2);
		if(GSTData.length>1) {
			addRowsCGST(table2,GSTData.length-1);
		}
		fillTableWithData(table2, GSTData,2);
		}else {
			Table table2 = document.getSections().get(0).getTables().get(2);
			if(GSTData.length>1) {
				addRowsIGST(table2,GSTData.length-1);
			}
			fillTableWithData(table2, GSTData,2);
		}
		
		//Page 2
		Table table3 = document.getSections().get(0).getTables().get(4);
		if(PurchaseData.length>1) {
			addRows(table3,PurchaseData.length-1);
		}
		fillTableWithData(table3, PurchaseData,1);
		
		if(flag) {
		Table table2 = document.getSections().get(0).getTables().get(5);
		if(GSTData.length>1) {
			addRowsCGST(table2,GSTData.length-1);
		}
		fillTableWithData(table2, GSTData,2);
		}else {
			Table table2 = document.getSections().get(0).getTables().get(5);
			if(GSTData.length>1) {
				addRowsIGST(table2,GSTData.length-1);
			}
			fillTableWithData(table2, GSTData,2);
		}
		
		//Page 3
		Table table4 = document.getSections().get(0).getTables().get(7);
		if(PurchaseData.length>1) {
			addRows(table4,PurchaseData.length-1);
		}
		fillTableWithData(table4, PurchaseData,1);
		
		if(flag) {
		Table table2 = document.getSections().get(0).getTables().get(8);
		if(GSTData.length>1) {
			addRowsCGST(table2,GSTData.length-1);
		}
		fillTableWithData(table2, GSTData,2);
		}else {
			Table table2 = document.getSections().get(0).getTables().get(8);
			if(GSTData.length>1) {
				addRowsIGST(table2,GSTData.length-1);
			}
			fillTableWithData(table2, GSTData,2);
		}
	}
	
	public void printPreview(File file) {
		mainPageController.printPreview(file);
	}
	
	
}
