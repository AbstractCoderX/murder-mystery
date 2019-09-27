package ru.abstractcoder.murdermystery.core.util;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

public final class SkinUtils {

    public static void setSkin(NPC npc, Skin skin) {
        npc.data().set(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, skin.getTexture());
        npc.data().set(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, skin.getSignature());
    }

    public static void notifySkinChange(NPC npc) {
        ((SkinnableEntity) npc.getEntity()).getSkinTracker().notifySkinChange(true);
    }

    public static void setSkinAndNotify(NPC npc, Skin skin) {
        setSkin(npc, skin);
        notifySkinChange(npc);
    }

    // Suppress default constructor to ensure non-instantiability.
    private SkinUtils() {
        throw new AssertionError("You should not be attempting to instantiate this class.");
    }

}