package org.eu.hanana.reimu.game.ottoca.game.customer.data;

public enum EnumDefaultVoiceType {
    YES,
    NO,
    FAIL,
    SUCCESS;
    public String getName(){
        return this.name().toLowerCase();
    }
}
