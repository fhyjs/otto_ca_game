package org.eu.hanana.reimu.game.ottoca.core;

import org.eu.hanana.reimu.hnnapp.mods.CfgCoreBase;
import org.eu.hanana.reimu.hnnapp.mods.interal.LSCfgCore;

public class Config extends CfgCoreBase {
    @Override
    public void init() {
        addCfgClass(ConfigValues.class);
    }
    public static class ConfigValues{
        public static String musicType=".mid";
    }
}
