package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Aleksander on 2014-12-14.
 */
public class Transaction{
    private String id;
    private String payee;
    private String date;
    private String balance;
    private String value;

    public Transaction(String p, String d, String b, String v){
        this.payee = p;
        this.date = d;
        this.balance = b;
        this.value = v;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayee() {
        return payee;
    }

    public String getDate() {
        return date;
    }

    public String getBalance() {
        return balance;
    }

    public String getValue() {
        return value;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
