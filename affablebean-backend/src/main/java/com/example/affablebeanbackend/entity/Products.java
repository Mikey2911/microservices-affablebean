package com.example.affablebeanbackend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor
@Getter
@Setter
public class Products {

    private List<Product> products = new ArrayList<>();

    public Products(Iterable<Product> iterable){
        products = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
