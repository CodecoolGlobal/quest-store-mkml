package com.queststore.Services;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.CardDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CardService {

    private CardDAO cardDAO;

    public CardService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public List<Card> getAllCards(CardTypes cardTypes) throws DaoException {
        return cardDAO.getCardsOfType(cardTypes);
    }
}
