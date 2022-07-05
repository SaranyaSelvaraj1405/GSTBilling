package com.billing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.BillItemBean;
import com.billing.beans.BillItemIdentifier;

@Repository
public interface BillItemRepository extends JpaRepository<BillItemBean,BillItemIdentifier>{
	List<BillItemBean> findAll();
}
