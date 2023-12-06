package az.edu.ada.db.as2.entity;

import java.sql.Date;

public class Author {
    private int authorId;
    private String name;
    private String bio;
    private Date birthDate;

    // Constructor
    public Author() {
    }

    public Author(int authorId, String name, String bio, Date birthDate) {
        this.authorId = authorId;
        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
    }

    // Getters and Setters
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Author: \n" +
                "   Author ID: " + authorId + "\n" +
                "   Name:" + name + '\'' + "\n" +
                "   Biography: " + bio + '\'' + "\n" +
                "   Date of Birth: " + birthDate;
    }
}
