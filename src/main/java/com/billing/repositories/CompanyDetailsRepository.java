package com.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.CompanyDetailsBean;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetailsBean,String> {

}
