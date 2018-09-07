package com.anan.commons.utils.retry;

/**
 * @author anan
 * @param <R> 返回值
 */
@FunctionalInterface
public interface RetryFunction<R> {

    /**
     * 执行的具体业务逻辑
     * @param params
     * @return
     */
    R process(Object... params);
}