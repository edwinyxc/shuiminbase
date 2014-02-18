package com.shuimin.base;

import java.util.Set;

public abstract class Result {
	
	@SuppressWarnings("unchecked")
	public static <T> Result of(Object o){
		
		if(o == null)
			return Result.<T>one(null);
		Class<?> c = o.getClass(); 
		if(c.isArray()){
			return Result.<T>many((T[])o);
		}
		if(o instanceof Iterable){
			return Result.<T>many((Iterable<T>)o);
		}
		return Result.<T>one((T)o);
	}

	final static public <T> One<T> instanceOf(Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		return new One<T>((T) clazz.newInstance());
	}

	final static public <T> One<T> one(T t) {
		return new One<T>(t);
	}

	final static public <L, R> Pair<L, R> pair(L l, R r) {
		return new Pair<L, R>(l, r);
	}

	final static public <T> Many<T> many(T[] t) {
		return new Many<T>(t);
	}

	final static public <T> Many<T> many(Iterable<T> t) {
		return new Many<T>(t);
	}

	protected final Object t;

	@SuppressWarnings("unchecked")
	final public <T> T get() {
		return (T) t;
	}

	protected Result(Object t) {
		this.t = t;
	}

	public abstract boolean isEmpty();

	final public boolean isNull() {
		return null == t;
	}

	final public boolean notNull() {
		return null != t;
	}

	public abstract <T> Set<T> asSet();

	// public static void main(String[] args) {
	// System.out.println(Result.<String> of("sdasd").asSet());
	// }

}
