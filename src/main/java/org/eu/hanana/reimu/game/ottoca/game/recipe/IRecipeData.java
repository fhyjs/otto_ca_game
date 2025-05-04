package org.eu.hanana.reimu.game.ottoca.game.recipe;

import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;

import java.util.List;

public interface IRecipeData {
    String getType();
    ItemStack getOutput(List<ItemStack> itemStacks);
    boolean isMatch(List<ItemStack> itemStacks);
}
