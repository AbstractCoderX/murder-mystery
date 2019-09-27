package ru.abstractcoder.murdermystery.core.game.role.detective.classes;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.benioapi.util.cooldown.CooldownBuilder;
import ru.abstractcoder.benioapi.util.cooldown.StartRememberCooldown;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOtherMoveResponsible;

import java.util.concurrent.TimeUnit;

public class MissPurpleRoleClass extends DetectiveRoleClass {

    public MissPurpleRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends DetectiveLogic implements AnyOtherMoveResponsible {

        private static final double DETECTIVE_MAX_DISTANCE_SQUARE = 30 * 30;

        private Lazy<StartRememberCooldown> nearMurderMsgCooldown = Lazy.create(() -> CooldownBuilder.create()
                .setDuration(SimpleTemporal.of(30, TimeUnit.SECONDS))
                .buildStartRemember());

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onOtherMove(GamePlayer other, Location from, Location to, Cancellable event) {
            if (other.getRole().getType() != GameRole.Type.DETECTIVE) {
                return;
            }
            if (!(to.distanceSquared(gamePlayer.getHandle().getLocation()) <= DETECTIVE_MAX_DISTANCE_SQUARE)) {
                return;
            }

            if (canSendNearMsg()) {
                Message message = msgConfig.get(Messages.game__miss_purple_murder_near);
                message.send(gamePlayer);
                message.sendActionBar(gamePlayer);
            }
        }

        private boolean canSendNearMsg() {
            if (nearMurderMsgCooldown.initialize()) {
                return true;
            }

            if (nearMurderMsgCooldown.get().isExpire()) {
                nearMurderMsgCooldown.get().redefine();
                return true;
            }
            return false;
        }

    }

}