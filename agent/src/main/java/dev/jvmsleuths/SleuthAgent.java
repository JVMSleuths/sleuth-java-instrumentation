package dev.jvmsleuths;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class SleuthAgent {

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {

        new AgentBuilder.Default(new ByteBuddy())
                .ignore(
                        nameStartsWith("java.")
                                .or(nameStartsWith("jdk."))
                                .or(nameStartsWith("sun."))
                                .or(nameStartsWith("com.sun."))
                                .or(nameStartsWith("net.bytebuddy."))

                )
                .type(nameStartsWith("io.jvmsleuths"))
                .transform((builder, td, cl, jm, pd) ->
                        builder.visit(Advice.to(SluethAdvice.class)
                                .on(isMethod())))
                .installOn(inst);
    }
}
