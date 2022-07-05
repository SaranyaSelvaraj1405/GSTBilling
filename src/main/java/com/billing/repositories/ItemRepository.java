package com.billing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.ItemBean;

@Repository
public interface ItemRepository extends JpaRepository<ItemBean,String>{

	List<ItemBean> findAll();
	
}
