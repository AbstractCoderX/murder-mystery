package ru.abstractcoder.murdermystery.core.cosmetic.service;

import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.resolver.CosmeticCategoryResolver;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Reusable
public class CosmeticService {

    private final CosmeticCategoryResolver categoryResolver;

    @Inject
    public CosmeticService(CosmeticCategoryResolver categoryResolver) {
        this.categoryResolver = categoryResolver;
    }

    public Collection<Cosmetic> getSelectedCosmetics(LobbyPlayer player) {
        return categoryResolver.getCategories().stream()
                .map(category -> category.getCosmeticById(player.getSelectedCosmeticId(category)))
                .collect(Collectors.toList());
    }

}
