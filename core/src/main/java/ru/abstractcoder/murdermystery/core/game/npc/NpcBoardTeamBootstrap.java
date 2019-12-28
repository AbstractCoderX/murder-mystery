package ru.abstractcoder.murdermystery.core.game.npc;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.abstractcoder.benioapi.board.BoardApi;
import ru.abstractcoder.murdermystery.core.game.action.GameActionService;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;

import javax.inject.Inject;

public class NpcBoardTeamBootstrap {

    private final BoardApi boardApi;
    private final GameActionService gameActionService;

    @Inject
    public NpcBoardTeamBootstrap(BoardApi boardApi, GameActionService gameActionService) {
        this.boardApi = boardApi;
        this.gameActionService = gameActionService;
    }

    public void boot() {
        gameActionService.addStartingAction(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Scoreboard scoreboard = boardApi.getBukkitScoreboardCache().getCachedScoreboard(player);
                Team team = scoreboard.registerNewTeam(SharedConstants.NPC_NAME);
                team.addEntry(SharedConstants.NPC_NAME);
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            });
        });
    }

}
