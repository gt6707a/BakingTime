package com.android.gt6707a.bakingtime.entity;

public class Recipe {
    private int id;
    public int getId() { return id; }
    public Recipe setId(int id) {
        this.id = id;
        return this;
    }

    private String name;
    public String getName() { return name; }
    public Recipe setName(String name) {
        this.name = name;
        return this;
    }
}
