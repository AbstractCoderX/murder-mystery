package ru.abstractcoder.murdermystery.core.slotbar;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SlotBarItemResolver {

    private final Map<Integer, SlotBarItem> slotBarItemMap;

    @JsonCreator
    public SlotBarItemResolver(List<SlotBarItem> slotBarItems) {
        this.slotBarItemMap = slotBarItems.stream()
                .collect(Collectors.toMap(SlotBarItem::getSlot, Function.identity()));
    }

    public BeniOptional<SlotBarItem> resolve(int slot) {
        return BeniOptional.ofNullable(slotBarItemMap.get(slot));
    }

    public Collection<SlotBarItem> getAllItems() {
        return slotBarItemMap.values();
    }

}