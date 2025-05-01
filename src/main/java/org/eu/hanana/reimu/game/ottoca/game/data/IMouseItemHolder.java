package org.eu.hanana.reimu.game.ottoca.game.data;

import org.eu.hanana.reimu.game.ottoca.game.width.WidthSlot;
import org.eu.hanana.reimu.thrunner.jthr.Item;
import org.jetbrains.annotations.NotNull;

public interface IMouseItemHolder {
    void setMouseItem(ItemStack item);
    ItemStack getMouseItem();
    @NotNull
    WidthSlot getSlot();
}
