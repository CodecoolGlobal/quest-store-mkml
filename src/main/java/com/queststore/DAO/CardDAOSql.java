package com.queststore.DAO;

import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import com.queststore.Services.CardService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDAOSql implements CardDAO {
    CardService cardService;


    public CardDAOSql(){
        this.cardService = new CardService();
    }

    @Override
    public Card getCardById(int id) {
        String SQL = "SELECT * FROM cards WHERE id=?";
        Card card = null;
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            card = cardService.createCardObject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return card;
    }

    @Override
    public Categories getCategoryById(int id) {
        String SQL = "SELECT * FROM categories WHERE id = ?";
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
}
