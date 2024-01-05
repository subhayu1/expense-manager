/* (C)1 */
package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.UserEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {
UserEntity findByUsername(String clientId);
}
