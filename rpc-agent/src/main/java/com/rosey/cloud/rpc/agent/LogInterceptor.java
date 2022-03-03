package com.rosey.cloud.rpc.agent;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * ClassName: LogInterceptor <br/>
 * Description: <br/>
 * date: 2021/12/1 4:39 下午<br/>
 *
 * @author tooru<br />
 */
public class LogInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        try {
            // 原有函数执行
            return callable.call();
        } finally {
            logger.info(method + ": took " + (System.currentTimeMillis() - start) + "ms");
        }
    }

}
