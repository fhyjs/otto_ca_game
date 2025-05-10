package org.eu.hanana.reimu.game.ottoca;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.core.Config;
import org.eu.hanana.reimu.game.ottoca.core.OttoGameAssets;
import org.eu.hanana.reimu.game.ottoca.game.GameLoader;
import org.eu.hanana.reimu.game.ottoca.util.TickerThread;
import org.eu.hanana.reimu.hnnapp.ModLoader;
import org.eu.hanana.reimu.hnnapp.mods.Event;
import org.eu.hanana.reimu.hnnapp.mods.ModEntry;
import org.eu.hanana.reimu.hnnapp.mods.events.PostInitModsEvent;
import org.eu.hanana.reimu.thrunner.core.IGameDataProcessor;
import org.eu.hanana.reimu.thrunner.core.JThRCfgCore;
import org.eu.hanana.reimu.thrunner.core.util.assets.IAssets;
import org.eu.hanana.reimu.thrunner.core.util.assets.JthrAssets;
import org.eu.hanana.reimu.thrunner.core.util.assets.LayeredAssets;
import org.eu.hanana.reimu.thrunner.core.util.assets.ThmkAssets;
import org.eu.hanana.reimu.thrunner.events.AssetsEvent;
import org.eu.hanana.reimu.thrunner.events.GameEvent;
import org.eu.hanana.reimu.thrunner.jthr.JthrGame;
import org.eu.hanana.reimu.thrunner.thmk.ThmkGame;

import java.io.File;
import java.util.function.Supplier;

import static org.eu.hanana.reimu.game.ottoca.Main.MOD_ID;

@ModEntry(id = MOD_ID,name = MOD_ID)
public class Main {
    public static final String MOD_ID="ottoca";
    private static final Logger log = LogManager.getLogger(Main.class);

    public Main(){
        log.info("init");
        ModLoader.getLoader().regEventBuses(this);
    }
    @Event
    public void onPostInitModsEvent(PostInitModsEvent event) {
        ModLoader.getLoader().regCfgCore(MOD_ID,new Config());
    }
    @Event
    public void onAssetsInit(AssetsEvent.AssetsInitEvent event){
        //ottoca
        event.assets.registerProcessor(OttoGameAssets.class, GameLoader.class);
        event.assets.registerByFileProcessor(file -> {
            if (file.getName().equalsIgnoreCase(MOD_ID))
                return new OttoGameAssets();
            return null;
        });
        event.assets.getGameDataProcessorGetter().add(() -> {
            if (event.assets.assets instanceof LayeredAssets assets){
                for (IAssets asset : assets.assets) {
                    if (asset instanceof OttoGameAssets){
                        return new GameLoader();
                    }
                }
            }
            return null;
        });
    }
    @Event
    public void onAssetsInit(GameEvent.ChooseData event){
        event.getCancelDialog().set(true);
        event.getVfile().set(new File(MOD_ID));
    }
}