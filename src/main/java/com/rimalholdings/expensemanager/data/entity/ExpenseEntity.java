package com.rimalholdings.expensemanager.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "expense")
public class ExpenseEntity extends BaseEntity {

  @JoinColumn(name = "vendorid", referencedColumnName = "id")
  @ManyToOne
  private VendorEntity vendor;

  @Column(name = "totalamount")
  private BigDecimal totalAmount;

  @Column(name = "amountpaid")
  private BigDecimal amountPaid;

  @Column(name = "amountdue")
  private BigDecimal amountDue;

  @Column(name = "description")
  private String description;

  @Column(name = "duedate")
  private Timestamp dueDate;


  @ManyToMany(mappedBy = "expenses")
  private List<BillPaymentEntity> payments;


}
