package com.rimalholdings.expensemanager.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "billpayment")
public class BillPaymentEntity extends BaseEntity {

  @ManyToMany(cascade = jakarta.persistence.CascadeType.PERSIST)
  @JoinTable(
      name = "billpayment_expense",
      joinColumns = @JoinColumn(name = "billpaymentid", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "expenseid", referencedColumnName = "id")
  )
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JsonManagedReference
  private List<ExpenseEntity> expenses = new ArrayList<>();

  private BigDecimal paymentAmount;
  private String paymentMethod;
  private String paymentReference;

  @JoinColumn(name = "vendorid", referencedColumnName = "id")
  @ManyToOne
  private VendorEntity vendor;

}
