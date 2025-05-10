package org.eu.hanana.reimu.game.ottoca.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.hanana.reimu.game.ottoca.Main;
import org.eu.hanana.reimu.game.ottoca.game.data.Item;
import org.eu.hanana.reimu.game.ottoca.game.recipe.RecipeManager;
import org.eu.hanana.reimu.game.ottoca.util.CConsoleCommands;
import org.eu.hanana.reimu.thrunner.GameData;
import org.eu.hanana.reimu.thrunner.ThMain;
import org.eu.hanana.reimu.thrunner.core.AudioManager;
import org.eu.hanana.reimu.thrunner.core.BaseGameConfig;
import org.eu.hanana.reimu.thrunner.core.IGameDataProcessor;
import org.eu.hanana.reimu.thrunner.core.groovy.Bridge;
import org.eu.hanana.reimu.thrunner.core.util.ConsoleCommands;
import org.eu.hanana.reimu.thrunner.core.util.assets.ResourceLocation;
import org.eu.hanana.reimu.thrunner.core.util.assets.VirtualFileHandle;
import org.eu.hanana.reimu.thrunner.jthr.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLoader implements IGameDataProcessor {
    private static final Logger log = ThMain.logger;
    public static Bridge BRIDGE;
    @Override
    public void process() throws IOException {
        if (GameInstance.TICKER_THREAD.getState()== Thread.State.NEW){
            GameInstance.TICKER_THREAD.start();
        }
        GameData.JthrData.groovyScript.setVariable("c",new CConsoleCommands());
        GameStorage.CURRENT=new GameStorage();
        GameData.baseGameConfig=new BaseGameConfig();
        GameData.baseGameConfig.menu=new BaseGameConfig.Menu();
        GameData.baseGameConfig.menu.entry=MainTitle.class.getName();
        GameData.JthrData.animationManager=new AnimationManager();
        GameData.JthrData.i18nManager=new I18nManager();
        GameData.audioManager=new AudioManager();
        GameData.JthrData.i18nManager=new I18nManager();
        GameData.JthrData.sounds=new Sounds();
        GameData.JthrData.sounds.data=new ArrayList<>();
        GameInstance.recipeManager=new RecipeManager();
        BRIDGE=new Bridge();
        GameData.audioManager.newAudio("pling", Gdx.files.classpath("assets/system/sound/pling.wav"));
        GameData.baseGameConfig.global_script="scripts/GlobalScript.groovy";
        var assetsList = GameData.assets.assets.getAllAssetNames();
        GameStorage.allItem.clear();
        for (ResourceLocation resourceLocation : assetsList) {
            if (resourceLocation.getPath().startsWith("images")){
                this.LoadImage(resourceLocation);
                log.info("LoadImage {}",resourceLocation);
            }else if (resourceLocation.getPath().startsWith("sounds")){
                GameData.audioManager.newAudio(resourceLocation.toString(),new VirtualFileHandle(resourceLocation));
                var data= new Sounds.Data();
                data.name=resourceLocation.toString();
                data.type="music";
                GameData.JthrData.sounds.data.add(data);
                log.info("LoadSound {}",resourceLocation);
            } else if (resourceLocation.getPath().startsWith("scripts")) {
                this.LoadScript(resourceLocation);
                log.info("LoadScript {}",resourceLocation);
            } else if (resourceLocation.getPath().startsWith("animations")) {
                this.LoadAnimation(resourceLocation);
                log.info("LoadAnimation {}",resourceLocation);
            } else if (resourceLocation.getPath().startsWith("items")) {
                this.LoadItem(resourceLocation);
                log.info("LoadItem {}",resourceLocation);
            } else if (resourceLocation.getPath().startsWith("langs")) {
                this.LoadLang(resourceLocation);
                log.info("LoadLang {}",resourceLocation);
            } else if (resourceLocation.getPath().startsWith("recipes")) {
                this.LoadRecipe(resourceLocation);
                log.info("LoadRecipe {}",resourceLocation);
            }

        }

        //after load
        GameStorage.CURRENT = new GameStorage();
        GameInstance.recipeManager.init();
        log.info("RecipeManager loaded {} entries.",GameInstance.recipeManager.recipeDataMap.size());
    }

    private void LoadRecipe(ResourceLocation resourceLocation) throws IOException {
        String s = new String(GameData.assets.assets.readAssetAsBytes(resourceLocation));
        GameInstance.recipeManager.loadRecipe(s,resourceLocation.toString());
    }

    private void LoadLang(ResourceLocation resourceLocation) throws IOException {
        String s = new String(GameData.assets.assets.readAssetAsBytes(resourceLocation));
        var lang = new File(resourceLocation.getPath()).getName().split("\\.")[0];
        GameData.JthrData.i18nManager.load(lang,s);
        if (BaseGameConfig.GameCfg.lang==null) {
            GameData.JthrData.i18nManager.setDefault();
            BaseGameConfig.GameCfg.lang=GameData.JthrData.i18nManager.getLang();
        }else {
            GameData.JthrData.i18nManager.setLang(BaseGameConfig.GameCfg.lang);
        }
    }

    private String LoadItem(ResourceLocation name) throws IOException {
        String s = new String(GameData.assets.assets.readAssetAsBytes(name));
        GameStorage.allItem.add(new Gson().fromJson(s, Item.class));
        return name.toString();
    }
    private String LoadScript(ResourceLocation name) throws IOException {
        String s = new String( GameData.assets.assets.readAssetAsBytes(name));
        GameData.JthrData.scriptManager.addScript(s, name.getPath());
        return name.toString();
    }
    private String LoadImage(ResourceLocation name) throws IOException {
        byte[] bytes = GameData.assets.assets.readAssetAsBytes(name);
        return GameData.textureManager.newTexture(name.getPath(),bytes);
    }
    private void LoadAnimation(ResourceLocation name) throws IOException {
        String s = new String( GameData.assets.assets.readAssetAsBytes(name));
        AnimationData animationData = new Gson().fromJson(s,AnimationData.class);
        for (int i = 0; i < animationData.images.size(); i++) {
            if (animationData.images.get(i)!=null)
                animationData.images.set(i,this.LoadImage(ResourceLocation.byDomainAndPath("ottoca_assets",animationData.images.get(i))));
        }
        GameData.JthrData.animationManager.addAnimation(name.toString(),animationData);
    }
}
