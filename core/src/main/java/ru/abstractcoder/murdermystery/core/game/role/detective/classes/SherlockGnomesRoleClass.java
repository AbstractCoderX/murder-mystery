package ru.abstractcoder.murdermystery.core.game.role.detective.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import ru.abstractcoder.benioapi.config.msg.MsgConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.util.probable.Probability;
import ru.abstractcoder.murdermystery.core.config.Messages;
import ru.abstractcoder.murdermystery.core.game.GameEngine;
import ru.abstractcoder.murdermystery.core.game.corpse.Corpse;
import ru.abstractcoder.murdermystery.core.game.player.GamePlayer;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.RoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveLogic;
import ru.abstractcoder.murdermystery.core.game.role.detective.DetectiveRoleClass;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.role.logic.responsible.AnyOwnMoveResponsible;

import java.util.ArrayList;
import java.util.List;

public class SherlockGnomesRoleClass extends DetectiveRoleClass {

    private boolean firstLogicInitialized = false;

    public SherlockGnomesRoleClass(RoleClassTemplate template, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
        super(template, gameEngine, msgConfig);
    }

    @Override
    public RoleLogic createLogic(GamePlayer gamePlayer) {
        Logic logic = new Logic(gamePlayer, gameEngine, msgConfig);

        if (firstLogicInitialized) {
            logic.secondary = true;
        } else {
            firstLogicInitialized = true;
        }

        return logic;
    }

    private static class Logic extends DetectiveLogic implements AnyOwnMoveResponsible {

        private static final int NOTEBOOK_SLOT = 3;
        private static final Probability PROOF_COLLECT_PROBABILITY = Probability.fromPercent(25);
        private static final int PROOFS_PER_PAGE = 2;

        private final List<String> proofPages = new ArrayList<>();
        private int proofCounter = 0;
        private boolean secondary = false;

        private void addCollectedProof(String proof) {
            int pageIndex = ((++proofCounter + PROOFS_PER_PAGE - 1) / PROOFS_PER_PAGE) - 1;
            if (pageIndex == proofPages.size()) {
                proofPages.add(proof);
            } else if (pageIndex < proofPages.size()) {
                String previousCombined = proofPages.get(pageIndex);
                proofPages.set(pageIndex, previousCombined + "\n" + proof);
            } else {
                throw new IllegalStateException(String.format("incorrect proof index: %s, pages size: %s", pageIndex, proofPages.size()));
            }
        }

        private Logic(GamePlayer gamePlayer, GameEngine gameEngine, MsgConfig<Messages> msgConfig) {
            super(gamePlayer, gameEngine, msgConfig);
        }

        @Override
        public void onAnyMove(Location from, Location to, Cancellable event) {
            super.onAnyMove(from, to, event);

            gameEngine.getCorpseService().nearbyCorpsesStream(to, 3)
                    .map(Corpse::proof)
                    .findFirst()
                    .ifPresent(proof -> {
                        if ((secondary && !proof.isCollectedByAnyone())
                                || !PROOF_COLLECT_PROBABILITY.checkLuck()) {
                            return;
                        }

                        String proofText = proof.collectBy(gamePlayer);
                        addCollectedProof(proofText);

                        //TODO format proof
                        gamePlayer.sendMessage(proofText);

                        ItemStack notebook = ItemBuilder.fromMaterial(Material.WRITTEN_BOOK)
                                .withItemMeta(BookMeta.class)
                                .customModifying(bookMeta -> bookMeta.setPages(proofPages))
                                .and().build();
                        gamePlayer.getHandle().getInventory().setItem(NOTEBOOK_SLOT, notebook);
                    });
        }

    }

}