package ru.abstractcoder.murdermystery.core.game.time;

public class GameTime {

    private int secondsLeft;

    public GameTime(int gameDuration) {
        secondsLeft = gameDuration;
    }

    public int decrement() {
        return --secondsLeft;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

}
