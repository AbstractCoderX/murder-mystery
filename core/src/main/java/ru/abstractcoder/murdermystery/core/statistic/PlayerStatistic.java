package ru.abstractcoder.murdermystery.core.statistic;

public class PlayerStatistic {

    private int wins = 0;
    private int defeats = 0;
    private int kills = 0;
    private int deaths = 0;
    private int goldPickuped;
    private int rating = 0;

    public PlayerStatistic(int wins, int defeats, int rating) {
        this.wins = wins;
        this.defeats = defeats;
        this.rating = rating;
    }

    public PlayerStatistic() {
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
