package com.anan.commons.utils.retry;

import java.util.Objects;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;

/**
 * 工具
 *
 * @param <R>
 * @author anan
 */
public class Retry<R> {

    Retry() {
    }

    public <R> R process(Config<R> policy, RetryFunction<R> function, Object... params) throws Throwable {
        if (Objects.isNull(policy)) {
            policy = Config.DEFAULT;
        }

        R result = null;
        Throwable throwable = null;
        try {
            result = function.process(params);
        } catch (Throwable t) {
            throwable = t;
        } finally {
            boolean needRetry = isNeedRetry(policy, throwable, result);
            if (needRetry) {
                sleep(policy);
                int nextCount = policy.getCount() - 1;
                int initPeroid = Math.min(policy.getInitPeroid() * 2, policy.getMaxPeroid());
                Config<R> config = policy.deepClone();
                config.setInitPeroid(initPeroid);
                config.setCount(nextCount);
                return process(config, function, params);
            } else if (Objects.nonNull(throwable)) {
                throw throwable;
            }

            return result;
        }
    }

    @SneakyThrows
    private static <R> void sleep(Config<R> policy) {
        policy.getTimeUnit().sleep(policy.getInitPeroid());
    }

    private static <R> boolean isNeedRetry(Config<R> policy, Throwable throwable, R result) {
        if (policy.getCount() <= 0) {
            return false;
        }

        if (Objects.nonNull(throwable) && policy.getExceptionList().stream()
            .filter(t -> t.equals(throwable.getClass()) || t.isAssignableFrom(throwable.getClass())).count() > 0) {
            return true;
        }
        if (Objects.nonNull(policy.getResultFunction()) && BooleanUtils.isFalse(policy.getResultFunction().apply(result))) {
            return true;
        }
        return false;
    }
}




