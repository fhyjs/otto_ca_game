package org.eu.hanana.reimu.game.ottoca.game.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListInventory implements IInventory{
    public final List<ItemStack> inventory = new ArrayList<>();
    @Override
    public ItemStack getStackInSlot(int id) {
        if (id < 0 || id >= inventory.size()){
            return null;
        }
        return inventory.get(id);
    }

    @Override
    public void setStack(int id, ItemStack stack) {
        while (inventory.size() <= id) {
            inventory.add(null);
        }
        inventory.set(id, stack);
    }

    @Override
    public List<ItemStack> getAllStack() {
        return inventory;
    }

    @Override
    public boolean add(ItemStack stack) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(inventory);
        for (ItemStack itemStack : itemStacks) {
            if (stack.equals(itemStack)){
                itemStack.amount+=stack.amount;
                return true;
            }
        }
        inventory.add(stack);
        return true;
    }

    @Override
    public void add(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            add(stack);
        }
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public @NotNull Iterator<ItemStack> iterator() {
        return new ArrayList<>(inventory).stream().iterator();
    }
}
