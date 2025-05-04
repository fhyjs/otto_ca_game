package org.eu.hanana.reimu.game.ottoca.game.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager implements IRecipeData{
    public final Map<String,IRecipeData> recipeDataMap = new HashMap<>();
    public final Map<String,String> beforeInitRecipeRaw = new HashMap<>();
    public void loadRecipe(String json,String name){
        beforeInitRecipeRaw.put(name,json);
    }
    public void init(String name,String json){
        JsonObject input = JsonParser.parseString(json).getAsJsonObject();
        var type = input.get("type").getAsString();
        if (type.equals(ShapedRecipeData.TYPE)){
            recipeDataMap.put(name,new ShapedRecipeData(input));
        }
    }
    public void init(){
        beforeInitRecipeRaw.forEach(this::init);
        beforeInitRecipeRaw.clear();
    }

    @Override
    public String getType() {
        return "manager";
    }

    @Override
    public ItemStack getOutput(List<ItemStack> itemStacks) {
        for (IRecipeData value : recipeDataMap.values()) {
            if (value.isMatch(itemStacks)) return value.getOutput(itemStacks);
        }
        return null;
    }

    @Override
    public boolean isMatch(List<ItemStack> itemStacks) {
        return recipeDataMap.values().stream().anyMatch(iRecipeData -> iRecipeData.isMatch(itemStacks));
    }
}
