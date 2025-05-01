package org.eu.hanana.reimu.game.ottoca.util;

import java.lang.StackWalker.StackFrame;
import java.util.Optional;

public class StackWalkerUtils {
    private static final StackWalker STACK_WALKER = 
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * 获取调用当前方法的直接调用者的类
     */
    public static Class<?> getCallerClass(int skip) {
        Optional<Class<?>> callerClass = STACK_WALKER.walk(frames -> 
            frames.skip(skip) // 跳过：0=walk方法自身，1=getCallerClass方法
                  .findFirst()
                  .map(StackFrame::getDeclaringClass)
        );
        return callerClass.orElseThrow(() -> 
            new RuntimeException("Caller class not found")
        );
    }

    public static void main(String[] args) {
        new CallerTest().test();
    }
}

class CallerTest {
    public void test() {
        Class<?> callerClass = StackWalkerUtils.getCallerClass(2);
        System.out.println("Caller class: " + callerClass.getName());
        // 输出: CallerTest
    }
}