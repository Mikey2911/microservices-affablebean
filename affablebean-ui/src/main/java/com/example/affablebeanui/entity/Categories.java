package com.example.affablebeanui.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@Setter
@NoArgsConstructor
public class Categories {

    private List<Category> categories = new ArrayList<>();

    public Categories(Iterable<Category> iterable){
        categories = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
