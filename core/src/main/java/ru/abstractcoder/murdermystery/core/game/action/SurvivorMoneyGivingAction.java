package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import java.util.concurrent.TimeUnit;

public class SurvivorMoneyGivingAction implements GameAction {

    private final GameEngine gameEngine;
    private final MsgConfig<Messages> msgConfig;

    public SurvivorMoneyGivingAction(GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.gameEngine = gameEngine;
        this.msgConfig = msgConfig;
    }

    @Override
    public void execute() {
        EconomyService economyService = gameEngine.getEconomyService();

        int money = gameEngine.settings().rewards().getSurvivorPeriodicMoney();
        int period = gameEngine.settings().rewards().getSurvivorMoneyTimePeriod();
        String formattedPeriod = SimpleTemporal.of(period, TimeUnit.SECONDS).format();
        Message message = msgConfig.get(Messages.game__survivor_get_money_periodic, money, formattedPeriod);
        gameEngine.getPlayerResolver().getSurvivors().forEach(gamePlayer -> {
            economyService.incrementBalanceAsync(gamePlayer, money);
            message.send(gamePlayer);
        });
    }

}