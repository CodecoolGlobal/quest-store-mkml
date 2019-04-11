package com.queststore.Services;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.CardDAOSql;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardService {


    public Card createCardObject(ResultSet rs) throws SQLException {
        CardDAO cardDAOSql = new CardDAOSql();
        rs.next();
        Categories category = cardDAOSql.getCategoryById(rs.getInt("category_id"));
        CardTypes cardType = cardDAOSql.getCardTypeById(rs.getInt("card_type_id"));

        return new Card(rs.getInt("id"),rs.getString("name"), rs.getString("description")
        , category, null, rs.getInt("value"), cardType, rs.getBoolean("is_active"));
//TODO: Wstawić pobranie zdjecia (poki co problem z BLOB dlatego null)

    }
}
