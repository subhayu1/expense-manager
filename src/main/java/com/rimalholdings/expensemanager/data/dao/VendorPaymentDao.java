package com.rimalholdings.expensemanager.data.dao;

// public class VendorPaymentDao {
//    public List<VendorPayment> findByIdWithExpenseAndVendor(String id) {
//        List<VendorPayment> vendorPayments = new ArrayList<>();
//
//        try {
//            Connection connection =
// DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "password");
//
//            String sql = """
//                SELECT v.integrationId AS vendorId, v.vendornumber AS vendorNumber,
//                e.externalInvoiceNumber AS documentNumber, e.vendorInvoiceNumber AS
// externalDocumentNumber,
//                e.integrationId AS appliesToInvoiceId, e.externalInvoiceNumber
//                AS appliesToInvoiceNumber, e.description, bp.id AS comment, bp.paymentamount,
// bp.createddate
//                FROM billpayment bp
//                INNER JOIN billpayment_expense bpe ON bp.id = bpe.billpaymentid
//                INNER JOIN expense e ON bpe.expenseid = e.id
//                INNER JOIN vendor v ON e.vendorId = v.id
//                WHERE bp.id = ?
//                AND bp.toSync = 1;
//                """;
//
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, id);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                VendorPayment vendorPayment = new VendorPayment();
//                vendorPayment.setVendorId(resultSet.getString("vendorId"));
//                vendorPayment.setVendorNumber(resultSet.getString("vendorNumber"));
//                vendorPayment.setDocumentNumber(resultSet.getString("documentNumber"));
//
// vendorPayment.setExternalDocumentNumber(resultSet.getString("externalDocumentNumber"));
//                vendorPayment.setAppliesToInvoiceId(resultSet.getString("appliesToInvoiceId"));
//
// vendorPayment.setAppliesToInvoiceNumber(resultSet.getString("appliesToInvoiceNumber"));
//                vendorPayment.setDescription(resultSet.getString("description"));
//                vendorPayment.setComment(resultSet.getString("comment"));
//                vendorPayment.setAmount(resultSet.getDouble("paymentamount"));
//                vendorPayment.setPostingDate(resultSet.getString("createddate"));
//
//                vendorPayments.add(vendorPayment);
//            }
//
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (Exception e) {
//          throw new RuntimeException(e);
//        }
//
//        return vendorPayments;
//    }
// }
