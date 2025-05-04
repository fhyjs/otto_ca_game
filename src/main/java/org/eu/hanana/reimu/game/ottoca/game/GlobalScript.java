package org.eu.hanana.reimu.game.ottoca.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.core.Config;

import static org.eu.hanana.reimu.game.ottoca.game.GameLoader.BRIDGE;

public class GlobalScript {
    protected static final Logger log = LogManager.getLogger(GlobalScript.class);

    public void onChangeScreen(String name){
        log.info(name);
        var fileType= Config.ConfigValues.musicType;
        if ("title".equals(name)){
            BRIDGE.setMusic(null);
            BRIDGE.setMusic("ottoca_assets:sounds/title_bgm"+fileType);
        }
    }
}
