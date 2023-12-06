package az.edu.ada.db.as2.operation;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorOperations {

    public void insertAuthor(Author author) throws SQLException {
        String query = "INSERT INTO Authors (Name, Bio, BirthDate) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getBio());
            pstmt.setDate(3, author.getBirthDate());
            pstmt.executeUpdate();
        }
    }


    public List<Author> getAllAuthors() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM Authors";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Author author = new Author();
                author.setAuthorId(rs.getInt("AuthorID"));
                author.setName(rs.getString("Name"));
                author.setBio(rs.getString("Bio"));
                author.setBirthDate(rs.getDate("BirthDate"));
                authors.add(author);
            }
        }
        return authors;
    }

    public void updateAuthor(Author author) throws SQLException {
        String query = "UPDATE Authors SET Name = ?, Bio = ?, BirthDate = ? WHERE AuthorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getBio());
            pstmt.setDate(3, author.getBirthDate());
            pstmt.setInt(4, author.getAuthorId());
            pstmt.executeUpdate();
        }
    }

    public void deleteAuthor(int authorId) throws SQLException {
        String query = "DELETE FROM Authors WHERE AuthorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, authorId);
            pstmt.executeUpdate();
        }
    }
}
