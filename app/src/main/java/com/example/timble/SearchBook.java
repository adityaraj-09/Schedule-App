package com.example.timble;

public class SearchBook {
    String book_id;
    String name;
    String cover;
    String url;
    String rating;
    String year;

    public SearchBook() {
    }

    public SearchBook(String book_id, String name, String cover, String url, String rating, String year) {
        this.book_id = book_id;
        this.name = name;
        this.cover = cover;
        this.url = url;
        this.rating = rating;
        this.year = year;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
