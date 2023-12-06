package az.edu.ada.db.as2.menu;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.Customer;
import az.edu.ada.db.as2.entity.Order;
import az.edu.ada.db.as2.entity.OrderDetail;
import az.edu.ada.db.as2.operation.BookOperations;
import az.edu.ada.db.as2.operation.CustomerOperations;
import az.edu.ada.db.as2.operation.OrderDetailOperations;
import az.edu.ada.db.as2.operation.OrderOperations;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;


public class CustomerMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerOperations customerOps = new CustomerOperations();
    private static final OrderOperations orderOps = new OrderOperations();
    private static final OrderDetailOperations orderDetailOps = new OrderDetailOperations();
    public static void CustomerOperations() {
        int choice;
        do {
            System.out.println("\n=<=<= Customer Operations Menu =>=>=");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewAllCustomers();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid number.");
            }
        } while (choice != 0);
    }

    private static void addCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Customer Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter Customer Address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer();
        // Assuming Customer class has a constructor without ID and with all these fields
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        try {
            customerOps.insertCustomer(customer);
            System.out.println("Customer added successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    private static void viewAllCustomers() {
        try {
            List<Customer> customers = customerOps.getAllCustomers();
            for (Customer customer : customers) {
                System.out.println(customer); // Ensure Customer class has a meaningful toString() method
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void updateCustomer() {
        System.out.print("Enter Customer ID to update: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Customer Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new Customer Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter new Customer Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter new Customer Address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer();
        customer.setCustomerId(customerId); // Make sure your Customer class has a method to set CustomerID
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        try {
            customerOps.updateCustomer(customer);
            System.out.println("Customer updated successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    private static void deleteCustomer() {
        System.out.print("Enter Customer ID to delete: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            customerOps.deleteCustomer(customerId);
            System.out.println("Customer deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Order Menu and Operations
    public static void OrderOperations() throws SQLException {
        int choice;
        do {
            System.out.println("\n=<=<= Order Operations Menu =>=>=");
            System.out.println("1. Add Order");
            System.out.println("2. View All Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Delete Order");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    viewAllOrders();
                    break;
                case 3:
                    updateOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid number.");
            }
        } while (choice != 0);
    }

    public static void placeOrder(Order order, int bookId, int quantity) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Check if enough books are available
            if (!checkBookAvailability(bookId, quantity, conn)) {
                throw new Exception("Not enough books in stock");
            }

            conn.commit(); // Commit transaction
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static boolean checkBookAvailability(int bookId, int quantity, Connection conn) throws SQLException {
        String query = "SELECT StockCount FROM Books WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int stockCount = rs.getInt("StockCount");
                    return stockCount >= quantity;
                }
                return false;
            }
        }
    }
    private static void updateBookStock(int bookId, int quantity, Connection conn) throws SQLException {
        String query = "UPDATE Books SET StockCount = StockCount - ? WHERE BookID = ? AND StockCount >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, bookId);
            pstmt.setInt(3, quantity);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Book stock update failed, possibly due to insufficient stock.");
            }
        }
    }

    public static void addOrder() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        scanner.nextLine(); // Consume newline
        System.out.print("Enter Order Date (yyyy-mm-dd): ");
        String orderDateStr = scanner.nextLine();
        Date orderDate = Date.valueOf(orderDateStr);

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setOrderDate(orderDate);

        BigDecimal price;

        try (Connection conn = DatabaseConnection.getConnection()) {
            price = getBookPrice(bookId, conn);
//            totalAmount = BigDecimal.multiply(quantity, price);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            // Handle exception or return from the method
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            if (!checkBookAvailability(bookId, quantity, conn)) {
                throw new Exception("Not enough books in stock");
            }

            updateBookStock(bookId, quantity, conn);
            placeOrder(order, bookId, quantity);

            conn.commit(); // Commit transaction
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }


    private static void viewAllOrders() {
        try {
            List<Order> orders = orderOps.getAllOrders();
            for (Order order : orders) {
                System.out.println(order); // Ensure Order class has a meaningful toString() method
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static Date convertToDate(String dateStr) throws ParseException {
        if (!isValidFormat(DATE_FORMAT, dateStr)) {
            throw new ParseException("Invalid date format", 0);
        }
        return Date.valueOf(dateStr);
    }

    private static boolean isValidFormat(String format, String value) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.parse(value);
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
    private static void updateOrder() {
        System.out.print("Enter Order ID to update: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();

        Date orderDate = null;
        while (orderDate == null) {
            System.out.print("Enter new Order Date (yyyy-mm-dd): ");
            String dateStr = scanner.next();

            try {
                orderDate = convertToDate(dateStr);
                // Process with the valid date
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd.");
                // The loop will continue, prompting the user again
            }
            orderDate = Date.valueOf(dateStr);
        }
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        Order order = new Order();
        order.setOrderId(orderId); // Assuming you have a setter for OrderId
        order.setCustomerId(customerId);
        order.setOrderDate(orderDate);

        BigDecimal price;

        try (Connection conn = DatabaseConnection.getConnection()) {
            price = getBookPrice(bookId, conn);
//            totalAmount = BigDecimal.multiply(quantity, price);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            // Handle exception or return from the method
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            if (!checkBookAvailability(bookId, quantity, conn)) {
                throw new Exception("Not enough books in stock");
            }

            updateBookStock(bookId, quantity, conn);
            placeOrder(order, bookId, quantity);

            conn.commit(); // Commit transaction
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }


    private static void deleteOrder() {
        System.out.print("Enter Order ID to delete: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            if (!orderDetailOps.orderDetailExists(orderId)) {
                System.out.println("Order detail with ID " + orderId + " does not exist.");
                return;
            }

            orderDetailOps.deleteOrderDetail(orderId);
            System.out.println("Order detail deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        try {
            orderOps.deleteOrder(orderId);
            System.out.println("Order deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Order Details Menu and Operations
    public static void OrderDetailOperations() {
        int choice;
        do {
            System.out.println("\n=== Order Detail Operations Menu ===");
            System.out.println("1. Add Order Detail");
            System.out.println("2. View All Order Details");
            System.out.println("3. Update Order Detail");
            System.out.println("4. Delete Order Detail");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addOrderDetail();
                    break;
                case 2:
                    viewAllOrderDetails();
                    break;
                case 3:
                    updateOrderDetail();
                    break;
                case 4:
                    deleteOrderDetail();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid number.");
            }
        } while (choice != 0);
    }

    private static void addOrderDetail() {
        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();

        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter Price per Item: ");
        BigDecimal price = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setBookId(bookId);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(price);

        try {
            orderDetailOps.insertOrderDetail(orderDetail);
            System.out.println("Order detail added successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    private static void viewAllOrderDetails() {
        try {
            List<OrderDetail> orderDetails = orderDetailOps.getAllOrderDetails();
            if (orderDetails.isEmpty()) {
                System.out.println("No order details found.");
                return;
            }
            for (OrderDetail orderDetail : orderDetails) {
                System.out.println(orderDetail);
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    private static void updateOrderDetail() {
        System.out.print("Enter Order Detail ID to update: ");
        int orderDetailId = scanner.nextInt();

        System.out.print("Enter new Order ID: ");
        int orderId = scanner.nextInt();

        System.out.print("Enter new Book ID: ");
        int bookId = scanner.nextInt();

        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter new Price per Item: ");
        BigDecimal price = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailId);
        orderDetail.setOrderId(orderId);
        orderDetail.setBookId(bookId);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(price);

        try {
            orderDetailOps.updateOrderDetail(orderDetail);
            System.out.println("Order detail updated successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void deleteOrderDetail() {
        System.out.print("Enter Order Detail ID to delete: ");
        int orderDetailId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            if (!orderDetailOps.orderDetailExists(orderDetailId)) {
                System.out.println("Order detail with ID " + orderDetailId + " does not exist.");
                return;
            }

            orderDetailOps.deleteOrderDetail(orderDetailId);
            System.out.println("Order detail deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static BigDecimal getBookPrice(int bookId, Connection conn) throws SQLException {
        String query = "SELECT Price FROM Books WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("Price");
                } else {
                    throw new SQLException("Book not found with ID: " + bookId);
                }
            }
        }
    }
}
