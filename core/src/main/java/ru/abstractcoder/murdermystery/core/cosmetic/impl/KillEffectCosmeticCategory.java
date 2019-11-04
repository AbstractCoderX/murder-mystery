package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.destroystokyo.paper.ParticleBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.Location;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.VictoryResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class KillEffectCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public KillEffectCosmeticCategory(MsgConfig<Messages> msgConfig,
            String id, List<Cosmetic> cosmetics) {
        super(msgConfig, id, cosmetics);
    }

    private class Cosmetic extends AbstractCosmetic implements VictoryResponsibleCosmetic {

        private final ParticleBuilder effect;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public Cosmetic(ParticleBuilder effect) {
            this.effect = effect;
        }

        @Override
        public void onVictory(GamePlayer gamePlayer) {
            Location location = gamePlayer.getHandle().getLocation().add(0, 0.7, 0);
            effect.location(location).allPlayers().spawn();
        }

    }

}
