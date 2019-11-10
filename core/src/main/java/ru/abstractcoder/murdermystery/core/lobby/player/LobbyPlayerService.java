package ru.abstractcoder.murdermystery.core.lobby.player;

import dagger.Reusable;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.cosmetic.repository.SelectedCosmeticRepository;
import ru.abstractcoder.murdermystery.core.game.role.chance.ClassedRoleDataRepository;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.repository.PurchasedRoleClassesRepository;
import ru.abstractcoder.murdermystery.core.game.role.skin.selected.SelectedSkinRepository;
import ru.abstractcoder.murdermystery.core.statistic.StatisticRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Reusable
public class LobbyPlayerService {

    private final ClassedRoleDataRepository classedRoleDataRepository;
    private final SelectedSkinRepository selectedSkinRepository;
    private final StatisticRepository statisticRepository;
    private final PurchasedRoleClassesRepository purchasedRoleClassesRepository;
    private final SelectedCosmeticRepository selectedCosmeticRepository;

    @Inject
    public LobbyPlayerService(ClassedRoleDataRepository classedRoleDataRepository,
            SelectedSkinRepository selectedSkinRepository,
            StatisticRepository statisticRepository,
            PurchasedRoleClassesRepository purchasedRoleClassesRepository,
            SelectedCosmeticRepository selectedCosmeticRepository) {
        this.classedRoleDataRepository = classedRoleDataRepository;
        this.selectedSkinRepository = selectedSkinRepository;
        this.statisticRepository = statisticRepository;
        this.purchasedRoleClassesRepository = purchasedRoleClassesRepository;
        this.selectedCosmeticRepository = selectedCosmeticRepository;
    }

    public CompletableFuture<LobbyPlayer> loadAsync(Player player) {
        String name = player.getName();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        return CompletableFuture.allOf(
                statisticRepository.load(name).thenAccept(lobbyPlayer::setStatistic),
                selectedSkinRepository.load(name).thenAccept(lobbyPlayer::setSelectedSkinMap),
                classedRoleDataRepository.load(name).thenAccept(lobbyPlayer::setClassedRoleDataMap),
                purchasedRoleClassesRepository.load(name).thenAccept(lobbyPlayer::setPurchasedRoleClasses),
                selectedCosmeticRepository.load(name).thenAccept(lobbyPlayer::setSelectedCosmetics)
        ).thenApply((__) -> lobbyPlayer);
    }

}
