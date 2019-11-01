package ru.abstractcoder.murdermystery.core.lobby.player;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.chance.ClassedRoleDataRepository;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.repository.PurchasedRoleClassesRepository;
import ru.abstractcoder.murdermystery.core.game.role.skin.selected.SelectedSkinRepository;
import ru.abstractcoder.murdermystery.core.statistic.StatisticRepository;

import java.util.concurrent.CompletableFuture;

public class LobbyPlayerService {

    private final ClassedRoleDataRepository classedRoleDataRepository;
    private final SelectedSkinRepository selectedSkinRepository;
    private final StatisticRepository statisticRepository;
    private final PurchasedRoleClassesRepository purchasedRoleClassesRepository;

    public LobbyPlayerService(ClassedRoleDataRepository classedRoleDataRepository,
            SelectedSkinRepository selectedSkinRepository,
            StatisticRepository statisticRepository,
            PurchasedRoleClassesRepository purchasedRoleClassesRepository) {
        this.classedRoleDataRepository = classedRoleDataRepository;
        this.selectedSkinRepository = selectedSkinRepository;
        this.statisticRepository = statisticRepository;
        this.purchasedRoleClassesRepository = purchasedRoleClassesRepository;
    }

    public CompletableFuture<LobbyPlayer> loadAsync(Player player) {
        String name = player.getName();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        return CompletableFuture.allOf(
                statisticRepository.load(name).thenAccept(lobbyPlayer::setStatistic),
                selectedSkinRepository.load(name).thenAccept(lobbyPlayer::setSelectedSkinMap),
                classedRoleDataRepository.load(name).thenAccept(lobbyPlayer::setClassedRoleDataMap),
                purchasedRoleClassesRepository.load(name).thenAccept(lobbyPlayer::setPurchasedRoleClasses)
        ).thenApply((__) -> lobbyPlayer);
    }

}
