package com.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.beans.PasswordBean;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordBean,String> {

}
