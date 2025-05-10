package org.eu.hanana.reimu.game.ottoca.game.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.RuleBasedBreakIterator;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.data.Item;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.thrunner.GameUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeData implements IRecipeData{
    public static final String TYPE = "shaped";

    /**
     * U
     * 123
     * 456
     * 789
     * D
     */
    public List<Item> requiredItems=new ArrayList<>();
    public ItemStack output;

    public ShapedRecipeData(JsonObject jsonObject){
        Map<String,Item> patterns = new HashMap<>();
        var patternsJson = jsonObject.get("pattern").getAsJsonObject();
        for (String s : patternsJson.keySet()) {
            var val = patternsJson.get(s).getAsString();
            patterns.put(s,GameStorage.getItemByType(val));
        }
        var shape = jsonObject.get("shape").getAsJsonArray();
        for (JsonElement jsonElement : shape) {
            var patternLine = jsonElement.getAsString();
            var chars = GameUtils.splitToGraphemeClusters(patternLine,BreakIterator.getCharacterInstance());
            for (String aChar : chars) {
                requiredItems.add(patterns.get(aChar));
            }
        }
        JsonObject opt = jsonObject.get("output").getAsJsonObject();
        output=new ItemStack(opt.get("type").getAsString(),opt.get("amount").getAsInt());
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public ItemStack getOutput(List<ItemStack> itemStacks) {
        if(!isMatch(itemStacks)) return null;
        var cpy = new ArrayList<>(itemStacks);
        var amount =Integer.MAX_VALUE;
        for (ItemStack itemStack : cpy) {
            amount=Math.min(amount,itemStack.amount);
        }
        return output.clone().setAmount(amount);
    }

    @Override
    public boolean isMatch(List<ItemStack> itemStacks) {
        var input = new ArrayList<Item>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack==null||itemStack.isEmpty()) {
                input.add(null);
                continue;
            }
            input.add(itemStack.item);
        }
        return requiredItems.equals(input);
    }
}
