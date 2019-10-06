package ru.abstractcoder.murdermystery.core.game.action;

import dagger.Reusable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import javax.inject.Inject;

@Reusable
public class GameActionService {

    private final Int2ObjectMap<GameAction> gameActionMap = new Int2ObjectOpenHashMap<>();
    private GameAction startingAction;

    @Inject
    public GameActionService() {
    }

    public void addStartingAction(GameAction action) {
        if (startingAction == null) {
            startingAction = action;
        } else {
            startingAction = startingAction.andThen(action);
        }
    }

    public void addTimedAction(int time, GameAction gameAction) {
        GameAction containedAction = gameActionMap.get(time);
        if (containedAction != null) {
            gameActionMap.put(time, containedAction.andThen(gameAction));
        } else {
            gameActionMap.put(time, gameAction);
        }
    }

    public void handleStarting() {
        if (startingAction != null) {
            startingAction.execute();
        }
    }

    public void handleTimeTicked(int newTime) {
        GameAction gameAction = gameActionMap.get(newTime);
        if (gameAction != null) {
            gameAction.execute();
        }
    }

}