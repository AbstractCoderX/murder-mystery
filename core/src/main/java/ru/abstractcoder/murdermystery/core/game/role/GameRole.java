package ru.abstractcoder.murdermystery.core.game.role;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.equipper.PlayerEquipper;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;

public interface GameRole {

    Type getType();

    RoleTemplate template();

    default String getDisplayName() {
        return template().getName();
    }

    void initLogic(GamePlayer gamePlayer);

    RoleLogic getLogic(GamePlayer gamePlayer);

    void removeLogic(GamePlayer gamePlayer);

    PlayerEquipper getEquipper();

    enum Type {
        MURDER('m', true),
        DETECTIVE('d', true),
        CIVILIAN('c', false);

        private final char guiChar;
        private final boolean classed;

        public static final Type[] VALUES = values();

        Type(char guiChar, boolean classed) {
            this.guiChar = guiChar;
            this.classed = classed;
        }

        public char getGuiChar() {
            return guiChar;
        }

        public boolean isClassed() {
            return classed;
        }

    }

}