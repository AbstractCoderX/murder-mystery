package ru.abstractcoder.murdermystery.core.game.skin;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Random;

public class SkinData {

    private static final Random random = new Random();

    private final List<String> proofs;
    private final String name;

    @JsonCreator
    public SkinData(List<String> proofs, String name) {
        this.proofs = proofs;
        this.name = name;
    }

    public String getRandomProof() {
        return proofs.get(random.nextInt(proofs.size()));
    }

    public String getName() {
        return name;
    }

}
