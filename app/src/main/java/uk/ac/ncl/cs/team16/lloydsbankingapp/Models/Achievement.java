package uk.ac.ncl.cs.team16.lloydsbankingapp.Models;

/**
 * Created by Roy on 14/04/2015.
 */
public class Achievement {

    private String name;
    private String description;
    private int points;

    public Achievement(String name, String description, int points) {

        this.name = name;
        this.description = description;
        this.points = points;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }

    public int getPoints() {

        return points;
    }
}
