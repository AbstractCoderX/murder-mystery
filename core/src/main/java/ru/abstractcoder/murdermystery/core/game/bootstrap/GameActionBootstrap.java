package ru.abstractcoder.murdermystery.core.game.bootstrap;

import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.GameSidebarManager;
import ru.abstractcoder.murdermystery.core.game.action.*;

import javax.inject.Inject;

public class GameActionBootstrap implements GameBootstrap {

    private final GameSidebarManager sidebarManager;
    private final GameActionService gameActionService;
    private final GeneralConfig generalConfig;
    private final SurvivorMoneyGivingAction survivorMoneyGivingAction;
    private final SurvivorDetectorAction survivorDetectorAction;
    private final GoldDroppingAction goldDroppingAction;
    private final RoleSelectingAnimationAction roleSelectingAnimationAction;
    private final InventoryPopulatingAction inventoryPopulatingAction;

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
        this.sidebarManager = sidebarManager;
        this.gameActionService = gameActionService;
        this.generalConfig = generalConfig;
        this.survivorMoneyGivingAction = survivorMoneyGivingAction;
        this.survivorDetectorAction = survivorDetectorAction;
        this.goldDroppingAction = goldDroppingAction;
        this.roleSelectingAnimationAction = roleSelectingAnimationAction;
        this.inventoryPopulatingAction = inventoryPopulatingAction;
    }

    @Override
    public void boot() {
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