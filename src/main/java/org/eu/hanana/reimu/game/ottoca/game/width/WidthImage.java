package org.eu.hanana.reimu.game.ottoca.game.width;

import com.badlogic.gdx.graphics.Texture;
import org.eu.hanana.reimu.thrunner.gui.WidthBase;

public class WidthImage extends WidthBase {
    private Texture texture;

    public WidthImage setImage(Texture texture){
        this.texture=texture;
        return this;
    }
    @Override
    public void onAction() {

    }

    @Override
    public void renderBackground(float delta) {
        if (texture==null) return;
        screen.batch.begin();
        screen.batch.draw(texture,0,0,width,height);
        screen.batch.end();
    }

    @Override
    public void renderForeground(float delta) {

    }
}
