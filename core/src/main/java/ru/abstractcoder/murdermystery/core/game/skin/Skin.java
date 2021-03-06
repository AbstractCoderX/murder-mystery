package ru.abstractcoder.murdermystery.core.game.skin;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mojang.authlib.properties.Property;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

//TODO extract interface
public class Skin {

    private final String texture;
    private final String signature;

    @JsonIgnore
    private SkinData data;
    @JsonIgnore
    private final Lazy<Property> property;
    @JsonIgnore
    private final Lazy<WrappedSignedProperty> wrappedProperty;

    @JsonCreator
    public Skin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;

        property = Lazy.create(() -> new Property("textures", texture, signature));
        wrappedProperty = Lazy.create(() -> WrappedSignedProperty.fromHandle(property.get()));
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

    public void setData(SkinData data) {
        this.data = data;
    }

    public Property getProperty() {
        return property.get();
    }

    public WrappedSignedProperty getWrappedProperty() {
        return wrappedProperty.get();
    }

}