package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Aleksander on 25/02/2015.
 */
public class Payee {
    private String id;
    private String name;
    private String sortCode;
    private String accNumber;
    private String amount;

    public Payee(String id, String name, String sortCode, String accNumber, String amount) {
        this.id = id;
        this.name = name;
        this.sortCode = sortCode;
        this.accNumber = accNumber;
        this.amount = amount;
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

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
