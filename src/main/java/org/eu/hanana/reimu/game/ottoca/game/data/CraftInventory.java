package org.eu.hanana.reimu.game.ottoca.game.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.game.GameInstance;
import org.eu.hanana.reimu.game.ottoca.game.recipe.RecipeManager;

public class CraftInventory extends ListInventory{
    private static final Logger log = LogManager.getLogger(CraftInventory.class);
    public int outputSlotId = 9;
    public ItemStack lastOutput;
    @Override
    public void setStack(int id, ItemStack stack) {
        super.setStack(id, stack);
    }

    @Override
    public void update(int slotId) {
        super.update(slotId);
        if (getAllStack().size()<10) setStack(9,null);
        if (slotId==outputSlotId&&lastOutput!=null&& !lastOutput.isEmpty()){
            var take=lastOutput.amount-getStackInSlot(outputSlotId).amount;
            log.debug("Took {} items",take);
            for (int i = 0; i < 9; i++) {
                ItemStack stackInSlot = getStackInSlot(i);
                if (stackInSlot!=null&&!stackInSlot.isEmpty()) stackInSlot.shrink(take);
            }
            return;
        }
        RecipeManager recipeManager = GameInstance.recipeManager;
        if (recipeManager.isMatch(getAllStack().subList(0,9))) {
            var output = recipeManager.getOutput(getAllStack().subList(0,9));
            log.debug(output);
            setStack(outputSlotId,output);
            lastOutput=output.clone();
        }else {
            lastOutput=null;
            setStack(outputSlotId,null);
        }
    }
}
