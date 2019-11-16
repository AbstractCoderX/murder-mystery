package ru.abstractcoder.murdermystery.core.game.role.civilian.professions;

import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.benioapi.util.cooldown.CooldownBuilder;
import ru.abstractcoder.benioapi.util.cooldown.StartRememberCooldown;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.game.role.civilian.CivilianLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.CorpseClickResponsible;
import ru.abstractcoder.murdermystery.core.game.role.profession.AbstractProfession;
import ru.abstractcoder.murdermystery.core.game.role.profession.template.ProfessionTemplate;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

import java.util.concurrent.TimeUnit;

public class DoctorProfession extends AbstractProfession {

    public DoctorProfession(ProfessionTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public Type getType() {
        return Type.DOCTOR;
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends CivilianLogic implements CorpseClickResponsible {

        private static final int TOTEM_SLOT = 3;

        private final Lazy<StartRememberCooldown> reviveCooldown = Lazy.create(() -> CooldownBuilder.create()
                .setDuration(SimpleTemporal.of(1, TimeUnit.MINUTES))
                .buildStartRemember());

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onCorpseClick(Corpse corpse, int slot) {
            if (slot != TOTEM_SLOT) {
                return;
            }

            if (reviveCooldown.isInitialized() && reviveCooldown.get().isValid()) {
                msgConfig.get(Msg.game__doctor_revive_cooldown, reviveCooldown.get().getRemainingTime().format())
                        .send(gamePlayer);
                return;
            }

            PlayerController playerController = gameEngine.getPlayerController();
            SpectatingPlayer sp = playerController.removeSpectating(corpse.getPlayerId());
            if (sp != null) {
                corpse.remove();
                GamePlayer gp = gameEngine.getPlayerFactory().revivePlayer(sp);
                //TODO maybe send msg to gp
                if (!reviveCooldown.initialize()) {
                    reviveCooldown.get().redefine();
                }
            } else {
                msgConfig.get(Msg.game__doctor_spectator_leaved).send(gamePlayer);
            }
        }

    }
    
}
