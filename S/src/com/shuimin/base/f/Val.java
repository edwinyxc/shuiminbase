package com.shuimin.base.f;

public final class Val<T> {
	
	final Object val;

	public Val(Object o) {
		this.val = o;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) val;
	}
	
}
