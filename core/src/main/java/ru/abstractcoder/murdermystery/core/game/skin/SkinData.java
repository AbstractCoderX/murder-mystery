package ru.abstractcoder.murdermystery.core.game.skin;

import java.util.List;
import java.util.Random;

public class SkinData {

    private static final Random random = new Random();

    private final List<String> proofs;

    public SkinData(List<String> proofs) {
        this.proofs = proofs;
    }

    public String getRandomProof() {
        return proofs.get(random.nextInt(proofs.size()));
    }


}
