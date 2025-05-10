package org.eu.hanana.reimu.game.ottoca.game.customer;

import com.ibm.icu.text.BreakIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.game.GameLoader;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.customer.data.EnumDefaultVoiceType;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.WidthCustomer;
import org.eu.hanana.reimu.thrunner.GameUtils;
import org.eu.hanana.reimu.thrunner.ThMain;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CustomerBase implements ITickable {
    public final WidthCustomer wc;


    public CustomerBase(WidthCustomer widthCustomer){
        this.wc= widthCustomer;
    }
    public void receiveItem(ItemStack stack){
        if (isRequiredItem(stack)){
            try {
                GameLoader.BRIDGE.playSound(String.format("ottoca_assets:sounds/item_%s.ogg",stack.item.type));
            } catch (Exception ignore) {
                ThMain.logger.debug("no sound for {}",stack.item.type);
            }
        }
    }

    @Override
    public void tick() {

    }

    public void remove() {
        if (wc.requiredItems.isEmpty()){
            GameStorage.CURRENT.customersSuccess++;
        }else {
            GameStorage.CURRENT.customersFail++;

        }
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

    public void autoPlayVoice(EnumDefaultVoiceType type){
        try {
            GameLoader.BRIDGE.playSound(String.format("ottoca_assets:sounds/%s_%s.ogg",wc.type.toLowerCase(),type.getName()));
        } catch (Exception e) {
            ThMain.logger.error("ERROR PLAY SOUND '"+type.getName()+"' ON '"+wc.type+"'",e);
            List<String> list = GameUtils.splitToGraphemeClusters(e.toString(), BreakIterator.getCharacterInstance());
            var sb = new StringBuilder("§4");
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i%15==0){
                    sb.append("/n");
                }
            }
            wc.message=sb.toString();
        }
    }
}
