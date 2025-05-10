package org.eu.hanana.reimu.game.ottoca.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.MainTitle;
import org.eu.hanana.reimu.game.ottoca.game.data.GameMode;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;
import org.eu.hanana.reimu.thrunner.core.screen.IHasInputProc;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.gui.WidthButton;
import org.eu.hanana.reimu.thrunner.gui.WidthTextLabel;
import org.lwjgl.opengl.GL11;

import static com.badlogic.gdx.Gdx.gl;

public class EndScreen extends ScreenAdapterBase implements IHasInputProc {
    private final IPWidthChooser inputProcessor=new IPWidthChooser(this);
    private final GameScreen gameScreen;

    public EndScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void renderBackground(float delta) {
        GL11.glClearColor(1,0,0,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        var batch = this.batch;
        batch.begin();
        batch.draw(GameData.textureManager.getTexture("images/background_end.png"),0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void renderForeground(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        guiComponents.clear();
        guiComponentsRenderOnly.clear();
        guiComponentsRenderOnly.add(new WidthTextLabel().setString("§l§4游戏结束").setPos(Gdx.graphics.getWidth()*0.24f, Gdx.graphics.getHeight()*0.8f).setSize(3,2));
        guiComponentsRenderOnly.add(new WidthTextLabel().setString(String.format("§l§2得分:%d/n愉悦送走:%d/n有问题:%d",GameStorage.CURRENT.score,GameStorage.CURRENT.customersSuccess,GameStorage.CURRENT.customersFail)).setPos(Gdx.graphics.getWidth()*0.35f, Gdx.graphics.getHeight()*0.7f).setSize(2,1.6));
        guiComponents.add(new WidthButton().setString("返回标题").setId(0).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.7f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.06f));
        inputProcessor.reLoad();
    }

    @Override
    public void onAction(int id) {
        super.onAction(id);
        if (id==0){
            gameRender.setScreen(new MainTitle());
        }
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
