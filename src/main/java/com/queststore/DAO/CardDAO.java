package com.queststore.DAO;

import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;

import java.util.List;

public interface CardDAO {

    List<Card> getCardsOfType(CardTypes cardTypes) throws DaoException;

    Card getCardById(int id);

    Categories getCategoryById(int id);

    CardTypes getCardTypeById(int id);

    void add(Card card) throws DaoException;

    void update(Card card) throws DaoException;

    void delete(int cardId) throws DaoException;
}
