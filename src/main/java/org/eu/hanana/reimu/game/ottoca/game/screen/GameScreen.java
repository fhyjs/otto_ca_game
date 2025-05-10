package org.eu.hanana.reimu.game.ottoca.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.eu.hanana.reimu.game.ottoca.Main;
import org.eu.hanana.reimu.game.ottoca.game.GameInstance;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.game.ottoca.game.MainTitle;
import org.eu.hanana.reimu.game.ottoca.game.data.*;
import org.eu.hanana.reimu.game.ottoca.game.hud.TableHud;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthHud;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthSlot;
import org.eu.hanana.reimu.game.ottoca.util.StackWalkerUtils;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.IPWidthChooser;
import org.eu.hanana.reimu.thrunner.core.screen.IHasInputProc;
import org.eu.hanana.reimu.thrunner.core.screen.IHasName;
import org.eu.hanana.reimu.thrunner.core.screen.ScreenAdapterBase;
import org.eu.hanana.reimu.thrunner.core.util.FontRenderResult;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;
import org.eu.hanana.reimu.thrunner.gui.WidthButton;
import org.eu.hanana.reimu.thrunner.gui.WidthTextLabel;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;

public class GameScreen extends ScreenAdapterBase implements IHasInputProc, IMouseItemHolder, ITickable, IHasName {
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
    public WidthTextLabel messageLabel = new WidthTextLabel();
    protected FontRenderResult messageResult=FontRenderResult.EMPTY;
    public GameScreen(){
        super();
        GameStorage.CURRENT.init();
    }
    public void update(){
    }
    @Override
    public void renderBackground(float delta) {
        update();
        GL11.glClearColor(1,0,0,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        var batch = this.batch;
        var render = this.shapeRenderer;
        batch.begin();
        batch.draw(GameData.textureManager.getTexture("images/background_game.png"),0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        render.begin(ShapeRenderer.ShapeType.Filled);
        gl.glEnable(GL11.GL_BLEND);
        render.setColor(0.1f,0.8f,0.2f,0.8f);
        render.rect(messageLabel.x, messageLabel.y,messageResult.textSize().x,messageResult.textSize().y);
        render.end();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void show() {
        super.show();
        customerManager = new CustomerManager(this);
        mouseSlot.setItem(null);
        GameInstance.TICKER_THREAD.tickables.add(this);
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
        {
            WidthBase widthBase = new WidthSlot(GameStorage.CURRENT.craftInventory, 9).setSize(0.06f, 0.06f).setPos(0.36f, 0.18f - (0.075 * (3 / 3)));
            guiComponents.add(widthBase);
            tableHud.getHud().tableSlots.add((WidthSlot) widthBase);
        }

        guiComponentsRenderOnly.add(messageLabel.setString("").setColor(Color.BLACK).setPos(0.2,0.9).setSize(1.1,1.1));

        tableHud.getHud().updateInvSlot();
        inputProcessor.reLoad();
        gameRunning=true;
    }
    @Override
    public void tick() {
        customerManager.tick();
        if (GameStorage.CURRENT.gameMode.equals(GameMode.Challenge)){
            // infinity resources
            List<ItemStack> allStack = new ArrayList<>(GameStorage.CURRENT.inventory.getAllStack());
            List<ItemStack> invCount = new ArrayList<>();
            for (ItemStack itemStack : allStack) {
                if (itemStack!=null&&!itemStack.isEmpty()&&GameStorage.getFirstInventory().contains(itemStack)){
                    itemStack.amount=100;
                }
                if (itemStack != null && itemStack.isEmpty()) {
                    GameStorage.CURRENT.inventory.getAllStack().remove(itemStack);
                }
                if (itemStack!=null) {
                    if (invCount.contains(itemStack)) {
                        if (GameStorage.getFirstInventory().contains(itemStack)){
                            GameStorage.CURRENT.inventory.getAllStack().remove(itemStack);
                        }
                    }else {
                        invCount.add(itemStack);
                    }
                }
            }
            for (ItemStack itemStack : GameStorage.getFirstInventory()) {
                if (!GameStorage.CURRENT.inventory.getAllStack().contains(itemStack)){
                    GameStorage.CURRENT.inventory.add(itemStack);
                }
            }
        }
        ArrayList<String> strings = addMessage(new ArrayList<String>());
        var sb = new StringBuilder();
        strings.forEach(sb::append);
        messageLabel.setString(sb.toString());
        messageResult=messageLabel.getResult();

        if (GameStorage.CURRENT.gameMode.maxTime>0){
            // limited time
            GameStorage.CURRENT.timeLeft--;
            if (GameStorage.CURRENT.timeLeft<=0){
                gameOver();
            }
        }
    }

    public void gameOver() {
        gameRender.setScreen(new EndScreen(this));
    }

    @Override
    public void hide() {
        super.hide();
        GameInstance.TICKER_THREAD.tickables.remove(this);
    }

    @Override
    public void onAction(int id) {
        if (id==0){
            gameRender.setScreen(new MainTitle());
        }
    }
    public ArrayList<String> addMessage(ArrayList<String> strings){
        strings.add("分数:");
        strings.add(String.valueOf(GameStorage.CURRENT.score));
        if (GameStorage.CURRENT.gameMode.maxTime>0){
            strings.add(",时间:");
            strings.add(String.valueOf(GameStorage.CURRENT.timeLeft/20));
        }
        return strings;
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
    public String getName() {
        return "game";
    }
}
