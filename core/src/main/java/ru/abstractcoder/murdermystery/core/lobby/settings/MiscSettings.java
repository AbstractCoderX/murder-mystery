package ru.abstractcoder.murdermystery.core.lobby.settings;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MiscSettings {

    private final int cosmeticCasePrice;

    @JsonCreator
    public MiscSettings(int cosmeticCasePrice) {
        this.cosmeticCasePrice = cosmeticCasePrice;
    }

    public int getCosmeticCasePrice() {
        return cosmeticCasePrice;
    }

}
