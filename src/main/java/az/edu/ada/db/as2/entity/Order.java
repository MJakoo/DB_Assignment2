package az.edu.ada.db.as2.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Order {
    private int orderId;
    private int customerId;
    private Date orderDate; // java.sql.Date is used to align with SQL DATE type
    private BigDecimal totalAmount; // BigDecimal is used for accurate representation of currency

    // Constructor
    public Order() {
    }

    public Order(int orderId, int customerId, Date orderDate, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Order:" +
                "   Order ID: " + orderId +"\n" +
                "   Customer ID: " + customerId + "\n" +
                "   Order Date: " + orderDate +"\n" +
                "   Total Amount: " + totalAmount;
    }
}
