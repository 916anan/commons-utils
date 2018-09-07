package com.anan.commons.utils.retry;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 重试的配置
 * @author anan
 * @param <R>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Config<R> {

    public static final Config DEFAULT = new Config();

    private int count = 3;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private int initPeroid = 3;
    private int maxPeroid = Integer.MAX_VALUE;
    private List<Class> exceptionList = Lists.newArrayList();
    private Function<R, Boolean> resultFunction;

    public Config<R> deepClone() {
        Config<R> config = new Config<>();
        config.setCount(this.getCount());
        config.setTimeUnit(this.getTimeUnit());
        config.setInitPeroid(this.getInitPeroid());
        config.setMaxPeroid(this.getMaxPeroid());
        config.setExceptionList(this.getExceptionList());
        config.setResultFunction(this.getResultFunction());

        return config;
    }
}