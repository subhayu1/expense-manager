package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.VendorEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends BaseRepository<VendorEntity> {

  Optional<VendorEntity> getVendorByExternalId(String vendorId);
}
