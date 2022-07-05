package com.billing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.CustomerBean;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerBean,String>
{
	List<CustomerBean> findAll();
	List<CustomerBean> findByCustomerGST(String gst);
	List<CustomerBean> findByCustomerName(String name);
}
