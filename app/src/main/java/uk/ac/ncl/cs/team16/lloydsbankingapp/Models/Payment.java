package uk.ac.ncl.cs.team16.lloydsbankingapp.models;

import java.util.Calendar;

/**
 * Created by James Mountain on 14/02/2015.
 */
public class Payment {
    private int paymentNumber;
    private String payee;
    private Calendar date;
    private String amount;

    public Payment(int paymentNumber, String payee, Calendar date, String amount) {
        this.paymentNumber = paymentNumber;
        this.payee = payee;
        this.date = date;
        this.amount = amount;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public String getPayee() {
        return payee;
    }

    public Calendar getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
