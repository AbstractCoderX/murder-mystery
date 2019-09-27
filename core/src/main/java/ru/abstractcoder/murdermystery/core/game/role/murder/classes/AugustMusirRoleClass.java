package ru.abstractcoder.murdermystery.core.game.role.murder.classes;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemUtils;
import ru.abstractcoder.benioapi.util.EventHelper;
import ru.abstractcoder.benioapi.util.Lazy;
import ru.abstractcoder.benioapi.util.cooldown.CooldownBuilder;
import ru.abstractcoder.benioapi.util.cooldown.StartRememberCooldown;
import ru.abstractcoder.benioapi.util.temporal.SimpleTemporal;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOwnMoveResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.InteractResponsible;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;

import java.util.concurrent.TimeUnit;

public class AugustMusirRoleClass extends MurderRoleClass {

    public AugustMusirRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private static class Logic extends MurderLogic implements InteractResponsible, AnyOwnMoveResponsible {

        private final Int2ObjectMap<Corpse> skullBySlotMap = new Int2ObjectOpenHashMap<>();
        private final Lazy<StartRememberCooldown> skinSwitchingCooldown = Lazy.create(() -> CooldownBuilder.create()
                .setDuration(SimpleTemporal.of(5, TimeUnit.SECONDS))
                .buildStartRemember());

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void kill(GamePlayer victim, DeathState deathState) {
            super.kill(victim, deathState);

            PlayerInventory inventory = gamePlayer.getHandle().getInventory();
            ItemStack skull = victim.getSkinContainer().getRealSkin().getSkull();
            for (int slot = 2; slot < inventory.getSize(); slot++) {
                ItemStack item = inventory.getItem(slot);
                if (ItemUtils.nullOrAir(item)) {
                    inventory.setItem(slot, skull);
                    Corpse corpse = deathState.getSpectatingPlayer().getCorpse();
                    skullBySlotMap.put(slot, corpse);
                    break;
                }
            }
        }

        @Override
        public void onInteract(int slot, ItemStack item, Action action, PlayerInteractEvent event) {
            if (EventHelper.isLeftClick(action)
                    && skinSwitchingCooldown.isInitialized()
                    && skinSwitchingCooldown.get().isValid()) {
                event.setCancelled(true);
                return;
            }

            if (!EventHelper.isRightClick(action)) {
                return;
            }

            Corpse targetCorpse = skullBySlotMap.get(slot);
            if (targetCorpse == null) {
                return;
            }

            gameEngine.getScheduler().runSyncLater(20 * 5, () -> {
                gamePlayer.getSkinContainer().setOwnSkin(targetCorpse.getSkin());
                targetCorpse.setSkin(gamePlayer.getSkinContainer().getRealSkin());
            });
            skinSwitchingCooldown.get().redefine();
        }

        @Override
        public void onAnyMove(Location from, Location to, Cancellable event) {
            if (skinSwitchingCooldown.isInitialized() && skinSwitchingCooldown.get().isValid()) {
                event.setCancelled(true);
                msgConfig.get(Messages.game__august_muzir_cooldown,
                        skinSwitchingCooldown.get().getRemainingTime().format()
                ).send(gamePlayer);
            }
        }

    }

}
