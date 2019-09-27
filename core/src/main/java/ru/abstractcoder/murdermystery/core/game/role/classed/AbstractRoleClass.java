package ru.abstractcoder.murdermystery.core.game.role.classed;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;

public abstract class AbstractRoleClass implements RoleClass {

    protected final RoleClassTemplate template;
    protected final GameEngine gameEngine;
    protected final MsgConfig<Messages> msgConfig;

    protected AbstractRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.template = template;
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @Override
    public RoleClassTemplate template() {
        return template;
    }

}