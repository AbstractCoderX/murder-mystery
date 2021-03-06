package ru.abstractcoder.murdermystery.core.data;

import dagger.Reusable;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.cosmetic.repository.SelectedCosmeticRepository;
import ru.abstractcoder.murdermystery.core.game.role.chance.ClassedRoleDataRepository;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.repository.PurchasedRoleClassesRepository;
import ru.abstractcoder.murdermystery.core.game.role.skin.selected.SelectedSkinRepository;
import ru.abstractcoder.murdermystery.core.rating.Rating;
import ru.abstractcoder.murdermystery.core.rating.RatingFactory;
import ru.abstractcoder.murdermystery.core.statistic.StatisticRepository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Reusable
public class PlayerDataService {

    private final RatingFactory ratingFactory;

    private final ClassedRoleDataRepository classedRoleDataRepository;
    private final SelectedSkinRepository selectedSkinRepository;
    private final StatisticRepository statisticRepository;
    private final PurchasedRoleClassesRepository purchasedRoleClassesRepository;
    private final SelectedCosmeticRepository selectedCosmeticRepository;

    private final Map<UUID, PlayerData> loadedDataMap = new HashMap<>();

    @Inject
    public PlayerDataService(RatingFactory ratingFactory,
            ClassedRoleDataRepository classedRoleDataRepository,
            SelectedSkinRepository selectedSkinRepository,
            StatisticRepository statisticRepository,
            PurchasedRoleClassesRepository purchasedRoleClassesRepository,
            SelectedCosmeticRepository selectedCosmeticRepository) {
        this.ratingFactory = ratingFactory;
        this.classedRoleDataRepository = classedRoleDataRepository;
        this.selectedSkinRepository = selectedSkinRepository;
        this.statisticRepository = statisticRepository;
        this.purchasedRoleClassesRepository = purchasedRoleClassesRepository;
        this.selectedCosmeticRepository = selectedCosmeticRepository;
    }

    public CompletableFuture<PlayerData> loadPlayerData(Player player) {
        String name = player.getName();

        PlayerData playerData = new PlayerData(player);
        loadedDataMap.put(player.getUniqueId(), playerData);    

        return CompletableFuture.allOf(
                statisticRepository.load(name).thenAccept(statistic -> {
                    playerData.setStatistic(statistic);

                    Rating rating = ratingFactory.create(statistic);
                    playerData.setRating(rating);
                }),
                selectedSkinRepository.load(name).thenAccept(playerData::setSelectedSkinMap),
                classedRoleDataRepository.load(name).thenAccept(playerData::setClassedRoleDataMap),
                purchasedRoleClassesRepository.load(name).thenAccept(playerData::setPurchasedRoleClasses),
                selectedCosmeticRepository.load(name).thenAccept(playerData::setSelectedCosmeticMap)
        ).thenApply((__) -> playerData);
    }

    public CompletableFuture<Void> savePlayerData(PlayerData data) {
        String name = data.getOwner().getName();
        return CompletableFuture.allOf(
                statisticRepository.save(name, data.statistic()),
                selectedSkinRepository.save(name, data.getSelectedSkinMap()),
                classedRoleDataRepository.save(name, data.getClassedRoleDataMap()),
                purchasedRoleClassesRepository.save(name, data.getPurchasedRoleClasses()),
                selectedCosmeticRepository.save(name, data.getSelectedCosmeticMap())
        );
    }

    public CompletableFuture<Void> saveAll() {
        CompletableFuture<?>[] futures = loadedDataMap.values()
                .stream()
                .map(this::savePlayerData)
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

}
