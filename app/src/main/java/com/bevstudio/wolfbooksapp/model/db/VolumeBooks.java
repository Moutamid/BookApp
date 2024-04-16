package com.bevstudio.wolfbooksapp.model.db;

public class VolumeBooks {

    private int id;
    private String str_id;
    private String volumeId;
    private String name;
    private String link;
    private boolean isBookmark;

    public VolumeBooks() {
    }


    public VolumeBooks(String volumeId, boolean isBookmark) {
        this.volumeId = volumeId;
        this.isBookmark = isBookmark;
    }    public VolumeBooks(String volumeId, boolean isBookmark, String name, String link ) {
        this.volumeId = volumeId;
        this.isBookmark = isBookmark;
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }
}
