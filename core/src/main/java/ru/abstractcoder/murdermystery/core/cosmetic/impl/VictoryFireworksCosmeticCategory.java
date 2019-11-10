package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.VictoryResponsible;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class VictoryFireworksCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public VictoryFireworksCosmeticCategory(MsgConfig<Messages> msgConfig,
            ItemData icon, List<Cosmetic> premiumCosmetics) {
        super(msgConfig, icon, premiumCosmetics);
    }

    @Override
    public Type getType() {
        return Type.VICTORY_FIREWORKS;
    }

    @Override
    protected ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic.Logic createDefaultLogic() {
        return new Logic(2, FireworkEffect.builder().build());
    }

    private static class Logic implements VictoryResponsible {

        private final int power;
        private final FireworkEffect effect;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Logic(int power, FireworkEffect effect) {
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

    private class Cosmetic extends AbstractPremiumCosmetic {

        @JsonCreator
        public Cosmetic(String id, ItemData icon, VictoryFireworksCosmeticCategory.Logic logic) {
            super(id, icon, logic);
        }

    }

}
