package ru.abstractcoder.murdermystery.core.listener;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.plugin.Plugin;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.game.corpse.CorpseService;

public class PacketListener {

    public PacketListener(Plugin plugin, ProtocolManager protocolManager, GeneralConfig generalConfig,
                          NPCRegistry npcRegistry, CorpseService corpseService) {
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerNamedEntitySpawn packet = new WrapperPlayServerNamedEntitySpawn(event.getPacket());
                BeniOptional
                        .ofNullable(packet.getEntity(generalConfig.game().getWorld()))
                        .map(npcRegistry::getNPC)
                        .map(NPC::getUniqueId)
                        .map(corpseService::getByCorpseId)
                        .ifPresent(corpse -> corpse.sendTo(event.getPlayer()));
            }
        });
    }

}