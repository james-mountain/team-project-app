package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Aleksander on 25/02/2015.
 */

public class Account {

    private String id;
    private String name;
    private String type;
    private String balance;

    public Account(String id, String name, String type, String balance) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
