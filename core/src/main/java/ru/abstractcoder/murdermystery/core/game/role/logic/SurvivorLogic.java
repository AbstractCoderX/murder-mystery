package ru.abstractcoder.murdermystery.core.game.role.logic;

import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.side.GameSide;

public abstract class SurvivorLogic extends AbstractRoleLogic {

    protected SurvivorLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(gamePlayer, gameEngine, msgConfig);
    }

    @Override
    public void kill(GamePlayer victim, DeathState deathState) {
        super.kill(victim, deathState);

        GameRole.Type roleType = victim.getRole().getType();
        if (roleType != GameRole.Type.MURDER) {
            msgConfig.get(Msg.game__wrong_killer_die).sendTitle(gamePlayer);
            gamePlayer.getRoleLogic().death();
        }
    }

    @Override
    public void death(@Nullable GamePlayer killer, DeathState deathState) {
        super.death(killer, deathState);
        checkDecrementedSurvivorCount();
    }

    @Override
    public void leaveGame() {
        super.leaveGame();
        checkDecrementedSurvivorCount();
    }

    @Override
    public final GameSide getGameSide() {
        return GameSide.SURVIVORS;
    }

    private void checkDecrementedSurvivorCount() {
        if (gameEngine.getPlayerResolver().getSurvivors().size() == 0) {
            gameEngine.endGame(GameSide.MURDER, gameEngine.getPlayerResolver().getMurder());
        }
    }

}