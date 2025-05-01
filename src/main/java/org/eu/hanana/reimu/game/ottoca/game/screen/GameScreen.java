package org.eu.hanana.reimu.game.ottoca.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import org.eu.hanana.reimu.game.ottoca.Main;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.MainTitle;
import org.eu.hanana.reimu.game.ottoca.game.data.CustomerManager;
import org.eu.hanana.reimu.game.ottoca.game.data.IMouseItemHolder;
import org.eu.hanana.reimu.game.ottoca.game.data.ItemStack;
import org.eu.hanana.reimu.game.ottoca.game.data.ListInventory;
import org.eu.hanana.reimu.game.ottoca.game.hud.TableHud;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthHud;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthSlot;
import org.eu.hanana.reimu.game.ottoca.util.StackWalkerUtils;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;
import org.eu.hanana.reimu.thrunner.core.screen.IHasInputProc;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.core.screen.TitleScreen;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;
import org.eu.hanana.reimu.thrunner.gui.WidthButton;
import org.eu.hanana.reimu.thrunner.gui.WidthTextLabel;
import org.eu.hanana.reimu.thrunner.jthr.Item;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GameScreen extends ScreenAdapterBase implements IHasInputProc, IMouseItemHolder, ITickable {
    private final IPWidthChooser inputProcessor=new IPWidthChooser(this){
        @Override
        public boolean keyDown(int keycode) {
            return StackWalkerUtils.getCallerClass(2).equals(IPWidthChooser.class) && super.keyDown(keycode);
        }
    };
    public WidthSlot mouseSlot = new WidthSlot(new ListInventory(),0);
    public WidthHud<TableHud> tableHud;
    public boolean gameRunning = false;
    public CustomerManager customerManager;
    public void update(){
    }
    @Override
    public void renderBackground(float delta) {
        update();
        GL11.glClearColor(1,0,0,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        var batch = this.batch;
        batch.begin();
        batch.draw(GameData.textureManager.getTexture("images/background_game.png"),0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void show() {
        super.show();
        customerManager = new CustomerManager(this);
        mouseSlot.setItem(null);
        Main.TICKER_THREAD.tickables.add(this);
        guiComponents.clear();
        guiComponentsRenderOnly.clear();
        guiComponentsRenderOnly.add(mouseSlot.setRenderHoverEffect(false).setSize(0.06f, 0.06f));
        guiComponents.add(new WidthButton().setString("菜单").setId(0).setPos(0.88f, 0.92f).setSize(0.1f,0.06f));
        guiComponents.add(tableHud= (WidthHud<TableHud>) new WidthHud<>(){
            @Override
            public void selected() {
            }
        }.setHud(new TableHud(this)).setPos(0.05f,0.01).setSize(0.9f,0.25f));

        tableHud.getHud().invSlots.clear();
        tableHud.getHud().tableSlots.clear();
        for (int i = 0; i < 9; i++) {
            WidthBase widthBase = new WidthSlot(GameStorage.CURRENT.inventory, i).setSize(0.06f, 0.06f).setPos(0.6f + (0.07 * (i % 3)), 0.17f - (0.07 * (i / 3)));
            guiComponents.add(widthBase);
            tableHud.getHud().invSlots.add((WidthSlot) widthBase);
        }
        for (int i = 0; i < 9; i++) {
            WidthBase widthBase = new WidthSlot(GameStorage.CURRENT.craftInventory, i).setSize(0.06f, 0.06f).setPos(0.0652f + (0.072 * (i % 3)), 0.18f - (0.075 * (i / 3)));
            guiComponents.add(widthBase);
            tableHud.getHud().tableSlots.add((WidthSlot) widthBase);
        }

        tableHud.getHud().updateInvSlot();
        inputProcessor.reLoad();

        gameRunning=true;
    }

    @Override
    public void hide() {
        super.hide();
        Main.TICKER_THREAD.tickables.remove(this);
    }

    @Override
    public void onAction(int id) {
        if (id==0){
            gameRender.setScreen(new MainTitle());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (WidthBase guiComponent : guiComponents) {
            guiComponent.setSize(-1,-1);
            guiComponent.setPos(-1,-1);
        }
        for (WidthBase guiComponent : guiComponentsRenderOnly) {
            guiComponent.setSize(-1,-1);
            guiComponent.setPos(-1,-1);
        }
        inputProcessor.reLoad();
    }

    @Override
    public void renderForeground(float delta) {

    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenY = Gdx.graphics.getHeight() - screenY;
        mouseSlot.setPos(screenX,screenY);
        return tableHud.getHud().mouseMoved(screenX, screenY);
    }


    @Override
    public void setMouseItem(ItemStack item) {
        mouseSlot.setItem(item);
    }

    @Override
    public ItemStack getMouseItem() {
        return mouseSlot.getItem();
    }

    @Override
    public @NotNull WidthSlot getSlot() {
        return mouseSlot;
    }

    @Override
    public void tick() {
        customerManager.tick();
    }
}
