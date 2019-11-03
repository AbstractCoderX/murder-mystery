package ru.abstractcoder.murdermystery.core.game.player;

import dagger.Reusable;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleResolver;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainerFactory;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;
import ru.abstractcoder.murdermystery.core.lobby.player.LobbyPlayer;

import javax.inject.Inject;

@Reusable
public class PlayerFactory {

    private final SkinContainerFactory skinContainerFactory;
    private final RoleResolver roleResolver;

    @Inject
    public PlayerFactory(SkinContainerFactory skinContainerFactory, RoleResolver roleResolver) {
        this.skinContainerFactory = skinContainerFactory;
        this.roleResolver = roleResolver;
    }

    public GamePlayer createPlayer(LobbyPlayer lobbyPlayer) {
        SkinContainer skinContainer = skinContainerFactory.createFor(lobbyPlayer);
        return new GamePlayer(lobbyPlayer.getPlayer(), lobbyPlayer.getBalancedRole(), skinContainer);
    }

    public GamePlayer revivePlayer(SpectatingPlayer sp) {
        Player player = sp.getHandle();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(sp.getDeathLocation());

        GameRole role = roleResolver.resolveCivilianRole(Profession.Type.DEFAULT);
        SkinContainer skinContainer = sp.getCachedSkinContainer().toSimpleCivilianContainer();

        GamePlayer gamePlayer = new GamePlayer(player, role, skinContainer);
        gamePlayer.getRoleLogic().load();

        return gamePlayer;
    }

}