package ru.abstractcoder.murdermystery.core.game.role.murder.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.misc.SharedConstants;
import ru.abstractcoder.murdermystery.core.game.npc.Npc;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.InteractResponsible;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.NpcDamageResponsible;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderLogic;
import ru.abstractcoder.murdermystery.core.game.role.murder.MurderRoleClass;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;
import ru.abstractcoder.murdermystery.core.game.spectate.SpectatingPlayer;

public class VoodooDollRoleClass extends MurderRoleClass {

    private static final ItemStack VOODOO_SKULL = new ItemStack(Material.SKELETON_SKULL);
    private static final ItemStack WOODEN_SWORD = new ItemStack(Material.WOODEN_SWORD);

    public VoodooDollRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        return new Logic(gamePlayer, gameEngine, msgConfig);
    }

    private class Logic extends MurderLogic implements InteractResponsible, NpcDamageResponsible {

        private boolean firstKillCommited = false;
        private boolean skullUsed = false;
        private SpectatingPlayer killedPlayer;
        private Npc npc;

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void kill(GamePlayer victim, DeathState deathState) {
            if (firstKillCommited) {
                super.kill(victim, deathState);
                return;
            }

            deathState.setNeedCorpse(false);
            super.kill(victim, deathState);

            firstKillCommited = true;
            killedPlayer = deathState.getSpectatingPlayer();
            gamePlayer.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, VOODOO_SKULL);
            npc = gameEngine.getNpcService().spawnNpc( //TODO think about skin
                    victim.getHandle().getLocation(),
                    victim.getSkinContainer());

            gameEngine.getScheduler().runSyncLater(30 * 20, () -> {
                if (skullUsed) {
                    return;
                }

                gamePlayer.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, getWeaponItem());

                npc.destroy();
                gameEngine.getCorpseService().spawnCorpse(killedPlayer, npc.getLocation());
            });
        }

        @Override
        public void onInteract(int slot, ItemStack item, Action action, PlayerInteractEvent event) {
            if (slot != SharedConstants.WEAPON_SLOT) {
                return;
            }

            if (skullUsed) {
                return;
            }

            skullUsed = true;

            SkinContainer npcSkinContainer = npc.getSkinContainer();
            Location npcLocation = npc.getLocation();

            npc.setSkinContainer(gamePlayer.getSkinContainer());
            npc.teleport(gamePlayer.getHandle().getLocation());

            gamePlayer.setSkinContainer(npcSkinContainer);
            gamePlayer.getHandle().teleport(npcLocation);

            gamePlayer.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, WOODEN_SWORD);
        }

        @Override
        public void onNpcDamage(GamePlayer murder, GamePlayer damager, Npc npc, Cancellable event) {
            if (!skullUsed || this.npc != npc || damager.getRole().getType() == GameRole.Type.MURDER) {
                return;
            }

            event.setCancelled(false);

            murder.getHandle().teleport(npc.getLocation());
            npc.destroy();
            this.npc = null;

            murder.getHandle().getInventory().setItem(SharedConstants.WEAPON_SLOT, getWeaponItem());
        }

    }


}