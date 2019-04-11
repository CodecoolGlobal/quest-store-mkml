package com.queststore.DAO;

import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;

public interface CardDAO {

    Card getCardById(int id);
    Categories getCategoryById(int id);
    CardTypes getCardTypeById(int id);
}
