package ru.abstractcoder.murdermystery.core.command;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.command.impl.ParentCommand;
import ru.abstractcoder.benioapi.util.reflect.TrustedLookup;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;
import ru.abstractcoder.murdermystery.core.scheduler.Scheduler;

import javax.inject.Inject;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static ru.abstractcoder.benioapi.command.availability.CommandAvailabilities.bukkitPerm;

public class MurderMysteryCommand extends ParentCommand {

    private static final MethodHandle refreshPlayerHandle = TrustedLookup.apply(lookup ->
            lookup.findVirtual(CraftPlayer.class, "refreshPlayer", MethodType.methodType(void.class)));

    @Inject
    public MurderMysteryCommand(CorpseService corpseService, Scheduler scheduler) {
        super("murdermystery");

        executingStrategy()
                .aliases("murder", "mm")
                .addAvailability(bukkitPerm("MurderMystery.admin"))
                .withSubcommand(new PlayerSubcommand("throw") {
                    @Override
                    protected void execute(Player player, String[] args, String label) {
                        Location location = player.getLocation().add(0, 1, 0);
                        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
                        armorStand.setCanMove(true);
                        armorStand.setCanTick(true);
                        armorStand.setSmall(true);
                        armorStand.setVisible(false);
                        armorStand.setSmall(true);
                        armorStand.setInvulnerable(true);
                        armorStand.setBasePlate(false);
                        armorStand.setArms(false);
                        armorStand.setHelmet(new ItemStack(Material.IRON_SWORD));
                        armorStand.setHeadPose(armorStand.getHeadPose().setX(Math.toRadians(location.getPitch())));
                        armorStand.setVelocity(location.getDirection().normalize().multiply(2.5));

                    }
                })
                .withSubcommand(new PlayerSubcommand("skin") {
                    @Override
                    protected void execute(Player player, String[] args, String label) {
                        Player target = player;
                        // Player target = Bukkit.getOnlinePlayers()
                        //         .stream()
                        //         .filter(p -> p != player)
                        //         .findAny()
                        //         .orElseThrow();

                        // Location location = player.getLocation();
                        // Vector direction = location.getDirection();
                        // location.add(direction.normalize().multiply(3));
                        // location.setDirection(direction.multiply(-1));
                        // target.teleport(location);

                        EntityPlayer targetNms = ((CraftPlayer) target).getHandle();

                        var infoRemovePacket = new WrapperPlayServerPlayerInfo();
                        infoRemovePacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                        infoRemovePacket.setData(Collections.singletonList(new PlayerInfoData(
                                new WrappedGameProfile(target.getUniqueId(), null),
                                0,
                                NativeGameMode.ADVENTURE,
                                null
                        )));

                        var infoAddPacket = new WrapperPlayServerPlayerInfo();

                        GameProfile profileCopy = new GameProfile(target.getPlayerProfile().getId(),
                                target.getPlayerProfile().getName());
                        Multimap<String, Property> properties = profileCopy.getProperties();
                        properties.put("textures", new Property("textures",
                                "eyJ0aW1lc3RhbXAiOjE1NzUxMjU4MDA2NTMsInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lOWFlMWRlNTAyNzI1YzdlZmU3NDJmODBkZTczOWQzMzhmZGY5NGUyMDAzMDMzOGZjNGY5YzFmMzRjOTk5ZGZjIiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=",
                                "OTeDHkJcbkA7AeyHk7ybw7h1apkETX2zfRINNg17+HtCbvpJQr3qmRpj6pq7IGGyWjYXTdj8XgnieSJYoCWC1Bri0lwftk2mm3oqXKejY+tYSWQrjGfJShNBW6xkjYulhYiK2lri542ZyIuIS8v5lhoFjWrZvpM7Y8qECnp0wYKpRija8CnODmep/yrOdLF5XLJG24B7Jxonhv/+/ZYv/oP74WqacwktasaY0N126+zvaMLB/IHTLo4eOStw9WP3xMtJ3LlTkhExcPcEbID5ba4rdlN0qxq8m7XC2eNLI5++Zh9hVmCFk4kjEjtD1Jzhi+tWHb5fVGGqSCnHtMn7r8evmERJrm1Bu2dBf+tCFC9zDv3j9KYHh4O2xkPJZNwxUNNsGqcBq6Xr8erZRAbovbRZwibZZNe6nTCvofd8qsrGbFongkH8zt52j8zStsNY43csGflPztt6TxjRwICY4Dae5IE47/+uk9CNi7KSMhHZmP/cfd1nh11/+TRxEVm2Dqv7dhxg8BBnv8c5Fe0EIyjR/pgVnNYstO2Pn98c5E30FRw7NIQUYtydHNLY17VpvAZBESIaZXD6lEI30gZhedHrLkeWI+t8aBb5ym70Z9LsnFZMs4VnFDzwuDEgdI8OJP2U2UKtHMZi9jrF5mny6s8fOG61HyHqRt4voMXR3RI="
                        ));
                        WrappedGameProfile wrappedProfile = WrappedGameProfile.fromHandle(profileCopy);

                        infoAddPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                        infoAddPacket.setData(List.of(new PlayerInfoData(
                                wrappedProfile,
                                targetNms.ping,
                                NativeGameMode.fromBukkit(player.getGameMode()),
                                WrappedChatComponent.fromHandle(targetNms.getDisplayName())
                        )));

                        var destroyPacket = new PacketPlayOutEntityDestroy(targetNms.getId());
                        var spawnPacket = new PacketPlayOutNamedEntitySpawn(targetNms);

                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                        connection.sendPacket(destroyPacket);
                        infoRemovePacket.sendPacket(player);

                        infoAddPacket.sendPacket(player);
                        connection.sendPacket(spawnPacket);

                        var respawnPacket = new WrapperPlayServerRespawn();
                        respawnPacket.setDimension(0);
                        respawnPacket.setGamemode(NativeGameMode.ADVENTURE);
                        respawnPacket.setLevelType(target.getWorld().getWorldType());
                        respawnPacket.sendPacket(target);

                        targetNms.updateAbilities();
                        Location loc = target.getLocation();
                        connection.sendPacket(
                                new PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
                                        loc.getPitch(), new HashSet<>(), 0));

                        MinecraftServer.getServer().getPlayerList().updateClient(targetNms);
                    }
                })
                .withSubcommand(new PlayerSubcommand("refreshme") {
                    @Override
                    protected void execute(Player player, String[] args, String label) {
                        if (args[0].equals("1")) {
                            EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                            PlayerConnection connection = nmsPlayer.playerConnection;
                            connection.sendPacket(
                                    new PacketPlayOutRespawn(nmsPlayer.dimension,
                                            nmsPlayer.world.getWorldData().getType(),
                                            nmsPlayer.playerInteractManager.getGameMode()));
                            nmsPlayer.updateAbilities();
                            Location loc = player.getLocation();
                            connection.sendPacket(
                                    new PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
                                            loc.getPitch(), new HashSet<>(), 0));

                            MinecraftServer.getServer().getPlayerList().updateClient(nmsPlayer);
                        } else if (args[0].equals("2")) {
                            try {
                                refreshPlayerHandle.invoke(player);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }

                        }
                    }
                })
                .withSubcommand(new PlayerSubcommand("spawncorpse") {
                    @Override
                    protected void execute(Player player, String[] args, String label) {
                        corpseService.spawnCorpse(
                                player,
                                new SkinData(
                                        "asd1",
                                        Collections.emptyList(),
                                        new Skin(
                                                "eyJ0aW1lc3RhbXAiOjE1NzUxMjY2MTMyNDksInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ4MTU2YjM4MWU5MzMwYmY4NzgyNmE1NDc0NTFiNzY3MjZmMmJkZGEzYjljMzFhZTEzOWEwYmQ0NDYzNjI1MGUiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==",
                                                "NyL1DBAat06ndfVLqSQ/AlysIO1QwLvluG3hjDzW6l0JwkaIdfRCjk5l3VFfCVH4N29RpXp9zPi6fEkNAXTJY/WDZKWnzf/p/qfopDxNMjytUVneUmGxLJTlbPLU/wfqHJ7hvPjhmpVA30hxwI47bJeUQyCRpO78uZx1YvbTScFGX5Ie7ZiHv4tu4tBl2JGScM0BTmWPnI/BxybTqU7vkLN/m6yEVqzY0/1PwEWMBAuTEARiLsHcZUUIQGClYX/ptl1ktGBDTC4CSPX1dHKbmEDqgS4sF9scxNmBGdi6HGkSVxpeDkaQzg9W/b6UBn0L2xhUfLVQhxSizGukIT/v4cT19sjZw+9zdMTtN++RBHp1SRQHWuPNFlWYJK+VgEJVPxq+su1xkpspkXYZIV/CEDzjL66h1y3e0pqfM4ETYg1SZC/700caSuoVX8T/t5QMVXgDJ9Ggd9IrGnFir2VnIR3I2svyDe/jVZak/BWK6HQf0Z5F24+YZuGH7asRqTNdZ34vuhIK1aEqzUP9zmCRUEIB3G5roAERxT0FQC5o4kw40puqU6AYgIwKi88OtzXomrK0WayM1N/9xOQuDX6pzAzVtZbg63h2Mxe8Txwb/0+zO4AT3235NVc/VCZL0+djnyHzkOZTnu/11AJ5HcfYHF0sa1UWhbdMO11i1mNDd3Q="
                                        )
                                ).getSkin(),
                                player.getLocation()
                        );
                    }
                });
    }

}