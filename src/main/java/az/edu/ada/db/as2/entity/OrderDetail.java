package az.edu.ada.db.as2.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int bookId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    // Constructor
    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, int bookId, int quantity, BigDecimal price, BigDecimal totalAmount) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Order Detail:" +
                "   Order Detail ID:" + orderDetailId + "\n" +
                "   Order ID" + orderId + "\n" +
                "   Book Id: " + bookId + "\n" +
                "   Quantity:" + quantity + "\n" +
                "   Price: " + price;
    }
}
