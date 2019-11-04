package ru.abstractcoder.murdermystery.core.game.role.logic;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;

public abstract class SurvivorLogic extends AbstractRoleLogic {

    protected SurvivorLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(gamePlayer, gameEngine, msgConfig);
    }

    @Override
    public void kill(GamePlayer victim, DeathState deathState) {
        super.kill(victim, deathState);

        GameRole.Type roleType = victim.getRole().getType();
        if (roleType != GameRole.Type.MURDER) {
            msgConfig.get(Messages.game__wrong_killer_die).sendTitle(gamePlayer);
            gamePlayer.getRoleLogic().death();
        }
    }

}