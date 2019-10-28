package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.abstractcoder.murdermystery.core.game.skin.data.SkinData;

import java.util.Collection;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "simple", value = SimpleSkinResolver.class),
        @JsonSubTypes.Type(name = "pooled", value = PooledSkinResolver.class)
})
public interface SkinResolver {

    boolean isPurchasable();

    SkinPool getSkinPool();

    SkinData getDefaultSkin();

    List<SkinData> getPremiumSkins();

    Collection<SkinData> getAllSkins();

}
