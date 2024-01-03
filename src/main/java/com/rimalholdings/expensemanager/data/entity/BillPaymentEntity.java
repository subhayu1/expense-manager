package com.rimalholdings.expensemanager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "billpayment")
public class BillPaymentEntity extends BaseEntity {

  @ManyToMany
  @JoinTable(
      name = "billpayment_expense",
      joinColumns = @JoinColumn(name = "billpaymentid", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "expenseid", referencedColumnName = "id")
  )
  private List<ExpenseEntity> expenses;

  private BigDecimal paymentAmount;
  private String paymentMethod;
  private String paymentReference;

  @JoinColumn(name = "vendorid", referencedColumnName = "id")
  @ManyToOne
  private VendorEntity vendor;

}
