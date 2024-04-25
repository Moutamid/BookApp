package com.bevstudio.wolfbooksapp.model;

public class BookShelf {
    private String shelfId;
    private String category;

    public BookShelf() {
        // Default constructor required for Firebase
    }

    public BookShelf(String shelfId, String category) {
        this.shelfId = shelfId;
        this.category = category;
    }

    public String getShelfId() {
        return shelfId;
    }

    public String getCategory() {
        return category;
    }
}
