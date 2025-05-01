package org.eu.hanana.reimu.game.ottoca.game.customer;

import org.eu.hanana.reimu.game.ottoca.game.GameLoader;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.WidthCustomer;
import org.eu.hanana.reimu.thrunner.GameData;

import static org.eu.hanana.reimu.thrunner.GameData.JthrData.i18nManager;

public class CustomerDaoli extends CustomerBase{
    public CustomerDaoli(WidthCustomer widthCustomer) {
        super(widthCustomer);
    }

    @Override
    public void receiveItem(ItemStack stack) {
        super.receiveItem(stack);
        setMessage(null);
        if (isRequiredItem(stack)){
            removeRequiredItem(stack);
            GameLoader.BRIDGE.playSound("ottoca_assets:sounds/daoli_accepted.ogg");
        }else {
            setMessage(i18nManager.get("customer.daoli.no"));
            GameLoader.BRIDGE.playSound("ottoca_assets:sounds/daoli_no.ogg");
        }
        if (wc.requiredItems.isEmpty()){
            wc.currentLeftTime= (int) (0.05*wc.maxWaitingTime);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (wc.requiredItems.isEmpty()){
            GameLoader.BRIDGE.playSound("ottoca_assets:sounds/daoli_full.mp3");
        }else {

        }
    }
}
