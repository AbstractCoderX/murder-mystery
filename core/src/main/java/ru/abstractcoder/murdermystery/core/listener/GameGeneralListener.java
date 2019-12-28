package ru.abstractcoder.murdermystery.core.listener;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.server.*;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.benioapi.config.msg.Message;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.event.SkinViewUpdatedEvent;
import ru.abstractcoder.murdermystery.core.game.misc.VendingMachine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolderResolver;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainable;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;
import ru.abstractcoder.murdermystery.core.rating.rank.Rank;
import ru.abstractcoder.murdermystery.core.scheduler.Scheduler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GameGeneralListener implements BukkitListener {

    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;
    private final VendingMachine vendingMachine;
    private final RoleHolderResolver roleHolderResolver;
    private final Scheduler scheduler;
    private final MsgConfig<Msg> msgConfig;

    @Inject
    public GameGeneralListener(GamePlayerResolver playerResolver,
            PlayerController playerController, VendingMachine vendingMachine,
            RoleHolderResolver roleHolderResolver, Scheduler scheduler, MsgConfig<Msg> msgConfig) {
        this.playerResolver = playerResolver;
        this.playerController = playerController;
        this.vendingMachine = vendingMachine;
        this.roleHolderResolver = roleHolderResolver;
        this.scheduler = scheduler;
        this.msgConfig = msgConfig;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = playerResolver.resolve(player);
        if (gamePlayer != null) {
            gamePlayer.getRoleLogic().leaveGame();
            return;
        }

        SpectatingPlayer sp = playerController.removeSpectating(player);
        Preconditions.checkState(sp != null,
                "Not spectating or gaming player %s", player.getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBow(EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();
        arrow.setDamage(1000);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        Player damager = (Player) event.getDamager();
        ItemStack itemInMainHand = damager.getInventory().getItemInMainHand();
        if (!itemInMainHand.getType().name().endsWith("_SWORD")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSkinViewUpdated(SkinViewUpdatedEvent event) {
        SkinContainable skinHolder = event.getSkinHolder();
        EntityPlayer skinHolderNms = skinHolder.getNmsHandle();
        GameProfile initialProfile = skinHolderNms.getProfile();
        GameProfile profile = new GameProfile(initialProfile.getId(), initialProfile.getName());

        var infoAddPacket = new WrapperPlayServerPlayerInfo();
        infoAddPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        var infoRemovePacket = new WrapperPlayServerPlayerInfo();
        infoRemovePacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        infoRemovePacket.setData(Collections.singletonList(new PlayerInfoData(
                new WrappedGameProfile(skinHolder.getUniqueId(), null),
                0,
                NativeGameMode.ADVENTURE,
                null
        )));

        var destroyPacket = new PacketPlayOutEntityDestroy(skinHolderNms.getId());
        var spawnPacket = new PacketPlayOutNamedEntitySpawn(skinHolderNms);

        roleHolderResolver.getAll().forEach(roleHolder -> {
            Skin skin = skinHolder.getSkinContainer().getSkinFor(roleHolder);

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", skin.getProperty());
            WrappedGameProfile wrappedProfile = WrappedGameProfile.fromHandle(profile);

            List<PlayerInfoData> dataList = Collections.singletonList(new PlayerInfoData(wrappedProfile,
                    skinHolderNms.ping,
                    NativeGameMode.ADVENTURE,
                    null));
            infoAddPacket.setData(dataList);

            PlayerConnection connection = ((CraftPlayer) roleHolder.getHandle()).getHandle().playerConnection;

            connection.sendPacket(destroyPacket);
            infoRemovePacket.sendPacket(roleHolder.getHandle());

            infoAddPacket.sendPacket(roleHolder.getHandle());
            connection.sendPacket(spawnPacket);

            if (roleHolder.getHandle().getUniqueId().equals(skinHolder.getUniqueId())) {
                // connection.sendPacket(new PacketPlayOutRespawn(skinHolderNms.dimension,
                //         skinHolderNms.world.getWorldData().getType(),
                //         skinHolderNms.playerInteractManager.getGameMode()));
                connection.sendPacket(
                        newRespawn(0, (byte) 2, roleHolder.getHandle().getWorld().getWorldType().getName()));

                skinHolderNms.updateAbilities();

                connection.sendPacket(new PacketPlayOutPosition(skinHolderNms.locX, skinHolderNms.locY,
                        skinHolderNms.locZ,
                        skinHolderNms.getBukkitYaw(),
                        skinHolderNms.pitch, Collections.emptySet(), 0));

                MinecraftServer.getServer().getPlayerList().updateClient(skinHolderNms);
            }
        });
    }

    private PacketPlayOutRespawn newRespawn(int id, byte gamemode, String worldType) {
        var packet = new PacketPlayOutRespawn();
        var buf = new PacketDataSerializer(Unpooled.buffer());
        try {
            buf.writeInt(id).writeByte(gamemode);
            buf.a(worldType);
            try {
                packet.a(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            buf.release();
        }
        return packet;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Msg msgKey;
        Rank rank;
        SpectatingPlayer spectatingPlayer = playerController.getSpectating(player);
        if (spectatingPlayer != null) {
            msgKey = Msg.chat__game_spectator_format;
            rank = spectatingPlayer.getCachedData().rating().getRank();

            Set<Player> recipients = event.getRecipients();
            recipients.clear();
            playerController.getAllSpectators().forEach(sp -> recipients.add(sp.getHandle()));
        } else {
            msgKey = Msg.chat__game_player_format;
            rank = playerResolver.resolvePresent(player).getRating().getRank();
        }

        Message message = msgConfig.get(msgKey, rank.getDisplayName(), player.getDisplayName(), event.getMessage());
        event.setFormat(message.getJoinedLines());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        GamePlayer gamePlayer = playerResolver.resolve(event.getPlayer());
        if (gamePlayer == null) {
            return;
        }

        @NotNull
        Block block = Objects.requireNonNull(event.getClickedBlock());
        if (!Tag.BUTTONS.isTagged(block.getType())) {
            return;
        }

        Switch button = (Switch) block.getBlockData();
        BlockFace facing = button.getFacing();
        if (block.getRelative(facing.getOppositeFace()).getType() == Material.GOLD_BLOCK) { //TODO change material
            vendingMachine.applyRandomAction(gamePlayer);
        }
    }

}