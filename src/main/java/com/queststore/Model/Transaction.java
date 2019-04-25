package com.queststore.Model;

import java.util.Date;

public class Transaction {

    private Integer id;
    private Date date;
    private User user;
    private Card card;
    private TransactionStatus transactionStatus;
    private int cost;

    public Transaction(Integer id, Date date, User user, Card card, TransactionStatus transactionStatus, int cost) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.card = card;
        this.transactionStatus = transactionStatus;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
