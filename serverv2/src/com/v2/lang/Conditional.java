package com.v2.lang;

public interface Conditional<T, U> {
    public boolean evaluate(T t, U u);
}
