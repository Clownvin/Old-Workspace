package com.limeapi.lang;

public interface Conditional<T, U> {
    public boolean evaluate(T t, U u);
}
