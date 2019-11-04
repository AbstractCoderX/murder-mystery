package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.VictoryResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class VictoryFireworksCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public VictoryFireworksCosmeticCategory(MsgConfig<Messages> msgConfig,
            String id, List<Cosmetic> cosmetics) {
        super(msgConfig, id, cosmetics);
    }

    private class Cosmetic extends AbstractCosmetic implements VictoryResponsibleCosmetic {

        private final int power;
        private final FireworkEffect effect;

        @JsonCreator
        public Cosmetic(int power, FireworkEffect effect) {
            this.power = power;
            this.effect = effect;
        }

        @Override
        public void onVictory(GamePlayer gamePlayer) {
            Location location = gamePlayer.getHandle().getLocation().add(0, 2.1, 0);
            Firework firework = location.getWorld().spawn(location, Firework.class);

            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(power);
            meta.addEffect(effect);
            firework.setFireworkMeta(meta);
        }

    }

}
