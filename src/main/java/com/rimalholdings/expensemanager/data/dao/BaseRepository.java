package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {
}
