package org.eu.hanana.reimu.game.ottoca.game.customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import org.eu.hanana.reimu.game.ottoca.game.GameLoader;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.customer.data.EnumDefaultVoiceType;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.WidthCustomer;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthImage;
import org.eu.hanana.reimu.game.ottoca.util.Util;
import org.eu.hanana.reimu.thrunner.GameData;

import static org.eu.hanana.reimu.thrunner.GameData.JthrData.i18nManager;

public class CustomerWaao extends CustomerBase{
    public WidthImage image;
    public int imageTick;
    public int maxImageTick=20;

    public CustomerWaao(WidthCustomer widthCustomer) {
        super(widthCustomer);
    }

    @Override
    public void receiveItem(ItemStack stack) {
        super.receiveItem(stack);
        setMessage(null);
        if (isRequiredItem(stack)){
            removeRequiredItem(stack);
            //GameLoader.BRIDGE.playSound("ottoca_assets:sounds/daoli_accepted.ogg");
            //autoPlayVoice(EnumDefaultVoiceType.YES);
            GameStorage.CURRENT.score+=25;
        }else {
            setMessage(i18nManager.get("customer.waao.no"));
            //GameLoader.BRIDGE.playSound("ottoca_assets:sounds/waao.mp3");
            autoPlayVoice(EnumDefaultVoiceType.NO);
            newEffect();
            GameStorage.CURRENT.score-=10;
        }
        if (wc.requiredItems.isEmpty()){
            wc.currentLeftTime= (int) (0.05*wc.maxWaitingTime);
        }
    }
    public void newEffect(){
        if (image!=null&&wc.screen.guiComponentsRenderOnly.contains(image)){
            return;
        }
        imageTick=0;
        image=new WidthImage().setImage(Util.safeTextureGetter("images/customer/waao.png"));
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
        if (wc.requiredItems.isEmpty()){
            GameLoader.BRIDGE.playSound("ottoca_assets:sounds/dao_li_success.mp3");
            //autoPlayVoice(EnumDefaultVoiceType.SUCCESS);
            GameStorage.CURRENT.score+=10;
        }else {
            GameStorage.CURRENT.score-=15;
            //GameLoader.BRIDGE.playSound("ottoca_assets:sounds/waao.mp3");
            autoPlayVoice(EnumDefaultVoiceType.FAIL);

        }
    }
}
