package ru.abstractcoder.murdermystery.core.game.role.logic;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.board.sidebar.Sidebar;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.DeathResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.KillResponsibleCosmetic;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyKillResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOwnMoveResponsible;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

public abstract class AbstractRoleLogic implements RoleLogic, AnyOwnMoveResponsible {

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
        gamePlayer.cosmetics(KillResponsibleCosmetic.class)
                .forEach(resp -> resp.onKill(gamePlayer, victim));

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
        PlayerController playerController = gameEngine.getPlayerController();
        SpectatingPlayer spectatingPlayer = playerController.makeSpectating(gamePlayer, deathState.isNeedCorpse());
        deathState.setSpectatingPlayer(spectatingPlayer);

        gamePlayer.cosmetics(DeathResponsibleCosmetic.class)
                .forEach(resp -> resp.onDeath(gamePlayer, deathState));

        Sidebar cachedSidebar = gamePlayer.getCachedSidebar();
        if (cachedSidebar != null) {
            gameEngine.getSidebarService().setSidebar(gamePlayer.getHandle(), cachedSidebar);
        }

        gamePlayer.setMuted(false);
        gamePlayer.setUnmovedDamageEnabled(false);
    }

    @Override
    public void death() {
        death(null, new DeathState(gamePlayer.getHandle().getLocation()));
    }

    @Override
    public void onGoldPickup(int amount) {
        gameEngine.getGoldManager().giveGold(gamePlayer, amount);
    }

    @Override
    public void onAnyMove(Location from, Location to, Cancellable event) {
        if (!gamePlayer.isUnmovedDamageEnabled()) {
            return;
        }

        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }

        gamePlayer.resetUnmovedSeconds();
    }

}