package com.billing.controllers;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.billing.SkvBillingApplication;
import com.billing.beans.BillItemBean;
import com.billing.beans.BillItemIdentifier;
import com.billing.beans.CompanyDetailsBean;
import com.billing.beans.CustomerBean;
import com.billing.beans.InvoiceBean;
import com.billing.beans.ItemBean;
import com.billing.beans.PasswordBean;
import com.billing.beans.PurchaseBean;
import com.billing.mains.ViewSceneChanger;
import com.billing.services.BackUpServices;
import com.billing.services.BillItemServices;
import com.billing.services.CompanyDetailsServices;
import com.billing.services.CustomerServices;
import com.billing.services.FinancialYearService;
import com.billing.services.InvoiceServices;
import com.billing.services.ItemServices;
import com.billing.services.PasswordServices;
import com.billing.services.PrintService;
import com.billing.services.PurchaseServices;
import com.dansoftware.pdfdisplayer.PDFDisplayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;

@Controller
@FxmlView("/fxmlPages/MainPage.fxml")
public class MainPageController implements Initializable{
	
	/***------------------------------Menu Tasks-------------------------------***/
	
	@FXML
	private Button InvoiceMenuButton;
	@FXML
	private Button CustomerMenuButton;
	@FXML
	private Button ItemMenuButton;
	@FXML
	private Button PurchasesMenuButton;
	@FXML
	private Button SettingsMenuButton;
	@FXML
	private Button LogoutMenuButton;
	
	@FXML
	private AnchorPane InvoiceAnchorPane;
	@FXML
	private AnchorPane CustomerAnchorPane;
	@FXML
	private AnchorPane ItemAnchorPane;
	@FXML
	private AnchorPane PurchasesAnchorPane;
	@FXML
	private AnchorPane SettingsAnchorPane;
	
	//Numeric Field Validation Pattern
	private String pattern = "-?\\d+(\\.\\d+)?";
	
	//Date Fields Validation Pattern
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter dateTimeFormatterForFile = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss");
	
	final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
	
	//Choice Box for Search
	private ObservableList<String> InvoiceSearchFieldList = FXCollections.observableArrayList();
	private ObservableList<String> CustomerSearchFieldList = FXCollections.observableArrayList();
	private ObservableList<String> ItemSearchFieldList = FXCollections.observableArrayList();
	private ObservableList<String> PurchaseSearchFieldList = FXCollections.observableArrayList();
	
	
	//Scene Change
	private ViewSceneChanger viewSceneChanger = new ViewSceneChanger();
	
/**************Menu Tab Actions Start***************/
	
	public void showInvoiceTab(ActionEvent actionEvent) {
		InvoiceAnchorPane.setVisible(true);
		loadInvoiceAfterwards();
		CustomerAnchorPane.setVisible(false);
		ItemAnchorPane.setVisible(false);
		PurchasesAnchorPane.setVisible(false);
		SettingsAnchorPane.setVisible(false);	
		
	}
	
	public void showCustomerTab(ActionEvent actionEvent) {
		CustomerAnchorPane.setVisible(true);
		InvoiceAnchorPane.setVisible(false);
		ItemAnchorPane.setVisible(false);
		PurchasesAnchorPane.setVisible(false);
		SettingsAnchorPane.setVisible(false);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, false);		
	}
	
	public void showItemTab(ActionEvent actionEvent) {
		ItemAnchorPane.setVisible(true);
		InvoiceAnchorPane.setVisible(false);
		CustomerAnchorPane.setVisible(false);
		PurchasesAnchorPane.setVisible(false);
		SettingsAnchorPane.setVisible(false);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, false);
	}
	
	public void showPurchasesTab(ActionEvent actionEvent) {
		PurchasesAnchorPane.setVisible(true);
		InvoiceAnchorPane.setVisible(false);
		CustomerAnchorPane.setVisible(false);
		ItemAnchorPane.setVisible(false);
		SettingsAnchorPane.setVisible(false);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, false);
	}
	
	public void showSettingsTab(ActionEvent actionEvent) {
		SettingsAnchorPane.setVisible(true);
		InvoiceAnchorPane.setVisible(false);
		CustomerAnchorPane.setVisible(false);
		ItemAnchorPane.setVisible(false);
		PurchasesAnchorPane.setVisible(false);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, false);
	}
	
	public void logoutEvent(ActionEvent actionEvent) {
		NewInvoice_InvoiceCancelButton.fire();		
		Stage stage = (Stage)LogoutMenuButton.getScene().getWindow();
		viewSceneChanger.successfullLogoutSceneChanger(stage);
		HeaderFinancilaYearText.setText("");
		
	}
	
	public  void exit() {
		try {
		if(!invoiceServices.isInvoiceExists(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()),financialYearService.getFinancialYear())) {
			billItemServices.deleteBillItemWithInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear());
		}
		refreshInvoiceFields();
		itemFieldsRefresh();
		customerFieldsRefresh();
		purchaseFieldsRefresh();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Error occured during Exit Operation", e);
			log(e);
			e.printStackTrace();
		}
	}
	
	private void setCustomerTableProperties() {
		CustomerTab_TableCustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
		CustomerTab_TableCustomerGST.setCellValueFactory(new PropertyValueFactory<>("CustomerGST"));
		CustomerTab_TableCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
	}
	
	private void setItemTableProperties() {
		ItemTab_TableItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
		ItemTab_TableItemDescription.setCellValueFactory(new PropertyValueFactory<>("ItemDescription"));
		ItemTab_TableCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
		ItemTab_TableUnit.setCellValueFactory(new PropertyValueFactory<>("ItemUnit"));
	}
	
	private void setBillItemTableProperties() {
		NewInvoice_TableSerialNumber.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));
		NewInvoice_TableItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
		NewInvoice_TableItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
		NewInvoice_TableNumberOfPackages.setCellValueFactory(new PropertyValueFactory<>("NoOfPackages"));
		NewInvoice_TableWeightPerPackage.setCellValueFactory(new PropertyValueFactory<>("WeightPerPackage"));
		NewInvoice_TableTotalWeight.setCellValueFactory(new PropertyValueFactory<>("TotalWeight"));
		NewInvoice_TableRate.setCellValueFactory(new PropertyValueFactory<>("ratePerUnit"));
		NewInvoice_TableRatePerUnit.setCellValueFactory(new PropertyValueFactory<>("PortionOfUnit"));
		NewInvoice_TableCGSTPercentage.setCellValueFactory(new PropertyValueFactory<>("CGSTPercentage"));
		NewInvoice_TableIGSTPercentage.setCellValueFactory(new PropertyValueFactory<>("IGSTPercentage"));
		NewInvoice_TableSGSTPercentage.setCellValueFactory(new PropertyValueFactory<>("SGSTPercentage"));
		NewInvoice_TableTotalAmount.setCellValueFactory(new PropertyValueFactory<>("ItemAmount"));
	}
	
	private void setInvoiceTableProperties() {
		InvoiceList_InvoiceTableInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("InvoiceNumber"));
		InvoiceList_InvoiceTableInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("InvoiceDate"));
		InvoiceList_InvoiceTableCustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
		InvoiceList_InvoiceTableCustomerGST.setCellValueFactory(new PropertyValueFactory<>("CustomerGST"));
		InvoiceList_InvoiceTableGrandTotal.setCellValueFactory(new PropertyValueFactory<>("GrandTotal"));
	}
	
	private void setPurchaseTableProperties() {
		PurchaseTab_TableSerialNumber.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));
		PurchaseTab_TableInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("PurchaseInvoiceNumber"));
		PurchaseTab_TableDate.setCellValueFactory(new PropertyValueFactory<>("PurchaseInvoiceDate"));
		PurchaseTab_TableCompanyName.setCellValueFactory(new PropertyValueFactory<>("PurchaseCompanyName"));
		PurchaseTab_TableItemDescription.setCellValueFactory(new PropertyValueFactory<>("PurchaseItemDescription"));
		PurchaseTab_TableGrandTotal.setCellValueFactory(new PropertyValueFactory<>("PurchaseGrandTotal"));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
		financialYearService.setFinancialYeartoCurrent();
		loadSettingTab();
		SettingsTab_CompanyDetailsUpdateButton.setVisible(false);
		SettingsTab_CompanyDetailsEditButton.setDisable(true);
		
		if(!companyDetailsServices.isCompanyDetailsEntered()){
			SettingsMenuButton.fire();
			InvoiceMenuButton.setDisable(true);
			CustomerMenuButton.setDisable(true);
			ItemMenuButton.setDisable(true);
			PurchasesMenuButton.setDisable(true);
			addCompanyDetailsAtBeginingAlert();	
			
		}
		else {
		InvoiceAnchorPane.setVisible(true);
		CustomerAnchorPane.setVisible(false);
		ItemAnchorPane.setVisible(false);
		PurchasesAnchorPane.setVisible(false);
		SettingsAnchorPane.setVisible(false);
		SettingsTab_CompanyDetailsSaveButton.setVisible(false);
		SettingsTab_CompanyDetailsEditButton.setDisable(false);
		loadCompanyDetailsatStart();
		}
		
		setInvoiceTableProperties();
		loadInvoiceTab();
		
		setCustomerTableProperties();
		loadCustomerTab();		
		
		setItemTableProperties();
		loadItemTab();
		
		setPurchaseTableProperties();
		loadPurchaseTab();
		
		setBillItemTableProperties();
		loadBillItemTable();
		NewInvoice_ItemAddButton.setDisable(false);
		NewInvoice_ItemUpdateButton.setVisible(false);
		NewInvoice_InvoiceUpdateButton.setVisible(false);
		NewInvoice_PrintButton.setVisible(false);
		NewInvoice_InvoiceSaveButton.setDisable(false);
		
		
		
		
		
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during initialization", e);
			exceptionAlert("Exception during Load. Check Logs");
			e.printStackTrace();
			log(e);
		}
		
		
	}
	
	public void showNewInvoiceTab(ActionEvent actionEvent) {
		loadBillItemTable();
		InvoiceTab_NewInvoiceScrollPane.setVisible(true);
		InvoiceTab_InvoiceListScrollPane.setVisible(false);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, true);
	}
	
	public void showInvoiceListTab(ActionEvent actionEvent) {
		InvoiceTab_NewInvoiceScrollPane.setVisible(false);
		InvoiceTab_InvoiceListScrollPane.setVisible(true);
		InvoiceMenuButton.pseudoClassStateChanged(focusClass, true);
		InvoiceTab_NewInvoiceScrollPane.pseudoClassStateChanged(errorClass, false);

	}
	
	public void loadInvoiceAfterwards() {
		loadCustomerNameAutoComplete();
		loadCustomerGSTAutoComplete();
		loadItemNameAutoComplete();
		loadItemCodeAutoComplete();
		if(!NewInvoice_ItemAddButton.isDisabled()) 
			NewInvoice_ItemAddButton.setDisable(false);
		if(!NewInvoice_ItemUpdateButton.isVisible())
			NewInvoice_ItemUpdateButton.setVisible(false);
		
		if(!NewInvoice_InvoiceUpdateButton.isVisible()) {
			NewInvoice_InvoiceUpdateButton.setVisible(false);
			NewInvoice_PrintButton.setVisible(false);
		}
		if(!NewInvoice_InvoiceSaveButton.isDisabled())
			NewInvoice_InvoiceSaveButton.setDisable(false);
	}
	
	public void loadInvoiceTab() {
		InvoiceTab_NewInvoiceScrollPane.setVisible(true);
		InvoiceTab_InvoiceListScrollPane.setVisible(false);
		
		loadInvoiceTable();
		
		InvoiceSearchFieldList.add("Customer Name");
		InvoiceSearchFieldList.add("Invoice Number");
		InvoiceList_SearchFieldChoiceBox.setItems(InvoiceSearchFieldList);
		InvoiceList_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		
		loadInvoiceNumber();
		loadBillItemNumber();
		loadInvoiceDate();
		loadCustomerNameAutoComplete();
		loadCustomerGSTAutoComplete();
		loadItemNameAutoComplete();
		loadItemCodeAutoComplete();
		
		if(!NewInvoice_ItemAddButton.isDisabled()) 
			NewInvoice_ItemAddButton.setDisable(false);
		if(!NewInvoice_ItemUpdateButton.isVisible())
			NewInvoice_ItemUpdateButton.setVisible(false);
		
		if(!NewInvoice_InvoiceUpdateButton.isVisible()) {
			NewInvoice_InvoiceUpdateButton.setVisible(false);
			NewInvoice_PrintButton.setVisible(false);
		}
		if(!NewInvoice_InvoiceSaveButton.isDisabled())
			NewInvoice_InvoiceSaveButton.setDisable(false);
		
		
		NewInvoice_CGSTPercentageTextField.setVisible(false);
		NewInvoice_CGSTAmountTextField.setVisible(false);
		NewInvoice_CGSTLabel.setVisible(false);
		NewInvoice_SGSTPercentageTextField.setVisible(false);
		NewInvoice_SGSTAmountTextField.setVisible(false);
		NewInvoice_SGSTLabel.setVisible(false);
		
		
		NewInvoice_IGSTAmountTextField.setText("0");
		
		NewInvoice_CGSTAmountTextField.setText("0");
		
		NewInvoice_SGSTAmountTextField.setText("0");
		
		NewInvoice_DateDatePicker.setConverter(new StringConverter<LocalDate>() {

		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateTimeFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateTimeFormatter);
		         } else {
		             return null;
		         }
		     }
		 });
		NewInvoice_LRDateDatePicker.setConverter(new StringConverter<LocalDate>() {

		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateTimeFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateTimeFormatter);
		         } else {
		             return null;
		         }
		     }
		 });
		
		NewInvoice_InvoiceNumberTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_CustomerNameTextField.requestFocus();
				}
			}
		});
		NewInvoice_BillingAddressTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.TAB)) {
					NewInvoice_DateDatePicker.requestFocus();
				}
			}
		});	
		NewInvoice_DateDatePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_LRNumberTextField.requestFocus();
				}
			}
		});
		NewInvoice_LRNumberTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_LRDateDatePicker.requestFocus();
				}
			}
		});
		NewInvoice_LRDateDatePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_DeliveryThroughTextField.requestFocus();
				}
			}
		});
		NewInvoice_DeliveryThroughTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_DestinationTextField.requestFocus();
				}
			}
		});
		NewInvoice_DestinationTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_ShippingAddressTextArea.requestFocus();
				}
			}
		});
		NewInvoice_ShippingAddressTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.TAB)) {
					NewInvoice_TermsOfDeliveryTextArea.requestFocus();
				}
			}
		});
		NewInvoice_TermsOfDeliveryTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.TAB)) {
					NewInvoice_ItemCodeTextField.requestFocus();
				}
			}
		});
		NewInvoice_ItemCodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_ItemUnitTextField.requestFocus();
				}
			}
		});
		NewInvoice_ItemUnitTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_RatePerTextField.requestFocus();
				}
			}
		});
		NewInvoice_RatePerTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_RatePerUnitTextField.requestFocus();
				}
			}
		});
		NewInvoice_RatePerUnitTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_NumberOfPackagesTextField.requestFocus();
				}
			}
		});
		NewInvoice_NumberOfPackagesTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_WeightPerPackageTextField.requestFocus();
				}
			}
		});
		NewInvoice_WeightPerPackageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_TotalWeightTextField.requestFocus();
				}
			}
		});
		NewInvoice_TotalWeightTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_ItemAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_IGSTPercentageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_IGSTAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_IGSTAmountTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_TotalItemAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_CGSTPercentageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_CGSTAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_CGSTAmountTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_SGSTPercentageTextField.requestFocus();
				}
			}
		});
		NewInvoice_SGSTPercentageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_SGSTAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_SGSTAmountTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_TotalItemAmountTextField.requestFocus();
				}
			}
		});
		NewInvoice_TotalAmountTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_RoundOffTextField.requestFocus();
				}
			}
		});
		NewInvoice_RoundOffTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					NewInvoice_GrandTotalTextField.requestFocus();
				}
			}
		});
	}
	
	private void loadCustomerTab() {
		
		loadCustomerTable();
		CustomerTab_UpdateButton.setVisible(false);
		CustomerSearchFieldList.add("Customer Name");
		CustomerSearchFieldList.add("GST Number");
		CustomerTab_SearchFieldChoiceBox.setItems(CustomerSearchFieldList);
		CustomerTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		
		CustomerTab_CustomerNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					CustomerTab_CustomerGSTTextField.requestFocus();
				}
			}
		});
		CustomerTab_CustomerGSTTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					CustomerTab_CustomerAddressTextArea.requestFocus();
				}
			}
		});
		
	}
	
	private void loadItemTab() {
		
		loadItemTable();
		ItemTab_UpdateButton.setVisible(false);
		ItemSearchFieldList.add("Item Name");
		ItemSearchFieldList.add("HSNC Code");
		ItemTab_SearchFieldChoiceBox.setItems(ItemSearchFieldList);
		ItemTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		
		ItemTab_ItemNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					ItemTab_ItemDescriptionTextField.requestFocus();
				}
			}
		});
		ItemTab_ItemDescriptionTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					ItemTab_ItemCodeTextField.requestFocus();
				}
			}
		});
		ItemTab_ItemCodeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					ItemTab_UnitTextField.requestFocus();
				}
			}
		});
	}
	
	private void loadPurchaseTab() {
		
		loadPurchaseTable();
		PurchaseTab_UpdateButton.setVisible(false);
		PurchaseSearchFieldList.add("Invoice Number");
		PurchaseSearchFieldList.add("Company Name");
		PurchaseTab_SearchFieldChoiceBox.setItems(PurchaseSearchFieldList);
		PurchaseTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		loadPurchaseSerialNumber();
		
		PurchaseTab_DateDatePicker.setConverter(new StringConverter<LocalDate>() {

		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateTimeFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateTimeFormatter);
		         } else {
		             return null;
		         }
		     }
		 });
		
		PurchaseTab_SerialNumberTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PurchaseTab_InvoiceNumberTextField.requestFocus();
				}
			}
		});
		PurchaseTab_InvoiceNumberTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PurchaseTab_DateDatePicker.requestFocus();
				}
			}
		});
		PurchaseTab_DateDatePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PurchaseTab_CompanyNameTextField.requestFocus();
				}
			}
		});
		PurchaseTab_CompanyNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PurchaseTab_ItemDescriptionTextField.requestFocus();
				}
			}
		});
		PurchaseTab_ItemDescriptionTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					PurchaseTab_GrandTotalTextField.requestFocus();
				}
			}
		});
	}
	
	public void loadSettingTab(){
		//Company Details
		SettingsTab_CompanyDetailsUpdateButton.setVisible(false);
		SettingsTab_CompanyDetailsEditButton.setDisable(true);
		
		SettingsTab_NewPasswordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					SettingsTab_ConfirmNewPasswordTextField.requestFocus();
				}
			}
		});
		SettingsTab_ConfirmNewPasswordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					SettingsTab_ChangePasswordButton.requestFocus();
				}
			}
		});
		
		SettingsTab_CompanyNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					SettingsTab_CompanyGSTTextField.requestFocus();
				}
			}
		});
		SettingsTab_CompanyGSTTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if(keyEvent.getCode().equals(KeyCode.ENTER)) {
					SettingsTab_CompanyAddressTextArea.requestFocus();
				}
			}
		});
		
		//Financial Year
		try {
		SettingsTab_CurrentFinancialYearText.setText(financialYearService.getFinancialYear());
		HeaderFinancilaYearText.setText("Current Financial Year: "+financialYearService.getFinancialYear());
		financialYearList.addAll(financialYearService.getListOfValidFinancialYears());
		SettingsTab_SelectFinancialYearChoiceBox.setItems(financialYearList);
		SettingsTab_SelectFinancialYearChoiceBox.setValue(financialYearList.get(0));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Settings Tab load",e);
			e.printStackTrace();
			log(e);
		}
	}
	
