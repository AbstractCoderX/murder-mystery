package ru.abstractcoder.murdermystery.core.game.role.holder;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;

public interface RoleHolder {

    Player getHandle();

    GameRole getRole();

}