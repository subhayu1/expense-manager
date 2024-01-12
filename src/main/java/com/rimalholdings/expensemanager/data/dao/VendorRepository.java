/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import java.util.Optional;

import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.entity.VendorEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends BaseRepository<VendorEntity> {

Optional<VendorEntity> getVendorByExternalId(String vendorId);

BaseDTOInterface findByExternalId(String vendorId);

VendorEntity findByIntegrationId(String integrationId);
}
