package ru.abstractcoder.murdermystery.core.game.role.civilian.professions;

import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.benioapi.util.cooldown.CooldownBuilder;
import ru.abstractcoder.benioapi.util.cooldown.StartRememberCooldown;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerData;
import ru.abstractcoder.murdermystery.core.game.role.civilian.CivilianLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.PlayerClickResponsible;
import ru.abstractcoder.murdermystery.core.game.role.profession.AbstractProfession;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;

import java.util.concurrent.TimeUnit;

public class ProstituteProfession extends AbstractProfession {

    public ProstituteProfession(ProfessionTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public Type getType() {
        return Type.PROSTITUTE;
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends CivilianLogic implements PlayerClickResponsible {

        private final Lazy<StartRememberCooldown> sexCooldown = Lazy.create(() -> CooldownBuilder.create()
                .setDuration(SimpleTemporal.of(1, TimeUnit.SECONDS))
                .buildStartRemember());

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onPlayerClick(GamePlayer gamePlayer, GamePlayer target) {
            if (sexCooldown.isInitialized() && sexCooldown.get().isValid()) {
                msgConfig.get(Messages.game__prostitute_sex_cooldown, sexCooldown.get().getRemainingTime().format())
                        .send(gamePlayer);
                return;
            }

            Message message = msgConfig.get(Messages.game__prostitute_role_info, target.getRole().getDisplayName());
            message.send(gamePlayer);
            gamePlayer.getHandle().sendTitle("", message.asSingleLine(), 10, 70, 20);

            if (!sexCooldown.initialize()) {
                sexCooldown.get().redefine();
            }
        }

    }

}
