package ru.abstractcoder.murdermystery.core.game.skin.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.List;
import java.util.Random;

public class SkinData {

    private static final Random random = new Random();

    private final List<String> proofs;
    private final String name;

    @JsonIgnore
    private final Skin skin;
    @JsonIgnore
    private final Lazy<ItemStack> skullItem;

    @JsonCreator
    public SkinData(List<String> proofs, String name, Skin skin) {
        this.proofs = proofs;
        this.name = name;
        this.skin = skin;
        skin.setData(this);

        skullItem = Lazy.create(() -> ItemUtils.createSkull(getSkin().getProperty()));
    }

    public String getRandomProof() {
        return proofs.get(random.nextInt(proofs.size()));
    }

    public String getName() {
        return name;
    }

    public Skin getSkin() {
        return skin;
    }

    public ItemStack getSkullItem() {
        return skullItem.get();
    }

}
