package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Alek on 2014-12-14.
 */
public class Transaction{
    private String payee;
    private String date;
    private String balance;
    private String value;

    public Transaction(String payee, String date, String balance, String value){
        this.payee = payee;
        this.date = date;
        this.balance = balance;
        this.value = value;
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
}
