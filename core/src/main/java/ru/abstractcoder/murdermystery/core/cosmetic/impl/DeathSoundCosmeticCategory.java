package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.util.misc.BenioSound;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.DeathResponsible;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class DeathSoundCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public DeathSoundCosmeticCategory(MsgConfig<Msg> msgConfig,
            @JsonProperty("cosmetics") List<Cosmetic> premiumCosmetics) {
        super(msgConfig, premiumCosmetics);
    }

    @Override
    public Type getType() {
        return Type.DEATH_SOUND;
    }

    private static class Logic implements DeathResponsible {

        private final BenioSound deathSound;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Logic(BenioSound deathSound) {
            this.deathSound = deathSound;
        }

        @Override
        public void onDeath(GamePlayer gamePlayer, DeathState deathState) {
            deathSound.playIn(deathState.getDeathLocation());
        }

    }

    private static class Cosmetic extends AbstractPremiumCosmetic {

        @JsonCreator
        public Cosmetic(String id, ItemData icon, DeathSoundCosmeticCategory.Logic logic) {
            super(id, icon, logic);
        }

    }

}
