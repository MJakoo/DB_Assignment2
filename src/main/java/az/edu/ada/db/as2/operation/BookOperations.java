package az.edu.ada.db.as2.operation;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookOperations {

    public void insertBook(Book book) throws SQLException {
        String query = "INSERT INTO Books (Title, ISBN, Price, Genre, Publisher, StockCount, AuthorID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());
            pstmt.setBigDecimal(3, book.getPrice());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getPublisher());
            pstmt.setInt(6, book.getStockCount());
            pstmt.setInt(7, book.getAuthorId());
            pstmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Books";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("BookID"));
                book.setTitle(rs.getString("Title"));
                book.setIsbn(rs.getString("ISBN"));
                book.setPrice(rs.getBigDecimal("Price"));
                book.setGenre(rs.getString("Genre"));
                book.setPublisher(rs.getString("Publisher"));
                book.setStockCount(rs.getInt("StockCount"));
                book.setAuthorId(rs.getInt("AuthorID"));
                books.add(book);
            }
        }
        return books;
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE Books SET Title = ?, ISBN = ?, Price = ?, Genre = ?, Publisher = ?, StockCount = ?, AuthorID = ? WHERE BookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());
            pstmt.setBigDecimal(3, book.getPrice());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getPublisher());
            pstmt.setInt(6, book.getStockCount());
            pstmt.setInt(7, book.getAuthorId());
            pstmt.setInt(8, book.getBookId());
            pstmt.executeUpdate();
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }
}

