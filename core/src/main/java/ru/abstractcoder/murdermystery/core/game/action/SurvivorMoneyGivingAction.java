package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.setting.RewardSettings;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import java.util.concurrent.TimeUnit;

public class SurvivorMoneyGivingAction implements GameAction {

    private final GamePlayerResolver playerResolver;
    private final EconomyService economyService;
    private final GeneralConfig generalConfig;
    private final MsgConfig<Messages> msgConfig;

    public SurvivorMoneyGivingAction(GamePlayerResolver playerResolver, EconomyService economyService, GeneralConfig generalConfig, MsgConfig<Messages> msgConfig) {
        this.playerResolver = playerResolver;
        this.economyService = economyService;
        this.generalConfig = generalConfig;
        this.msgConfig = msgConfig;
    }

    @Override
    public void execute() {
        RewardSettings rewards = generalConfig.game().rewards();

        int money = rewards.getSurvivorPeriodicMoney();
        int period = rewards.getSurvivorMoneyTimePeriod();
        String formattedPeriod = SimpleTemporal.of(period, TimeUnit.SECONDS).format();
        Message message = msgConfig.get(Messages.game__survivor_get_money_periodic, money, formattedPeriod);
        playerResolver.getSurvivors().forEach(gamePlayer -> {
            economyService.incrementBalanceAsync(gamePlayer, money)
                    .thenRun(() -> message.send(gamePlayer));
        });
    }

}