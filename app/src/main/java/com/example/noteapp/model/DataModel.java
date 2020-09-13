package com.example.noteapp.model;

import java.io.Serializable;

public class DataModel implements Serializable {
    final static long serialVersionUID = 1L;
    private int id;
    private String title;
    private String showText;


    public DataModel(String showText) {
        this.showText = showText;
    }

    public DataModel(int id, String showText) {
        this.id = id;
        this.showText = showText;
    }

    public DataModel(int id, String title, String showText) {
        this(id, showText);
        this.title = title;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }
}
