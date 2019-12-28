package ru.abstractcoder.murdermystery.core.game.side;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import dagger.Reusable;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.data.PlayerData;
import ru.abstractcoder.murdermystery.core.game.action.GameActionService;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.player.PlayerController;
import ru.abstractcoder.murdermystery.core.rating.Rating;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Reusable
public class GameSideService {

    private final GamePlayerResolver playerResolver;
    private final PlayerController playerController;

    private final ListMultimap<GameSide, PlayerData> playerDataMap = Multimaps.newListMultimap(
            Maps.newEnumMap(GameSide.class),
            ArrayList::new
    );

    private final ListMultimap<GameSide, Player> playerMap = Multimaps.newListMultimap(
            Maps.newEnumMap(GameSide.class),
            ArrayList::new
    );

    @Inject
    public GameSideService(GamePlayerResolver playerResolver, PlayerController playerController,
            GameActionService gameActionService) {
        this.playerResolver = playerResolver;
        this.playerController = playerController;

        gameActionService.addStartingAction(() -> {
            playerResolver.getAll().forEach(gamePlayer -> {
                GameSide gameSide = gamePlayer.getRoleLogic().getGameSide();
                playerMap.put(gameSide, gamePlayer.getHandle());
                playerDataMap.put(gameSide, gamePlayer.data());
            });
        });
    }

    public Collection<GamePlayer> getAlivePlayers(GameSide gameSide) {
        if (gameSide == GameSide.MURDER) {
            return playerResolver.isMurderAlive()
                   ? Collections.singletonList(playerResolver.getMurder())
                   : Collections.emptyList();
        }
        return playerResolver.getSurvivors();
    }

    public Collection<Player> getPlayers(GameSide gameSide) {
        List<Player> players = playerMap.get(gameSide);
        players.removeIf(Predicate.not(Player::isOnline));
        return players;
    }

    public Collection<PlayerData> getDatas(GameSide gameSide) {
        return playerDataMap.get(gameSide);
    }

    //    public Collection<SpectatingPlayer> getSpectatingPlayers(GameSide gameSide) {
    //        if (gameSide == GameSide.MURDER) {
    //            if (playerResolver.isMurderAlive()) {
    //                Player player = playerResolver.getMurder().getHandle();
    //                return Collections.singletonList(playerController.getSpectating(player));
    //            }
    //            return Collections.emptyList();
    //        }
    //        return ;
    //    }

    public double getAverageRating(GameSide gameSide) {
        return playerDataMap.get(gameSide).stream()
                .map(PlayerData::rating)
                .mapToInt(Rating::value)
                .average().orElseThrow(IllegalAccessError::new);
    }

}
