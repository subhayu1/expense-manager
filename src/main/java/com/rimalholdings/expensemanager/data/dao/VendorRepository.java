package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Long> {

}
