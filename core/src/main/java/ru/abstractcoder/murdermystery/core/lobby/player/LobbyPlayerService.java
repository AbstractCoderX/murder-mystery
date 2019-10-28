package ru.abstractcoder.murdermystery.core.lobby.player;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.chance.ClassedRoleDataRepository;
import ru.abstractcoder.murdermystery.core.game.role.skin.selected.SelectedSkinRepository;
import ru.abstractcoder.murdermystery.core.statistic.StatisticRepository;

import java.util.concurrent.CompletableFuture;

public class LobbyPlayerService {

    private final ClassedRoleDataRepository classedRoleDataRepository;
    private final SelectedSkinRepository selectedSkinRepository;
    private final StatisticRepository statisticRepository;

    public LobbyPlayerService(ClassedRoleDataRepository classedRoleDataRepository,
            SelectedSkinRepository selectedSkinRepository, StatisticRepository statisticRepository) {
        this.classedRoleDataRepository = classedRoleDataRepository;
        this.selectedSkinRepository = selectedSkinRepository;
        this.statisticRepository = statisticRepository;
    }

    public CompletableFuture<LobbyPlayer> loadAsync(Player player) {
        String name = player.getName();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        return CompletableFuture.allOf(
                statisticRepository.load(name).thenAccept(lobbyPlayer::setStatistic),
                selectedSkinRepository.load(name).thenAccept(lobbyPlayer::setSelectedSkinMap),
                classedRoleDataRepository.load(name).thenAccept(lobbyPlayer::setClassedRoleDataMap)
        ).thenApply((__) -> lobbyPlayer);
    }

}
