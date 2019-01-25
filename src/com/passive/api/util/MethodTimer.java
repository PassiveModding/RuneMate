package com.passive.api.util;

import java.util.concurrent.Callable;

public class MethodTimer {
    public <T> T timeAndGet(Callable<T> callable) {
        T res = null;
        try {
            res = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
