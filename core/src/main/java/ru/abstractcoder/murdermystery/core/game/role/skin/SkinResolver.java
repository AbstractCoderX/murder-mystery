package ru.abstractcoder.murdermystery.core.game.role.skin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.game.skin.data.PurchasableSkinData;

import java.util.Collection;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "pooled", value = PooledSkinResolver.class),
        @JsonSubTypes.Type(name = "purchasable", value = PurchasableSkinResolver.class)
})
public interface SkinResolver {

    boolean isPurchasable();

    SkinPool getSkinPool();

    Skin getDefaultSkin();

    Collection<PurchasableSkinData> getPurchasableSkins();

}
