package ru.abstractcoder.murdermystery.core.cosmetic.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.KillResponsible;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

import java.util.List;

public class KillMessageCosmeticCategory extends AbstractCosmeticCategory {

    @JsonCreator
    public KillMessageCosmeticCategory(MsgConfig<Messages> msgConfig,
            ItemData icon, List<Cosmetic> premiumCosmetics) {
        super(msgConfig, icon, premiumCosmetics);
    }

    @Override
    public Type getType() {
        return Type.KILL_MESSAGE;
    }

    @Override
    protected ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic.Logic createDefaultLogic() {
        return new Logic("убил");
    }

    private class Logic implements KillResponsible {

        private final String action;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Logic(String action) {
            this.action = action;
        }

        @Override
        public void onKill(GamePlayer killer, GamePlayer victim) {
            msgConfig.get(Messages.game__kill_player, action, victim.getName()).send(killer);
        }

    }

    private class Cosmetic extends AbstractPremiumCosmetic {

        @JsonCreator
        public Cosmetic(String id, ItemData icon, KillMessageCosmeticCategory.Logic logic) {
            super(id, icon, logic);
        }

    }

}
