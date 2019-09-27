package ru.abstractcoder.murdermystery.core.listener;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.skin.SkinContainable;

//TODO register this
public class SkinListener {

    private final GameEngine gameEngine;

    public SkinListener(ProtocolManager protocolManager, Plugin plugin, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());
                packet.getData().forEach(data -> {
                    Multimap<String, WrappedSignedProperty> properties = data.getProfile().getProperties();

                    gameEngine.getSkinContainableRepository().findById(data.getProfile().getUUID())
                            .map(SkinContainable::getSkinContainer)
                            .map(skinContainer -> skinContainer.resolveSkinFor(player.getUniqueId()))
                            .ifPresent(skin -> {
                                properties.clear();
                                properties.put("textures", skin.getWrappedProperty());
                            });
                });
            }
        });
    }

}