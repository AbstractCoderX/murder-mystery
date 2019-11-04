package ru.abstractcoder.murdermystery.core.game.skin.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.List;

public class PremiumSkinData extends SkinData {

    public static final String AVAILABILITY_PERMISSION_PARENT_NODE = "MurderMystery.skin";

    private final String id;
    private final String permission;

    @JsonCreator
    public PremiumSkinData(String id, String name, List<String> proofs, Skin skin) {
        super(name, proofs, skin);
        this.id = id;

        permission = AVAILABILITY_PERMISSION_PARENT_NODE + "." + id;
    }

    @Override
    public boolean isAvailableFor(Player player) {
        return player.hasPermission(permission);
    }

    public String getId() {
        return id;
    }

    @Override
    public String asStringKey() {
        return getId();
    }

}
