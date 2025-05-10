package org.eu.hanana.reimu.game.ottoca.game;

import org.eu.hanana.reimu.game.ottoca.game.recipe.RecipeManager;
import org.eu.hanana.reimu.game.ottoca.util.TickerThread;

public class GameInstance {
    public static RecipeManager recipeManager;
    public static TickerThread TICKER_THREAD = new TickerThread();
}
