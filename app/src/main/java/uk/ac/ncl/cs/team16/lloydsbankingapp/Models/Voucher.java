package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by aleksander on 16/04/15.
 */
public class Voucher {

    private String name;
    private int id, cost;

    public Voucher(String name, int cost, int id) {
        this.name = name;
        this.cost = cost;
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
