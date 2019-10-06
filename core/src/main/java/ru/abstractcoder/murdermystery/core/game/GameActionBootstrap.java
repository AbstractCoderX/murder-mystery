package ru.abstractcoder.murdermystery.core.game;

import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.action.*;

import javax.inject.Inject;

//TODO run this
public class GameActionBootstrap {

    @Inject
    public GameActionBootstrap(
            GameSidebarManager sidebarManager,
            GameActionService gameActionService,
            GeneralConfig generalConfig,
            SurvivorMoneyGivingAction survivorMoneyGivingAction,
            SurvivorDetectorAction survivorDetectorAction,
            GoldDroppingAction goldDroppingAction,
            RoleSelectingAnimationAction roleSelectingAnimationAction,
            InventoryPopulatingAction inventoryPopulatingAction) {
        gameActionService.addStartingAction(sidebarManager::showForAll);

        GeneralConfig.Game settings = generalConfig.game();
        int gameDuration = settings.general().getGameDuration();

        {
            int period = settings.rewards().getSurvivorMoneyTimePeriod();
            for (int time = settings.rewards().getSurvivorMoneyStartTime(); time > 0; time -= period) {
                gameActionService.addTimedAction(time, survivorMoneyGivingAction);
            }
        }

        {
            int period = settings.general().getGoldDroppingPeriod();
            for (int time = gameDuration - period; time > 0; time -= period) {
                gameActionService.addTimedAction(time, goldDroppingAction);
            }
        }

        gameActionService.addTimedAction(45, survivorDetectorAction);

        gameActionService.addTimedAction(gameDuration - 2, roleSelectingAnimationAction);

        gameActionService.addTimedAction(gameDuration - 15, inventoryPopulatingAction);
    }

}