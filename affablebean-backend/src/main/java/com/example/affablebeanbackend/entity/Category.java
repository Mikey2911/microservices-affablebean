package com.example.affablebeanbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "category")
@Entity
@Getter
@Setter
public class Category {

    @Id
    private int id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}
