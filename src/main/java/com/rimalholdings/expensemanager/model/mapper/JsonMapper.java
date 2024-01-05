/* (C)1 */
package com.rimalholdings.expensemanager.model.mapper;

import java.util.List;

public interface JsonMapper<T> {

List<T> mapJson(String json);
}
