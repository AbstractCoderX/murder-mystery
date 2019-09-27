package ru.abstractcoder.murdermystery.core.game;

import org.bukkit.Bukkit;
import ru.abstractcoder.benioapi.board.SidebarService;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.action.*;

//TODO run this
public class GameBootstrap {

    public GameBootstrap(GameEngine gameEngine, MsgConfig<Messages> msgConfig,
            SidebarService sidebarService) {
        GameActionService gameActionService = gameEngine.getActionService();

        GameSidebarManager sidebarManager = new GameSidebarManager(sidebarService, gameEngine, msgConfig);
        gameActionService.addStartingAction(() -> Bukkit.getOnlinePlayers().forEach(sidebarManager::showFor));

        GeneralConfig.Game settings = gameEngine.settings();
        int gameDuration = settings.general().getGameDuration();

        {
            GameAction action = new SurvivorMoneyGivingAction(gameEngine, msgConfig);
            int period = settings.rewards().getSurvivorMoneyTimePeriod();
            for (int time = settings.rewards().getSurvivorMoneyStartTime(); time > 0; time -= period) {
                gameActionService.addTimedAction(time, action);
            }
        }

        {
            GameAction action = new GoldDroppingAction(gameEngine);
            int period = settings.general().getGoldDroppingPeriod();
            for (int time = gameDuration - period; time > 0; time -= period) {
                gameActionService.addTimedAction(time, action);
            }
        }

        {
            GameAction action = new SurvivorDetectorAction(gameEngine, msgConfig);
            gameActionService.addTimedAction(45, action);
        }

        {
            GameAction action = new RoleSelectingAnimationAction(gameEngine, msgConfig);
            gameActionService.addTimedAction(gameDuration - 2, action);
        }

        {
            GameAction action = new InventoryPopulatingAction(gameEngine);
            gameActionService.addTimedAction(gameDuration - 15, action);
        }
    }

}