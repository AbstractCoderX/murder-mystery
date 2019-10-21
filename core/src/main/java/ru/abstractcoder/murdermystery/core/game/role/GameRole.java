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
        MURDER('m'),
        DETECTIVE('d'),
        CIVILIAN('c');

        private final char guiChar;

        Type(char guiChar) {
            this.guiChar = guiChar;
        }

        public char getGuiChar() {
            return guiChar;
        }
    }

}