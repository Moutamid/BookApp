package com.bevstudio.wolfbooksapp.adapters;

public class BookItem {
    private String name;
    private String image;
    private String volumeId;
    private boolean bookmark;
    public BookItem() {
    }
    public BookItem(String name, String image, String volumeId, boolean bookmark) {
        this.name = name;
        this.image = image;
        this.volumeId = volumeId;
        this.bookmark = bookmark;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
