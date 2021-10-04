package com.example.stockwatch;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Stock implements Serializable, Comparable<Stock>{
    private String Symbol;
    private String Company;
    private double price;
    private double priceChange;
    private double percentage;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "Symbol='" + Symbol + '\'' +
                ", Company='" + Company + '\'' +
                ", price=" + price +
                ", priceChange=" + priceChange +
                ", percentage=" + percentage +
                '}';
    }

    @Override
    public int compareTo(@NonNull Stock s) {
        return getSymbol().compareTo(s.getSymbol());
    }
}