/*****************Menu Tab Actions End***************/	
	/***------------------------------Invoice Tab------------------------------***/
	
	//Fields For Inner Menu Tab
	@FXML
	private Button InvoiceTab_NewInvoiceMenuButton;
	@FXML
	private Button InvoiceTab_InvoiceListMenuButton;
	@FXML
	private ScrollPane InvoiceTab_NewInvoiceScrollPane;
	@FXML
	private ScrollPane InvoiceTab_InvoiceListScrollPane;
	
	//New Invoice Fields
	@FXML
	private TextField NewInvoice_InvoiceNumberTextField;
	@FXML
	private DatePicker NewInvoice_DateDatePicker;
	@FXML
	private TextField NewInvoice_CustomerNameTextField;
	@FXML
	private TextField NewInvoice_CustomerGSTTextField;
	@FXML
	private TextArea NewInvoice_BillingAddressTextArea;
	@FXML
	private TextField NewInvoice_LRNumberTextField;
	@FXML
	private DatePicker NewInvoice_LRDateDatePicker;
	@FXML
	private TextArea NewInvoice_ShippingAddressTextArea;
	@FXML
	private TextField NewInvoice_DeliveryThroughTextField;
	@FXML
	private TextField NewInvoice_DestinationTextField;
	@FXML
	private TextArea NewInvoice_TermsOfDeliveryTextArea;
	@FXML
	private TextField NewInvoice_ItemSerialNumberTextField;
	@FXML
	private TextField NewInvoice_ItemNameTextField;
	@FXML
	private TextField NewInvoice_ItemCodeTextField;
	@FXML
	private TextField NewInvoice_ItemUnitTextField;
	@FXML
	private TextField NewInvoice_NumberOfPackagesTextField;
	@FXML
	private TextField NewInvoice_WeightPerPackageTextField;
	@FXML
	private TextField NewInvoice_TotalWeightTextField;
	@FXML
	private TextField NewInvoice_RatePerTextField;
	@FXML
	private TextField NewInvoice_RatePerUnitTextField;
	@FXML
	private TextField NewInvoice_IGSTPercentageTextField;
	@FXML
	private TextField NewInvoice_SGSTPercentageTextField;
	@FXML
	private TextField NewInvoice_CGSTPercentageTextField;
	@FXML
	private TextField NewInvoice_IGSTAmountTextField;
	@FXML
	private TextField NewInvoice_SGSTAmountTextField;
	@FXML
	private TextField NewInvoice_CGSTAmountTextField;
	@FXML
	private TextField NewInvoice_ItemAmountTextField;
	@FXML
	private TextField NewInvoice_TotalItemAmountTextField;
	@FXML
	private TextField NewInvoice_TotalAmountTextField;
	@FXML
	private TextField NewInvoice_RoundOffTextField;
	@FXML
	private TextField NewInvoice_GrandTotalTextField;
	@FXML
	private Button NewInvoice_ItemCancelButton;
	@FXML
	private Button NewInvoice_ItemAddButton;
	@FXML
	private Button NewInvoice_ItemUpdateButton;
	@FXML
	private MenuItem NewInvoice_ItemEditMenuItem;
	@FXML
	private MenuItem NewInvoice_ItemDeleteMenuItem;
	@FXML
	private Button NewInvoice_InvoiceSaveButton;
	@FXML
	private Button NewInvoice_InvoiceUpdateButton;
	@FXML
	private Button NewInvoice_InvoiceCancelButton;
	@FXML 
	private CheckBox NewInvoice_DeliveryAddressCheckBox;
	@FXML
	private RadioButton NewInvoice_IGSTRadioButton;
	@FXML
	private RadioButton NewInvoice_CGSTSGSTRadioButton;
	@FXML
	private Label NewInvoice_IGSTLabel;
	@FXML
	private Label NewInvoice_CGSTLabel;
	@FXML
	private Label NewInvoice_SGSTLabel;
	
	
	@FXML
	private TableView<BillItemBean> NewInvoice_BillItemTable;
	@FXML
	private TableColumn<BillItemBean,Integer> NewInvoice_TableSerialNumber;
	@FXML
	private TableColumn<BillItemBean,String> NewInvoice_TableItemName;
	@FXML
	private TableColumn<BillItemBean,String> NewInvoice_TableItemCode;
	@FXML
	private TableColumn<BillItemBean,Integer> NewInvoice_TableNumberOfPackages;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableWeightPerPackage;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableTotalWeight;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableRate;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableRatePerUnit;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableCGSTPercentage;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableSGSTPercentage;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableIGSTPercentage;
	@FXML
	private TableColumn<BillItemBean,Double> NewInvoice_TableTotalAmount;
	
	//Invoice List Tab
	@FXML
	private TableView<InvoiceBean> InvoiceList_InvoiceTable;
	@FXML
	private TableColumn<InvoiceBean,Integer> InvoiceList_InvoiceTableInvoiceNumber;
	@FXML
	private TableColumn<InvoiceBean,LocalDate> InvoiceList_InvoiceTableInvoiceDate;
	@FXML
	private TableColumn<InvoiceBean,String> InvoiceList_InvoiceTableCustomerName;
	@FXML
	private TableColumn<InvoiceBean,String> InvoiceList_InvoiceTableCustomerGST;
	@FXML
	private TableColumn<InvoiceBean,Double> InvoiceList_InvoiceTableGrandTotal;
	
	@FXML
	private Text InvoiceTab_BillItemFieldErrorText;
	@FXML
	private Text NewInvoice_InvoiceFieldErrorText;
	
	@FXML
	private MenuItem InvoiceList_DeleteMenuItem;
	@FXML
	private MenuItem InvoiceList_EditMenuItem;
	@FXML
	private ChoiceBox<String> InvoiceList_SearchFieldChoiceBox;
	@FXML
	private TextField InvoiceList_SearchValueTextField;
	@FXML
	private Button InvoiceList_SearchButton;
	@FXML
	private Button InvoiceList_ClearSearchButton;
	
	private ObservableList<BillItemBean> BillItemList = FXCollections.observableArrayList();
	private ObservableList<InvoiceBean> InvoiceList = FXCollections.observableArrayList();
	
	@Autowired
	private BillItemBean billItemBean;
	@Autowired
	private BillItemServices billItemServices;
	
	@Autowired
	private InvoiceServices invoiceServices;
	@Autowired
	private InvoiceBean invoiceBean;
	@Autowired
	private FinancialYearService financialYearService;
	
	final PseudoClass focusClass = PseudoClass.getPseudoClass("additionalFocus");
	
	
	
	private void loadInvoiceNumber() {
		try {
		NewInvoice_InvoiceNumberTextField.setText(String.valueOf(invoiceServices.getInvoiceNumber(financialYearService.getFinancialYear())));
		}
		catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Loading Invoice Number", e);
			log(e);
			e.printStackTrace();
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	private void loadBillItemNumber() {
		try {
		NewInvoice_ItemSerialNumberTextField.setText(String.valueOf(billItemServices.getBillItemserialNumber(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear())));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Loading Bill Item Number", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	private void loadPurchaseSerialNumber() {
		try {
			PurchaseTab_SerialNumberTextField.setText(String.valueOf(purchaseServices.getPurchaseSerialNumber(financialYearService.getFinancialYear())));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Loading Bill Item Number", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	private void loadInvoiceDate() {
		NewInvoice_DateDatePicker.setValue(LocalDate.now());
	}
	
	private void loadCustomerNameAutoComplete() {
		try {
		List<String> CustomerNameList=getCustomerNameList();
		TextFields.bindAutoCompletion(NewInvoice_CustomerNameTextField,CustomerNameList );
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Name Load", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	private void loadCustomerGSTAutoComplete() {
		try {
		List<String> CustomerGSTList=getCustomerGSTList();
		TextFields.bindAutoCompletion(NewInvoice_CustomerGSTTextField, CustomerGSTList);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer GST Load", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	private void loadItemNameAutoComplete() {
		try {
		List<String> ItemNameList=getItemNameList();
		TextFields.bindAutoCompletion(NewInvoice_ItemNameTextField, ItemNameList);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Name Load");
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	private void loadItemCodeAutoComplete() {
		try {
		List<String> ItemCodeList=getItemCodeList();
		TextFields.bindAutoCompletion(NewInvoice_ItemCodeTextField, ItemCodeList);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Code Load", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	public void loadCustomerDetailsWithName(ActionEvent actionEvent) {
		try {
		String name=NewInvoice_CustomerNameTextField.getText();
		CustomerBean customer=customerServices.findCustomerByName(name);
		NewInvoice_CustomerGSTTextField.setText(customer.getCustomerGST());
		NewInvoice_BillingAddressTextArea.setText(customer.getCustomerAddress());
		NewInvoice_BillingAddressTextArea.requestFocus();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Details Loading with Name", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	public void loadCustomerDetailsWithGST(ActionEvent actionEvent) {
		try {
		String gst=NewInvoice_CustomerGSTTextField.getText();
		CustomerBean customer=customerServices.findCustomerByGST(gst);
		NewInvoice_CustomerNameTextField.setText(customer.getCustomerName());
		NewInvoice_BillingAddressTextArea.setText(customer.getCustomerAddress());
		NewInvoice_BillingAddressTextArea.requestFocus();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Details Loading with GST", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	public void loadItemDetailsWithName(ActionEvent actionEvent) {
		try {
		String name=NewInvoice_ItemNameTextField.getText();
		ItemBean item=itemServices.findItemByName(name);
		NewInvoice_ItemCodeTextField.setText(item.getItemCode());
		NewInvoice_ItemUnitTextField.setText(item.getItemUnit());
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Details Loading with Name", e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	public void loadItemDetailsWithCode(ActionEvent actionEvent) {
		try {
		String code= NewInvoice_ItemCodeTextField.getText();
		ItemBean item=itemServices.findItemByCode(code);
		NewInvoice_ItemNameTextField.setText(item.getItemName());
		NewInvoice_ItemUnitTextField.setText(item.getItemUnit());
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Details Loading with Code",e);
			e.printStackTrace();
			log(e);
			exceptionAlert("Exception Occured during Load. Check Logs.");
		}
	}
	
	//BillItemFunctions
	
	public void addBillItemEvent(ActionEvent actionEvent) {
		try {
		if(billItemValidation()) {
		setBillItemBean();
		if(billItemServices.addBillItem(billItemBean)) {
			successAlert("Bill Item Successfully");
		}else {
			//Alert
			failureAlert("Bill Item with Serial Number "+billItemBean.getSerialNumber()+" already exists");
		}
		loadBillItemTable();
		billItemFieldsRefresh();
		calculateGrandTotal();
		}
		}catch(Exception e) {
			e.printStackTrace();
			SkvBillingApplication.logger.error("Exception during Adding BillItem", e);
			exceptionAlert("Exception during Adding BillItem. Check the log file");
			log(e);
		}
	}
	
	public void editBillItemEvent(ActionEvent actionEvent) {
		try {
		BillItemBean tempBillItemBean=NewInvoice_BillItemTable.getSelectionModel().getSelectedItem();
		BillItemIdentifier billItemIdentifer = new BillItemIdentifier(tempBillItemBean.getSerialNumber(),Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()),financialYearService.getFinancialYear());
		BillItemBean selectedBillItemBean= billItemServices.findBillItemById(billItemIdentifer);
		populateBillItemFieldsForUpdate(selectedBillItemBean);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during BillItem Edit", e);
			e.printStackTrace();
			exceptionAlert("Exception during Editing BillItem. Check the log file");
			log(e);
		}
	}
	public void updateBillItemEvent(ActionEvent actionEvent) {
		try {
		if(billItemValidation()) {
		setBillItemBean();
		billItemServices.updateBillItem(billItemBean);
		loadBillItemTable();
		billItemFieldsRefresh();
		NewInvoice_ItemAddButton.setDisable(false);
		NewInvoice_ItemUpdateButton.setVisible(false);
		calculateGrandTotal();
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Updating Bill Item", e);
			e.printStackTrace();
			exceptionAlert("Exception during Updating BillItem. Check the log file");
			log(e);
		}
	}
	public void deleteBillItemEvent(ActionEvent actionEvent) {
		try {
		BillItemBean tempBillItemBean=NewInvoice_BillItemTable.getSelectionModel().getSelectedItem();
		billItemServices.deleteBillItem(tempBillItemBean);
		loadBillItemNumber();
		loadBillItemTable();
		calculateGrandTotal();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Deletion of BillItem", e);
			e.printStackTrace();
			exceptionAlert("Exception during Deleting BillItem. Check the log file");
			log(e);
		}
	}
	
	public void cancelBillItemEvent(ActionEvent actionEvent) {
		billItemFieldsRefresh();
		NewInvoice_ItemAddButton.setDisable(false);
		NewInvoice_ItemUpdateButton.setVisible(false);
	}
	
	private void loadBillItemTable() {
		try {
		BillItemList.clear();
		BillItemList.addAll(billItemServices.getAllBillItemforInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear()));
		NewInvoice_BillItemTable.getSortOrder().add(NewInvoice_TableSerialNumber);
		NewInvoice_BillItemTable.setItems(BillItemList);
		NewInvoice_BillItemTable.sort();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Loading BillItem Table", e);
			e.printStackTrace();
			exceptionAlert("Exception during Loading BillItem Table. Check the log file");
			log(e);
		}
	}
	
	private void setBillItemBean() {
		try {
		billItemBean = new BillItemBean();
		billItemBean.setSerialNumber(Integer.valueOf(NewInvoice_ItemSerialNumberTextField.getText()));
		billItemBean.setInvoiceNumber(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()));
		billItemBean.setItemName(NewInvoice_ItemNameTextField.getText());
		billItemBean.setItemCode(NewInvoice_ItemCodeTextField.getText());
		billItemBean.setItemUnit(NewInvoice_ItemUnitTextField.getText());
		billItemBean.setNoOfPackages(Integer.valueOf(NewInvoice_NumberOfPackagesTextField.getText()));
		billItemBean.setWeightPerPackage(Double.valueOf(NewInvoice_WeightPerPackageTextField.getText()));
		billItemBean.setTotalWeight(Double.valueOf(NewInvoice_TotalWeightTextField.getText()));
		billItemBean.setRatePerUnit(Double.valueOf(NewInvoice_RatePerTextField.getText()));
		billItemBean.setPortionOfUnit(Double.valueOf(NewInvoice_RatePerUnitTextField.getText()));
		billItemBean.setItemAmount(Double.valueOf(NewInvoice_ItemAmountTextField.getText()));
		
		billItemBean.setTotalItemAmount(Double.valueOf(NewInvoice_TotalItemAmountTextField.getText()));
		billItemBean.setFinancialYear(financialYearService.getFinancialYear());
		if(NewInvoice_IGSTRadioButton.isSelected()) {
			billItemBean.setIsIGSTSelected(true);
			billItemBean.setIGSTPercentage(Double.valueOf(NewInvoice_IGSTPercentageTextField.getText()));
			billItemBean.setIGSTAmount(Double.valueOf(NewInvoice_IGSTAmountTextField.getText()));
			
			billItemBean.setCGSTPercentage(0);
			billItemBean.setCGSTAmount(0);
			billItemBean.setSGSTPercentage(0);
			billItemBean.setSGSTAmount(0);
			
		}else if(NewInvoice_CGSTSGSTRadioButton.isSelected()) {
			billItemBean.setIsIGSTSelected(false);
			billItemBean.setCGSTPercentage(Double.valueOf(NewInvoice_CGSTPercentageTextField.getText()));
			billItemBean.setCGSTAmount(Double.valueOf(NewInvoice_CGSTAmountTextField.getText()));
			billItemBean.setSGSTPercentage(Double.valueOf(NewInvoice_SGSTPercentageTextField.getText()));
			billItemBean.setSGSTAmount(Double.valueOf(NewInvoice_SGSTAmountTextField.getText()));
			
			billItemBean.setIGSTPercentage(0);
			billItemBean.setIGSTAmount(0);
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during setting values of BillItem", e);
			e.printStackTrace();
			exceptionAlert("Exception during setting values of BillItem. Check the log file");
			log(e);
		}
	}
	
	private void populateBillItemFieldsForUpdate(BillItemBean billItemBean) {
		NewInvoice_ItemAddButton.setDisable(true);
		NewInvoice_ItemUpdateButton.setVisible(true);
		NewInvoice_ItemSerialNumberTextField.setText(String.valueOf(billItemBean.getSerialNumber()));
		NewInvoice_ItemNameTextField.setText(billItemBean.getItemName());
		NewInvoice_ItemCodeTextField.setText(billItemBean.getItemCode());
		NewInvoice_ItemUnitTextField.setText(billItemBean.getItemUnit());
		NewInvoice_NumberOfPackagesTextField.setText(String.valueOf(billItemBean.getNoOfPackages()));
		NewInvoice_WeightPerPackageTextField.setText(String.valueOf(billItemBean.getWeightPerPackage()));
		NewInvoice_TotalWeightTextField.setText(String.valueOf(billItemBean.getTotalWeight()));
		NewInvoice_RatePerTextField.setText(String.valueOf(billItemBean.getRatePerUnit()));
		NewInvoice_RatePerUnitTextField.setText(String.valueOf(billItemBean.getPortionOfUnit()));
		NewInvoice_ItemAmountTextField.setText(String.valueOf(billItemBean.getItemAmount()));
		if(billItemBean.getIsIGSTSelected()) {
			NewInvoice_IGSTRadioButton.setSelected(true);
			setGSTVisibility(true,false);
		}else {
			NewInvoice_CGSTSGSTRadioButton.setSelected(true);
			setGSTVisibility(false,true);
		}
		NewInvoice_CGSTPercentageTextField.setText(String.valueOf(billItemBean.getCGSTPercentage()));
		NewInvoice_CGSTAmountTextField.setText(String.valueOf(billItemBean.getCGSTAmount()));
		NewInvoice_SGSTPercentageTextField.setText(String.valueOf(billItemBean.getSGSTPercentage()));
		NewInvoice_SGSTAmountTextField.setText(String.valueOf(billItemBean.getSGSTAmount()));
		NewInvoice_IGSTPercentageTextField.setText(String.valueOf(billItemBean.getIGSTPercentage()));
		NewInvoice_IGSTAmountTextField.setText(String.valueOf(billItemBean.getIGSTAmount()));
		NewInvoice_TotalItemAmountTextField.setText(String.valueOf(billItemBean.getTotalItemAmount()));
	}
	
	public void billItemFieldsRefresh() {
		NewInvoice_ItemSerialNumberTextField.clear();
		loadBillItemNumber();
		NewInvoice_ItemNameTextField.clear();
		NewInvoice_ItemCodeTextField.clear();
		NewInvoice_ItemUnitTextField.clear();
		NewInvoice_NumberOfPackagesTextField.clear();
		NewInvoice_WeightPerPackageTextField.clear();
		NewInvoice_TotalWeightTextField.clear();
		NewInvoice_RatePerTextField.clear();
		NewInvoice_RatePerUnitTextField.clear();
		NewInvoice_ItemAmountTextField.clear();
		NewInvoice_IGSTRadioButton.setSelected(true);
		setGSTVisibility(true,false);
		NewInvoice_CGSTPercentageTextField.clear();
		NewInvoice_CGSTAmountTextField.setText("0");
		NewInvoice_SGSTPercentageTextField.clear();
		NewInvoice_SGSTAmountTextField.setText("0");
		NewInvoice_IGSTPercentageTextField.clear();
		NewInvoice_IGSTAmountTextField.setText("0");
		NewInvoice_TotalItemAmountTextField.clear();
	}	
	
	public void IGSTSelectionEvent(ActionEvent actionEvent) {
		setGSTVisibility(true, false);	
	}
	
	public void CGSTSelectionEvent(ActionEvent actionEvent) {
		setGSTVisibility(false, true);		
	}
	
	private void setGSTVisibility(boolean igst, boolean cgst) {
		NewInvoice_IGSTPercentageTextField.setVisible(igst);
		NewInvoice_IGSTAmountTextField.setVisible(igst);
		NewInvoice_IGSTLabel.setVisible(igst);
		if(igst) {
			NewInvoice_IGSTPercentageTextField.clear();
			NewInvoice_IGSTAmountTextField.setText("0");
		}
		
		NewInvoice_CGSTPercentageTextField.setVisible(cgst);
		NewInvoice_CGSTAmountTextField.setVisible(cgst);
		NewInvoice_CGSTLabel.setVisible(cgst);
		NewInvoice_SGSTPercentageTextField.setVisible(cgst);
		NewInvoice_SGSTAmountTextField.setVisible(cgst);
		NewInvoice_SGSTLabel.setVisible(cgst);
		if(cgst) {
			NewInvoice_CGSTPercentageTextField.clear();
			NewInvoice_CGSTAmountTextField.setText("0");
			NewInvoice_SGSTPercentageTextField.clear();
			NewInvoice_SGSTAmountTextField.setText("0");
		}
	}
	
	public void calculateTotalWeightEvent(ActionEvent actionEvent) {
		try {
		if(validationForTotalWeight()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setNoOfPackages(Integer.valueOf(NewInvoice_NumberOfPackagesTextField.getText()));
			tempBillItemBean.setWeightPerPackage(Double.valueOf(NewInvoice_WeightPerPackageTextField.getText()));
			NewInvoice_TotalWeightTextField.setText(String.valueOf(billItemServices.calculateTotalWeight(tempBillItemBean)));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during calculation of Total Weigth", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of Total Weigth. Check the log file");
			log(e);
		}
	}
	
	public void calculateItemAmountEvent(ActionEvent actionEvent) {
		try {
		if(validationForItemAmount()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setRatePerUnit( Double.valueOf(NewInvoice_RatePerTextField.getText()));
			tempBillItemBean.setPortionOfUnit(Double.valueOf(NewInvoice_RatePerUnitTextField.getText()));
			tempBillItemBean.setTotalWeight(Double.valueOf(NewInvoice_TotalWeightTextField.getText()));
			NewInvoice_ItemAmountTextField.setText(String.valueOf(billItemServices.calculateItemAmount(tempBillItemBean)));
		}}
		catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of Item Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of Item Amount. Check the log file");
			log(e);
		}
	}
	
	public void calculateIGSTAmountEvent(ActionEvent actionEvent) {
		try {
		if(validationForIGSTCalculation()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setItemAmount(Double.valueOf(NewInvoice_ItemAmountTextField.getText()));
			tempBillItemBean.setIGSTPercentage(Double.valueOf(NewInvoice_IGSTPercentageTextField.getText()));
			NewInvoice_IGSTAmountTextField.setText(String.valueOf(billItemServices.calculateIGST(tempBillItemBean)));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during calculation of IGST Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of IGST Amount. Check the log file");
			log(e);

		}
	}
	
	public void calculateCGSTAmountEvent(ActionEvent actionEvent) {
		try {
		if(validationForCGSTCalculation()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setItemAmount(Double.valueOf(NewInvoice_ItemAmountTextField.getText()));
			tempBillItemBean.setCGSTPercentage(Double.valueOf(NewInvoice_CGSTPercentageTextField.getText()));
			NewInvoice_CGSTAmountTextField.setText(String.valueOf(billItemServices.calculateCGST(tempBillItemBean)));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of CGST Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of GST Amount. Check the log file");
			log(e);

		}
	}
	
	public void calculateSGSTAmountEvent(ActionEvent actionEvent) {
		try {
		if(validationForSGSTCalculation()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setItemAmount(Double.valueOf(NewInvoice_ItemAmountTextField.getText()));
			tempBillItemBean.setSGSTPercentage(Double.valueOf(NewInvoice_SGSTPercentageTextField.getText()));
			NewInvoice_SGSTAmountTextField.setText(String.valueOf(billItemServices.calculateSGST(tempBillItemBean)));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of SGST Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of SGST Amount. Check the log file");
			log(e);

		}
	}
	
	public void calculateTotalItemAmountEvent(ActionEvent actionEvent) {
		try{
			if(validationForTotalItemAmount()) {
			BillItemBean tempBillItemBean = new BillItemBean();
			tempBillItemBean.setItemAmount(Double.valueOf(NewInvoice_ItemAmountTextField.getText()));
			tempBillItemBean.setIGSTAmount(Double.valueOf(NewInvoice_IGSTAmountTextField.getText()));
			tempBillItemBean.setCGSTAmount(Double.valueOf(NewInvoice_CGSTAmountTextField.getText()));
			tempBillItemBean.setSGSTAmount(Double.valueOf(NewInvoice_SGSTAmountTextField.getText()));
			NewInvoice_TotalItemAmountTextField.setText(String.valueOf(billItemServices.calculateTotalItemAmount(tempBillItemBean)));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of Total Item Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during calculation of Total Item Amount. Check the log file");
			log(e);

		}
	}
	
	
		//Invoice Event
	
	public void addInvoiceEvent(ActionEvent actionEvent) {
		
		try {
			if(invoiceValidation()) {
		setInvoiceBean();
		if(invoiceServices.addInvoice(invoiceBean)) {
			successAlert("Invoice Added Successfully");
		}else {
			failureAlert("Invoice with Number "+invoiceBean.getInvoiceNumber()+" already Exists");
		}
		loadInvoiceTable();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Print");
		alert.setContentText("Do you want to Print the Invoice?");
		 Optional<ButtonType> option = alert.showAndWait();
		 
		 if (option.get() != null) {
	         
	      if (option.get() == ButtonType.OK) {
	    	NewInvoice_PrintButton.fire();
	      } 
	   }
		
		refreshInvoiceFields();
			}
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Add", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Add. Check the log file");
			log(e);

		}
	}
	
	public void invoiceEditEvent(ActionEvent actionEvent) {
		InvoiceBean tempInvoiceBean = InvoiceList_InvoiceTable.getSelectionModel().getSelectedItem();
		populateInvoiceFieldsForUpdate(tempInvoiceBean);
		billItemFieldsRefresh();
	}
	
	public void invoiceUpdateEvent() {
		try {
			if(invoiceValidation()) {
		setInvoiceBean();
		invoiceServices.updateInvoice(invoiceBean);
		loadInvoiceTable();
		loadBillItemNumber();
		refreshInvoiceFields();
		NewInvoice_InvoiceSaveButton.setDisable(false);
		NewInvoice_InvoiceUpdateButton.setVisible(false);
		NewInvoice_PrintButton.setVisible(false);
			}
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Update", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Update. Check the log file");
			log(e);

		}
	}
	
	public void invoiceDeleteEvent(ActionEvent actionEvent) {
		try {
		InvoiceBean tempInvoiceBean = InvoiceList_InvoiceTable.getSelectionModel().getSelectedItem();
		billItemServices.deleteBillItemWithInvoice(tempInvoiceBean.getInvoiceNumber(), tempInvoiceBean.getFinancialYear());
		invoiceServices.deleteInvoice(tempInvoiceBean);
		loadInvoiceTable();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Delete", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Delete. Check the log file");
			log(e);

		}
	}
	
	public void invoiceCancelEvent(ActionEvent actionEvent) {
		try {
		if(!invoiceServices.isInvoiceExists(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()),financialYearService.getFinancialYear())) {
			billItemServices.deleteBillItemWithInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear());
		}
		refreshInvoiceFields();
		setGSTVisibility(true,false);
		billItemFieldsRefresh();
		NewInvoice_ItemAddButton.setDisable(false);
		NewInvoice_ItemUpdateButton.setVisible(false);
		NewInvoice_IGSTAmountTextField.setText("0");
		NewInvoice_CGSTAmountTextField.setText("0");
		NewInvoice_SGSTAmountTextField.setText("0");
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Cancel", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Cancel. Check the log file");
			log(e);

		}
	}
	
	public void searchInvoiceEvent(ActionEvent actionEvent) {
		try {
		String field=InvoiceList_SearchFieldChoiceBox.getValue();
		String value=InvoiceList_SearchValueTextField.getText();
		if(field.equals("Invoice Number")) {
			loadInvoiceTableAfterSearch(invoiceServices.searchInvoiceByInvoiceNumber(value,financialYearService.getFinancialYear()));
		}else if(field.equals("Customer Name")) {
			loadInvoiceTableAfterSearch(invoiceServices.searchInvoiceByCustomerName(value,financialYearService.getFinancialYear()));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Eception during Invoice Search", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Search. Check the log file");
			log(e);

		}
	}
	
	public void searchClearInvoiceEvent(ActionEvent actionEvent) {
		InvoiceList_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		InvoiceList_SearchValueTextField.clear();
		loadInvoiceTable();
	}
	
	private void loadInvoiceTable() {
		try{
		InvoiceList.clear();
		InvoiceList.addAll(invoiceServices.getAllInvoice(financialYearService.getFinancialYear()));
		InvoiceList_InvoiceTableInvoiceNumber.setSortType(TableColumn.SortType.ASCENDING);
		InvoiceList_InvoiceTable.getSortOrder().add(InvoiceList_InvoiceTableInvoiceNumber);
		InvoiceList_InvoiceTable.setItems(InvoiceList);
		InvoiceList_InvoiceTable.sort();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Table Load", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Table Load. Check the log file");
			log(e);
		}
	}
	
	private void loadInvoiceTableAfterSearch(List<InvoiceBean> list) {
		
		InvoiceList_InvoiceTableInvoiceNumber.setSortType(TableColumn.SortType.ASCENDING);
		InvoiceList_InvoiceTable.getSortOrder().add(InvoiceList_InvoiceTableInvoiceNumber);
		ObservableList<InvoiceBean> observableList=FXCollections.observableArrayList();
		observableList.addAll(list);
		InvoiceList_InvoiceTable.setItems(observableList);
		InvoiceList_InvoiceTable.sort();
	}
	
	private void setInvoiceBean() {
		try {
		invoiceBean= new InvoiceBean();
		invoiceBean.setInvoiceNumber(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()));
		invoiceBean.setInvoiceDate(NewInvoice_DateDatePicker.getValue());
		invoiceBean.setCustomerName(NewInvoice_CustomerNameTextField.getText());
		invoiceBean.setCustomerGST(NewInvoice_CustomerGSTTextField.getText());
		invoiceBean.setCustomerAddress(NewInvoice_BillingAddressTextArea.getText());
		invoiceBean.setLRNumber(NewInvoice_LRNumberTextField.getText());
		invoiceBean.setLRDate(NewInvoice_LRDateDatePicker.getValue());
		invoiceBean.setShippingAddress(NewInvoice_ShippingAddressTextArea.getText());
		invoiceBean.setDestination(NewInvoice_DestinationTextField.getText());
		invoiceBean.setDispatchThrough(NewInvoice_DeliveryThroughTextField.getText());
		invoiceBean.setTermsOfDelivery(NewInvoice_TermsOfDeliveryTextArea.getText());
		invoiceBean.setTotalAmount(Double.valueOf(NewInvoice_TotalAmountTextField.getText()));
		invoiceBean.setRoundoff(Double.valueOf(NewInvoice_RoundOffTextField.getText()));
		invoiceBean.setGrandTotal(Double.valueOf(NewInvoice_GrandTotalTextField.getText()));
		invoiceBean.setFinancialYear(financialYearService.getFinancialYear());
		invoiceBean.setItems(billItemServices.getAllBillItemforInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear()));
		invoiceBean.setTotalTaxAmount(invoiceServices.calculateTotalTaxAmount(billItemServices.getAllBillItemforInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear())));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Invoice Values Loading", e);
			e.printStackTrace();
			exceptionAlert("Exception during Invoice Values Loading. Check the log file");
			log(e);

		}
	}
	
	private void populateInvoiceFieldsForUpdate(InvoiceBean invoiceBean) {
		NewInvoice_InvoiceNumberTextField.setText(String.valueOf(invoiceBean.getInvoiceNumber()));
		NewInvoice_DateDatePicker.setValue(invoiceBean.getInvoiceDate());
		NewInvoice_CustomerNameTextField.setText(invoiceBean.getCustomerName());;
		NewInvoice_CustomerGSTTextField.setText(invoiceBean.getCustomerGST());
		NewInvoice_BillingAddressTextArea.setText(invoiceBean.getCustomerAddress());
		NewInvoice_LRNumberTextField.setText(invoiceBean.getLRNumber());
		NewInvoice_LRDateDatePicker.setValue(invoiceBean.getLRDate());
		NewInvoice_ShippingAddressTextArea.setText(invoiceBean.getShippingAddress());
		NewInvoice_DestinationTextField.setText(invoiceBean.getDestination());
		NewInvoice_DeliveryThroughTextField.setText(invoiceBean.getDispatchThrough());
		NewInvoice_TermsOfDeliveryTextArea.setText(invoiceBean.getTermsOfDelivery());;
		NewInvoice_TotalAmountTextField.setText(String.valueOf(invoiceBean.getTotalAmount()));
		NewInvoice_RoundOffTextField.setText(String.valueOf(invoiceBean.getRoundoff()));
		NewInvoice_GrandTotalTextField.setText(String.valueOf(invoiceBean.getGrandTotal()));;
		loadBillItemTable();
		loadBillItemNumber();
		InvoiceTab_NewInvoiceScrollPane.setVisible(true);
		InvoiceTab_NewInvoiceScrollPane.pseudoClassStateChanged(errorClass, true);
		InvoiceTab_InvoiceListScrollPane.setVisible(false);
		NewInvoice_InvoiceSaveButton.setDisable(true);
		NewInvoice_InvoiceUpdateButton.setVisible(true);
		NewInvoice_PrintButton.setVisible(true);
		
		
	}
	
	public void refreshInvoiceFields() {
		
		NewInvoice_InvoiceNumberTextField.clear();
		loadInvoiceNumber();
		loadBillItemNumber();
		NewInvoice_DateDatePicker.setValue(LocalDate.now());
		NewInvoice_CustomerNameTextField.clear();
		NewInvoice_CustomerGSTTextField.clear();
		NewInvoice_BillingAddressTextArea.clear();
		NewInvoice_LRNumberTextField.clear();
		NewInvoice_LRDateDatePicker.getEditor().clear();
		NewInvoice_ShippingAddressTextArea.clear();
		NewInvoice_DestinationTextField.clear();
		NewInvoice_DeliveryThroughTextField.clear();
		NewInvoice_TermsOfDeliveryTextArea.clear();
		NewInvoice_TotalAmountTextField.clear();
		NewInvoice_RoundOffTextField.clear();
		NewInvoice_GrandTotalTextField.clear();
		NewInvoice_BillItemTable.getItems().clear();		
	}
	
	public void calculateTotalAmount() {
		try {
		if(validationForTotalAmount()) 
			NewInvoice_TotalAmountTextField.setText(String.valueOf(invoiceServices.calculateTotalAmount(billItemServices.getAllBillItemforInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear()))));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of Total Amount", e);
			e.printStackTrace();
			exceptionAlert("Exception during Calculation of Total Amount. Check the log file");
			log(e);

		}
	}
	
	public void calculateRoundOff() {
		try {
		if(validationForRoundOff()) 
			NewInvoice_RoundOffTextField.setText(String.valueOf(invoiceServices.calculateRoundOff(NewInvoice_TotalAmountTextField.getText())));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of RoundOff", e);
			e.printStackTrace();
			exceptionAlert("Exception during Calculation of RoundOff. Check the log file");
			log(e);

		}
	}
	
	public void calculateRoundOffEvent(ActionEvent actionEvent) {
		try {
		if(validationForRoundOff()) 
			NewInvoice_RoundOffTextField.setText(String.valueOf(invoiceServices.calculateRoundOff(NewInvoice_TotalAmountTextField.getText())));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of RoundOff", e);
			e.printStackTrace();
			exceptionAlert("Exception during Calculation of roundOff. Check the log file");
			log(e);

		}
	}
	
	public void calculateGrandTotalEvent(ActionEvent actionEvent) {
		try {
		if(validationForGrandTotal())
			NewInvoice_GrandTotalTextField.setText(String.valueOf(invoiceServices.calculateGrandTotal(Double.valueOf(NewInvoice_TotalAmountTextField.getText()),Double.valueOf(NewInvoice_RoundOffTextField.getText()))));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of Grand Total", e);
			e.printStackTrace();
			exceptionAlert("Exception during Calculation of Grand Total. Check the log file");
			log(e);
		}
	}
	
	public void calculateGrandTotal() {
		try {
		calculateTotalAmount();
		calculateRoundOff();
		if(validationForGrandTotal())
			NewInvoice_GrandTotalTextField.setText(String.valueOf(invoiceServices.calculateGrandTotal(Double.valueOf(NewInvoice_TotalAmountTextField.getText()),Double.valueOf(NewInvoice_RoundOffTextField.getText()))));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Calculation of Grand Total", e);
			e.printStackTrace();
			exceptionAlert("Exception during Calculation of Grand Total. Check the log file");
			log(e);
		}
	}
	
	public void deliveryAddressEvent(ActionEvent actionEvent) {
		NewInvoice_ShippingAddressTextArea.setText(NewInvoice_BillingAddressTextArea.getText());
		NewInvoice_ShippingAddressTextArea.requestFocus();
	}
	
	////////////////////
	@FXML
	private Button NewInvoice_PrintButton;
	@Autowired
	PrintService printService;
	public void printInvoiceEvent(ActionEvent actionEvent) {
		 try {
		if(invoiceValidation()) {
		setInvoiceBean();
	
	printService.createInvoice(invoiceBean);
	saveAndPrint();}
		else {
			NewInvoice_InvoiceFieldErrorText.setText("Error Exists..Check the Data Entered.");
		}
	 }catch (Exception e){
		 	SkvBillingApplication.logger.error("Exception during Creating Invoice", e);
			e.printStackTrace();
			exceptionAlert("Exception during Creating Invoice. Check the log file");
			log(e);
	 }
	
	}
	
	public void printPreview(File file) {
		
		Rectangle rect = new Rectangle(100, 100, 200, 300);
		Pane root = new Pane(rect);
		root.setPrefSize(500, 500);

		Parent content = root;
		Scene scene = new Scene(content);
		
		Stage window = new Stage();
		window.setScene(scene);
		
		PDFDisplayer displayer = new PDFDisplayer();
		
		window.setScene(new Scene(displayer.toNode())); 
		
		try {
			SkvBillingApplication.logger.info("Opening print preview");
			displayer.loadPDF(file);
		} catch (Exception e) {
			
			SkvBillingApplication.logger.error("Exception during Print Preview",e);
			e.printStackTrace();
			log(e);
		}
		window.showAndWait();
	}
	
	public void saveAndPrint() {
		

				System.setProperty("java.awt.headless", "false");
				PrinterJob printerJob = PrinterJob.getPrinterJob();
		      
		        PDDocument document;
				try {
					document = PDDocument.load(new File("D:\\Invoice\\Invoice.pdf"));
				
		        printerJob.setPageable(new PDFPageable(document));
		        
		        if (printerJob.printDialog()==true) {
		            try {
		                printerJob.print();
		            } catch (PrinterException e) {
		                e.printStackTrace();
		                SkvBillingApplication.logger.error(e.getStackTrace().toString());
						exceptionAlert("Printer Exception during Invoice Print. Check the log file");
						log(e);
		            }
		        }
		        
				} catch (IOException e1) {
					
					SkvBillingApplication.logger.error(e1.getStackTrace().toString());
					exceptionAlert("IOException during Invoice Print. Check the log file");
					log(e1);

					e1.printStackTrace();
				} catch (Exception e1) {
					
					SkvBillingApplication.logger.error(e1.getStackTrace().toString());
					exceptionAlert("Printer Exception during Invoice Print. Check the log file");

					e1.printStackTrace();
					log(e1);
				}
	}
	
	
	/***------------------------------Customer Tab------------------------------***/
	
	@FXML
	private TextField CustomerTab_CustomerNameTextField;
	@FXML
	private TextField CustomerTab_CustomerGSTTextField;
	@FXML
	private TextArea CustomerTab_CustomerAddressTextArea;
	@FXML
	private Button CustomerTab_AddButton;
	@FXML
	private Button CustomerTab_CancelButton;
	@FXML
	private Button CustomerTab_UpdateButton;
	@FXML
	private MenuItem CustomerTab_EditMenuItem;
	@FXML
	private MenuItem CustomerTab_DeleteMenuItem;
	@FXML
	private Text CustomerTab_GSTErrorText;
	@FXML
	private Text CustomerTab_NameErrorText;
	
	@FXML
	private ChoiceBox<String> CustomerTab_SearchFieldChoiceBox;
	@FXML
	private TextField CustomerTab_SearchValueTextField;
	@FXML
	private Button CustomerTab_SearchButton;
	@FXML 
	private Button CustomerTab_ClearSearchButton;
	
	@FXML
	private TableView<CustomerBean> CustomerTab_CustomerTable;
	@FXML 
	private TableColumn<CustomerBean, String> CustomerTab_TableCustomerName;
	@FXML
	private TableColumn<CustomerBean, String> CustomerTab_TableCustomerGST;
	@FXML 
	private TableColumn<CustomerBean, String> CustomerTab_TableCustomerAddress;
	
	private ObservableList<CustomerBean> CustomerList = FXCollections.observableArrayList();
	
	@Autowired
	private CustomerServices customerServices;
	@Autowired
	private CustomerBean customerBean;
	
	public void addCustomerEvent(ActionEvent actionEvent) {
		try{
			if(validateCustomerData()) {
			CustomerTab_GSTErrorText.setText(null);
			CustomerTab_NameErrorText.setText(null);
		setCustomerBean();
		if(customerServices.addCustomer(customerBean)) {
			successAlert("Customer Successfully Added");
		}else {
			failureAlert("Customer with GST "+customerBean.getCustomerGST()+" already exists");
		}
		customerFieldsRefresh();
		loadCustomerTable();
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Add", e);
			e.printStackTrace();
			exceptionAlert("Exception during Customer Add. Check the log file");
			log(e);
		}
	}
	
	public void editCustomerEvent(ActionEvent actionEvent) {
		CustomerBean tempCustomerBean = CustomerTab_CustomerTable.getSelectionModel().getSelectedItem();
		populateCustomerFieldsForUpdate(tempCustomerBean);
		
	}
	
	public void updateCustomerEvent(ActionEvent actionEvent) {
		try{
			if(validateCustomerData()) {
			CustomerTab_GSTErrorText.setText(null);
			CustomerTab_NameErrorText.setText(null);
		setCustomerBean();
		customerServices.updateCustomer(customerBean);
		loadCustomerTable();
		customerFieldsRefresh();
		CustomerTab_AddButton.setDisable(false);
		CustomerTab_UpdateButton.setVisible(false);
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Update", e);
			e.printStackTrace();
			exceptionAlert("Exception during Customer Update. Check the log file");
			log(e);
		}
	}
	
	public void deleteCustomerEvent(ActionEvent actionEvent) {
		try {
		CustomerBean tempCustomerBean = CustomerTab_CustomerTable.getSelectionModel().getSelectedItem();
		customerServices.deleteCustomer(tempCustomerBean);
		loadCustomerTable();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Delete", e);
			e.printStackTrace();
			exceptionAlert("Exception during Customer Delete. Check the log file");
			log(e);
		}
	}
	
	public void searchCustomerEvent(ActionEvent actionEvent) {
		try {
		String field=CustomerTab_SearchFieldChoiceBox.getValue();
		String value=CustomerTab_SearchValueTextField.getText();
		if(field.equals("Customer Name")) {
			loadCustomerTableAfterSearch((customerServices.searchCustomerByName(value)));
		}else if(field.equals("GST Number")) {
			loadCustomerTableAfterSearch(customerServices.searchCustomerByGST(value));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Search", e);
			e.printStackTrace();
			exceptionAlert("Exception during Customer Search. Check the log file");
			log(e);
		}
	}
	
	public void searchClearCustomerEvent(ActionEvent actionEvent) {
		CustomerTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		CustomerTab_SearchValueTextField.clear();
		loadCustomerTable();
	}
	
	public void cancelCustomerEvent(ActionEvent actionEvent) {
		customerFieldsRefresh();
		CustomerTab_AddButton.setDisable(false);
		CustomerTab_UpdateButton.setVisible(false);
	}
	
	public void customerFieldsRefresh() {
		CustomerTab_CustomerNameTextField.clear();
		CustomerTab_CustomerGSTTextField.clear();
		CustomerTab_CustomerAddressTextArea.clear();
	}
	
	private void setCustomerBean() {
		customerBean = new CustomerBean();
		customerBean.setCustomerName(this.CustomerTab_CustomerNameTextField.getText());
		customerBean.setCustomerGST(this.CustomerTab_CustomerGSTTextField.getText());
		customerBean.setCustomerAddress(this.CustomerTab_CustomerAddressTextArea.getText());
	}
	
	public void loadCustomerTable() {
		try {
		CustomerList.clear();
		CustomerList.addAll(customerServices.getAllCustomers());
		CustomerTab_TableCustomerName.setSortType(TableColumn.SortType.ASCENDING);
		CustomerTab_CustomerTable.getSortOrder().add(CustomerTab_TableCustomerName);
		CustomerTab_CustomerTable.setItems(CustomerList);
		CustomerTab_CustomerTable.sort();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Customer Table Load", e);
			e.printStackTrace();
			exceptionAlert("Exception during Customer Table Load. Check the log file");
			log(e);
		}
	}
	
	private void loadCustomerTableAfterSearch(List<CustomerBean> list) {
		CustomerTab_TableCustomerName.setSortType(TableColumn.SortType.ASCENDING);
		CustomerTab_CustomerTable.getSortOrder().add(CustomerTab_TableCustomerName);
		ObservableList<CustomerBean> observableList=FXCollections.observableArrayList();
		observableList.addAll(list);
		CustomerTab_CustomerTable.setItems(observableList);
		CustomerTab_CustomerTable.sort();
	}
	
	private void populateCustomerFieldsForUpdate(CustomerBean customer) {
		CustomerTab_AddButton.setDisable(true);
		CustomerTab_UpdateButton.setVisible(true);
		CustomerTab_CustomerNameTextField.setText(customer.getCustomerName());
		CustomerTab_CustomerGSTTextField.setText(customer.getCustomerGST());
		CustomerTab_CustomerAddressTextArea.setText(customer.getCustomerAddress());
	}
	
	
	
	//To populate Invoice Tab
	private List<String> getCustomerNameList() throws Exception{
		List<CustomerBean> customerBeanList=customerServices.getAllCustomers();
		List<String> CustomerNameList = new ArrayList<String>();
		for(CustomerBean bean:customerBeanList) {
			CustomerNameList.add(bean.getCustomerName());
		}
		return CustomerNameList;
	}
	
	private List<String> getCustomerGSTList() throws Exception{
		List<CustomerBean> customerBeanList=customerServices.getAllCustomers();
		List<String> CustomerGSTList = new ArrayList<String>();
		for(CustomerBean bean:customerBeanList) {
			CustomerGSTList.add(bean.getCustomerGST());
		}
		return CustomerGSTList;
	}
	
	
	
	// Alerts
	private void successAlert(String message){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/Stylesheets/DialogBox.css").toExternalForm());
		dialogPane.getStyleClass().add("dialogBox");
		dialogPane.applyCss();
		alert.showAndWait();
	}
	
	private void failureAlert(String message){
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/Stylesheets/DialogBox.css").toExternalForm());
		dialogPane.getStyleClass().add("dialogBox");
		dialogPane.applyCss();
		alert.showAndWait();
	}
	

	/***------------------------------Item Tab-----------------------------***/
	
	@FXML
	private TextField ItemTab_ItemNameTextField;
	@FXML
	private TextField ItemTab_ItemDescriptionTextField;
	@FXML 
	private TextField ItemTab_ItemCodeTextField;
	@FXML
	private TextField ItemTab_UnitTextField;
	@FXML
	private Button ItemTab_AddButton;
	@FXML
	private Button ItemTab_CancelButton;
	@FXML
	private Button ItemTab_UpdateButton;
	@FXML
	private MenuItem ItemTab_EditMenuItem;
	@FXML
	private MenuItem ItemTab_DeleteMenuItem;
	@FXML
	private Text ItemTab_ItemNameErrorText;
	@FXML
	private Text ItemTab_ItemCodeErrorText;
	
	@FXML
	private ChoiceBox<String> ItemTab_SearchFieldChoiceBox;
	@FXML
	private TextField ItemTab_SearchValueTextField;
	@FXML
	private Button ItemTab_SearchButton;
	@FXML
	private Button ItemTab_ClearSearchButton;
	
	@FXML
	private TableView<ItemBean> ItemTab_ItemTable;
	@FXML
	private TableColumn<ItemBean,String> ItemTab_TableItemName;
	@FXML
	private TableColumn<ItemBean,String> ItemTab_TableItemDescription;
	@FXML
	private TableColumn<ItemBean,String> ItemTab_TableCode;
	@FXML
	private TableColumn<ItemBean,String> ItemTab_TableUnit;
	
	private ObservableList<ItemBean> ItemList = FXCollections.observableArrayList();
	
	@Autowired
	private ItemBean itemBean;
	@Autowired
	private ItemServices itemServices;
	
	public void addItemEvent(ActionEvent actionEvent) {
		try {
		if(validateItemData()) {
		setItemBean();
		if(itemServices.addItem(itemBean)) {
			successAlert("Item added Successfully");
		}else {
			failureAlert("Item with code "+itemBean.getItemCode()+" already exists");
		}
		loadItemTable();
		itemFieldsRefresh();
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Add", e);
			e.printStackTrace();
			exceptionAlert("Exception during Item Add. Check the log file");
			log(e);
		}
	}
	
	public void editItemEvent(ActionEvent actionEvent) {
		ItemBean tempItemBean = ItemTab_ItemTable.getSelectionModel().getSelectedItem();
		populateItemFieldsForUpdate(tempItemBean);
	}
		
	public void updateItemEvent(ActionEvent actionEvent) {
		try {
		if(validateItemData()) {
		setItemBean();
		itemServices.updateItem(itemBean);
		loadItemTable();
		itemFieldsRefresh();
		ItemTab_AddButton.setDisable(false);
		ItemTab_UpdateButton.setVisible(false);
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Update", e);
			e.printStackTrace();
			exceptionAlert("Exception during Item Update. Check the log file");
			log(e);
		}
	}
	
	public void deleteItemEvent(ActionEvent actionEvent) {
		try {
		ItemBean tempItemBean = ItemTab_ItemTable.getSelectionModel().getSelectedItem();
		itemServices.deleteItem(tempItemBean);
		loadItemTable();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Delete", e);
			e.printStackTrace();
			exceptionAlert("Exception during Item Delete. Check the log file");
			log(e);
		}
	}
	
	public void searchItemEvent(ActionEvent actionEvent) {
		try {
		String field=ItemTab_SearchFieldChoiceBox.getValue();
		String value=ItemTab_SearchValueTextField.getText();
		if(field.equals("Item Name")) {
			loadItemTableAfterSearch(itemServices.searchItemByName(value));
		}else if(field.equals("HSNC Code")) {
			loadItemTableAfterSearch(itemServices.searchItemByCode(value));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Search", e);
			e.printStackTrace();
			exceptionAlert("Exception during Item Search. Check the log file");
			log(e);
		}
	}
	
	public void searchClearItemEvent(ActionEvent actionEvent) {
		ItemTab_SearchValueTextField.clear();
		ItemTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		loadItemTable();
	}
	
	public void cancelItemEvent(ActionEvent actionEvent) {
		itemFieldsRefresh();
		ItemTab_AddButton.setDisable(false);
		ItemTab_UpdateButton.setVisible(false);
	}
	
	public void loadItemTable() {
		try {
		ItemList.clear();
		ItemList.addAll(itemServices.getAllItems());
		ItemTab_TableItemName.setSortType(TableColumn.SortType.ASCENDING);
		ItemTab_ItemTable.getSortOrder().add(ItemTab_TableItemName);
		ItemTab_ItemTable.setItems(ItemList);
		ItemTab_ItemTable.sort();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Item Table Load");
			e.printStackTrace();
			exceptionAlert("Exception during Item Table Load. Check the log file");
			log(e);
		}
	}
	
	private void loadItemTableAfterSearch(List<ItemBean> list) {
		ItemTab_TableItemName.setSortType(TableColumn.SortType.ASCENDING);
		ItemTab_ItemTable.getSortOrder().add(ItemTab_TableItemName);
		ObservableList<ItemBean> observableList=FXCollections.observableArrayList();
		observableList.addAll(list);
		ItemTab_ItemTable.setItems(observableList);
		ItemTab_ItemTable.sort();
	}
	
	private void setItemBean() {
		itemBean.setItemName(ItemTab_ItemNameTextField.getText());
		itemBean.setItemDescription(ItemTab_ItemDescriptionTextField.getText());
		itemBean.setItemCode(ItemTab_ItemCodeTextField.getText());
		itemBean.setItemUnit(ItemTab_UnitTextField.getText());
	}
	
	private void populateItemFieldsForUpdate(ItemBean itemBean) {
		ItemTab_AddButton.setDisable(true);
		ItemTab_UpdateButton.setVisible(true);
		ItemTab_ItemNameTextField.setText(itemBean.getItemName());
		ItemTab_ItemDescriptionTextField.setText(itemBean.getItemDescription());
		ItemTab_ItemCodeTextField.setText(itemBean.getItemCode());
		ItemTab_UnitTextField.setText(itemBean.getItemUnit());
	}
	
	public void itemFieldsRefresh() {
		ItemTab_ItemNameTextField.clear();
		ItemTab_ItemDescriptionTextField.clear();
		ItemTab_ItemCodeTextField.clear();
		ItemTab_UnitTextField.clear();
	}
	
	//To populate Invoice Fields
	private List<String> getItemNameList() throws Exception{
		List<ItemBean> itemBeanList=itemServices.getAllItems();
		List<String> ItemNameList = new ArrayList<String>();
		for(ItemBean item: itemBeanList) {
			ItemNameList.add(item.getItemName());
		}
		return ItemNameList;
	}
	
	private List<String> getItemCodeList() throws Exception{
		List<ItemBean> itemBeanList=itemServices.getAllItems();
		List<String> ItemCodeList =  new ArrayList<String>();
		for(ItemBean item: itemBeanList) {
			ItemCodeList.add(item.getItemCode());
		}
		return ItemCodeList;
	}
	
	/***-----------------------------Purchase Tab-------------------------------***/
	
	@FXML
	private TextField PurchaseTab_SerialNumberTextField;
	@FXML
	private TextField PurchaseTab_InvoiceNumberTextField;
	@FXML
	private DatePicker PurchaseTab_DateDatePicker;
	@FXML
	private TextField PurchaseTab_CompanyNameTextField;
	@FXML
	private TextField PurchaseTab_ItemDescriptionTextField;
	@FXML
	private TextField PurchaseTab_GrandTotalTextField;
	@FXML
	private Button PurchaseTab_AddButton;
	@FXML
	private Button PurchaseTab_CancelButton;
	@FXML
	private Button PurchaseTab_UpdateButton;
	@FXML
	private MenuItem PurchaseTab_EditMenuItem;
	@FXML
	private MenuItem PurchaseTab_DeleteMenuItem;
	
	@FXML
	private Text PurchaseTab_ErrorText;
	
	@FXML
	private ChoiceBox<String> PurchaseTab_SearchFieldChoiceBox;
	@FXML
	private TextField PurchaseTab_SearchValueTextField;
	@FXML
	private Button PurchaseTab_SearchButton;
	@FXML
	private Button PurchaseTab_ClearSearchButton;
	
	@FXML
	private TableView<PurchaseBean> PurchaseTab_PurchaseTable;
	@FXML
	private TableColumn<PurchaseBean,Integer> PurchaseTab_TableSerialNumber;
	@FXML
	private TableColumn<PurchaseBean,String> PurchaseTab_TableInvoiceNumber;
	@FXML
	private TableColumn<PurchaseBean,LocalDate> PurchaseTab_TableDate;
	@FXML
	private TableColumn<PurchaseBean,String> PurchaseTab_TableCompanyName;
	@FXML
	private TableColumn<PurchaseBean,String> PurchaseTab_TableItemDescription;
	@FXML
	private TableColumn<PurchaseBean,Double> PurchaseTab_TableGrandTotal;
	
	@Autowired
	private PurchaseBean purchaseBean;
	@Autowired
	private PurchaseServices purchaseServices;
	
	ObservableList<PurchaseBean> PurchaseList = FXCollections.observableArrayList();
	
	
	
	public void addPurchaseEvent(ActionEvent actionEvent) {
		try {
		if(validatePurchaseData()) {
		setPurchaseBean();
		
		if(purchaseServices.addPurchase(purchaseBean)) {
			successAlert("Purchase Added Successfully");
		}else {
			failureAlert("Purchase entry already Exists");
		}
		loadPurchaseTable();
		purchaseFieldsRefresh();
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Add");
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Add. Check the log file");
			log(e);
		}
	
	}
	
	public void editPurchaseEvent(ActionEvent actionEvent) {
		PurchaseBean tempPurchaseBean = PurchaseTab_PurchaseTable.getSelectionModel().getSelectedItem();
		populatePurchaseFieldsForUpdate(tempPurchaseBean);
	}
	
	public void updatePurchaseEvent(ActionEvent actionEvent) {
		try {
		if(validatePurchaseData()) {
		setPurchaseBean();
		purchaseServices.updatePurchase(purchaseBean);
		loadPurchaseTable();
		purchaseFieldsRefresh();
		PurchaseTab_AddButton.setDisable(false);
		PurchaseTab_UpdateButton.setVisible(false);
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Update");
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Update. Check the log file");
			log(e);
		}
	}
	
	public void deletePurchaseEvent(ActionEvent actionEvent) {
		try {
		PurchaseBean tempPurchaseBean = PurchaseTab_PurchaseTable.getSelectionModel().getSelectedItem();
		purchaseServices.deletePurchase(tempPurchaseBean);
		loadPurchaseTable();
		loadPurchaseSerialNumber();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Delete");
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Delete. Check the log file");
			log(e);
		}
	}
	
	public void searchPurchaseEvent(ActionEvent actionEvent) {
		try {
		String field = PurchaseTab_SearchFieldChoiceBox.getValue();
		String value = PurchaseTab_SearchValueTextField.getText();
		if(field.equals("Invoice Number")) {
			loadPurchaseTableAfterSearch(purchaseServices.searchPurchaseByInvoiceNumber(value));
		}else if(field.equals("Company Name")) {
			loadPurchaseTableAfterSearch(purchaseServices.searchPurchaseByCompanyName(value));
		}}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Search",e);
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Search. Check the log file");
			log(e);
		}
	}
	
	public void searchClearPurchaseEvent(ActionEvent actionEvent) {
		PurchaseTab_SearchFieldChoiceBox.getSelectionModel().selectFirst();
		PurchaseTab_SearchValueTextField.clear();
		loadPurchaseTable();
	}
	
	public void cancelPurchaseEvent(ActionEvent actionEvent) {
		purchaseFieldsRefresh();
		PurchaseTab_AddButton.setDisable(false);
		PurchaseTab_UpdateButton.setVisible(false);
	}
	
	private void loadPurchaseTable() {
		try {
		PurchaseList.clear();
		PurchaseList.addAll(purchaseServices.getAllPurchase());
		PurchaseTab_TableSerialNumber.setSortType(TableColumn.SortType.ASCENDING);
		PurchaseTab_PurchaseTable.getSortOrder().add(PurchaseTab_TableSerialNumber);
		PurchaseTab_PurchaseTable.setItems(PurchaseList);
		PurchaseTab_PurchaseTable.sort();
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Table Load", e);
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Table Load. Check the log file");
			log(e);
		}
	}
	
	private void loadPurchaseTableAfterSearch(List<PurchaseBean> list) {
		
		PurchaseTab_TableSerialNumber.setSortType(TableColumn.SortType.ASCENDING);
		PurchaseTab_PurchaseTable.getSortOrder().add(PurchaseTab_TableSerialNumber);
		ObservableList<PurchaseBean> observableList=FXCollections.observableArrayList();
		observableList.addAll(list);
		PurchaseTab_PurchaseTable.setItems(observableList);
		PurchaseTab_PurchaseTable.sort();
	}
	
	private void setPurchaseBean() {
		try {
		purchaseBean.setSerialNumber(Integer.valueOf(PurchaseTab_SerialNumberTextField.getText()));
		purchaseBean.setPurchaseInvoiceNumber(PurchaseTab_InvoiceNumberTextField.getText());
		purchaseBean.setPurchaseInvoiceDate(PurchaseTab_DateDatePicker.getValue());
		purchaseBean.setFinancialYear(financialYearService.getFinancialYear());
		purchaseBean.setPurchaseCompanyName(PurchaseTab_CompanyNameTextField.getText());
		purchaseBean.setPurchaseItemDescription(PurchaseTab_ItemDescriptionTextField.getText());
		purchaseBean.setPurchaseGrandTotal(Double.valueOf(PurchaseTab_GrandTotalTextField.getText()));
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Purchase Value load");
			e.printStackTrace();
			exceptionAlert("Exception during Purchase Value load. Check the log file");
			log(e);
		}
	}
	
	private void populatePurchaseFieldsForUpdate(PurchaseBean purchaseBean) {
		PurchaseTab_AddButton.setDisable(true);
		PurchaseTab_UpdateButton.setVisible(true);
		PurchaseTab_SerialNumberTextField.setText(String.valueOf(purchaseBean.getSerialNumber()));
		PurchaseTab_InvoiceNumberTextField.setText(purchaseBean.getPurchaseInvoiceNumber());
		PurchaseTab_DateDatePicker.setValue(purchaseBean.getPurchaseInvoiceDate());
		PurchaseTab_CompanyNameTextField.setText(purchaseBean.getPurchaseCompanyName());
		PurchaseTab_ItemDescriptionTextField.setText(purchaseBean.getPurchaseItemDescription());
		PurchaseTab_GrandTotalTextField.setText(String.valueOf(purchaseBean.getPurchaseGrandTotal()));
	}
	
	private void purchaseFieldsRefresh() {
		PurchaseTab_SerialNumberTextField.clear();
		loadPurchaseSerialNumber();
		PurchaseTab_InvoiceNumberTextField.clear();
		PurchaseTab_DateDatePicker.getEditor().clear();
		PurchaseTab_CompanyNameTextField.clear();
		PurchaseTab_ItemDescriptionTextField.clear();
		PurchaseTab_GrandTotalTextField.clear();		
	}
	
	
	/***------------------------------Settings Tab-----------------------------***/
	///COMPANY DETAILS///
	
	@FXML
	private TextField SettingsTab_CompanyNameTextField;
	@FXML
	private TextField SettingsTab_CompanyGSTTextField;
	@FXML
	private TextArea SettingsTab_CompanyAddressTextArea;
	@FXML
	private TextArea SettingsTab_CompanyBankDetailsTextArea;
	@FXML
	private TextArea SettingsTab_DeclarationTextArea;
	@FXML
	private Button SettingsTab_CompanyDetailsSaveButton;
	@FXML
	private Button SettingsTab_CompanyDetailsEditButton;
	@FXML
	private Button SettingsTab_CompanyDetailsCancelButton;
	@FXML
	private Button SettingsTab_CompanyDetailsUpdateButton;
	@FXML
	private Text SettingsTab_CompanyDetailsErrorText;
	
	@Autowired
	private CompanyDetailsBean companyDetailsBean;
	@Autowired
	private CompanyDetailsServices companyDetailsServices;
	
	public void saveCompanyDetailsEvent(ActionEvent actionEvent) {
		try {
			if(validateCompanyDetails()) {
		loadCompanyDetailsBean();
		companyDetailsServices.saveCompanyDetails(companyDetailsBean);
		SettingsTab_CompanyDetailsEditButton.setDisable(false);
		InvoiceMenuButton.setDisable(false);
		CustomerMenuButton.setDisable(false);
		ItemMenuButton.setDisable(false);
		PurchasesMenuButton.setDisable(false);
		SettingsTab_CompanyDetailsSaveButton.setVisible(false);
			}else {
				SettingsTab_CompanyDetailsErrorText.setText("All Fields of the Company Details are mandatory");
			}
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Company Details Save", e);
			e.printStackTrace();
			exceptionAlert("Exception during Company Details Save. Check the log file");
			log(e);
		}
	}
	
	public void editCompanyDetailsEvent(ActionEvent actionEvent) {
		try {
		companyDetailsBean = companyDetailsServices.getCompanyDetails();
		populateCompanyDetailsForEdit(companyDetailsBean);
		SettingsTab_CompanyDetailsSaveButton.setVisible(false);
		SettingsTab_CompanyDetailsUpdateButton.setVisible(true);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Company Details Edit", e);
			e.printStackTrace();
			exceptionAlert("Exception during Company Details Edit. Check the log file");
			log(e);
		}
	}
	
	public void updateCompanyDetailsEvent(ActionEvent actionEvent) {
		
		try {
			if(validateCompanyDetails()) {
		loadCompanyDetailsBean();
		companyDetailsServices.saveCompanyDetails(companyDetailsBean);
			}else {
				SettingsTab_CompanyDetailsErrorText.setText("All Fields of the Company Details are mandatory");
			}
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Company Details Update", e);
			e.printStackTrace();
			exceptionAlert("Exception during Company Details Update. Check the log file");
			log(e);
		}
	}
	
	public void cancelCompanyDetailsEvent(ActionEvent actionEvent) {
		try {
		companyDetailsBean = new CompanyDetailsBean();
		companyDetailsBean = companyDetailsServices.getCompanyDetails();
		populateCompanyDetailsForEdit(companyDetailsBean);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Company Details Cancel", e);
			e.printStackTrace();
			exceptionAlert("Exception during Company Details Cancel. Check the log file");
			log(e);
		}
	}
	
	public void loadCompanyDetailsatStart() {
		try{
		companyDetailsBean = new CompanyDetailsBean();
		companyDetailsBean = companyDetailsServices.getCompanyDetails();
		populateCompanyDetailsForEdit(companyDetailsBean);
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Company Details Load", e);
			e.printStackTrace();
			exceptionAlert("Exception during Company Details Load. Check the log file");
			log(e);
		}
	}
	
	private void populateCompanyDetailsForEdit(CompanyDetailsBean companyDetailsBean) {
		SettingsTab_CompanyNameTextField.setText(companyDetailsBean.getCompanyName());
		SettingsTab_CompanyGSTTextField.setText(companyDetailsBean.getCompanyGST());
		SettingsTab_CompanyAddressTextArea.setText(companyDetailsBean.getCompanyAddress());
		SettingsTab_CompanyBankDetailsTextArea.setText(companyDetailsBean.getCompanyBankDetails());
		SettingsTab_DeclarationTextArea.setText(companyDetailsBean.getDeclaration());
	}
	
	
	private void loadCompanyDetailsBean() {
		companyDetailsBean.setCompanyName(SettingsTab_CompanyNameTextField.getText());
		companyDetailsBean.setCompanyGST(SettingsTab_CompanyGSTTextField.getText());
		companyDetailsBean.setCompanyAddress(SettingsTab_CompanyAddressTextArea.getText());
		companyDetailsBean.setCompanyBankDetails(SettingsTab_CompanyBankDetailsTextArea.getText());
		companyDetailsBean.setDeclaration(SettingsTab_DeclarationTextArea.getText());
	}
	
private void addCompanyDetailsAtBeginingAlert(){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Alert");
		alert.setHeaderText(null);
		alert.setContentText("Add Company Details to Continue");
		alert.showAndWait();
	}
	
	///FINANCIAL YEAR///
	
	@FXML
	private Text SettingsTab_CurrentFinancialYearText;
	@FXML
	private ChoiceBox<String> SettingsTab_SelectFinancialYearChoiceBox;
	@FXML
	private Button SettingTab_FinancialYearReloadButton;
	@FXML
	private Button SettingsTab_FinancialYearResetButton;
	@FXML
	private Text HeaderFinancilaYearText;
	
	//private FinancialYearBean financialYearBean;
	private ObservableList<String> financialYearList = FXCollections.observableArrayList();
	
	
	
	public void changeFinancialYear(ActionEvent actionEvent) {
		try {
		financialYearService.setFinalcialYear(SettingsTab_SelectFinancialYearChoiceBox.getValue());
		SettingsTab_CurrentFinancialYearText.setText(financialYearService.getFinancialYear());
		HeaderFinancilaYearText.setText("Current Financial Year: "+financialYearService.getFinancialYear());
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Changing Financial Year ", e);
			e.printStackTrace();
			exceptionAlert("Exception during Changing Financial Year. Check the log file");
			log(e);
		}
	}
	
	public void resetCurrentFinancialYear(ActionEvent actionEvent) {
		try {
		financialYearService.setFinancialYeartoCurrent();
		SettingsTab_CurrentFinancialYearText.setText(financialYearService.getFinancialYear());
		HeaderFinancilaYearText.setText("Current Financial Year: "+financialYearService.getFinancialYear());
		}catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Resetting Financial Year ", e);
			e.printStackTrace();
			exceptionAlert("Exception during Resetting Financial Year. Check the log file");
			log(e);
		}
	}
	//Password Change
	
	@FXML
	private Button SettingsTab_ChangePasswordButton;
	@FXML
	private Button SettingsTab_ChangePasswordCancelButton;
	@FXML
	private PasswordField SettingsTab_NewPasswordTextField;
	@FXML
	private PasswordField SettingsTab_ConfirmNewPasswordTextField;
	@FXML
	private Text SettingsTab_PasswordErrorText;
	
	@Autowired
	private PasswordServices passwordServices;
	@Autowired
	PasswordBean passwordBean;
	
	public void changePasswordEvent(ActionEvent actionEvent) {
		try {
		if(!SettingsTab_NewPasswordTextField.getText().isEmpty() && SettingsTab_NewPasswordTextField.getText().equals(SettingsTab_ConfirmNewPasswordTextField.getText())) {
			SettingsTab_PasswordErrorText.setText("");
				passwordBean = new PasswordBean();
				passwordBean.setPassword(SettingsTab_NewPasswordTextField.getText());
				if(passwordServices.changePassword(passwordBean)) 
				{
					successAlert("Password Changed Successfully");
					SettingsTab_NewPasswordTextField.pseudoClassStateChanged(errorClass, false);
					SettingsTab_ConfirmNewPasswordTextField.pseudoClassStateChanged(errorClass, false);
					SettingsTab_NewPasswordTextField.clear();
					SettingsTab_ConfirmNewPasswordTextField.clear();
				}else {
					exceptionAlert("Exception during Password Change. Check the log file");
					failureAlert("Password Change Failed");
				}
		}else {
			SettingsTab_PasswordErrorText.setText("Re-Entered Password does not match");
			SettingsTab_NewPasswordTextField.pseudoClassStateChanged(errorClass, true);
			SettingsTab_ConfirmNewPasswordTextField.pseudoClassStateChanged(errorClass, true);
		}}
		catch(Exception e) {
			SkvBillingApplication.logger.error("Exception during Resetting Financial Year ", e);
			e.printStackTrace();
			exceptionAlert("Exception during Password Change. Check the log file");
			log(e);
		}
	}
	
	public void changePasswordCancelEvent(ActionEvent actionEvent) {
		SettingsTab_NewPasswordTextField.clear();
		SettingsTab_ConfirmNewPasswordTextField.clear();
	}
	
	
	//BackUp and Restore
	
	@FXML
	private Button SettingsTab_BackUpButton;
	@FXML
	private Button SettingsTab_RestoreButton;
	@FXML
	private Button SettingsTab_BackUprestoreCancelButton;
	@FXML
	private Button SettingsTab_SelectDirectoryForBackUpButton;
	@FXML
	private TextField SettingsTab_SelectedDirectoryTextField;
	@FXML
	private Text SettingsTab_BackRestoreDirectoryErrorText;
	
	@Autowired
	BackUpServices backUpServices;
		
	public void selectBackUpDirectory(ActionEvent actionEvent) {
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
	        directoryChooser.setInitialDirectory(new File("D:/"));
	        Stage stage = (Stage)SettingsTab_SelectDirectoryForBackUpButton.getScene().getWindow();
	            	        File file = directoryChooser.showDialog(stage);
	                        if (file != null) {
	                        	SettingsTab_SelectedDirectoryTextField.setText(file.getAbsolutePath());
	                        }
	}
	
	public void backUpEvent(ActionEvent actionEvent) {
		if(SettingsTab_SelectedDirectoryTextField.getText()!=null ) {
			SettingsTab_BackRestoreDirectoryErrorText.setText("");
		File file = new File(SettingsTab_SelectedDirectoryTextField.getText());
		try {
			backUpServices.backup(file);
		} catch (Exception e) {
			SkvBillingApplication.logger.error("Exception during BackUp", e);
			e.printStackTrace();
			log(e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("BackUp Failed");
			alert.showAndWait();
		}}else {
            	SettingsTab_BackRestoreDirectoryErrorText.setText("Select a Directory Path");
            }
	}
	
	public void restoreEvent(ActionEvent actionEvent) {
		if(SettingsTab_SelectedDirectoryTextField.getText()!=null ) {
			SettingsTab_BackRestoreDirectoryErrorText.setText("");
		File file = new File(SettingsTab_SelectedDirectoryTextField.getText());
		try {
			backUpServices.restore(file);
		} catch (Exception e) {
			SkvBillingApplication.logger.error("Exception during Restore", e);
			e.printStackTrace();
			log(e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Restore Failed");
			alert.showAndWait();
		}
		loadInvoiceTable();
		loadItemTable();
		loadCustomerTable();
		loadPurchaseTable();
		}else {
        	SettingsTab_BackRestoreDirectoryErrorText.setText("Select a Directory Path");
        }
	}
	
	public void backUpRestoreCancelEvent(ActionEvent actionEvent) {
		SettingsTab_SelectedDirectoryTextField.clear();
	}
	
	/****************Logging***********************/
	public void exceptionAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Occured");
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/Stylesheets/DialogBox.css").toExternalForm());
		dialogPane.getStyleClass().add("dialogBox");
		dialogPane.applyCss();
		alert.showAndWait();
	}
	
	private void  log(Exception e) {
		
		LocalDateTime localDateTime = LocalDateTime.now();
		String filename="D:\\Invoice\\Logs\\"+dateTimeFormatterForFile.format(localDateTime)+".txt";
		System.out.println(filename);
		try (Writer fileWriter = new FileWriter(new File(filename))){
			PrintWriter printWriter = new PrintWriter(fileWriter);
	      
	      e.printStackTrace(printWriter);
	      
	      printWriter.close();
		} catch (IOException e2) {
			exceptionAlert("Logging Failed");
			e2.printStackTrace();
		}
	}
	
	/****************Validations***********************/
	
	//Bill Items and Invoice
	
	
	private boolean billItemValidation() {
		
		final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
		
		
		if(!NewInvoice_ItemSerialNumberTextField.getText().matches(pattern) || NewInvoice_ItemSerialNumberTextField.getText().isEmpty() ) {
			InvoiceTab_BillItemFieldErrorText.setText("Serial Number must be Numeric and Not Empty");
			NewInvoice_ItemSerialNumberTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_ItemSerialNumberTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_RatePerTextField.getText().matches(pattern) || NewInvoice_RatePerTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Rate must be Nnumeric and Not Empty");
			NewInvoice_RatePerTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_RatePerTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_RatePerUnitTextField.getText().matches(pattern) || NewInvoice_RatePerUnitTextField.getText().isEmpty()) {	
			InvoiceTab_BillItemFieldErrorText.setText("Rate's Unit must be Numeric and Not Empty");
			NewInvoice_RatePerUnitTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_RatePerUnitTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_NumberOfPackagesTextField.getText().matches(pattern) || NewInvoice_NumberOfPackagesTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Number of packages must be Numeric and Not Empty");
			NewInvoice_NumberOfPackagesTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_NumberOfPackagesTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_WeightPerPackageTextField.getText().matches(pattern) || NewInvoice_WeightPerPackageTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Weight per Package must be Numeric and Not Empty");
			NewInvoice_WeightPerPackageTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_WeightPerPackageTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_TotalWeightTextField.getText().matches(pattern) || NewInvoice_TotalWeightTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Total Weight must be Numeric and not Empty");
			NewInvoice_TotalWeightTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_TotalWeightTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_ItemAmountTextField.getText().matches(pattern) || NewInvoice_ItemAmountTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Item Amount must be Numeric and Not Empty");
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(NewInvoice_IGSTRadioButton.isSelected()) {
			if(!NewInvoice_IGSTPercentageTextField.getText().matches(pattern) || NewInvoice_IGSTPercentageTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("IGST Percentage must be Numeric and Not Empty");
				NewInvoice_IGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_IGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
			
			if(!NewInvoice_IGSTAmountTextField.getText().matches(pattern) || NewInvoice_IGSTAmountTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("IGST Amount must be Numeric and Not Empty");
				NewInvoice_IGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_IGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
		}
		if(NewInvoice_CGSTSGSTRadioButton.isSelected()) {
			if(!NewInvoice_CGSTPercentageTextField.getText().matches(pattern) || NewInvoice_CGSTPercentageTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("CGST Percentage must be Numeric and Not Empty");
				NewInvoice_CGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_CGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
			
			if(!NewInvoice_CGSTAmountTextField.getText().matches(pattern) || NewInvoice_CGSTAmountTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("CGST Amount must be Numeric and Not Empty");
				NewInvoice_CGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_CGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
			
			if(!NewInvoice_SGSTPercentageTextField.getText().matches(pattern) || NewInvoice_SGSTPercentageTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("SGST Percentage must be Numeric and Not Empty");
				NewInvoice_SGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_SGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
			
			if(!NewInvoice_SGSTAmountTextField.getText().matches(pattern) || NewInvoice_SGSTAmountTextField.getText().isEmpty()) {
				InvoiceTab_BillItemFieldErrorText.setText("SGST Amount must be Numeric and Not Empty");
				NewInvoice_SGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
				return false;
			}else {
				NewInvoice_SGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
				InvoiceTab_BillItemFieldErrorText.setText("");}
		}
		
		
		if(!NewInvoice_TotalItemAmountTextField.getText().matches(pattern) || NewInvoice_TotalItemAmountTextField.getText().isEmpty()) {
			InvoiceTab_BillItemFieldErrorText.setText("Total Item Amount must be numeric");
			NewInvoice_TotalItemAmountTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_TotalItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
			
		return true;
	}
	
	private boolean invoiceValidation() throws NumberFormatException, Exception {
		boolean flag=true;
		String pattern = "-?\\d+(\\.\\d+)?";
		
		if(!NewInvoice_InvoiceNumberTextField.getText().matches(pattern) || NewInvoice_InvoiceNumberTextField.getText().isEmpty()) {
			NewInvoice_InvoiceFieldErrorText.setText("Invoice Number must be Numeric and Not Empty");
			NewInvoice_InvoiceNumberTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_InvoiceNumberTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");}
		
		if(!NewInvoice_TotalAmountTextField.getText().matches(pattern) || NewInvoice_TotalAmountTextField.getText().isEmpty()) {
			NewInvoice_InvoiceFieldErrorText.setText("Total Amount must be Numeric and Not Empty");
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");}
		
		if(!NewInvoice_RoundOffTextField.getText().matches(pattern) || NewInvoice_RoundOffTextField.getText().isEmpty()) {
			NewInvoice_InvoiceFieldErrorText.setText("Rounf Off must be Numeric and Not Empty");
			NewInvoice_RoundOffTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_RoundOffTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");
		}
		
		flag = validationForTotalAmount();

		if(!NewInvoice_GrandTotalTextField.getText().matches(pattern) || NewInvoice_GrandTotalTextField.getText().isEmpty()) {
			NewInvoice_InvoiceFieldErrorText.setText("Grand Total must be Numeric and Not Empty");
			NewInvoice_GrandTotalTextField.pseudoClassStateChanged(errorClass, true);
			return false;
		}else {
			NewInvoice_GrandTotalTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");
		}
		return flag;
	}
	
	private boolean validationForTotalWeight() {
		boolean flag=true;
		if(!NewInvoice_NumberOfPackagesTextField.getText().matches(pattern) || NewInvoice_NumberOfPackagesTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Number of packages must be Numeric and Not Empty");
			NewInvoice_NumberOfPackagesTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_NumberOfPackagesTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_WeightPerPackageTextField.getText().matches(pattern) || NewInvoice_WeightPerPackageTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Weight per Package must be Numeric and Not Empty");
			NewInvoice_WeightPerPackageTextField.pseudoClassStateChanged(errorClass, true);

		}else {
			NewInvoice_WeightPerPackageTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		return flag;
	}
	
	private boolean validationForItemAmount() {
		boolean flag=true;
		if(!NewInvoice_RatePerTextField.getText().matches(pattern) || NewInvoice_RatePerTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Rate must be Nnumeric and Not Empty");
			NewInvoice_RatePerTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_RatePerTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_RatePerUnitTextField.getText().matches(pattern) || NewInvoice_RatePerUnitTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Rate's Unit must be Numeric and Not Empty");
			NewInvoice_RatePerUnitTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_RatePerUnitTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_TotalWeightTextField.getText().matches(pattern) || NewInvoice_TotalWeightTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Total Weight must be Numeric and not Empty");
			NewInvoice_TotalWeightTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_TotalWeightTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		return flag;
	}
	
	private boolean validationForIGSTCalculation() {
		boolean flag=true;
		if(!NewInvoice_ItemAmountTextField.getText().matches(pattern) || NewInvoice_ItemAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Item Amount must be Numeric and Not Empty");
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_IGSTPercentageTextField.getText().matches(pattern) || NewInvoice_IGSTPercentageTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("IGST Percentage must be Numeric and Not Empty");
			NewInvoice_IGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_IGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		return flag;
	}
	private boolean validationForCGSTCalculation() {
		boolean flag=true;
		if(!NewInvoice_ItemAmountTextField.getText().matches(pattern) || NewInvoice_ItemAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Item Amount must be Numeric and Not Empty");
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_CGSTPercentageTextField.getText().matches(pattern) || NewInvoice_CGSTPercentageTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("CGST Percentage must be Numeric and Not Empty");
			NewInvoice_CGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_CGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		return flag;
	}
	private boolean validationForSGSTCalculation() {
		boolean flag=true;
		if(!NewInvoice_ItemAmountTextField.getText().matches(pattern) || NewInvoice_ItemAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Item Amount must be Numeric and Not Empty");
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_SGSTPercentageTextField.getText().matches(pattern) || NewInvoice_SGSTPercentageTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("SGST Percentage must be Numeric and Not Empty");
			NewInvoice_SGSTPercentageTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_SGSTPercentageTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		return flag;
	}
	
	private boolean validationForTotalItemAmount() {
		boolean flag=true;
		if(!NewInvoice_ItemAmountTextField.getText().matches(pattern) || NewInvoice_ItemAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("Item Amount must be Numeric and Not Empty");
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_ItemAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_IGSTAmountTextField.getText().matches(pattern) || NewInvoice_IGSTAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("IGST Amount must be Numeric and Not Empty");
			NewInvoice_IGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_IGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_CGSTAmountTextField.getText().matches(pattern) || NewInvoice_CGSTAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("CGST Amount must be Numeric and Not Empty");
			NewInvoice_CGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_CGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		
		if(!NewInvoice_SGSTAmountTextField.getText().matches(pattern) || NewInvoice_SGSTAmountTextField.getText().isEmpty()) {
			flag = false;
			InvoiceTab_BillItemFieldErrorText.setText("SGST Amount must be Numeric and Not Empty");
			NewInvoice_SGSTAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_SGSTAmountTextField.pseudoClassStateChanged(errorClass, false);
			InvoiceTab_BillItemFieldErrorText.setText("");}
		return flag;
	}
	
	private boolean validationForTotalAmount() throws NumberFormatException, Exception {
		boolean flag=true;
		List<BillItemBean> list;
		
		list = billItemServices.getAllBillItemforInvoice(Integer.valueOf(NewInvoice_InvoiceNumberTextField.getText()), financialYearService.getFinancialYear());
		
		if(list.isEmpty()) {
			flag=false;
		}
		NewInvoice_InvoiceFieldErrorText.setText("No Items to geneate bill.");
		return flag;
	}
	
	private boolean validationForRoundOff() {
		boolean flag=true;
		if(!NewInvoice_TotalAmountTextField.getText().matches(pattern) || NewInvoice_TotalAmountTextField.getText().isEmpty()) {
			flag = false;
			NewInvoice_InvoiceFieldErrorText.setText("Total Amount must be Numeric and Not Empty");
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");}
		return flag;
	}
	
	private boolean validationForGrandTotal() {
		boolean flag=true;
		if(!NewInvoice_TotalAmountTextField.getText().matches(pattern) || NewInvoice_TotalAmountTextField.getText().isEmpty()) {
			flag = false;
			NewInvoice_InvoiceFieldErrorText.setText("Total Amount must be Numeric and Not Empty");
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_TotalAmountTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");}

		if(!NewInvoice_RoundOffTextField.getText().matches(pattern) || NewInvoice_RoundOffTextField.getText().isEmpty()) {
			flag = false;
			NewInvoice_InvoiceFieldErrorText.setText("Rounf Off must be Numeric and Not Empty");
			NewInvoice_RoundOffTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			NewInvoice_RoundOffTextField.pseudoClassStateChanged(errorClass, false);
			NewInvoice_InvoiceFieldErrorText.setText("");}
		return flag;
	}
	
	//Customer
	
	private boolean validateCustomerData() {
		boolean flag=true;
		if(CustomerTab_CustomerNameTextField.getText().isEmpty()) {
			CustomerTab_NameErrorText.setText("Enter Customer Name");
			flag=false;
			CustomerTab_CustomerNameTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			CustomerTab_CustomerNameTextField.pseudoClassStateChanged(errorClass, false);
			CustomerTab_NameErrorText.setText("");}
		
		if(CustomerTab_CustomerGSTTextField.getText().isEmpty()) {
			CustomerTab_GSTErrorText.setText("Enter GST Number");
			flag=false;
			CustomerTab_CustomerGSTTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			CustomerTab_CustomerGSTTextField.pseudoClassStateChanged(errorClass, false);
			CustomerTab_GSTErrorText.setText("");}
		return flag;
	}
	
	//Item
	
	private boolean validateItemData() {
		boolean flag=true;
		
		if(ItemTab_ItemNameTextField.getText().isEmpty()) {
			ItemTab_ItemNameErrorText.setText("Enter Item Name");
			flag=false;
			ItemTab_ItemNameTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			ItemTab_ItemNameTextField.pseudoClassStateChanged(errorClass, false);
			ItemTab_ItemNameErrorText.setText("");}
		
		if(ItemTab_ItemCodeTextField.getText().isEmpty()) {
			ItemTab_ItemCodeErrorText.setText("Enter HSN Code");
			flag=false;
			ItemTab_ItemCodeTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			ItemTab_ItemCodeTextField.pseudoClassStateChanged(errorClass, false);
			ItemTab_ItemCodeErrorText.setText("");}
		
		return flag;
	}
	
	//Purchase
	
	private boolean validatePurchaseData() {
		boolean flag=true;
		
		if(PurchaseTab_SerialNumberTextField.getText().isEmpty()) {
			PurchaseTab_ErrorText.setText("Enter Serial Number");
			flag=false;
			PurchaseTab_SerialNumberTextField.pseudoClassStateChanged(errorClass, true);
		}else if(!PurchaseTab_SerialNumberTextField.getText().matches(pattern)) {
			PurchaseTab_ErrorText.setText("Serial Number must be Numeric");
			flag=false;
			PurchaseTab_SerialNumberTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			PurchaseTab_SerialNumberTextField.pseudoClassStateChanged(errorClass, false);
			PurchaseTab_ErrorText.setText("");}
		
		if(PurchaseTab_InvoiceNumberTextField.getText().isEmpty()) {
			PurchaseTab_ErrorText.setText("Enter Invoice Number");
			flag=false;
			PurchaseTab_InvoiceNumberTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			PurchaseTab_InvoiceNumberTextField.pseudoClassStateChanged(errorClass, false);
			PurchaseTab_ErrorText.setText("");}
		
		if(!PurchaseTab_GrandTotalTextField.getText().matches(pattern)) {
			PurchaseTab_ErrorText.setText("Grand Total field must be Numeric");
			flag=false;
			PurchaseTab_GrandTotalTextField.pseudoClassStateChanged(errorClass, true);
		}else {
			PurchaseTab_GrandTotalTextField.pseudoClassStateChanged(errorClass, false);
			PurchaseTab_ErrorText.setText("");}
		
		return flag;
	}
	
	//Settings
	
	//Company Details
	public boolean validateCompanyDetails() {
		boolean flag=true;
		if(SettingsTab_CompanyNameTextField.getText()== null) {
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company Name");
			SettingsTab_CompanyNameTextField.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else if(SettingsTab_CompanyNameTextField.getText().isEmpty()){
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company Name");
			SettingsTab_CompanyNameTextField.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else {
			SettingsTab_CompanyNameTextField.pseudoClassStateChanged(errorClass, false);
			SettingsTab_CompanyDetailsErrorText.setText("");}
		
		if(SettingsTab_CompanyGSTTextField.getText()== null) {
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company GST Number");
			SettingsTab_CompanyGSTTextField.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else if(SettingsTab_CompanyGSTTextField.getText().isEmpty()){
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company GST Number");
			SettingsTab_CompanyGSTTextField.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else {
			SettingsTab_CompanyGSTTextField.pseudoClassStateChanged(errorClass, false);
			SettingsTab_CompanyDetailsErrorText.setText("");}
		
		if(SettingsTab_CompanyAddressTextArea.getText()== null) {
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company Address");
			SettingsTab_CompanyAddressTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else if(SettingsTab_CompanyAddressTextArea.getText().isEmpty()){
			SettingsTab_CompanyDetailsErrorText.setText("Enter Company Address");
			SettingsTab_CompanyAddressTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else {
			SettingsTab_CompanyAddressTextArea.pseudoClassStateChanged(errorClass, false);
			SettingsTab_CompanyDetailsErrorText.setText("");}
		
		if(SettingsTab_DeclarationTextArea.getText()== null) {
			SettingsTab_CompanyDetailsErrorText.setText("Enter Declaration");
			SettingsTab_DeclarationTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else if(SettingsTab_DeclarationTextArea.getText().isEmpty()){
			SettingsTab_CompanyDetailsErrorText.setText("Enter Declaration");
			SettingsTab_DeclarationTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else {
			SettingsTab_DeclarationTextArea.pseudoClassStateChanged(errorClass, false);
			SettingsTab_CompanyDetailsErrorText.setText("");}
		
		if(SettingsTab_CompanyBankDetailsTextArea.getText()== null) {
			SettingsTab_CompanyDetailsErrorText.setText("Enter Bank Details");
			SettingsTab_CompanyBankDetailsTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else if(SettingsTab_CompanyBankDetailsTextArea.getText().isEmpty()){
			SettingsTab_CompanyDetailsErrorText.setText("Enter Bank Details");
			SettingsTab_CompanyBankDetailsTextArea.pseudoClassStateChanged(errorClass, true);
			flag=false;
		}else {
			SettingsTab_CompanyBankDetailsTextArea.pseudoClassStateChanged(errorClass, false);
			SettingsTab_CompanyDetailsErrorText.setText("");}
		
		return flag;
	}
	

}
