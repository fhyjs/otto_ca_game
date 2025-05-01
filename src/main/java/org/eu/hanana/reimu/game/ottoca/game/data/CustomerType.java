package org.eu.hanana.reimu.game.ottoca.game.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.eu.hanana.reimu.game.ottoca.game.customer.CustomerDaoli;
import org.eu.hanana.reimu.game.ottoca.game.customer.CustomerGunmu;
import org.eu.hanana.reimu.game.ottoca.game.customer.CustomerWaao;
import org.eu.hanana.reimu.game.ottoca.util.RandomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class CustomerType {
    private static List<CustomerType> values;
    public static final List<CustomerType> exValues = new ArrayList<>();
    public static final CustomerType DaoLi = new CustomerType("dao_li","images/customer/daoli.png",CustomerDaoli.class.getName(),0.4);
    public static final CustomerType WaAo = new CustomerType("wa_ao","images/customer/waao.png", CustomerWaao.class.getName(),0.3);
    public static final CustomerType GunMu = new CustomerType("gun_mu","images/customer/gunmu.png", CustomerGunmu.class.getName(),0.1);
    public static final CustomerType BigBlueEle = new CustomerType("big_blue_ele","animation:ottoca_assets:animations/bigblueele.json", CustomerDaoli.class.getName(),0.2);
    public final String type,texture,customerClass;
    @Getter
    public final double probability;
    public static List<CustomerType> getDefaultValues(){
        if (values!=null) return values;
        values=new ArrayList<>(exValues);
        Field[] fields = CustomerType.class.getFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType()!=CustomerType.class) continue;
            try {
                values.add((CustomerType) field.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomerType customerType){
            return this.type.equals(customerType.type);
        }
        return false;
    }

    public static CustomerType getRandom(Random random){
        return RandomUtils.selectByProbability(getDefaultValues(),CustomerType::getProbability,random);
    }
}
