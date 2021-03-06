package com.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.FinancialYearBean;

@Repository
public interface FinancialYearRepository extends JpaRepository<FinancialYearBean, String>{

}
