package ru.abstractcoder.murdermystery.core.game.role.logic;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.board.sidebar.Sidebar;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.DeathResponsible;
import ru.abstractcoder.murdermystery.core.cosmetic.responsible.KillResponsible;
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
    protected final MsgConfig<Msg> msgConfig;

    protected AbstractRoleLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        this.gamePlayer = gamePlayer;
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @Override
    public void kill(GamePlayer victim, DeathState deathState) {
        gamePlayer.data().statistic().incrementKills();

        gamePlayer.cosmetics(KillResponsible.class)
                .forEach(resp -> resp.onKill(gamePlayer, victim));

        victim.getRoleLogic().death(gamePlayer, deathState);

        gameEngine.getRoleResolver().getResponsibleLogics(AnyKillResponsible.class)
                .forEach(resp -> resp.onAnyKill(gamePlayer, victim, deathState));
    }

    @Override
    public final void kill(GamePlayer victim) {
        kill(victim, new DeathState(victim.getHandle().getLocation()));
    }

    @Override
    public void death(@Nullable GamePlayer killer, DeathState deathState) {
        this.unload();
        gamePlayer.data().statistic().incrementDeaths();

        PlayerController playerController = gameEngine.getPlayerController();
        SpectatingPlayer spectatingPlayer = playerController.makeSpectating(gamePlayer, deathState.isNeedCorpse());
        deathState.setSpectatingPlayer(spectatingPlayer);

        gamePlayer.cosmetics(DeathResponsible.class)
                .forEach(resp -> resp.onDeath(gamePlayer, deathState));

        Sidebar cachedSidebar = gamePlayer.getCachedSidebar();
        if (cachedSidebar != null) {
            gameEngine.getSidebarService().setSidebar(gamePlayer.getHandle(), cachedSidebar);
        }
    }

    @Override
    public final void death() {
        death(null, new DeathState(gamePlayer.getHandle().getLocation()));
    }

    @Override
    public void leaveGame() {
        this.unload();
        msgConfig.get(Msg.game__player_leaved, gamePlayer.getName(), gamePlayer.getRole().getDisplayName())
                .broadcastSession()
                .broadcastChat();
    }

    @Override
    public void pickupGolds(int amount) {
        gameEngine.getGoldManager().giveGold(gamePlayer, amount);
        gamePlayer.data().statistic().incrementGoldsPickedUpBy(amount);
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