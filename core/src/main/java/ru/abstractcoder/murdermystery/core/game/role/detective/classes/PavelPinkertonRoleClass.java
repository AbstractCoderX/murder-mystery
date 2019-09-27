package ru.abstractcoder.murdermystery.core.game.role.detective.classes;

import org.bukkit.Particle;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.math.Vector3;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyKillResponsible;

import java.util.HashSet;
import java.util.Set;

public class PavelPinkertonRoleClass extends DetectiveRoleClass {

    public PavelPinkertonRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends DetectiveLogic implements AnyKillResponsible {

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onAnyKill(GamePlayer killer, GamePlayer victim, DeathState deathState) {
            if (killer.getRole().getType() != GameRole.Type.MURDER) {
                return;
            }
            Corpse corpse = deathState.getSpectatingPlayer().getCorpse();
            if (corpse == null) {
                return;
            }

            Set<Vector3> particlePositions = new HashSet<>();
            gameEngine.benio().getTickingService().register(new Ticking() {
                int i = 0;

                @Override
                public boolean doTick() {
                    Vector3 position = Vector3.atLocation(killer.getHandle().getLocation().add(0, 1, 0));
                    particlePositions.add(position);
                    return ++i >= 30;
                }

                @Override
                public int getPeriod() {
                    return 2;
                }
            });

            gameEngine.benio().getTickingService().register(new Ticking() {
                @Override
                public boolean doTick() {
                    if (corpse.isRemoved()) {
                        return true;
                    }

                    particlePositions.forEach(pos ->
                            gameEngine.settings().getWorld().spawnParticle(Particle.REDSTONE,
                                    pos.getX(), pos.getY(), pos.getZ(),
                                    2, 0.01, 0.01, 00.1, 0.001)
                    );

                    return false;
                }

                @Override
                public int getPeriod() {
                    return 5;
                }
            });

        }

    }

}