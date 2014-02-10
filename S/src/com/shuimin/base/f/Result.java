package com.shuimin.base.f;

public final class Result<T> {
	private final F0<T> _function;

	public Result(F0<T> f) {
		this._function = f;
	}
	
	public final T get(){
		return _function.f();
	}
}
