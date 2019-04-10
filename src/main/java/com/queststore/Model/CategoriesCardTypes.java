package com.queststore.Model;

public class CategoriesCardTypes {

    private int id;
    private CardTypes cardTypes;
    private Categories categories;

    public CategoriesCardTypes(int id, CardTypes cardTypes, Categories categories) {
        this.id = id;
        this.cardTypes = cardTypes;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardTypes getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(CardTypes cardTypes) {
        this.cardTypes = cardTypes;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
