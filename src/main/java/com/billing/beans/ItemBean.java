package com.billing.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="item")
public class ItemBean {
	
	@Column(name="itemname")
	private String ItemName;
	@Column(name="itemdescription")
	private String ItemDescription;
	@Column(name="itemcode")
	@Id
	private String ItemCode;
	@Column(name="itemUnit")
	private String ItemUnit;
	
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getItemDescription() {
		return ItemDescription;
	}
	public void setItemDescription(String itemDescription) {
		ItemDescription = itemDescription;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public String getItemUnit() {
		return ItemUnit;
	}
	public void setItemUnit(String itemUnit) {
		ItemUnit = itemUnit;
	}

}
