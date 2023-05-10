package com.example.timble;

public class Books {
    String book_id;
    String name;
    String author;
    String votes;
    String cover;
    String url;

    public Books() {
    }

    public Books(String book_id, String name, String author, String votes, String cover, String url) {
        this.book_id = book_id;
        this.name = name;
        this.author = author;
        this.votes = votes;
        this.cover = cover;
        this.url = url;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
