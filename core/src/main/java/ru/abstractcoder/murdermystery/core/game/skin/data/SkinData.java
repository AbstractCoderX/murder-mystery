package ru.abstractcoder.murdermystery.core.game.skin.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.gui.template.issuer.GuiAndCommandUserMixin;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.benioapi.item.ItemDataImposible;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.benioapi.util.ColorUtils;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.List;
import java.util.Random;

public class SkinData implements ItemDataImposible {

    private static final Random random = new Random();

    private final String name;
    private final List<String> proofs;
    private final Skin skin;

    @JsonIgnore
    private final Lazy<ItemStack> skullItem;

    @JsonCreator
    public SkinData(String name, List<String> proofs, Skin skin) {
        this.proofs = proofs;
        this.name = ColorUtils.color(name);
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

    public boolean isAvailableFor(Player player) {
        return true;
    }

    public boolean isAvailableFor(GuiAndCommandUserMixin player) {
        return isAvailableFor(player.getHandle());
    }

    public String asStringKey() {
        return "default";
    }

    @Override
    public void imposeTo(ItemData itemData) {
        itemData
                .nameReplacements(s -> s.replace("{name}", name))
                .skullOwnerReplacements(s -> s.replace("{skull}", skin.getTexture()));
    }

}
