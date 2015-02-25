package uk.ac.ncl.cs.team16.lloydsbankingapp.models;

/**
 * Created by Alek on 2014-12-14.
 */
public class Transaction{
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
