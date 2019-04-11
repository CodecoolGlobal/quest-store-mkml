package com.queststore.Model;

import java.sql.Blob;

public class Card {

    private int id;
    private String name;
    private String description;
    private Categories categories;
    private Blob photo;
    private int value;
    private CardTypes cardTypes;
    private boolean is_active;

    public Card(int id, String name, String description, Categories categories, Blob photo, int value, CardTypes cardTypes, boolean is_active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.photo = photo;
        this.value = value;
        this.cardTypes = cardTypes;
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public CardTypes getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(CardTypes cardTypes) {
        this.cardTypes = cardTypes;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
