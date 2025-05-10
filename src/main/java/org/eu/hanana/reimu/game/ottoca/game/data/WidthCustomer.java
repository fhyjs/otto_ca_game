package org.eu.hanana.reimu.game.ottoca.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.Main;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.customer.CustomerBase;
import org.eu.hanana.reimu.game.ottoca.util.RandomUtils;
import org.eu.hanana.reimu.game.ottoca.util.Util;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.GameUtils;
import org.eu.hanana.reimu.thrunner.core.util.FontRenderResult;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;
import org.eu.hanana.reimu.thrunner.jthr.AnimationData;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.eu.hanana.reimu.thrunner.core.InputProcessor.mouseX;
import static org.eu.hanana.reimu.thrunner.core.InputProcessor.mouseY;

public class WidthCustomer extends WidthBase implements ITickable {
    private static final Logger log = LogManager.getLogger(WidthCustomer.class);
    private final CustomerManager cm;
    public List<ItemStack> requiredItems = new ArrayList<>();
    protected Vector2 requiredItemRenderArea = new Vector2();
    public int maxWaitingTime,currentLeftTime;
    public CustomerType customerType;
    private Random random = new Random();
    protected boolean mouseOver=false;
    public String type,texture;
    public AnimationData.AnimationObj animationObj;
    public Vector3 renderOffset = new Vector3();
    public Vector3 renderScale = new Vector3(1,1,1);
    public WidthCustomer(CustomerManager customerManager) {
        this.cm = customerManager;
    }
    public String message="";
    public CustomerBase customer;
    @Override
    public void onAction() {

    }
    public void randomData(){
        GameMode gameMode = GameStorage.CURRENT.gameMode;
        maxWaitingTime=20* RandomUtils.getRandomInt(random,gameMode.waitSecRange.getV1(),gameMode.waitSecRange.getV2());
        currentLeftTime=maxWaitingTime;
        customerType=CustomerType.getRandom(random);
        texture=customerType.texture;
        type=customerType.type;
        try {
            customer= (CustomerBase) Class.forName(customerType.customerClass,true,GameData.JthrData.groovyClassLoader).getConstructor(getClass()).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            currentLeftTime=-1;
            log.error(e);
        }
        if (texture.startsWith("animation:")){
            animationObj=GameData.JthrData.animationManager.getAnimation(texture.split("animation:")[1]).getAnimationObj();
            animationObj.getTexture(true);
        }
        var requiredAmount = RandomUtils.getRandomInt(random,gameMode.requiredItemAmountRange.getV1(),gameMode.requiredItemAmountRange.getV2());
        for (int i = 0; i < requiredAmount; i++) {
            requiredItems.add(RandomUtils.selectByProbability(GameStorage.getAllItems(), stack -> {
                if (GameStorage.getFirstInventory().contains(stack.getStack())){
                    if (!GameStorage.CURRENT.inventory.getAllStack().contains(stack.getStack())){
                        return 0.5;
                    }
                    return 0.3;
                }
                return 0.6;
            },random).getStack());
        }
    }
    @Override
    public void renderBackground(float delta) {
        var shapeRenderer = screen.shapeRenderer;
        var batch = screen.batch;

        shapeRenderer.getTransformMatrix().translate(renderOffset);
        batch.getTransformMatrix().translate(renderOffset);

        shapeRenderer.getTransformMatrix().scale(renderScale.x,renderScale.y,renderScale.z);
        batch.getTransformMatrix().scale(renderScale.x,renderScale.y,renderScale.z);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        shapeRenderer.setColor(Util.fromRGBA8888(0xf5f5f71e));
        shapeRenderer.rect(0,0,width,height);
        shapeRenderer.setColor(Util.fromRGBA8888(0x3ea39e46));
        shapeRenderer.rect(width*0.1f,height*0.1f,requiredItemRenderArea.x+width*0.1f,requiredItemRenderArea.y+height*0.1f);
        shapeRenderer.end();
        batch.begin();
        batch.draw(Util.safeTextureGetter(texture),0,0,width,height);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1,0.1f,0.15f,0.9f);
        shapeRenderer.rect(width*0.1f,height,width*0.8f,height*0.1f);
        shapeRenderer.setColor(0.3f,1f,0.15f,0.9f);
        if (currentLeftTime>=0)
            shapeRenderer.rect(width*0.1f,height,width*0.8f*currentLeftTime/maxWaitingTime,height*0.1f);
        shapeRenderer.end();
    }

    @Override
    public void renderForeground(float delta) {
        mouseOver=mouseX > x&&mouseX<x+width&&mouseY>y&&mouseY<y+height;
        ShapeRenderer shapeRenderer = screen.shapeRenderer;
        var batch = screen.batch;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        shapeRenderer.setColor(Util.fromRGBA8888(0x3ea39e46));
        shapeRenderer.rect(width*0.1f,height*0.1f,requiredItemRenderArea.x+width*0.1f,requiredItemRenderArea.y+height*0.1f);
        shapeRenderer.end();

        batch.begin();
        var i = 0;
        requiredItemRenderArea = new Vector2();
        for (ItemStack requiredItem : requiredItems) {
            if (requiredItem==null) continue;
            var tex = Util.safeTextureGetter(requiredItem.item.icon);
            requiredItemRenderArea.x= Math.max(requiredItemRenderArea.x,width*(0.1f+i%5*0.15f));
            requiredItemRenderArea.y= Math.max(requiredItemRenderArea.y,height*(0.1f+i/5*0.15f));
            batch.draw(tex,width*(0.1f+i%5*0.15f),height*(0.1f+i/5*0.15f),width*0.13f,height*0.13f);
            i++;
        }
        //render Message
        screen.gameRender.font.drawString(batch, message, width * 0.05f, height * 0.7f, Color.GREEN,width/153,height/172);
        batch.end();

        shapeRenderer.getTransformMatrix().scale(1/renderScale.x,1/renderScale.y,1/renderScale.z);
        batch.getTransformMatrix().scale(1/renderScale.x,1/renderScale.y,1/renderScale.z);

        shapeRenderer.getTransformMatrix().translate(-renderOffset.x,-renderOffset.y,-renderOffset.z);
        batch.getTransformMatrix().translate(-renderOffset.x,-renderOffset.y,-renderOffset.z);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (mouseOver){
            if (cm.gameScreen.mouseSlot.isEmpty()) return false;
            var mouseItem = cm.gameScreen.mouseSlot.getItem();
            customer.receiveItem(mouseItem.shrink(1));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void tick() {
        currentLeftTime--;
        if (animationObj!=null){
            animationObj.getTexture(true);
            texture=animationObj.lastImage;
        }
        if (currentLeftTime<=0){
            this.remove();
        } else if ((double) currentLeftTime /maxWaitingTime<0.05&& renderOffset.x>-1) {
            renderOffset.x=-5;
        }
        if (renderOffset.x>1){
            renderOffset.x/=1.1f;
        } else if (renderOffset.x<-1){
            renderOffset.x-=width*0.1f;
        }
        customer.tick();
    }

    private void remove() {
        cm.removeCustomer(this);
        customer.remove();
    }

    @Override
    public WidthBase setSize(float w, float h) {
        renderOffset.x=w;
        return super.setSize(w, h);
    }
}
