package org.eu.hanana.reimu.game.ottoca.game.data;

import java.util.List;

public interface IInventory extends Iterable<ItemStack>{
    ItemStack getStackInSlot(int id);
    void setStack(int id,ItemStack stack);
    List<ItemStack> getAllStack();
    boolean add(ItemStack stack);
    void add(ItemStack... stacks);
    int size();
}
