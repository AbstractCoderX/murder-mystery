package ru.abstractcoder.murdermystery.core.game.role.profession;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

public abstract class AbstractProfession implements Profession {

    private final ProfessionTemplate template;
    protected final GameEngine gameEngine;
    protected final MsgConfig<Msg> msgConfig;

    protected AbstractProfession(ProfessionTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        this.template = template;
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @Override
    public ProfessionTemplate template() {
        return template;
    }

}
