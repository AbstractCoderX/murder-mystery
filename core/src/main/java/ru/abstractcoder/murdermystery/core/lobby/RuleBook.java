package ru.abstractcoder.murdermystery.core.lobby;

import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.gui.template.issuer.GuiAndCommandUserMixin;

public interface RuleBook {

    void openFor(Player player);

    default void openFor(GuiAndCommandUserMixin player) {
        openFor(player.getHandle());
    }

}