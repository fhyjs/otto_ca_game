package org.eu.hanana.reimu.game.ottoca.game.customer;

import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.WidthCustomer;
import org.jetbrains.annotations.Nullable;

public abstract class CustomerBase implements ITickable {
    public final WidthCustomer wc;

    public CustomerBase(WidthCustomer widthCustomer){
        this.wc= widthCustomer;
    }
    public void receiveItem(ItemStack stack){

    }

    @Override
    public void tick() {

    }

    public void remove() {

    }
    public void setMessage(@Nullable String msg){
        if (msg==null) msg="";
        wc.message=msg;
    }
    public boolean isRequiredItem(ItemStack itemStack){
        return wc.requiredItems.contains(itemStack);
    }
    public void removeRequiredItem(ItemStack itemStack){
        wc.requiredItems.remove(itemStack);
    }
}
