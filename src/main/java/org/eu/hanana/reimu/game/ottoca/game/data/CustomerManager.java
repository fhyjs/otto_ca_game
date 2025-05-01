package org.eu.hanana.reimu.game.ottoca.game.data;

import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.screen.GameScreen;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager implements ITickable {
    public final GameScreen gameScreen;
    public final List<WidthCustomer> customers = new ArrayList<>();
    public int addInterval = 20*3;
    public int currentInterval = 0;
    public CustomerManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentInterval=addInterval;
    }
    public void newRandomCustomer(){
        var customer = new WidthCustomer(this );
        customer.randomData();
        gameScreen.guiComponents.add(customer);
        customers.add(customer);
        for (int i = 0; i < customers.size(); i++) {
            var customerT = customers.get(i);
            var x = 0.02+i*0.35;
            customerT.setPos(x,0.26f).setSize(0.3,0.4);
        }
        if (gameScreen.getInputProcessor() instanceof IPWidthChooser widthChooser) {
            widthChooser.reLoad();
        }
    }
    public void removeCustomer(WidthCustomer widthCustomer){
        gameScreen.guiComponents.remove(widthCustomer);
        customers.remove(widthCustomer);
        if (gameScreen.getInputProcessor() instanceof IPWidthChooser widthChooser) {
            widthChooser.reLoad();
        }
    }
    @Override
    public void tick() {
        if (gameScreen.gameRunning){
            if (currentInterval<=0){
                currentInterval=addInterval;
                if (customers.size()<3) {
                    newRandomCustomer();
                }
            }else {
                currentInterval--;
            }
            var customers = new ArrayList<>(this.customers);
            for (WidthCustomer customer : customers) {
                customer.tick();
            }
        }
    }
}
