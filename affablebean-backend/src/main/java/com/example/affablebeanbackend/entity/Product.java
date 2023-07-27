package com.example.affablebeanbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {

    @Id
    private int id;
    private String name;
    private double price;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
