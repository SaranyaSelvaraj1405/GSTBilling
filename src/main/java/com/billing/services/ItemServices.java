package com.billing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.beans.ItemBean;
import com.billing.repositories.ItemRepository;

@Service
public class ItemServices{
	
	@Autowired
	private ItemRepository itemRepository;
	
	public boolean addItem(ItemBean itemBean) throws Exception {
		if(itemRepository.existsById(itemBean.getItemCode())) {
			return false;
			}else {
			itemRepository.save(itemBean);
			}
			return true;
	}
	
	public void updateItem(ItemBean itemBean) throws Exception {
		itemRepository.save(itemBean);
	}
	
	public void deleteItem(ItemBean itemBean) throws Exception {
		itemRepository.deleteById(itemBean.getItemCode());
	}
	
	public List<ItemBean> searchItemByName(String name) throws Exception {
		List<ItemBean> fullList =itemRepository.findAll();
		List<ItemBean> filteredList=new ArrayList<ItemBean>();
		for(ItemBean item: fullList) {
			String s=item.getItemName();
			if(s.toLowerCase().contains(name)) {
				filteredList.add(item);
			}
		}
 		return filteredList;
	}
	
	public List<ItemBean> searchItemByCode(String code) throws Exception {
		List<ItemBean> fullList =itemRepository.findAll();
		List<ItemBean> filteredList=new ArrayList<ItemBean>();
		for(ItemBean item: fullList) {
			String s=item.getItemCode();
			if(s.toLowerCase().contains(code)) {
				filteredList.add(item);
			}
		}
 		return filteredList;
	}
	
	public ItemBean findItemByName(String Name) throws Exception {
		List<ItemBean> fullList =itemRepository.findAll();
		
		for(ItemBean item:fullList) {
			String s=item.getItemName();
			if(s.equalsIgnoreCase(Name)) {
				return item;
			}
		}
		return null;
	}
	
	public ItemBean findItemByCode(String code) throws Exception{
		List<ItemBean> fullList =itemRepository.findAll();
		
		for(ItemBean item:fullList) {
			String s=item.getItemCode();
			if(s.equalsIgnoreCase(code)) {
				return item;
			}
		}
		return null;
	}
	
	
	public List<ItemBean> getAllItems() throws Exception {
		return this.itemRepository.findAll();
	}
	
	public void addAllItems(List<ItemBean> list) throws Exception {
		itemRepository.saveAll(list);
	}
	
	public String toStringForBackup(ItemBean itemBean) throws Exception {
		String s="";
		s+=itemBean.getItemName()+"{}";
		if(itemBean.getItemDescription()!=null && !itemBean.getItemDescription().isEmpty()) {
			s+=itemBean.getItemDescription()+"{}";
		}else {s+="*{}";}
		s+=itemBean.getItemCode()+"{}";
		if(itemBean.getItemUnit()!=null && !itemBean.getItemUnit().isEmpty()) {
			s+=itemBean.getItemUnit()+"#";
		}else {s+="*#";}
		return s;
	}
	
	public List<ItemBean> toBeanForRestore(String s) throws Exception {
		List<ItemBean> list = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(s, "#");
		while(stringTokenizer.hasMoreTokens()) {
			String[] parameters = new String[4];
			int i=0;
			StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().toString(),"{}");
			while(stringTokenizer2.hasMoreTokens()) {
				parameters[i]=stringTokenizer2.nextToken();
				i++;
			}
			ItemBean itemBean = new ItemBean();
			itemBean.setItemName(parameters[0]);
			if(parameters[1].equals("*")) {
				itemBean.setItemDescription(null);
			}else {itemBean.setItemDescription(parameters[1]);}			
			itemBean.setItemCode(parameters[2]);
			if(parameters[3].equals("*")) {
				itemBean.setItemUnit(null);
			}else {itemBean.setItemUnit(parameters[3]);}
			list.add(itemBean);			
		}
		return list;
	}
}
