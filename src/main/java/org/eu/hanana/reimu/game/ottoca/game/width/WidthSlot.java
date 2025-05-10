package org.eu.hanana.reimu.game.ottoca.game.width;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector4;
import org.eu.hanana.reimu.game.ottoca.util.Util;
import org.eu.hanana.reimu.game.ottoca.game.data.IInventory;
import org.eu.hanana.reimu.game.ottoca.game.data.IMouseItemHolder;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IFontRender;
import org.eu.hanana.reimu.thrunner.core.util.FontRenderResult;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;

import static org.eu.hanana.reimu.thrunner.core.InputProcessor.mouseY;
import static org.eu.hanana.reimu.thrunner.core.InputProcessor.mouseX;

public class WidthSlot extends WidthBase {
    public int slotId;
    public IInventory inventory=null;
    protected boolean mouseOver=false;
    public boolean renderHoverEffect=true;
    public WidthSlot(IInventory inventory,int slotId){
        this.inventory=inventory;
        this.slotId=slotId;
    }
    @Override
    public void onAction() {

    }

    public WidthSlot setSlotId(int slotId) {
        this.slotId = slotId;
        return this;
    }

    public ItemStack getItem(){
        return inventory.getStackInSlot(slotId);
    }
    public WidthSlot setItem(ItemStack stack){
        inventory.setStack(slotId,stack);
        return this;
    }
    @Override
    public void renderBackground(float delta) {
        mouseOver=mouseX > x&&mouseX<x+width&&mouseY>y&&mouseY<y+height;
        var batch = screen.batch;
        batch.begin();
        if (!isEmpty()) {
            Texture texture = GameData.textureManager.getTexture(getItem().item.icon);
            if (texture != null)
                batch.draw(texture, 0, 0, width, height);
        }
        batch.end();
        var sr = screen.shapeRenderer;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (mouseOver&&renderHoverEffect) {
            // 配置预乘 Alpha 混合（适合半透明叠加）
            Gdx.gl.glEnable(GL30.GL_BLEND);

            sr.setColor(1, 1, 1, 0.5f);
            sr.rect(0, 0, width, height);
        }
        sr.end();
    }

    public WidthSlot setRenderHoverEffect(boolean renderHoverEffect) {
        this.renderHoverEffect = renderHoverEffect;
        return this;
    }

    @Override
    public void renderForeground(float delta) {
        if (isEmpty()) return;
        var batch = screen.batch;
        IFontRender font = screen.gameRender.font;
        batch.begin();
        var text= String.valueOf(getItem().amount);
        FontRenderResult renderResult = font.drawString(Util.V_BATCH, text, 0, 0, Color.LIME);
        // 计算缩放比例
        var pos = new Vector4(0,-height*0.1f,width*0.7f,height*0.5f);
        float scaleX = pos.z / renderResult.textSize().x;  // 目标宽度 / 原始宽度
        float scaleY = pos.w / renderResult.textSize().y; // 目标高度 / 原始高度
        pos.x=width-pos.z;
        font.drawString(batch,text,pos.x,pos.y, Color.FIREBRICK,scaleX,scaleY);
        batch.end();
    }
    public boolean isEmpty(){
        return this.getItem()==null||this.getItem().amount<=0;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!mouseOver) return false;
        var mouseSlot = (WidthSlot) null;
        if (screen instanceof IMouseItemHolder iMouseItemHolder){
            mouseSlot=iMouseItemHolder.getSlot();
        }else {
            return false;
        }
        if (mouseSlot.isEmpty()&&!this.isEmpty()){
            mouseSlot.setItem(this.getItem().shrink(screen.gameRender.inputProcessor.isDown(Input.Keys.SHIFT_LEFT)?this.getItem().amount:(button==0?1:this.getItem().amount/2)));
            inventory.update(slotId);
            return true;
        }
        if(!mouseSlot.isEmpty()&&mouseSlot.getItem().equals(this.getItem())&& !this.isEmpty()){
            ItemStack mouseItem = mouseSlot.getItem();
            if (button==0){
                mouseItem.amount+=screen.gameRender.inputProcessor.isDown(Input.Keys.SHIFT_LEFT)?this.getItem().amount:1;
                this.getItem().amount-=screen.gameRender.inputProcessor.isDown(Input.Keys.SHIFT_LEFT)?this.getItem().amount:1;
            }else {
                mouseItem.amount-=screen.gameRender.inputProcessor.isDown(Input.Keys.SHIFT_LEFT)?this.getItem().amount:1;
                this.getItem().amount+=screen.gameRender.inputProcessor.isDown(Input.Keys.SHIFT_LEFT)?this.getItem().amount:1;
            }
            inventory.update(slotId);
            return true;
        }
        if (this.isEmpty()&& !mouseSlot.isEmpty()){
            this.setItem(mouseSlot.getItem().shrink(button==0?mouseSlot.getItem().amount:1));
            //mouseSlot.setItem(null);
            inventory.update(slotId);
            return true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

}
