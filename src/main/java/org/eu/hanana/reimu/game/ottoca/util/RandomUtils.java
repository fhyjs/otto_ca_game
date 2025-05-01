package org.eu.hanana.reimu.game.ottoca.util;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class RandomUtils {
    /**
     * 根据元素概率权重随机选择一个元素
     * @param list 元素列表
     * @param probabilityGetter 获取元素概率的方法引用（取值范围建议0~1）
     * @param random 随机数生成器实例
     * @return 被选中的元素
     * @throws IllegalArgumentException 如果列表为空或总概率<=0
     */
    public static <T> T selectByProbability(List<T> list,
                                            Function<T, Double> probabilityGetter,
                                            Random random) {
        // 边界检查
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("列表不能为空");
        }

        // 计算总概率（考虑浮点精度）
        double total = list.stream()
                .mapToDouble(e -> {
                    double prob = probabilityGetter.apply(e);
                    if (prob < 0) {
                        throw new IllegalArgumentException("元素概率不能为负数: " + prob);
                    }
                    return prob;
                })
                .sum();

        if (total <= 1e-10) { // 处理浮点误差
            throw new IllegalArgumentException("总概率必须大于0");
        }

        // 生成随机数（0 <= randomValue < total）
        double randomValue = random.nextDouble() * total;

        // 遍历列表寻找命中区间
        double cumulative = 0.0;
        for (T element : list) {
            double prob = probabilityGetter.apply(element);
            if (prob == 0) continue; // 跳过概率为0的元素

            cumulative += prob;
            if (randomValue < cumulative) {
                return element;
            }
        }

        // 浮点精度补偿：返回最后一个有效元素
        return list.stream()
                .filter(e -> probabilityGetter.apply(e) > 0)
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalStateException("无有效概率元素"));
    }

    /**
     * 使用默认随机源（ThreadLocalRandom）的快捷方法
     */
    public static <T> T selectByProbability(List<T> list,
                                            Function<T, Double> probabilityGetter) {
        return selectByProbability(list, probabilityGetter, new Random());
    }
    /**
     * 生成 [min, max] 闭区间的随机整数
     * @param random Random实例（可自定义种子或类型）
     * @param a 区间端点1
     * @param b 区间端点2
     * @return 区间内的随机整数
     */
    public static int getRandomInt(Random random, int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 生成 [min, max) 左闭右开区间的随机整数
     * @param random Random实例
     * @param a 区间端点1
     * @param b 区间端点2
     * @return 区间内的随机整数
     */
    public static int getRandomIntOpenEnd(Random random, int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return random.nextInt(max - min) + min;
    }

    /**
     * 生成 [min, max) 左闭右开区间的随机浮点数
     * @param random Random实例
     * @param a 区间端点1
     * @param b 区间端点2
     * @return 区间内的随机浮点数
     */
    public static double getRandomDouble(Random random, double a, double b) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        return min + (max - min) * random.nextDouble();
    }

    /**
     * 生成 [min, max] 闭区间的随机浮点数（近似实现）
     * @param random Random实例
     * @param a 区间端点1
     * @param b 区间端点2
     * @param precision 小数精度位数
     * @return 区间内的随机浮点数
     */
    public static double getRandomDoubleClosed(Random random, double a, double b, int precision) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        double scaled = min + (max - min) * random.nextDouble();
        return Math.round(scaled * Math.pow(10, precision)) / Math.pow(10, precision);
    }
}