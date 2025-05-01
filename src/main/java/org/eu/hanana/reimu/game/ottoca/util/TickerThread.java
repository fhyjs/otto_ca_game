package org.eu.hanana.reimu.game.ottoca.util;

import org.eu.hanana.reimu.game.ottoca.game.ITickable;
import org.eu.hanana.reimu.thrunner.GameData;

import java.util.ArrayList;
import java.util.List;

public class TickerThread extends Thread{
    public final List<ITickable> tickables = new ArrayList<>();
    public long duration = 1000/20;
    @Override
    public void run() {
        while (!isInterrupted()){
            GameData.gameRender.runnables.add(()->new ArrayList<>(tickables).forEach(ITickable::tick));
            try {
                sleep(duration);
            } catch (InterruptedException e) { }
        }
    }
}
