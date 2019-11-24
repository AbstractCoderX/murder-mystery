package ru.abstractcoder.murdermystery.core.cosmetic.service;

import dagger.Reusable;
import ru.abstractcoder.murdermystery.core.config.GeneralConfig;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Reusable
public class CosmeticService {

    private final GeneralConfig generalConfig;

    @Inject
    public CosmeticService(GeneralConfig generalConfig) {
        this.generalConfig = generalConfig;
    }

    public Collection<Cosmetic> getSelectedCosmetics(LobbyPlayer player) {
        return generalConfig.game().getCosmeticCategoryResolver().getCategories()
                .stream()
                .map(category -> category.getCosmeticById(player.data().getSelectedCosmeticId(category)))
                .collect(Collectors.toList());
    }

}
