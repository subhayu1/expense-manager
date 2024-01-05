package com.rimalholdings.expensemanager.data.dao;

import com.rimalholdings.expensemanager.data.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity> {
  UserEntity findByUsername(String clientId);



}
