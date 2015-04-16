package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Roy on 16/04/2015.
 */
public class Reward {

    private String name, code;
    private int cost;

    public Reward(String name, String code, int cost) {

        this.name = name;
        this.code = code;
        this.cost = cost;
    }

    public String getName() {

        return name;
    }

    public String getCode() {

        return code;
    }

    public int getCost() {

        return cost;
    }
}
