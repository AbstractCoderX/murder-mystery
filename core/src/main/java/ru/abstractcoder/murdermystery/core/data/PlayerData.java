package ru.abstractcoder.murdermystery.core.data;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import ru.abstractcoder.murdermystery.core.cosmetic.Cosmetic;
import ru.abstractcoder.murdermystery.core.cosmetic.CosmeticCategory;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;
import ru.abstractcoder.murdermystery.core.game.role.classed.template.PurchasableRoleClassTemplate;
import ru.abstractcoder.murdermystery.core.game.role.component.RoleComponent;
import ru.abstractcoder.murdermystery.core.game.skin.Skin;
import ru.abstractcoder.murdermystery.core.statistic.PlayerStatistic;

import java.util.Map;
import java.util.Set;

public class PlayerData {

    private final Player owner;

    private Map<GameRole.Type, ClassedRoleData> classedRoleDataMap;
    private Map<RoleComponent.Type, Skin> selectedSkinMap;
    private PlayerStatistic statistic;
    private Set<RoleClass.Type> purchasedRoleClasses;
    private Map<CosmeticCategory.Type, String> selectedCosmeticMap;

    public PlayerData(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
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

    public ClassedRoleData getClassedRoleData(GameRole.Type type) {
        Preconditions.checkArgument(type.isClassed(), "role type must be classed");
        return classedRoleDataMap.computeIfAbsent(type, (__) -> new ClassedRoleData());
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

    public void setSelectedCosmeticMap(Map<CosmeticCategory.Type, String> selectedCosmeticMap) {
        this.selectedCosmeticMap = selectedCosmeticMap;
    }

    public void selectedCosmetic(CosmeticCategory category, Cosmetic cosmetic) {
        selectedCosmetic(category.getType(), cosmetic);
    }

    public void selectedCosmetic(CosmeticCategory.Type category, Cosmetic cosmetic) {
        selectedCosmeticMap.put(category, cosmetic.getId());
    }

    public void unselectCosmetic(CosmeticCategory category) {
        unselectCosmetic(category.getType());
    }

    public void unselectCosmetic(CosmeticCategory.Type category) {
        selectedCosmeticMap.remove(category);
    }

    public String getSelectedCosmeticId(CosmeticCategory category) {
        return selectedCosmeticMap.getOrDefault(category.getType(), category.getDefaultCosmetic().getId());
    }

    public boolean isCosmeticSelected(CosmeticCategory category, Cosmetic cosmetic) {
        String selectedId = getSelectedCosmeticId(category);
        return selectedId != null && cosmetic.getId().equals(selectedId);
    }

    public Map<GameRole.Type, ClassedRoleData> getClassedRoleDataMap() {
        return classedRoleDataMap;
    }

    public Map<RoleComponent.Type, Skin> getSelectedSkinMap() {
        return selectedSkinMap;
    }

    public Set<RoleClass.Type> getPurchasedRoleClasses() {
        return purchasedRoleClasses;
    }

    public Map<CosmeticCategory.Type, String> getSelectedCosmeticMap() {
        return selectedCosmeticMap;
    }

}
