package org.eu.hanana.reimu.game.ottoca.util;

import org.eu.hanana.reimu.game.ottoca.game.GameInstance;
import org.eu.hanana.reimu.thrunner.core.util.ConsoleCommands;

public class CConsoleCommands extends ConsoleCommands {
    public void tick(boolean enable){
        GameInstance.TICKER_THREAD.running=enable;
    }
}
