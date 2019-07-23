package com.queststore.Services;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemCardUpdateTest {

    @Test
    public void updateCardInDB() throws DaoException {
        CardDAO cardDAOMock = mock(CardDAO.class);

        ItemCardUpdate itemCardUpdate = new ItemCardUpdate(cardDAOMock);

        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("dupa");
        items.add("1");
        items.add("dupa");

        Card card = new Card(Integer.parseInt(items.get(0)), items.get(1), items.get(3), new Categories(1, "easy"), null,
                Integer.parseInt(items.get(2)), new CardTypes(2, "artifact"), true);

        List<Card> listOfCards = new ArrayList<>();
        listOfCards.add(card);

        when(cardDAOMock.getCardsOfType(cardDAOMock.getCardTypeById(2))).thenReturn(listOfCards);

        assertEquals(listOfCards, itemCardUpdate.updateCardInDB(items));
    }
}