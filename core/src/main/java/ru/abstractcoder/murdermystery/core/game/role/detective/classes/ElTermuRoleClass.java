package ru.abstractcoder.murdermystery.core.game.role.detective.classes;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.benioapi.util.EventHelper;
import ru.abstractcoder.benioapi.util.Materials;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.npc.Npc;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.BlockPlaceResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.InteractResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.NpcDamageResponsible;

import java.util.ArrayList;
import java.util.List;

public class ElTermuRoleClass extends DetectiveRoleClass {

    public ElTermuRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends DetectiveLogic
            implements NpcDamageResponsible, BlockPlaceResponsible, InteractResponsible {

        private static final int CAMERA_SLOT = 5;
        private static final ItemStack TABLET = ItemUtils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNjNjk0YmQxNGQ1NDVjMzFjYzJiOGU0NGQ2OWExZjdjYmQwNTA0MzFiYThmMGViN2ZmZDQ4NGM1YmZlNCJ9fX0=");

        private List<ArmorStand> cameraStands = new ArrayList<>();
        private boolean tabletGived = false;
        private boolean spectating = false;
        private Npc npc;

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onBlockPlace(Block block, ItemStack itemInHand, Cancellable event) {
            if (Materials.isSkullOrHead(block.getType())) {
                throw new IllegalStateException(String.format("ElTermu %s placed non-skull block: %s", gamePlayer.getName(), block.getType()));
            }

            Rotatable rotatable = (Rotatable) block.getBlockData();
            BlockFace rotation = rotatable.getRotation();
            if (rotation == BlockFace.UP) {
                event.setCancelled(true);
                msgConfig.get(Messages.game__el_termu_camera_can_placed_only_on_wall).send(gamePlayer);
                return;
            }
            tabletGived = true;

            Vector vector = new Vector(rotation.getModX(), rotation.getModY(), rotation.getModZ());
            Location loc = block.getLocation()
                    .add(0.5, -0.5, 0.5)
                    .setDirection(vector);

            ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setSmall(true);

            cameraStands.add(armorStand);

            if (itemInHand.getAmount() == 1) {
                gamePlayer.getHandle().getInventory().setItem(CAMERA_SLOT, TABLET);
                tabletGived = true;
            }
        }

        @Override
        public void onInteract(int slot, ItemStack item, Action action, PlayerInteractEvent event) {
            if (!EventHelper.isRightClick(action) || slot != CAMERA_SLOT || !tabletGived || spectating) {
                return;
            }

            ArmorStand armorStand = cameraStands.get(0);
            Player player = gamePlayer.getHandle();

            npc = gameEngine.getNpcService().spawnNpc(player.getLocation(), gamePlayer.getSkinContainer()); //TODO think about skin
            npc.setItemInHand(TABLET);

            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(armorStand);
            player.setSpectatorTarget(armorStand);
            spectating = true;
        }

        @Override
        public void onNpcDamage(GamePlayer damager, Npc npc, Cancellable event) {
            if (!spectating || this.npc != npc) {
                return;
            }

            gameEngine.getNpcService().removeNpc(npc);
            cameraStands.forEach(Entity::remove);
            damager.getRoleLogic().kill(murder);
        }

    }

}