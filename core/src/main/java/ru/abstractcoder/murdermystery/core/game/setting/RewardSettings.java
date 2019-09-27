package ru.abstractcoder.murdermystery.core.game.setting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.util.temporal.TemporalUtils;

import java.util.concurrent.TimeUnit;

public class RewardSettings {

    private final int murderKillMoney;
    private final int survivorMoneyStartTime;
    private final int survivorMoneyTimePeriod;
    private final int survivorPeriodicMoney;

    @JsonCreator
    public RewardSettings(
            int murderKillMoney,
            @JsonProperty("survivorMoneyStartTime") String survivorMoneyStartTime,
            @JsonProperty("survivorMoneyTimePeriod") String survivorMoneyTimePeriod,
            int survivorPeriodicMoney) {
        this.murderKillMoney = murderKillMoney;
        this.survivorPeriodicMoney = survivorPeriodicMoney;
        this.survivorMoneyStartTime = parseSeconds(survivorMoneyStartTime);
        this.survivorMoneyTimePeriod = parseSeconds(survivorMoneyTimePeriod);
    }

    private int parseSeconds(String duration) {
        return (int) TemporalUtils.parseTemporal(duration).get(TimeUnit.SECONDS);
    }

    public int getMurderKillMoney() {
        return murderKillMoney;
    }

    public int getSurvivorMoneyStartTime() {
        return survivorMoneyStartTime;
    }

    public int getSurvivorMoneyTimePeriod() {
        return survivorMoneyTimePeriod;
    }

    public int getSurvivorPeriodicMoney() {
        return survivorPeriodicMoney;
    }

}