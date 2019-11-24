package ru.abstractcoder.murdermystery.core.game.player;

import dagger.Reusable;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.cosmetic.service.CosmeticService;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainerFactory;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;

@Reusable
public class GamePlayerService {

    private final SkinContainerFactory skinContainerFactory;
    private final RoleResolver roleResolver;
    private final CosmeticService cosmeticService;

    @Inject
    public GamePlayerService(SkinContainerFactory skinContainerFactory, RoleResolver roleResolver,
            CosmeticService cosmeticService) {
        this.skinContainerFactory = skinContainerFactory;
        this.roleResolver = roleResolver;
        this.cosmeticService = cosmeticService;
    }

    public GamePlayer createPlayer(LobbyPlayer lobbyPlayer) {
        var skinContainer = skinContainerFactory.createFor(lobbyPlayer);
        var selectedCosmetics = cosmeticService.getSelectedCosmetics(lobbyPlayer);

        return new GamePlayer(lobbyPlayer.getPlayer(), lobbyPlayer.getBalancedRole(),
                lobbyPlayer.data(), skinContainer, selectedCosmetics);
    }

    public GamePlayer revivePlayer(SpectatingPlayer sp) {
        Player player = sp.getHandle();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(sp.getDeathLocation());

        var role = roleResolver.resolveCivilianRole(Profession.Type.DEFAULT);
        return GamePlayer.fromSpectatingPlayer(sp, role);
    }

}