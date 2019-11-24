package ru.abstractcoder.murdermystery.core.listener;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.plugin.Plugin;

public abstract class AbstractPacketListener implements MurderListener {

    private PacketListener[] listeners;

    protected abstract PacketListener[] createListeners(Plugin plugin);

    @Override
    public void register(Plugin plugin) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        for (PacketListener listener : (listeners = createListeners(plugin))) {
            protocolManager.addPacketListener(listener);
        }
    }

    @Override
    public void unregister() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        for (PacketListener listener : listeners) {
            protocolManager.removePacketListener(listener);
        }
    }

}
