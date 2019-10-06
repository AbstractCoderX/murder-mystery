package ru.abstractcoder.murdermystery.core.dagger.module;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dagger.Module;
import dagger.Provides;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;

@Module
public class DependencyModule {

    @Provides
    ProtocolManager protocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }

    @Provides
    NPCRegistry npcRegistry() {
        return CitizensAPI.getNPCRegistry();
    }

}