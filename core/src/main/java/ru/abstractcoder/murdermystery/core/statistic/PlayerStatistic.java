package ru.abstractcoder.murdermystery.core.statistic;

public class PlayerStatistic {

    private int wins = 0;
    private int defeats = 0;
    private int kills = 0;
    private int deaths = 0;
    private int goldsPickedUp = 0;
    private int rating = 0;

    public PlayerStatistic(int wins, int defeats, int kills, int deaths, int goldsPickedUp, int rating) {
        this.wins = wins;
        this.defeats = defeats;
        this.kills = kills;
        this.deaths = deaths;
        this.goldsPickedUp = goldsPickedUp;
        this.rating = rating;
    }

    public PlayerStatistic() {
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementDefeats() {
        deaths++;
    }

    public void incrementKills() {
        kills++;
    }

    public void incrementDeaths() {
        deaths++;
    }

    public void incrementGoldsPickedUp() {
        goldsPickedUp++;
    }

    public void incrementRating(int amount) {
        rating += amount;
    }

    public void decrementRating(int amount) {
        rating -= amount;
    }

    public int getWins() {
        return wins;
    }

    public int getDefeats() {
        return defeats;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getGoldsPickedUp() {
        return goldsPickedUp;
    }

    public int getRating() {
        return rating;
    }

}