package ru.abstractcoder.murdermystery.core.listener;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainable;

import javax.inject.Inject;

public class SkinPacketListener extends AbstractPacketListener {

    private final GameEngine gameEngine;

    @Inject
    public SkinPacketListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    protected PacketListener[] createListeners(Plugin plugin) {
        return new PacketListener[] {
                new PacketAdapter(plugin, ListenerPriority.NORMAL, Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        Player player = event.getPlayer();
                        GamePlayer gamePlayer = gameEngine.getPlayerResolver().resolve(player);

                        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());
                        packet.getData().forEach(data -> {
                            Multimap<String, WrappedSignedProperty> properties = data.getProfile().getProperties();

                            gameEngine.getSkinContainableResolver().getById(data.getProfile().getUUID())
                                    .map(SkinContainable::getSkinContainer)
                                    .map(skinContainer -> skinContainer.getSkinFor(gamePlayer))
                                    .ifPresent(skin -> {
                                        properties.clear();
                                        properties.put("textures", skin.getWrappedProperty());
                                    });
                        });
                    }
                }
        };
    }

}