package org.eu.hanana.reimu.game.ottoca.game.hud;

import com.badlogic.gdx.Gdx;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;
import org.eu.hanana.reimu.game.ottoca.game.screen.GameScreen;
import org.eu.hanana.reimu.game.ottoca.game.width.WidthSlot;
import org.eu.hanana.reimu.thrunner.GameData;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TableHud extends HudBase {
    private final GameScreen gameScreen;
    public int invStartPos;
    public List<WidthSlot> invSlots= new ArrayList<>();
    public List<WidthSlot> tableSlots= new ArrayList<>();
    public TableHud(GameScreen gameScreen) {
        this.gameScreen=gameScreen;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        guiComponents.clear();
    }
    public void updateInvSlot(){
        for (int i = 0; i < 9; i++) {
            var slot = invSlots.get(i);
            slot.setSlotId(i + invStartPos);
        }
    }
    @Override
    public void renderBackground(float delta) {
        GL11.glClearColor(1,0,0,0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        var batch = this.batch;
        batch.begin();
        for (int i = 0; i < 4; i++) {
            batch.draw(GameData.textureManager.getTexture("images/birch_planks.png"),Gdx.graphics.getWidth()*i*0.25f,0, Gdx.graphics.getWidth()*0.25f,Gdx.graphics.getHeight());
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                batch.draw(GameData.textureManager.getTexture("images/crafting_table_top.png"),Gdx.graphics.getWidth()*(0.01f+0.08f*j),Gdx.graphics.getHeight()*(0.05f+0.3f*i), Gdx.graphics.getWidth()*0.08f,Gdx.graphics.getHeight()*0.3f);
            }
        }
        batch.end();
    }
    @Override
    public void renderForeground(float delta) {
        var batch = this.batch;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }
}
