package org.eu.hanana.reimu.game.ottoca.game.data;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Item {
    public String type;
    public String icon;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item stack){
            return stack.type.equals(this.type);
        }else {
            return false;
        }
    }
    public ItemStack getStack(){
        return new ItemStack(this,1);
    }
}
