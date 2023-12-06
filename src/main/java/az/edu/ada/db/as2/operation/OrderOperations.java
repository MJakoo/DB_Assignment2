package az.edu.ada.db.as2.operation;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.Order;
import az.edu.ada.db.as2.entity.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderOperations {

    // Insert a new order
    public void insertOrder(Order order, Connection conn) throws SQLException {
        String query = "INSERT INTO Orders (CustomerID, OrderDate, TotalAmount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setDate(2, order.getOrderDate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1)); // Retrieve and set the generated order ID
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }
    public void insertOrder(Order order) throws SQLException {
        String query = "INSERT INTO Orders (CustomerID, OrderDate, TotalAmount) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, order.getCustomerId());
            ps.setDate(2, order.getOrderDate());
            ps.executeUpdate();
        }
    }

    // Retrieve all orders
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                order.setCustomerId(rs.getInt("CustomerID"));
                order.setOrderDate(rs.getDate("OrderDate"));
                orders.add(order);
            }
        }
        return orders;
    }

    // Update an order's details
    public void updateOrder(Order order) throws SQLException {
        String query = "UPDATE Orders SET CustomerID = ?, OrderDate = ?, TotalAmount = ? WHERE OrderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, order.getCustomerId());
            ps.setDate(2, order.getOrderDate());
            ps.setInt(4, order.getOrderId());
            ps.executeUpdate();
        }
    }

    // Delete an order
    public void deleteOrder(int orderId) throws SQLException {
        String query = "DELETE FROM Orders WHERE OrderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }

    private boolean isBookAvailable(int bookId, int quantity, Connection conn) throws SQLException {
        String query = "SELECT StockCount FROM Books WHERE BookID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int stockCount = rs.getInt("StockCount");
                    return stockCount >= quantity;
                }
                return false;
            }
        }
    }

    private void updateBookStock(int bookId, int quantity, Connection conn) throws SQLException {
        String query = "UPDATE Books SET StockCount = StockCount - ? WHERE BookID = ? AND StockCount >= ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, bookId);
            ps.setInt(3, quantity);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Book stock update failed, possibly due to insufficient stock.");
            }
        }
    }

    //  Place order
    public void placeOrder(Order order, List<OrderDetail> orderDetails) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            for (OrderDetail detail : orderDetails) {
                if (!isBookAvailable(detail.getBookId(), detail.getQuantity(), conn)) {
                    throw new SQLException("Not enough books in stock for BookID: " + detail.getBookId());
                }
            }
            for (OrderDetail detail : orderDetails) {
                updateBookStock(detail.getBookId(), detail.getQuantity(), conn);
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
