package org.eu.hanana.reimu.game.ottoca.game;

import org.eu.hanana.reimu.game.ottoca.game.data.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class GameStorage {
    public static GameStorage CURRENT;
    public final IInventory inventory = new ListInventory();
    public final IInventory craftInventory = new CraftInventory();
    public  static List<Item> allItem = new ArrayList<>();
    public int score;
    public int timeLeft;
    public int customersSuccess;
    public int customersFail;
    @ApiStatus.Internal
    public int respondFail;
    @ApiStatus.Internal
    public int respondSuccess;
    public GameMode gameMode = GameMode.Challenge;
    public void init(){
        timeLeft= gameMode.maxTime;
    }
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
