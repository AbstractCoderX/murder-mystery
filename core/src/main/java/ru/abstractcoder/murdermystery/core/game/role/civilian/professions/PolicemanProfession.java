package ru.abstractcoder.murdermystery.core.game.role.civilian.professions;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.civilian.CivilianLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.profession.AbstractProfession;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

public class PolicemanProfession extends AbstractProfession {

    public PolicemanProfession(ProfessionTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public Type getType() {
        return Type.POLICEMAN;
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends CivilianLogic {

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        protected int getBowGoldPeriod() {
            return 7;
        }

    }

}
