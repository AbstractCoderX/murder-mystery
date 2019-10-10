package ru.abstractcoder.murdermystery.core.game.skin;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.benioapi.util.Lazy;

public class Skin {

    private final String texture;
    private final String signature;
    private final SkinData data;

    private final Lazy<Property> property;
    private final Lazy<WrappedSignedProperty> wrappedProperty;
    private final Lazy<ItemStack> skull;

    @JsonCreator
    public Skin(String texture, String signature, SkinData data) {
        this.texture = texture;
        this.signature = signature;
        this.data = data;

        property = Lazy.create(() -> new Property("textures", texture, signature));
        wrappedProperty = Lazy.create(() -> WrappedSignedProperty.fromHandle(property.get()));
        skull = Lazy.create(() -> ItemUtils.createSkull(property.get()));
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    public SkinData data() {
        return data;
    }

    public Property getProperty() {
        return property.get();
    }

    public WrappedSignedProperty getWrappedProperty() {
        return wrappedProperty.get();
    }

    public ItemStack getSkull() {
        return skull.get();
    }

}