package ru.abstractcoder.murdermystery.core.game.role.classed.template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import ru.abstractcoder.benioapi.util.CollectionGeneraliser;
import ru.abstractcoder.murdermystery.core.game.role.GameRole;
import ru.abstractcoder.murdermystery.core.game.role.classed.RoleClass;

import java.util.*;

public class RoleClassTemplateResolver {

    private final Map<RoleClass.Type, RoleClassTemplate> byRoleClassTypeMap;
    private final Map<GameRole.Type, TemplateSettings> byGameRoleTypeMap;
    private final Collection<PurchasableRoleClassTemplate> allPurchasableTemplates;

    @JsonCreator
    public RoleClassTemplateResolver(
            @JsonDeserialize(as = EnumMap.class) Map<GameRole.Type, TemplateSettings> templateSettingsMap) {

        byGameRoleTypeMap = templateSettingsMap;
        byRoleClassTypeMap = new HashMap<>();
        allPurchasableTemplates = new ArrayList<>();

        templateSettingsMap.forEach((roleType, templateSettings) -> {
            templateSettings.initTypes(roleType);
            templateSettings.putToMap(byRoleClassTypeMap);
            allPurchasableTemplates.addAll(templateSettings.purchasableTemplates);
        });
    }

    public RoleClassTemplate getByType(RoleClass.Type type) {
        return byRoleClassTypeMap.get(type);
    }

    public RoleClassTemplate getDefaultTemplate(GameRole.Type roleType) {
        return getTemplateSettings(roleType).defaultTemplate;
    }

    public List<PurchasableRoleClassTemplate> getPurchasableTemplates(GameRole.Type roleType) {
        return CollectionGeneraliser.generaliseList(getTemplateSettings(roleType).purchasableTemplates);
    }

    public Set<GameRole.Type> getRoleTypes() {
        return byGameRoleTypeMap.keySet();
    }

    //    public Collection<RoleClassTemplate> getAllDefaultTemplates() {
    //        return byGameRoleTypeMap.values().stream()
    //                .map(templateSettings -> templateSettings.defaultTemplate)
    //                .collect(Collectors.toList());
    //    }

    public Collection<RoleClassTemplate> getAll() {
        return byRoleClassTypeMap.values();
    }

//    public Stream<PurchasableRoleClassTemplate> allPurchasableTemplates() {
//        return Stream.concat(
//                getPurchasableTemplates(GameRole.Type.DETECTIVE).stream(),
//                getPurchasableTemplates(GameRole.Type.MURDER).stream()
//        );
//    }

    public Collection<PurchasableRoleClassTemplate> getAllPurchasableTemplates() {
        return allPurchasableTemplates;
    }

    @NotNull
    private TemplateSettings getTemplateSettings(GameRole.Type roleType) {
        TemplateSettings templateSettings = byGameRoleTypeMap.get(roleType);
        Preconditions.checkState(templateSettings != null, "Not loaded templates for " + roleType);
        return templateSettings;
    }

    private static class TemplateSettings {

        @JsonProperty("default")
        private final SimpleRoleClassTemplate defaultTemplate;
        @JsonProperty("purchasable")
        private final List<PurchasableRoleClassTemplateImpl> purchasableTemplates;

        @JsonCreator
        public TemplateSettings(SimpleRoleClassTemplate defaultTemplate, List<PurchasableRoleClassTemplateImpl> purchasableTemplates) {
            this.defaultTemplate = defaultTemplate;
            this.purchasableTemplates = purchasableTemplates;
        }

        private void initTypes(GameRole.Type roleType) {
            defaultTemplate.initType(roleType);
            purchasableTemplates.forEach(template -> template.initType(roleType));
        }

        private void putToMap(Map<RoleClass.Type, RoleClassTemplate> map) {
            map.put(defaultTemplate.getType(), defaultTemplate);
            purchasableTemplates.forEach(template -> map.put(template.getType(), template));
        }

    }

}