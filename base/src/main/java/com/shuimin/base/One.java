package com.shuimin.base;

import java.lang.reflect.Method;
import java.util.Set;

final public class One<T> extends Result {
	protected One(T t) {
		super(t);
	}

	@Override
	public boolean isEmpty() {
		Class<?> clazz = t.getClass();
		Method m;
		try {
			m = clazz.getMethod("isEmpty");
			if (m != null)
				return (Boolean) m.invoke(null);
		} catch (Exception e) {
		}
		return isNull();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> Set<T> asSet() {
		return S.collection.set.<T> hashSet(S.array.<T> of(new Object[] { t }));
	}

}
