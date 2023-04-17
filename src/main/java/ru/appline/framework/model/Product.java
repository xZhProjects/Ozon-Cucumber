package ru.appline.framework.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Product implements Comparable<Product>{
    private String name;
    private int price;


    public static List<Product> listProducts = new ArrayList<Product>();

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public int compareTo(Product o) {
        return this.getPrice() - o.getPrice();
    }

    @Override
    public String toString() {
        return  name + ", цена: " +
                price + " ₽\n";
    }
}
