package ru.abstractcoder.murdermystery.core.game.role.murder.classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Msg;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOtherMoveResponsible;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;

import java.util.Random;

public class WolfRoleClass extends MurderRoleClass {

    public WolfRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }


    public static final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, 12 * 20, 0);
    public static final PotionEffect CONFUSION = new PotionEffect(PotionEffectType.CONFUSION, 12 * 20, 0);
    public static final PotionEffect UNLUCK = new PotionEffect(PotionEffectType.UNLUCK, 20 * 20, 0);

    private static class Logic extends MurderLogic implements AnyOtherMoveResponsible {

        private static final Random random = new Random();

        private final Multimap<Player, Corpse> visitedCorps = HashMultimap.create();

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Msg> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onOtherMove(GamePlayer other, Location from, Location to, Cancellable event) {
            Player player = other.getPlayer();
            gameEngine.getCorpseService().nearbyCorpsesStream(to, 3)
                    .filter(corpse -> !visitedCorps.containsEntry(player, corpse))
                    .findFirst()
                    .ifPresent(corpse -> {
                        visitedCorps.put(player, corpse);

                        if (random.nextBoolean()) {
                            player.addPotionEffect(SLOW);
                        } else {
                            player.addPotionEffect(CONFUSION);
                        }
                        player.addPotionEffect(UNLUCK);
                    });
        }

    }

}