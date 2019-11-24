package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.destroystokyo.paper.ParticleBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Location;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.VictoryResponsible;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class KillEffectCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public KillEffectCosmeticCategory(MsgConfig<Msg> msgConfig,
            @JsonProperty("cosmetics") List<Cosmetic> premiumCosmetics) {
        super(msgConfig, premiumCosmetics);
    }

    @Override
    public Type getType() {
        return Type.KILL_EFFECT;
    }

    private static class Logic implements VictoryResponsible {

        private final ParticleBuilder effect;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Logic(ParticleBuilder effect) {
            this.effect = effect;
        }

        @Override
        public void onVictory(GamePlayer gamePlayer) {
            Location location = gamePlayer.getHandle().getLocation().add(0, 0.7, 0);
            effect.location(location).allPlayers().spawn();
        }

    }

    private static class Cosmetic extends AbstractPremiumCosmetic {

        @JsonCreator
        public Cosmetic(String id, ItemData icon, KillEffectCosmeticCategory.Logic logic) {
            super(id, icon, logic);
        }

    }

}
