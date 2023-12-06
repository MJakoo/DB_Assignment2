package az.edu.ada.db.as2.operation;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class OrderDetailOperations {

    // Insert a new order detail
    public void insertOrderDetail(OrderDetail orderDetail) throws SQLException {
        String query = "INSERT INTO OrderDetails (OrderID, BookID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetail.getOrderId());
            pstmt.setInt(2, orderDetail.getBookId());
            pstmt.setInt(3, orderDetail.getQuantity());
            pstmt.setBigDecimal(4, orderDetail.getPrice());
            pstmt.executeUpdate();
        }
    }

    // Retrieve all order details
    public List<OrderDetail> getAllOrderDetails() throws SQLException {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String query = "SELECT * FROM OrderDetails";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailId(rs.getInt("OrderDetailID"));
                orderDetail.setOrderId(rs.getInt("OrderID"));
                orderDetail.setBookId(rs.getInt("BookID"));
                orderDetail.setQuantity(rs.getInt("Quantity"));
                orderDetail.setPrice(rs.getBigDecimal("Price"));
                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }

    // Update an order detail
    public void updateOrderDetail(OrderDetail orderDetail) throws SQLException {
        String query = "UPDATE OrderDetails SET OrderID = ?, BookID = ?, Quantity = ?, Price = ? WHERE OrderDetailID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetail.getOrderId());
            pstmt.setInt(2, orderDetail.getBookId());
            pstmt.setInt(3, orderDetail.getQuantity());
            pstmt.setBigDecimal(4, orderDetail.getPrice());
            pstmt.setInt(5, orderDetail.getOrderDetailId());
            pstmt.executeUpdate();
        }
    }

    // Delete an order detail
    public void deleteOrderDetail(int orderDetailId) throws SQLException {
        String query = "DELETE FROM OrderDetails WHERE OrderDetailID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetailId);
            pstmt.executeUpdate();
        }
    }


    public boolean orderDetailExists(int orderDetailId) throws SQLException {
        String query = "SELECT COUNT(*) FROM OrderDetails WHERE OrderDetailID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetailId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

}
