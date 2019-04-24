package com.queststore.DAO;

import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import com.queststore.Services.CardService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAOSql implements CardDAO {

    public static void main(String[] args) {
        CardDAO cardDAO = new CardDAOSql();
        Categories categories = new Categories(1, "name");
        CardTypes cardTypes = new CardTypes(1, "name");
        Card card = new Card(1, "myCard", "this is a card", categories, null, 300, cardTypes, true);
        try {
            cardDAO.delete(card.getId());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> getCardsOfType(CardTypes cardTypes) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT * FROM cards WHERE card_type_id = ? AND is_active = true ORDER BY id ASC;"
             )){
            List<Card> cards = new ArrayList<>();
            pstmt.setInt(1, cardTypes.getId());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                cards.add(createCardObject(rs));
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during fetching all cards");
        }
    }

    @Override
    public Card getCardById(int id) {
        String SQL = "SELECT * FROM cards WHERE id=? AND is_active = true;";
        Card card = null;
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            card = createCardObject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return card;
    }

    @Override
    public Categories getCategoryById(int id) {
        String SQL = "SELECT * FROM categories WHERE id = ?;";
        Categories category = null;

        try(Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            category = new Categories(id, rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public CardTypes getCardTypeById(int id) {
        String SQL = "SELECT * FROM card_types WHERE id = ?";
        CardTypes cardType = null;

        try(Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            cardType = new CardTypes(id, rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardType;
    }

    @Override
    public void add(Card card) throws DaoException {
        try(Connection connection = DBCPDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO cards (name, description, category_id, photo, value, card_type_id, is_active) " +
                        "VALUES (?, ?, ?, ?, ?, ?, true);"
        )){
            setStatementValues(statement, card);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Card card) throws DaoException {
        try(Connection connection = DBCPDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE cards SET name = ?, description = ?, category_id = ?, photo = ?, value = ?, card_type_id = ? " +
                            "WHERE id = ?;"
            )){
            setStatementValues(statement, card);
            statement.setInt(7, card.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStatementValues(PreparedStatement statement, Card card) throws SQLException {
        statement.setString(1, card.getName());
        statement.setString(2, card.getDescription());
        statement.setInt(3, card.getCategories().getId());
        statement.setBytes(4, "dupa".getBytes());
        statement.setInt(5, card.getValue());
        statement.setInt(6, card.getCardTypes().getId());
    }

    @Override
    public void delete(int cardId) throws DaoException {
        try(Connection connection = DBCPDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE cards SET is_active = false WHERE id = ?;"
            )){
            statement.setInt(1, cardId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Card createCardObject(ResultSet rs) throws SQLException {
        Categories category = getCategoryById(rs.getInt("category_id"));
        CardTypes cardType = getCardTypeById(rs.getInt("card_type_id"));

        return new Card(rs.getInt("id"),rs.getString("name"), rs.getString("description")
                , category, null, rs.getInt("value"), cardType, rs.getBoolean("is_active"));
        //TODO: Wstawić pobranie zdjecia (poki co problem z BLOB dlatego null)
    }
}
