/* (C)1 */
package com.rimalholdings.expensemanager.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {
public UserEntity(String username, String password, String role) {
	this.username = username;
	this.password = password;
	this.role = role;
}

@Column(name = "username", unique = true)
private String username;

@Column(name = "password")
private String password;

@Column(name = "role")
private String role;
}
