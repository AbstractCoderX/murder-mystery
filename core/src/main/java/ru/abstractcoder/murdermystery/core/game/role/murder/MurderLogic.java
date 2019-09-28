package ru.abstractcoder.murdermystery.core.game.role.murder;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.civilian.CivilianRole;
import ru.abstractcoder.murdermystery.core.game.role.logic.AbstractRoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.profession.Profession;

public class MurderLogic extends AbstractRoleLogic {

    private static final PotionEffect BLINDESS_EFFECT = new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0);

    public MurderLogic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(gamePlayer, gameEngine, msgConfig);
    }

    @Override
    public void load() {
        gameEngine.getPlayerResolver().loadMurder(gamePlayer);
    }

    @Override
    public void kill(GamePlayer victim, DeathState deathState) {
        super.kill(victim, deathState);

        victim.getHandle().addPotionEffect(BLINDESS_EFFECT);

        int murderKillMoney = gameEngine.settings().rewards().getMurderKillMoney();
        gameEngine.getEconomyService().incrementBalanceAsync(gamePlayer, murderKillMoney);
        msgConfig.get(Messages.game__murder_get_money_for_kill, murderKillMoney).send(gamePlayer);

        GamePlayerResolver playerResolver = gameEngine.getPlayerResolver();
        playerResolver.getSurvivors().forEach(gamePlayer -> {
            Player player = gamePlayer.getHandle();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
        });

        if (victim.getRole().getType() == GameRole.Type.DETECTIVE) {
            boolean hasPoliceman = playerResolver.getCivilians().stream().anyMatch(gamePlayer -> {
                CivilianRole role = (CivilianRole) gamePlayer.getRole();
                if (role.getProfession().getType() == Profession.Type.POLICEMAN) {
                    gamePlayer.setRole(gameEngine.getRoleResolver().getDefaultClassedRole(GameRole.Type.DETECTIVE));
                    //TODO new detective msg
                    return true;
                }
                return false;
            });
            if (!hasPoliceman) {
                playerResolver.setDetective(null);
                gameEngine.getBowDropProcessor().dropBow(deathState.getDeathLocation());
            }
        }

        //TODO death message for victim
    }

}