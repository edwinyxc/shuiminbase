package com.shuimin.base;

import java.util.Iterator;
import java.util.Set;

final public class Many<T> extends Result implements Iterable<T> {
	
	protected Many(Iterable<T> t) {
		super(new FArray<T>(t));
	}

	protected Many(T[] t) {
		super(new FArray<T>(t));
	}

	final public FArray<T> asArray() {
		return super.<FArray<T>> get();
	};

	@Override
	public boolean isEmpty() {
		return asArray().size() == 0;
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <T> Set<T> asSet() {
		return S.collection.set.<T> hashSet((Iterable<T>) this);
	}

	@Override
	public Iterator<T> iterator() {
		return super.<FArray<T>> get().iterator();
	}

	public static void main(String[] a) {
		S.echo(new Many<String>(new String[] { "1", "2", "3" }).asArray());
	}
}
