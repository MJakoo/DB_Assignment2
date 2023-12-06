package az.edu.ada.db.as2.menu;

import az.edu.ada.db.as2.DatabaseConnection;
import az.edu.ada.db.as2.entity.Author;
import az.edu.ada.db.as2.entity.Book;
import az.edu.ada.db.as2.operation.AuthorOperations;
import az.edu.ada.db.as2.operation.BookOperations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthorOperations authorOps = new AuthorOperations();
    private static final BookOperations bookOps = new BookOperations();
    public static void AuthorOperations() {
        System.out.println("\n=<=<=  Author Operations Menu =>=>=");
        System.out.println("1. Add Author");
        System.out.println("2. View All Authors");
        System.out.println("3. Update Author");
        System.out.println("4. Delete Author");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addAuthor();
                break;
            case 2:
                viewAllAuthors();
                break;
            case 3:
                updateAuthor();
                break;
            case 4:
                deleteAuthor();
                break;
            case 0:
                System.out.println("Exiting the program.");
                return;
            default:
                System.out.println("Invalid choice. Please enter a valid number.");
        }

    }

    private static void addAuthor() {
        System.out.print("Enter Author Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Author Bio: ");
        String bio = scanner.nextLine();

        System.out.print("Enter Author Birth Date (yyyy-mm-dd): ");
        Date birthDate = Date.valueOf(scanner.nextLine());

        Author author = new Author();
        author.setName(name);
        author.setBio(bio);
        author.setBirthDate(birthDate);

        try {
            authorOps.insertAuthor(author);
            System.out.println("Author added successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void viewAllAuthors() {
        try {
            List<Author> authors = authorOps.getAllAuthors();
            for (Author author : authors) {
                System.out.println(author);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void updateAuthor() {
        System.out.print("Enter Author ID to update: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Author Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new Author Bio: ");
        String bio = scanner.nextLine();

        System.out.print("Enter new Author Birth Date (yyyy-mm-dd): ");
        Date birthDate = Date.valueOf(scanner.nextLine());

        Author author = new Author();
        author.setAuthorId(authorId);
        author.setName(name);
        author.setBio(bio);
        author.setBirthDate(birthDate);

        try {
            authorOps.updateAuthor(author);
            System.out.println("Author updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void deleteAuthor() {
        System.out.print("Enter Author ID to delete: ");
        int authorId = scanner.nextInt();

        try {
            authorOps.deleteAuthor(authorId);
            System.out.println("Author deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    public static void BookOperations() throws SQLException {
        int choice;
        do {
            System.out.println("\n=<=<= Book Operations Menu =>=>=");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Update Book Details");
            System.out.println("4. Delete a Book");
            System.out.println("5. Reset Book Count");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    resetBookStock(DatabaseConnection.getConnection());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid number.");
            }
        } while (choice != 0);
    }

    private static void resetBookStock(Connection conn) throws SQLException {
        System.out.print("Which Book to Reset (ID):  ");
        int bookId = scanner.nextInt();
        System.out.print("How many books should be:  ");
        int quantity = scanner.nextInt();

        String query = "UPDATE Books SET StockCount = StockCount + ? WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, quantity); // Quantity to add back to the stock
            pstmt.setInt(2, bookId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update book stock, book may not exist.");
            }
        }
    }
    private static void addBook() throws SQLException {
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter Price: ");
        BigDecimal price = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();

        System.out.print("Enter Stock Count: ");
        int stockCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int authorId;
        do {
            System.out.print("Enter Author ID: ");
            authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check if author exists
            if (!authorExists(authorId)) {
                System.out.println("Author ID does not exist. Please enter a valid Author ID.");
            }
        } while (!authorExists(authorId));

        Book book = new Book(); // Assuming a constructor is available
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setGenre(genre);
        book.setPublisher(publisher);
        book.setStockCount(stockCount);
        book.setAuthorId(authorId);

        try {
            bookOps.insertBook(book);
            System.out.println("Book added successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Test if the Author Exists
    public static boolean authorExists(int authorId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Authors WHERE AuthorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }


    private static void viewAllBooks() {
        try {
            List<Book> books = bookOps.getAllBooks();
            for (Book book : books) {
                System.out.println(book); // Assuming toString() is overridden in Book class
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static void updateBook() {
        System.out.print("Enter Book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Book Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter new Price: ");
        BigDecimal price = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter new Publisher: ");
        String publisher = scanner.nextLine();

        System.out.print("Enter new Stock Count: ");
        int stockCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new Author ID: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setGenre(genre);
        book.setPublisher(publisher);
        book.setStockCount(stockCount);
        book.setAuthorId(authorId);

        try {
            bookOps.updateBook(book);
            System.out.println("Book updated successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    private static void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            bookOps.deleteBook(bookId);
            System.out.println("Book deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

}
