package ru.abstractcoder.murdermystery.core.game.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;

import java.util.Comparator;
import java.util.Objects;

public class BowDropProcessor {

    private static final EulerAngle ARM_DEF_POSE = new EulerAngle(0, 0, Math.PI / 2);
    private static final ItemStack BOW_ITEM = new ItemStack(Material.BOW);
    public static final int COMPASS_SLOT = 4;

    private final GameEngine gameEngine;
    private final MsgConfig<Messages> msgConfig;
    private final ItemStack compassItem;

    public BowDropProcessor(GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        this.gameEngine = gameEngine;
        compassItem = ItemBuilder.fromMaterial(Material.COMPASS)
                .withItemMeta()
                .setName(msgConfig.get(Messages.misc__bow_detector_compass).asSingleLine())
                .and().build();
        this.msgConfig = msgConfig;
    }

    public void dropBow(Location location) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setBasePlate(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setCanTick(false);
        armorStand.setArms(true);
        armorStand.setRightArmPose(ARM_DEF_POSE);
        armorStand.getEquipment().setItemInMainHand(BOW_ITEM);
        gameEngine.benio().getTickingService().register(new DroppedBowTicking(armorStand));

        gameEngine.getPlayerResolver().getSurvivors().forEach(gp -> {
            Player player = gp.getHandle();
            player.setCompassTarget(location);
            player.getInventory().setItem(COMPASS_SLOT, compassItem);
        });

        msgConfig.get(Messages.game__detective_die_chat).broadcastSession()
                .setPlayerIssuers(gameEngine.getPlayerResolver().getAll())
                .broadcastChat();
        msgConfig.get(Messages.game__detective_die_title_survivors).broadcastSession()
                .setPlayerIssuers(gameEngine.getPlayerResolver().getSurvivors())
                .broadcastTitle();
        msgConfig.get(Messages.game__detective_die_title_murder)
                .sendTitle(gameEngine.getPlayerResolver().getMurder());
    }

    private class DroppedBowTicking implements Ticking {

        private final ArmorStand armorStand;
        private final Location standLoc;
        private final Vector vector;
        private int horizontalDegrees = 0;
        private int verticalDegrees = 0;

        private DroppedBowTicking(ArmorStand armorStand) {
            this.armorStand = armorStand;
            standLoc = armorStand.getLocation();


            vector = standLoc.getDirection().setY(0).normalize();
            vector.setX(-vector.getZ());
            vector.setZ(vector.getX());
        }

        @Override
        public boolean doTick() {
            Location bowLoc = standLoc.clone().add(vector);

            if (bowLoc.getNearbyPlayers(1).stream()
                    .map(gameEngine.getPlayerResolver()::resolve)
                    .filter(Objects::nonNull)
                    .filter(gamePlayer -> gamePlayer.getRole().getType() == GameRole.Type.CIVILIAN)
                    .min(Comparator.comparing(gp -> bowLoc.distanceSquared(gp.getHandle().getLocation())))
                    .map(gamePlayer -> {
                        gamePlayer.setRole(gameEngine.getRoleResolver().getDefaultClassedRole(GameRole.Type.DETECTIVE));
                        gamePlayer.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, BOW_ITEM);
                        armorStand.remove();
                        return true;
                    })
                    .orElse(false)) {
                return true;
            }

            verticalDegrees = (verticalDegrees + 24) % 360;
            horizontalDegrees = (horizontalDegrees + 12) % 360;

            armorStand.setRightArmPose(new EulerAngle(
                    Math.toRadians(horizontalDegrees),
                    Math.toDegrees(verticalDegrees),
                    Math.PI / 2
            ));

            vector.setX(Math.sin(-horizontalDegrees));
            vector.setZ(Math.cos(-horizontalDegrees));

            return false;
        }

        @Override
        public int getPeriod() {
            return 1;
        }

    }

}
