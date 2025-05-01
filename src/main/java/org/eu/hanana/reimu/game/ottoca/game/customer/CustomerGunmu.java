package org.eu.hanana.reimu.game.ottoca.game.customer;

import com.badlogic.gdx.Gdx;
import org.eu.hanana.reimu.game.ottoca.game.GameLoader;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.WidthCustomer;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthImage;
import org.eu.hanana.reimu.game.ottoca.util.Util;
import org.eu.hanana.reimu.thrunner.GameData;

import static org.eu.hanana.reimu.thrunner.GameData.JthrData.i18nManager;

public class CustomerGunmu extends CustomerBase{
    public WidthImage image;
    public int imageTick;
    public int maxImageTick=20;

    public CustomerGunmu(WidthCustomer widthCustomer) {
        super(widthCustomer);
    }

    @Override
    public void receiveItem(ItemStack stack) {
        super.receiveItem(stack);
        setMessage(null);
        newEffect();
        setMessage("§4§k????????\n??????????\n??????????\n???????");
        if (wc.requiredItems.isEmpty()){
            wc.currentLeftTime= (int) (0.05*wc.maxWaitingTime);
        }
    }
    public void newEffect(){
        if (image!=null&&wc.screen.guiComponentsRenderOnly.contains(image)){
            return;
        }
        imageTick=0;
        image=new WidthImage().setImage(Util.safeTextureGetter(wc.texture));
        GameData.gameRender.runnables.add(()->wc.screen.guiComponentsRenderOnly.add(image));
    }

    @Override
    public void tick() {
        super.tick();
        if (image!=null){
            imageTick++;
            image.setSize(Gdx.graphics.getWidth()*imageTick/(maxImageTick/3f), Gdx.graphics.getHeight()*imageTick/(maxImageTick/3f));
            image.setPos(-image.width/2f+Gdx.graphics.getWidth()/2f,-image.height/2+Gdx.graphics.getHeight()/2f);
            if (imageTick>maxImageTick){
                GameData.gameRender.runnables.add(()-> {
                    wc.screen.guiComponentsRenderOnly.remove(image);
                    image=null;
                });

            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (image!=null){
            GameData.gameRender.runnables.add(()-> {
                wc.screen.guiComponentsRenderOnly.remove(image);
                image=null;
            });
        }
    }
}
