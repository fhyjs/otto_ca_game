package org.eu.hanana.reimu.game.ottoca.game;

import org.eu.hanana.reimu.game.ottoca.game.data.IInventory;
import org.eu.hanana.reimu.game.ottoca.game.data.Item;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.ListInventory;

import java.util.ArrayList;
import java.util.List;

public class GameStorage {
    public static GameStorage CURRENT;
    public final IInventory inventory = new ListInventory();
    public final IInventory craftInventory = new ListInventory();
    public  static List<Item> allItem = new ArrayList<>();
    public GameStorage(){
        inventory.getAllStack().addAll(getFirstInventory());
    }

    public static List<Item> getAllItems() {
        return allItem;
    }
    public static List<ItemStack> getFirstInventory(){
        return List.of(
                new ItemStack("ice",10),
                new ItemStack("big_fruit",10),
                new ItemStack("chives",10)
        );
    }
    public static Item getItemByType(String type){
        for (Item item : allItem) {
            if (item.type.equals(type))
                return item;
        }
        return null;
    }
}
