package com.nxin.framework.converter.bean;

import java.util.List;

public abstract class BeanConverter<T, S> {

    public abstract T convert(S s, String... ignores);

    public abstract List<T> convert(List<S> s, String... ignores);
}
