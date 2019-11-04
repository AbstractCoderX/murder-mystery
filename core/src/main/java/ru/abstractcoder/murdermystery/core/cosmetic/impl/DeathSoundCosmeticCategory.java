package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.misc.BenioSound;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.DeathResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class DeathSoundCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public DeathSoundCosmeticCategory(String id, List<Cosmetic> cosmetics) {
        super(id, cosmetics);
    }

    private class Cosmetic extends AbstractCosmetic implements DeathResponsibleCosmetic {

        private final BenioSound deathSound;

        @JsonCreator
        public Cosmetic(BenioSound deathSound) {
            this.deathSound = deathSound;
        }

        @Override
        public void onDeath(GamePlayer gamePlayer) {
            deathSound.playIn(gamePlayer.getHandle().getLocation()); //TODO think about death state
        }

    }

}
