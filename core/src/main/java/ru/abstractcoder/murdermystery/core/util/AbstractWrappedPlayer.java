package ru.abstractcoder.murdermystery.core.util;

import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.gui.template.issuer.GuiAndCommandUserMixin;

public abstract class AbstractWrappedPlayer implements GuiAndCommandUserMixin {

    protected final Player handle;

    protected AbstractWrappedPlayer(Player handle) {
        this.handle = handle;
    }

    @Override
    public Player getPlayer() {
        return handle;
    }

}