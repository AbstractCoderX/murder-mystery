package ru.abstractcoder.murdermystery.economy;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dagger.Reusable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.util.PlayerUtils;
import ru.abstractcoder.benioapi.util.listener.QuickListener;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class CachingEconomyService implements EconomyService {

    private final EconomyRepository economyRepository;
    private final Cache<Player, Integer> balanceCache;

    @Inject
    public CachingEconomyService(EconomyRepository economyRepository, Plugin plugin) {
        this.economyRepository = economyRepository;
        this.balanceCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .build();

        QuickListener.create()
                .easyEvent(PlayerJoinEvent.class, event -> {
                    Player player = event.getPlayer();
                    economyRepository.loadBalance(player.getName())
                            .thenAccept(bal -> balanceCache.put(player, bal != null ? bal : 0));
                })
                .easyEvent(PlayerQuitEvent.class, event -> {
                    Player player = event.getPlayer();
                    if (!balanceCache.asMap().containsKey(player)) {
                        return;
                    }
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> balanceCache.invalidate(player), 4);
                })
                .register(plugin);

//        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", (channel, p, bytes) -> {
//            if (!channel.equals("BungeeCord")) {
//                return;
//            }
//            final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
//            final String subchannel = in.readUTF();
//            if (!subchannel.equals("EconomyUpdateBalance")) {
//                return;
//            }
//
//            final byte[] msgBytes = new byte[in.readShort()];
//            in.readFully(msgBytes);
//
//            final ByteArrayDataInput msgIn = ByteStreams.newDataInput(msgBytes);
//            final String playerName = msgIn.readUTF();
//            final int newBalance = msgIn.readInt();
//
//            PlayerUtils.parseOnlinePlayer(playerName).ifPresent(player -> balanceCache.put(player, newBalance));
//        });
    }

    @Override
    public int getCachedBalance(Player player) {
        Preconditions.checkArgument(player.isOnline(), "Player " + player.getName() + " not online!");
        return BeniOptional.ofNullable(balanceCache.getIfPresent(player))
                .orThrow(() -> new IllegalStateException("Balance not cached for online player: " + player.getName()));
    }

    @Override
    public CompletableFuture<Integer> getBalanceAsync(String playerName) {
        return economyRepository.loadBalance(playerName);
    }

    @Override
    public CompletableFuture<Void> setOfflineBalanceAsync(String playerName, int balance) {
        return economyRepository.saveBalance(playerName, balance);

//        final Iterator<? extends Player> iterator = plugin.getServer().getOnlinePlayers().iterator();
//        if (iterator.hasNext()) {
//            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
//            out.writeUTF("Forward");
//            out.writeUTF("ALL");
//            out.writeUTF("EconomyUpdateBalance");
//
//            final ByteArrayDataOutput outMsg = ByteStreams.newDataOutput();
//            outMsg.writeUTF(playerName);
//            outMsg.writeInt(balance);
//
//            final byte[] msgBytes = outMsg.toByteArray();
//            out.writeShort(msgBytes.length);
//            out.write(msgBytes);
//            iterator.next().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
//        }
    }

    @Override
    public CompletableFuture<Void> setBalanceAsync(String playerName, int balance) {
        return PlayerUtils.parseOnlinePlayer(playerName)
                .map(player -> setBalanceAsync(player, balance))
                .orGet(() -> setOfflineBalanceAsync(playerName, balance));
    }

    @Override
    public CompletableFuture<Void> setBalanceAsync(Player player, int balance) {
        Preconditions.checkArgument(player.isOnline(), "Player " + player.getName() + " not online!");
        balanceCache.put(player, balance);
        return economyRepository.saveBalance(player.getName(), balance);
    }

}