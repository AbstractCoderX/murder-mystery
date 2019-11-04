package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.misc.BenioSound;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.DeathResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class DeathSoundCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public DeathSoundCosmeticCategory(MsgConfig<Messages> msgConfig,
            String id, List<Cosmetic> cosmetics) {
        super(msgConfig, id, cosmetics);
    }

    private class Cosmetic extends AbstractCosmetic implements DeathResponsibleCosmetic {

        private final BenioSound deathSound;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public Cosmetic(BenioSound deathSound) {
            this.deathSound = deathSound;
        }

        @Override
        public void onDeath(GamePlayer gamePlayer, DeathState deathState) {
            deathSound.playIn(deathState.getDeathLocation());
        }

    }

}
