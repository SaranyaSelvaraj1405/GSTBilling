package com.billing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.PurchaseBean;
import com.billing.beans.PurchaseIdentifier;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseBean,PurchaseIdentifier>{

	List<PurchaseBean> findAll();
}
