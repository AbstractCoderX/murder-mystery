package ru.abstractcoder.murdermystery.core.game.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.board.sidebar.Sidebar;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.logic.RoleLogic;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainable;
import ru.abstractcoder.murdermystery.core.game.skin.container.SkinContainer;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public class GamePlayer extends AbstractWrappedPlayer implements SkinContainable {

    private GameRole role;
    private SkinContainer skinContainer;
    private final Collection<Cosmetic> cosmetics;

    private boolean roleShowed = false;

    private double goldMultiplier = 1.0;

    private int murderProtectionPoints = 0;

    //TODO handle this
    private boolean muted = false;

    private boolean unmovedDamageEnabled = false;
    private int unmovedSeconds = 0;

    private boolean chatCursed = false;

    @Nullable
    private Sidebar cachedSidebar;

    public GamePlayer(Player player, GameRole role, SkinContainer skinContainer, Collection<Cosmetic> cosmetics) {
        super(player);
        this.role = role;
        this.skinContainer = skinContainer;
        this.cosmetics = cosmetics;
    }

    public GameRole getRole() {
        return role;
    }

    public void setRole(GameRole role) {
        role.initLogic(this);
        this.role = role;
    }

    @Override
    public UUID getUniqueId() {
        return handle.getUniqueId();
    }

    @Override
    public SkinContainer getSkinContainer() {
        return skinContainer;
    }

    @Override
    public void setSkinContainer(SkinContainer skinContainer) {
        this.skinContainer = skinContainer;
    }

    public boolean isRoleShowed() {
        return roleShowed;
    }

    public void setRoleShowed(boolean roleShowed) {
        this.roleShowed = roleShowed;
    }

    public RoleLogic getRoleLogic() {
        return role.getLogic(this);
    }

    public <T extends Cosmetic.Logic> Stream<T> cosmetics(Class<? extends T> clazz) {
        return cosmetics.stream()
                .map(Cosmetic::getLogic)
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public Collection<Cosmetic> getCosmetics() {
        return cosmetics;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }

    public void setGoldMultiplier(double goldMultiplier) {
        this.goldMultiplier = goldMultiplier;
    }

    @Nullable
    public Sidebar getCachedSidebar() {
        return cachedSidebar;
    }

    public void setCachedSidebar(@Nullable Sidebar cachedSidebar) {
        this.cachedSidebar = cachedSidebar;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isUnmovedDamageEnabled() {
        return unmovedDamageEnabled;
    }

    public void setUnmovedDamageEnabled(boolean unmovedDamageEnabled) {
        this.unmovedDamageEnabled = unmovedDamageEnabled;
    }

    public int getUnmovedSeconds() {
        return unmovedSeconds;
    }

    public int incrementUnmovedSeconds() {
        return ++unmovedSeconds;
    }

    public void resetUnmovedSeconds() {
        unmovedSeconds = 0;
    }

    public boolean isChatCursed() {
        return chatCursed;
    }

    public void setChatCursed(boolean chatCursed) {
        this.chatCursed = chatCursed;
    }

    public int getMurderProtectionPoints() {
        return murderProtectionPoints;
    }

    public void incrementMurderProtectionPoints() {
        murderProtectionPoints++;
    }

    public void decrementMurderProtectionPoints() {
        murderProtectionPoints--;
    }

}