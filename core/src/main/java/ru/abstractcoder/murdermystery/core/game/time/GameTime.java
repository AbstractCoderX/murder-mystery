package ru.abstractcoder.murdermystery.core.game.time;

import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;

import java.util.concurrent.TimeUnit;

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

    public String getFormattedTimeLeft() {
        return SimpleTemporal.of(secondsLeft, TimeUnit.SECONDS).format();
    }

}
