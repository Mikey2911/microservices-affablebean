package com.example.affablebeanui.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Product {

    private int id;
    private String name;
    private double price;
    private String description;

    private int quantity;

    private List<Integer> quantityList = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdate;

    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
