package ru.abstractcoder.murdermystery.core.game.skin;

import java.util.List;
import java.util.Random;

public class SkinData {

    private static final Random random = new Random();

    private final Skin skin;

    private final List<String> proofs;
    private final String name;

    public SkinData(Skin skin, List<String> proofs, String name) {
        this.skin = skin;
        this.proofs = proofs;
        this.name = name;
    }

    public String getRandomProof() {
        return proofs.get(random.nextInt(proofs.size()));
    }

    public Skin getSkin() {
        return skin;
    }

    public String getName() {
        return name;
    }

}
