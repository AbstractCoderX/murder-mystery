package ru.abstractcoder.murdermystery.core.lobby.player;

import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.RoleTemplate;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.PurchasableRoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;
import ru.abstractcoder.murdermystery.core.util.AbstractWrappedPlayer;

import java.util.Map;
import java.util.Set;

//TODO extract interface
public class LobbyPlayer extends AbstractWrappedPlayer {

    private RoleTemplate preferredRole;
    private Map<GameRole.Type, ClassedRoleData> classedRoleDataMap;
    private Map<RoleComponent.Type, Skin> selectedSkinMap;
    private PlayerStatistic statistic;
    private Set<RoleClass.Type> purchasedRoleClasses;

    private GameRole balancedRole;

    public LobbyPlayer(Player handle) {
        super(handle);
    }

    public void setClassedRoleDataMap(Map<GameRole.Type, ClassedRoleData> classedRoleDataMap) {
        this.classedRoleDataMap = classedRoleDataMap;
    }

    public void setSelectedSkinMap(Map<RoleComponent.Type, Skin> selectedSkinMap) {
        this.selectedSkinMap = selectedSkinMap;
    }

    public void setStatistic(PlayerStatistic statistic) {
        this.statistic = statistic;
    }

    public RoleTemplate getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(RoleTemplate preferredRole) {
        this.preferredRole = preferredRole;
    }

    public ClassedRoleData getClassedRoleData(GameRole.Type type) {
        return classedRoleDataMap.computeIfAbsent(type, (__) -> new ClassedRoleData());
    }

    public GameRole getBalancedRole() {
        return balancedRole;
    }

    public void setBalancedRole(GameRole balancedRole) {
        this.balancedRole = balancedRole;
    }

    public PlayerStatistic getStatistic() {
        return statistic;
    }

    public Skin getSelectedSkin(RoleComponent.Type componentType, Skin defaultSkin) {
        return selectedSkinMap.getOrDefault(componentType, defaultSkin);
    }

    public void setSelectedSkin(RoleComponent.Type componentType, Skin selectedSkin) {
        selectedSkinMap.put(componentType, selectedSkin);
    }

    public boolean isRoleClassPurchased(RoleClass.Type classType) {
        return purchasedRoleClasses.contains(classType);
    }

    public boolean isRoleClassPurchased(PurchasableRoleClassTemplate template) {
        return isRoleClassPurchased(template.getType());
    }

    public void addPurchasedRoleClass(RoleClass.Type classType) {
        purchasedRoleClasses.add(classType);
    }

    public void addPurchasedRoleClass(PurchasableRoleClassTemplate template) {
        addPurchasedRoleClass(template.getType());
    }

    public void setPurchasedRoleClasses(Set<RoleClass.Type> purchasedRoleClasses) {
        this.purchasedRoleClasses = purchasedRoleClasses;
    }

    public static class ClassedRoleData {

        private int chancePoints;
        private RoleClass.Type selectedClassType;

        public ClassedRoleData(int chancePoints, RoleClass.Type selectedClassType) {
            this.chancePoints = chancePoints;
            this.selectedClassType = selectedClassType;
        }

        public ClassedRoleData() {
        }

        public int getChancePoints() {
            return chancePoints;
        }

        public void incrementChancePoints() {
            chancePoints++;
        }

        public void resetChancePoints() {
            chancePoints = 0;
        }

        public boolean isClassTypeSelected() {
            return selectedClassType != null;
        }

        public RoleClass.Type getSelectedClassType() {
            return selectedClassType;
        }

    }

}