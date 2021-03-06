package com.queststore.DAO;

import java.sql.*;


public class CreateTables {

    public static void main(String[] args) throws SQLException {
        CreateTables createTables = new CreateTables();
        createTables.createAllTables();
    }
    public void createAllTables() throws SQLException {


        String userTypeSQL = "CREATE TABLE IF NOT EXISTS user_type(\n" +
                "id serial,\n" +
                "name VARCHAR(20),\n" +
                "PRIMARY KEY (id)\n" +
                ");";


        String classesSQL = "CREATE TABLE IF NOT EXISTS classes(\n" +
                "id serial,\n" +
                "name VARCHAR(20) UNIQUE NOT NULL,\n" +
                "is_active BOOLEAN NOT NULL,\n" +
                "PRIMARY KEY (id)\n" +
                ");";

        String levelsSQL = "CREATE TABLE IF NOT EXISTS levels(\n" +
                "id serial PRIMARY KEY,\n" +
                "name VARCHAR(20),\n" +
                "level_start INTEGER NOT NULL\n" +
                ");\n";

        String transactionsStatusSQL = "CREATE TABLE IF NOT EXISTS transactions_status (\n" +
                "id serial PRIMARY KEY,\n" +
                "name VARCHAR(20) UNIQUE NOT NULL\n" +
                ");";

        String cardTypesSQL = "CREATE TABLE IF NOT EXISTS card_types(\n" +
                "id serial PRIMARY KEY,\n" +
                "name VARCHAR(30)\n" +
                ");";

        String categoriesSQL = "CREATE TABLE IF NOT EXISTS categories(\n" +
                "id serial PRIMARY KEY,\n" +
                "name VARCHAR(20)\n" +
                ");";

        String cardsSQL = "CREATE TABLE IF NOT EXISTS cards(\n" +
                "id serial PRIMARY KEY,\n" +
                "name VARCHAR(30) UNIQUE NOT NULL,\n" +
                "description VARCHAR(60) NOT NULL,\n" +
                "category_ID INTEGER,\n" +
                "photo BYTEA,\n" +
                "value INTEGER NOT NULL,\n" +
                "card_type_id INTEGER,\n" +
                "is_active BOOLEAN,\n" +
                "FOREIGN KEY (category_id) REFERENCES categories (id),\n" +
                "FOREIGN KEY (card_type_id) REFERENCES card_types (id)\n" +
                ");";

        String transactionsSQL = "CREATE TABLE IF NOT EXISTS transactions(\n" +
                "id serial PRIMARY KEY,\n" +
                "date DATE NOT NULL DEFAULT CURRENT_DATE,\n" +
                "user_id INTEGER,\n" +
                "card_id INTEGER,\n" +
                "status_id INTEGER,\n" +
                "cost INTEGER NOT NULL,\n" +
                "FOREIGN KEY (status_id) REFERENCES transactions_status (id),\n" +
                "FOREIGN KEY (user_id) REFERENCES users (id),\n" +
                "FOREIGN KEY (card_id) REFERENCES cards (id)\n" +
                ");";

        String categoriesCardTypesSQL = "CREATE TABLE IF NOT EXISTS categories_card_types(\n" +
                "id serial PRIMARY KEY,\n" +
                "card_type_id INTEGER,\n" +
                "category_id INTEGER,\n" +
                "FOREIGN KEY (card_type_id) REFERENCES card_types (id),\n" +
                "FOREIGN KEY (category_id) REFERENCES categories (id)\n" +
                ");";

        String usersSQL = "CREATE TABLE IF NOT EXISTS users(\n" +
                "id serial PRIMARY KEY,\n" +
                "password BYTEA NOT NULL,\n" +
                "firstname VARCHAR(30) NOT NULL,\n" +
                "lastname VARCHAR(40) NOT NULL,\n" +
                "email VARCHAR(40) UNIQUE NOT NULL,\n" +
                "class_id INTEGER,\n" +
                "avatar BYTEA,\n" +
                "user_type_id INTEGER NOT NULL,\n" +
                "is_active BOOLEAN,\n" +
                "FOREIGN KEY (class_id) REFERENCES classes (id),\n" +
                "FOREIGN KEY (user_type_id) REFERENCES user_type (id)\n" +
                ");";

        String sessionSQL = "CREATE TABLE IF NOT EXISTS sessions( " +
                "session_id VARCHAR(50) NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "CONSTRAINT sessions_pkey PRIMARY KEY (session_id));";

        Statement stmt = DBCPDataSource.getConnection().createStatement();
        stmt.execute(classesSQL);
        stmt.execute(levelsSQL);
        stmt.execute(transactionsStatusSQL);
        stmt.execute(cardTypesSQL);
        stmt.execute(categoriesSQL);
        stmt.execute(cardsSQL);
        stmt.execute(userTypeSQL);
        stmt.execute(usersSQL);
        stmt.execute(transactionsSQL);
        stmt.execute(categoriesCardTypesSQL);
        stmt.execute(sessionSQL);
    }
}

