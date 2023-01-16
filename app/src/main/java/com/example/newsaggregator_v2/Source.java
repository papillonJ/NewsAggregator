package com.example.newsaggregator_v2;

import androidx.annotation.NonNull;

public class Source {

    private String id;
    private String name;
    private String category;

    public Source(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    public String toString() {
        return name;
    }

}
