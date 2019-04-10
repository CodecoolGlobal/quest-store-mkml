package com.queststore;

import com.queststore.DAO.CreateTables;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        CreateTables createTables = new CreateTables();
        try {
            createTables.createAllTables();
        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }
}
