package ru.abstractcoder.murdermystery.core.lobby.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.misc.BenioSound;

public class StartingSettings {

    private final int minCountStartTime;
    private final int maxCountStartTime;
    private final int standartNotifyingPeriod;
    private final int perSecondNotifyingTime;
    private final BenioSound perSecondNitifyingSound;

    @JsonCreator
    public StartingSettings(
            int minCountStartTime, int maxCountStartTime, int standartNotifyingPeriod, int perSecondNotifyingTime,
            BenioSound perSecondNitifyingSound) {
        this.minCountStartTime = minCountStartTime;
        this.maxCountStartTime = maxCountStartTime;
        this.standartNotifyingPeriod = standartNotifyingPeriod;
        this.perSecondNotifyingTime = perSecondNotifyingTime;
        this.perSecondNitifyingSound = perSecondNitifyingSound;
    }

    public int getMinCountStartTime() {
        return minCountStartTime;
    }

    public int getMaxCountStartTime() {
        return maxCountStartTime;
    }

    public int getStandartNotifyingPeriod() {
        return standartNotifyingPeriod;
    }

    public int getPerSecondNotifyingTime() {
        return perSecondNotifyingTime;
    }

    public BenioSound getPerSecondNitifyingSound() {
        return perSecondNitifyingSound;
    }

}