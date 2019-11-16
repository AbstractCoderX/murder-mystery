package ru.abstractcoder.murdermystery.core.game.action;

import dagger.Reusable;
import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.setting.RewardSettings;
import ru.abstractcoder.murdermystery.economy.EconomyService;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Reusable
public class SurvivorMoneyGivingAction implements GameAction {

    private final GamePlayerResolver playerResolver;
    private final EconomyService economyService;
    private final GeneralConfig generalConfig;
    private final MsgConfig<Msg> msgConfig;

    @Inject
    public SurvivorMoneyGivingAction(GamePlayerResolver playerResolver, EconomyService economyService,
            GeneralConfig generalConfig, MsgConfig<Msg> msgConfig) {
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
        Message message = msgConfig.get(Msg.game__survivor_get_money_periodic, money, formattedPeriod);
        playerResolver.getSurvivors().forEach(gamePlayer -> {
            economyService.incrementBalanceAsync(gamePlayer, money)
                    .thenRun(() -> message.send(gamePlayer));

            gamePlayer.getRating().incrementBy(1);
        });
    }

}