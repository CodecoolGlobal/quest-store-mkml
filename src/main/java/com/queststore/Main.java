package com.queststore;

import com.queststore.DAO.DBCPDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DBCPDataSource.getConnection();
            System.out.println("Connection get");
        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
}
