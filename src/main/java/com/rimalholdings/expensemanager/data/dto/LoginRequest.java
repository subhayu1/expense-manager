/* (C)1 */
package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginRequest {

private String username;
private String password;
}
