package ru.abstractcoder.murdermystery.core.game.skin.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;

import java.util.List;

public class PurchasableSkinData extends SkinData {

    private final int price;

    @JsonCreator
    public PurchasableSkinData(List<String> proofs, String name, Skin skin, int price) {
        super(proofs, name, skin);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

}
