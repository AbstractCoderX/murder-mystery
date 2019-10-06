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

import javax.inject.Inject;

public class PacketListener implements RegistrableListener {

    private final Plugin plugin;
    private final ProtocolManager protocolManager;
    private final GeneralConfig generalConfig;
    private final NPCRegistry npcRegistry;
    private final CorpseService corpseService;

    @Inject
    public PacketListener(Plugin plugin, ProtocolManager protocolManager, GeneralConfig generalConfig,
            NPCRegistry npcRegistry, CorpseService corpseService) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
        this.generalConfig = generalConfig;
        this.npcRegistry = npcRegistry;
        this.corpseService = corpseService;
    }

    @Override
    public void register() {
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