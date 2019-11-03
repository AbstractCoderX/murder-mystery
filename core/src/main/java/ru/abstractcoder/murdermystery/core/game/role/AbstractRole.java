package ru.abstractcoder.murdermystery.core.game.role;

import com.google.common.base.Preconditions;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.equipper.PlayerEquipper;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRole implements GameRole {

    private final RoleTemplate template;
    private final PlayerEquipper equipper;
    private final Map<GamePlayer, RoleLogic> logicMap = new HashMap<>();

    protected AbstractRole(RoleTemplate template, PlayerEquipper equipper) {
        this.template = template;
        this.equipper = equipper;
    }

    @Override
    public RoleTemplate template() {
        return template;
    }

    @Override
    public PlayerEquipper getEquipper() {
        return equipper;
    }

    @Override
    public void initLogic(GamePlayer gamePlayer) {
        logicMap.put(gamePlayer, getComponent().createLogic(gamePlayer));
    }

    @Override
    public RoleLogic getLogic(GamePlayer gamePlayer) {
        RoleLogic roleLogic = logicMap.get(gamePlayer);
        Preconditions.checkState(roleLogic != null, "RoleLogic not initialized for %s", gamePlayer);
        return roleLogic;
    }

}