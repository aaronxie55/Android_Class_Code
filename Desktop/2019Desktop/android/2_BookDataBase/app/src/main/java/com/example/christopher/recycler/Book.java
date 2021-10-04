package com.example.christopher.recycler;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {

    private String title;
    private String author;
    private int year;
    private String publisher;
    private String isbn;
    private double cost;

    Book(String title, String author, int year, String publisher, String isbn, double cost) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.isbn = isbn;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    int getYear() {
        return year;
    }

    String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        return getIsbn() != null ? getIsbn().equals(book.getIsbn()) : book.getIsbn() == null;
    }

    @Override
    public int hashCode() {
        return getIsbn() != null ? getIsbn().hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull Book o) {
        return getTitle().compareTo(o.getTitle());
    }
}