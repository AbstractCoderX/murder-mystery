package ru.abstractcoder.murdermystery.core.game.role.detective;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.logic.SurvivorLogic;

public class DetectiveLogic extends SurvivorLogic {

    protected DetectiveLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(gamePlayer, gameEngine, msgConfig);
    }

    @Override
    public void load() {
        gameEngine.getPlayerResolver().loadDetective(gamePlayer);
    }

    @Override
    public void unload() {
        gameEngine.getPlayerResolver().setDetective(null);
    }

    @Override
    public void death(@Nullable GamePlayer killer, DeathState deathState) {
        if (killer == null || killer.getRole().getType() != GameRole.Type.MURDER) {
            dropBowRandomly();
        }

        super.death(killer, deathState);
    }

    @Override
    public void leaveGame() {
        super.leaveGame();
        dropBowRandomly();
    }

    private void dropBowRandomly() {
        Location location = gameEngine.getArena().getRandomSpawnPoint();
        gameEngine.getBowDropProcessor().dropBow(location);
    }

}