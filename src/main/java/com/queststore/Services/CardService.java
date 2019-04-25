package com.queststore.Services;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;

import java.util.List;

public class CardService {

    private CardDAO cardDAO;

    public CardService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public List<Card> getAllCards(CardTypes cardTypes) throws DaoException {
        return cardDAO.getCardsOfType(cardTypes);
    }

    public Card getCardById(int id) throws DaoException {
        return cardDAO.getCardById(id);
    }

    public void addNew(Card card) throws DaoException {
        cardDAO.add(card);
    }

    public void update(Card card) throws DaoException {
        cardDAO.update(card);
    }

    public void deleteCard(int cardId) throws DaoException {
        cardDAO.delete(cardId);
    }
}
