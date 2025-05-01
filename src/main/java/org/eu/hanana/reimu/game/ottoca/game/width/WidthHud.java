package org.eu.hanana.reimu.game.ottoca.game.width;

import com.badlogic.gdx.Gdx;
import org.eu.hanana.reimu.game.ottoca.game.hud.HudBase;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.gui.WidthScreenWindowV1;

public class WidthHud<T extends HudBase> extends WidthScreenWindowV1 {
    public WidthHud(){
        super(false);
        this.movable=false;
        this.resizable=false;
        this.renderWndTitle=false;
    }
    public WidthHud<T> setHud(T hud){
        super.setWindow(hud);
        return this;
    }
    @SuppressWarnings("unchecked")
    public T getHud(){
        return ((T) window);
    }

    @Override
    public void renderForeground(float delta) {
        getHud().realX=x;
        getHud().realY=y;
        getHud().realW=width;
        getHud().realH=height;
        super.renderForeground(delta);
    }
}
