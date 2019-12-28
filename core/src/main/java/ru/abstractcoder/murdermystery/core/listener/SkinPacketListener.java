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
import ru.abstractcoder.murdermystery.core.game.role.holder.RoleHolder;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainable;

import javax.inject.Inject;

//TODO delete candidate
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

                        RoleHolder roleHolder = gameEngine.getRoleHolderResolver().resolve(player.getUniqueId());

                        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());
                        System.out.println(String.format("=====Start handling player info packet with action %s =====", packet.getAction()));
                        packet.getData().forEach(data -> {
                            Multimap<String, WrappedSignedProperty> properties = data.getProfile().getProperties();
                            System.out.println("=========================================================");
                            System.out.println("data before: " + data);
                            System.out.println("properties before: " + properties);

                            gameEngine.getSkinContainableResolver().getById(data.getProfile().getUUID())
                                    .map(SkinContainable::getSkinContainer)
                                    .map(skinContainer -> skinContainer.getSkinFor(roleHolder))
                                    .ifPresent(skin -> {
                                        properties.clear();
                                        properties.put("textures", skin.getWrappedProperty());
                                    });

                            System.out.println("properties after: " + properties);
                            System.out.println("data after: " + data);
                            System.out.println("=========================================================");
                        });
                        System.out.println("====End handling player info packet=====");
                    }
                }
        };
    }

}