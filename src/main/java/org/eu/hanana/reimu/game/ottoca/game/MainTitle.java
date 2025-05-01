package org.eu.hanana.reimu.game.ottoca.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.google.gson.internal.JavaVersion;
import org.eu.hanana.reimu.game.ottoca.game.screen.GameScreen;
import org.eu.hanana.reimu.game.ottoca.game.screen.HelpScreen;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;
import org.eu.hanana.reimu.thrunner.core.screen.IHasInputProc;
import org.eu.hanana.reimu.thrunner.core.screen.IHasName;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.core.screen.SettingScreen;
import org.eu.hanana.reimu.thrunner.gui.WidthButton;
import org.eu.hanana.reimu.thrunner.gui.WidthScreenWindow;
import org.eu.hanana.reimu.thrunner.gui.WidthScreenWindowV1;
import org.eu.hanana.reimu.thrunner.gui.WidthTextLabel;
import org.eu.hanana.reimu.thrunner.window.JDialogMoreSettings;
import org.lwjgl.opengl.GL11;

import javax.swing.*;

public class MainTitle extends ScreenAdapterBase implements IHasInputProc, IHasName {
    private final IPWidthChooser inputProcessor=new IPWidthChooser(this);
    @Override
    public void show() {
        super.show();
        Gdx.graphics.setTitle("GameRender/LWJGL3/"+Gdx.graphics.getGLVersion().getType()+"/JAVA"+ JavaVersion.getMajorJavaVersion());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        guiComponents.clear();
        guiComponentsRenderOnly.clear();
        guiComponentsRenderOnly.add(new WidthTextLabel().setString("§l§4合成天下").setPos(Gdx.graphics.getWidth()*0.24f, Gdx.graphics.getHeight()*0.8f).setSize(3,2));
        guiComponents.add(new WidthButton().setString("开始游戏").setId(0).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.7f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.06f));
        guiComponents.add(new WidthButton().setString("说明").setId(3).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.6f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.06f));
        guiComponents.add(new WidthButton().setString("选项").setId(1).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.5f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.06f));
        guiComponents.add(new WidthButton().setString("退出").setId(2).setPos(Gdx.graphics.getWidth()*0.06f, Gdx.graphics.getHeight()*0.4f).setSize(Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.06f));
        inputProcessor.reLoad();
    }

    @Override
    public void onAction(int id) {
        super.onAction(id);
        if (id==1) {
            JDialogMoreSettings jDialogMoreSettings = new JDialogMoreSettings(null);
            jDialogMoreSettings.setVisible(true);
            while (jDialogMoreSettings.isVisible()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if (id==2) {
            new Thread(() -> {
                JOptionPane jOptionPane = new JOptionPane("确定退出吗?", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                JDialog dialog = jOptionPane.createDialog("Question");
                dialog.setVisible(true);
                dialog.setAlwaysOnTop(true);
                if (jOptionPane.getValue() != null)
                    if (((int) jOptionPane.getValue()) == 0) {
                        Gdx.app.exit();
                    }
            }).start();
        }else if (id==3) {
            guiComponents.add(new WidthScreenWindowV1(false).setWindow(new HelpScreen(this)).setPos(Gdx.graphics.getWidth()*0.16f, Gdx.graphics.getHeight()*0.2f).setSize(Gdx.graphics.getWidth()*0.7f, Gdx.graphics.getHeight()*0.6f));
            inputProcessor.reLoad();
        }else if (id==0) {
            GameStorage.CURRENT=new GameStorage();
            gameRender.setScreen(new GameScreen());
        }
    }
    @Override
    public void renderBackground(float delta) {
        GL11.glClearColor(1,0,0,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        var batch = this.batch;
        batch.begin();
        batch.draw(GameData.textureManager.getTexture("images/background_title.png"),0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void renderForeground(float delta) {
        var batch = this.batch;
        batch.begin();

        batch.end();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this.inputProcessor;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public String getName() {
        return "title";
    }
}
