package dev.jvmsleuths;

import net.bytebuddy.asm.Advice;

public class SluethAdvice {

    @Advice.OnMethodEnter
    public static void onEnter(@Advice.Origin("#t") String className) {
        System.out.println("loaded: " +className);
    }
}
