package ru.abstractcoder.murdermystery.core.game.setting;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.util.temporal.TemporalUtils;

import java.util.concurrent.TimeUnit;

public class GameGeneralSettings {

    private final int gameDuration;
    private final int goldDroppingPeriod;
    private final ItemData goldItemData;

    public GameGeneralSettings(
            @JsonProperty("gameDuration") String gameDuration,
            @JsonProperty("goldDroppingPeriod") String goldDroppingPeriod,
            @JsonProperty("goldItem") ItemData goldItemData) {
        this.gameDuration = parseSeconds(gameDuration);
        this.goldDroppingPeriod = parseSeconds(goldDroppingPeriod);
        this.goldItemData = goldItemData;
    }

    private int parseSeconds(String duration) {
        return (int) TemporalUtils.parseTemporal(duration).get(TimeUnit.SECONDS);
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public int getGoldDroppingPeriod() {
        return goldDroppingPeriod;
    }

    public ItemStack getGoldItem(int amount) {
        return goldItemData.toItemBuilder().amount(amount).build();
    }

}