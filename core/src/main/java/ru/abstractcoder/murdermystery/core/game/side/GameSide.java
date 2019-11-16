package ru.abstractcoder.murdermystery.core.game.side;

import ru.abstractcoder.murdermystery.core.config.Msg;

public enum GameSide {

    MURDER {
        @Override
        public GameSide getOppositeSide() {
            return SURVIVORS;
        }

        @Override
        public Msg getWinMsg() {
            return Msg.game__murder_win_subtitle;
        }

        @Override
        public Msg getLoseMsg() {
            return Msg.game__murder_lose_subtitle;
        }
    },

    SURVIVORS {
        @Override
        public GameSide getOppositeSide() {
            return MURDER;
        }

        @Override
        public Msg getWinMsg() {
            return Msg.game__survivors_win_subtitle;
        }

        @Override
        public Msg getLoseMsg() {
            return Msg.game__survivors_lose_subtitle;
        }
    };

    public static final GameSide[] VALUES = values();

    public abstract GameSide getOppositeSide();

    public abstract Msg getWinMsg();

    public abstract Msg getLoseMsg();

}
