package org.eu.hanana.reimu.game.ottoca.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.core.util.EmptyBatch;

public class Util {
    public static final Batch V_BATCH = new EmptyBatch();
    public static Color fromRGBA8888(int val){
        Color color = new Color();
        Color.rgba8888ToColor(color,val);
        return color;
    }
    public static Texture safeTextureGetter(String name){
        var tex = GameData.textureManager.getTexture(name);
        if (tex==null)
            tex=GameData.textureManager.getTexture("images/empty.png");
        return tex;
    }
}
