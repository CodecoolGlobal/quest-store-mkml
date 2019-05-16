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

public class ItemCardAddTest{

    @Test
    public void addCardToDB() throws DaoException {
        CardDAO cardDAOMock = mock(CardDAO.class);
        ItemCardAdd itemCardAdd = new ItemCardAdd(cardDAOMock);

        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("4");

        Card card = new Card(4, items.get(0), items.get(2), new Categories(1, "easy"), null,
                Integer.parseInt(items.get(1)), new CardTypes(2, "artifact"), true);

        List<Card> listOfCards = new ArrayList<>();
        listOfCards.add(card);

        assertEquals(itemCardAdd.addCardToDB(items).get(0).getValue(), listOfCards.get(0).getValue());
        assertEquals(itemCardAdd.addCardToDB(items).get(0).getId(), listOfCards.get(0).getId());
        assertEquals(itemCardAdd.addCardToDB(items).get(0).getDescription(), listOfCards.get(0).getDescription());
        assertEquals(itemCardAdd.addCardToDB(items).get(0).getName(), listOfCards.get(0).getName());
    }

}