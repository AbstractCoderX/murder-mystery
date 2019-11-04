package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.KillResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class KillMessageCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public KillMessageCosmeticCategory(MsgConfig<Messages> msgConfig,
            String id, List<Cosmetic> cosmetics) {
        super(msgConfig, id, cosmetics);
    }

    private class Cosmetic extends AbstractCosmetic implements KillResponsibleCosmetic {

        private final String action;

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public Cosmetic(String action) {
            this.action = action;
        }

        @Override
        public void onKill(GamePlayer killer, GamePlayer victim) {
            msgConfig.get(Messages.game__kill_player, action, victim.getName()).send(killer);
        }

    }

}
