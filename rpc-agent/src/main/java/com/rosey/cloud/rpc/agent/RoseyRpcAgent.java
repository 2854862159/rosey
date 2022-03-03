package com.rosey.cloud.rpc.agent;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.DeclaredByType;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.NameMatcher;
import net.bytebuddy.matcher.StringMatcher;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.instrument.Instrumentation;
import java.util.Objects;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

/**
 * ClassName: RoseyRpcAgent <br/>
 * Description: <br/>
 * date: 2021/12/1 4:23 下午<br/>
 *
 * @author tooru<br />
 */
public class RoseyRpcAgent {

    private static final Logger logger = LoggerFactory.getLogger(RoseyRpcAgent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(false));

        AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy)

                .ignore(
                nameStartsWith("net.bytebuddy.")
                        .or(nameStartsWith("org.slf4j."))
                        .or(nameStartsWith("org.groovy."))
                        .or(nameContains("javassist"))
                        .or(nameContains(".asm."))
                        .or(nameContains(".reflectasm."))
                        .or(nameStartsWith("sun.reflect"))
                        .or(ElementMatchers.isSynthetic()))
                ;


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

        };


        ;

//        ElementMatcher.Junction classJudge = new AbstractJunction<MethodDescription>() {
//
//
//            @Override
//            public boolean matches(MethodDescription target) {
//                System.out.println(target.getName());
//                return false;
//            }
//        };


//        ElementMatcher.Junction jj = new NameMatcher<>(new StringMatcher("com.rosey.cloud.rpc.proxy", StringMatcher.Mode.STARTS_WITH));
//        ElementMatcher.Junction judge = new NameMatcher<MethodDescription>(new StringMatcher("invoke", StringMatcher.Mode.EQUALS_FULLY));
//
//        jj.and(classJudge);

        ElementMatcher.Junction judge = new AbstractJunction<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return target.getActualName().startsWith("com.rosey.cloud.rpc.proxy");
//                return nameMatchDefine.containsKey();
            }
        };

        ElementMatcher.Junction mm = new AbstractJunction<MethodDescription>(){

            @Override
            public boolean matches(MethodDescription target) {
                return Objects.equals(target.getName(), "invoke");
            }
        };

        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                return builder
                        .method(mm)// 拦截任意方法
                        .intercept(MethodDelegation.to(LogInterceptor.class)); // 委托
            }
        };



        agentBuilder
                .type(judge)
//                .type(ElementMatchers.nameStartsWith("com.rosey.cloud"))
                .transform(transformer)
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(listener)
                .installOn(instrumentation);
    }

}
