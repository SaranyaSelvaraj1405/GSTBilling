package com.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.InvoiceBean;
import com.billing.beans.InvoiceIdentifier;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceBean,InvoiceIdentifier>{

}
