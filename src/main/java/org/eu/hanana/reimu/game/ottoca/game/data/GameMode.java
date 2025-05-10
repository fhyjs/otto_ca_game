package org.eu.hanana.reimu.game.ottoca.game.data;

import groovy.lang.Tuple2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class GameMode {
    public static final GameMode Challenge = new GameMode("challenge",new Tuple2<>(20,26),new Tuple2<>(2,4),120*20);

    private static List<GameMode> values;
    public static List<GameMode> exValues = new ArrayList<>();
    public String type;
    public Tuple2<Integer,Integer> waitSecRange;
    public Tuple2<Integer,Integer> requiredItemAmountRange;
    public int maxTime;

    public GameMode(String type, Tuple2<Integer,Integer> waitSecRange, Tuple2<Integer,Integer> requiredItemAmountRange, int maxTime){
        this.type=type;
        this.waitSecRange=waitSecRange;
        this.requiredItemAmountRange=requiredItemAmountRange;
        this.maxTime = maxTime;
    }

    public String getTranslateKey(){
        return "oca.gamemode."+type;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameMode gameMode){
            return this.type.equals(gameMode.type);
        }
        return false;
    }
    public static List<GameMode> getDefaultValues(){
        if (values!=null) return values;
        values=new ArrayList<>(exValues);
        Field[] fields = GameMode.class.getFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType()!=GameMode.class) continue;
            try {
                values.add((GameMode) field.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return values;
    }
}
