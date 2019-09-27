package ru.abstractcoder.murdermystery.core.game.role.murder.classes;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.util.EventHelper;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.InteractResponsible;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;

public class JackRoleClass extends MurderRoleClass {

    public JackRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private class Logic extends MurderLogic implements InteractResponsible {

        private boolean swordThrown;

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onInteract(int slot, ItemStack item, Action action, PlayerInteractEvent event) {
            if (!EventHelper.isRightClick(action) || slot != SharedConstants.WEAPON_SLOT) {
                return;
            }

            if (swordThrown) {
                return;
            }

            gamePlayer.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, null);
            swordThrown = true;
            gameEngine.benio().getTickingService().register(new ThrownSword(gamePlayer));
        }

        private class ThrownSword implements Ticking {

            private static final double INTERACT_MIN_DISTANCE_SQUARED = 0.5 * 0.5;
            private static final double ROTATION_RADIANS = (36 * Math.PI) / 180;

            private final GamePlayer murder;

            private ArmorStand stand;
            private Location currentLoc;
            private boolean returning = false;

            private ThrownSword(GamePlayer murder) {
                this.murder = murder;

                Location location = murder.getHandle().getLocation().add(0, 1, 0);
                currentLoc = location;

                stand = location.getWorld().spawn(location, ArmorStand.class);
                stand.setCanMove(true);
                stand.setCanTick(true);
                stand.setSmall(true);
                stand.setVisible(false);
                stand.setSmall(true);
                stand.setInvulnerable(true);
                stand.setBasePlate(false);
                stand.setArms(false);
                stand.setHelmet(getWeaponItem());
                stand.setHeadPose(stand.getHeadPose().setX(Math.toRadians(location.getPitch())));
                stand.setVelocity(location.getDirection().normalize().multiply(2.5));
            }

            @Override
            public boolean doTick() {
                if (returning) {
                    Vector vector = murder.getHandle().getLocation().subtract(currentLoc).toVector();
                    if (vector.lengthSquared() <= INTERACT_MIN_DISTANCE_SQUARED) {
                        murder.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, getWeaponItem());
                        stand.remove();
                        swordThrown = false;
                        return true;
                    }

                    stand.teleport(currentLoc
                            .setDirection(vector)
                            .add(vector.normalize().multiply(0.2))
                    );

                    stand.setHeadPose(stand.getHeadPose()
                            .setX(currentLoc.getYaw())
                            .setY(currentLoc.getPitch())
                    );
                } else {
                    currentLoc = stand.getLocation();
                    gameEngine.getPlayerResolver().getSurvivors().stream()
                            .filter(gamePlayer -> gamePlayer.getHandle().getLocation()
                                    .distanceSquared(currentLoc) <= INTERACT_MIN_DISTANCE_SQUARED)
                            .findFirst()
                            .ifPresentOrElse(victim -> {
                                murder.getRoleLogic().
                                        kill(victim);
                                returning = true;
                            }, () -> {
                                EulerAngle headPose = stand.getHeadPose();
                                stand.setHeadPose(headPose.setX(headPose.getX() + ROTATION_RADIANS));
                            });
                }
                return false;
            }

            @Override
            public int getPeriod() {
                return 1;
            }

        }

    }

}