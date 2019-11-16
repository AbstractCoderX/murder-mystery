package ru.abstractcoder.murdermystery.core.game.role.murder.classes;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.CorpseClickResponsible;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;

public class GraveNicholasRoleClass extends MurderRoleClass {

    public GraveNicholasRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends MurderLogic implements CorpseClickResponsible {

        private long lastClickTime = -1;
        private long amountOfClicks = 0;

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        private boolean checkClickFinished() {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastClickTime == -1) {
                lastClickTime = currentTimeMillis;
                return false;
            }

            boolean isClickSaved = currentTimeMillis - lastClickTime < 250;
            lastClickTime = currentTimeMillis;

            if (isClickSaved && ++amountOfClicks >= 5 * 3) {
                amountOfClicks = 0;
                return true;
            }
            return false;
        }

        private boolean isClickProcessing() {
            return amountOfClicks > 0;
        }

        @Override
        public void onCorpseClick(Corpse corpse, int slot) {
            if (slot != SharedConstants.WEAPON_SLOT) {
                return;
            }

            if (checkClickFinished()) {
                gameEngine.getCorpseService().removeCorpse(corpse);
            }
        }

    }

}