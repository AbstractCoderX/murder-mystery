package ru.abstractcoder.murdermystery.core.game.role.logic;

import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyKillResponsible;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

public abstract class AbstractRoleLogic implements RoleLogic {

    protected final GamePlayer gamePlayer;
    protected final GameEngine gameEngine;
    protected final MsgConfig<Messages> msgConfig;

    protected AbstractRoleLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.gamePlayer = gamePlayer;
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @Override
    public void kill(GamePlayer victim, DeathState deathState) {
        victim.getRoleLogic().death(gamePlayer, deathState);

        gameEngine.getRoleResolver().getResponsibleLogics(AnyKillResponsible.class)
                .forEach(resp -> resp.onAnyKill(gamePlayer, victim, deathState));
    }

    @Override
    public void kill(GamePlayer victim) {
        kill(victim, new DeathState(victim.getHandle().getLocation()));
    }

    @Override
    public void death(@Nullable GamePlayer killer, DeathState deathState) {
        SpectatingPlayer spectatingPlayer = gameEngine.getPlayerController()
                .makeSpectating(gamePlayer, deathState.isNeedCorpse());
        deathState.setSpectatingPlayer(spectatingPlayer);
    }

    @Override
    public void death() {
        death(null, new DeathState(gamePlayer.getHandle().getLocation()));
    }

    @Override
    public void onGoldPickup(int amount) {
        gameEngine.getGoldManager().giveGold(gamePlayer, amount);
    }

}