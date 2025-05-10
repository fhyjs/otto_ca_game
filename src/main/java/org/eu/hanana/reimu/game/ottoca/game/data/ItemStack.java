package org.eu.hanana.reimu.game.ottoca.game.data;


import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eu.hanana.reimu.game.ottoca.game.GameStorage;

@AllArgsConstructor
public class ItemStack implements Cloneable{
    public Item item;
    public int amount;
    public ItemStack(String type,int amount){
        this.amount=amount;
        this.item= GameStorage.getItemByType(type);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack stack){
            if (!stack.isEmpty()&&this.isEmpty()) return false;
            if (stack.isEmpty()&&!this.isEmpty()) return false;
            return this.item.equals(stack.item);
        }else {
            return false;
        }
    }
    public ItemStack shrink(int amount){
        if (this.amount<amount) amount=this.amount;
        this.amount-=amount;
        return new ItemStack(item,amount);
    }
    public boolean isEmpty(){
        return item==null||this.amount<=0;
    }

    @Override
    public String toString() {
        return String.format("%dx%s",amount,item.type);
    }

    @Override
    public ItemStack clone() {
        try {
            return (ItemStack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}
