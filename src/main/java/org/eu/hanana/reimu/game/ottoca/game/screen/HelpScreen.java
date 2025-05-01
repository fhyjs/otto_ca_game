package org.eu.hanana.reimu.game.ottoca.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import org.eu.hanana.reimu.game.ottoca.game.MainTitle;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;
import org.eu.hanana.reimu.thrunner.core.screen.IHasInputProc;
import org.eu.hanana.reimu.thrunner.core.screen.IHasName;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;
import org.eu.hanana.reimu.thrunner.gui.WidthButton;
import org.eu.hanana.reimu.thrunner.gui.WidthScreenWindowV1;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Parameter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HelpScreen extends ScreenAdapterBase implements IHasName, IHasInputProc {
    private final ScreenAdapterBase parent;
    private final IPWidthChooser inputProcessor=new IPWidthChooser(this);
    public HelpScreen(ScreenAdapterBase parent) {
        super();
        this.parent=parent;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        guiComponents.clear();
        guiComponents.add(new WidthButton().setString("关闭").setId(0).setTextScale(1.8f,1.8f).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.9f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.1f));
        inputProcessor.reLoad();
    }

    @Override
    public void renderBackground(float delta) {
        GL11.glClearColor(0,1,0,1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onAction(int id) {
        super.onAction(id);
        if (id==0) {
            ArrayList<WidthBase> widthBases = new ArrayList<>(parent.guiComponents);
            var width= new AtomicReference<WidthScreenWindowV1>();
            for (WidthBase widthBase : widthBases) {
                if (widthBase instanceof WidthScreenWindowV1 widthScreenWindowV1){
                    if (widthScreenWindowV1.getWindow()==this){
                        width.set(widthScreenWindowV1);
                        break;
                    }
                }
            }
            GameData.gameRender.runnables.add(()->{
                parent.guiComponents.remove(width.get());
                if (parent instanceof IHasInputProc inputProc){
                    if (inputProc.getInputProcessor() instanceof IPWidthChooser widthChooser){
                        widthChooser.reLoad();
                    }
                }
            });
        }
    }

    @Override
    public void renderForeground(float delta) {
        var batch = this.batch;
        batch.begin();
        //batch.draw(GameData.textureManager.getTexture("images/background_title.png"),0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public String getName() {
        return "游戏说明";
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
