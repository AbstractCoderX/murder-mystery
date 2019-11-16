package ru.abstractcoder.murdermystery.core.game.role.detective.classes;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;

public class BenedictCyberslotzRoleClass extends DetectiveRoleClass {

    public BenedictCyberslotzRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends DetectiveLogic {

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void kill(GamePlayer victim, DeathState deathState) {
            super.kill(victim, deathState);
            gameEngine.getScheduler().runSyncLater(5, () -> {
                deathState.getSpectatingPlayer().getCorpse().enableGlowingFor(gamePlayer.getHandle());
            });
        }

    }

}