package az.edu.ada.db.as2.entity;

import java.math.BigDecimal;

public class Book {
    private int bookId;
    private String title;
    private String isbn;
    private BigDecimal price;  // BigDecimal is used for accurate representation of currency
    private String genre;
    private String publisher;
    private int stockCount;
    private int authorId;  // Assuming this is a foreign key reference to an Author

    // Constructor
    public Book() {
    }

    public Book(int bookId, String title, String isbn, BigDecimal price, String genre, String publisher, int stockCount, int authorId) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.genre = genre;
        this.publisher = publisher;
        this.stockCount = stockCount;
        this.authorId = authorId;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Book: " +
                "   Book ID:" + bookId + "\n" +
                "   Title: " + title + '\'' + "\n" +
                "   ISBN: " + isbn + '\'' + "\n" +
                "   Price: " + price + "\n" +
                "   Genre:" + genre + '\'' + "\n" +
                "   Publisher: " + publisher + '\'' + "\n" +
                "   Stock Count: " + stockCount + "\n" +
                "    Author ID" + authorId;
    }
}
