package com.rosey.cloud.rpc.agent;

import net.bytebuddy.matcher.ElementMatcher;

/**
 * ClassName: AbstractJunction <br/>
 * Description: <br/>
 * date: 2021/12/1 4:36 下午<br/>
 *
 * @author tooru<br />
 */
public abstract class AbstractJunction<V> implements ElementMatcher.Junction<V> {
    @Override
    public <U extends V> Junction<U> and(ElementMatcher<? super U> other) {
        return new Conjunction<U>(this, other);
    }

    @Override
    public <U extends V> Junction<U> or(ElementMatcher<? super U> other) {
        return new Disjunction<U>(this, other);
    }
}