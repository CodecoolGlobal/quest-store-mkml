package com.queststore.Model;

import java.util.Date;

public class Transaction {

    private int id;
    private Date date;
    private User user;
    private Card card;
    private TransactionStatus transactionStatus;
    private int cost;

    public Transaction(int id, Date date, User user, Card card, TransactionStatus transactionStatus, int cost) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.card = card;
        this.transactionStatus = transactionStatus;
        this.cost = cost;
    }
}
