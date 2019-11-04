package ru.abstractcoder.murdermystery.core.game.role.logic;

import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.murdermystery.core.game.misc.DeathState;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;

public interface RoleLogic {

    void load();

    void kill(GamePlayer victim, DeathState deathState);

    void kill(GamePlayer victim);

    void death(@Nullable GamePlayer killer, DeathState deathState);

    void death();

    void onGoldPickup(int amount);

}