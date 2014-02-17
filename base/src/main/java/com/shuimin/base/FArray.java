package com.shuimin.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FArray<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7011047662253434408L;
	
	public FArray() {
		super();
	}

	public FArray(T[] a) {
		super();
		for (T t : a) {
			this.add(t);
		}
	}

	public FArray(List<T> a) {
		super(a);
	}

	public FArray(Iterable<T> iter) {
		super();
		final Iterator<T> it = iter.iterator();
		while (it.hasNext()) {
			this.add(it.next());
		}
	}

	public FArray(For<T> _for) {
		super();
		final Iterator<T> it = _for.<T>val().iterator();
		while (it.hasNext()) {
			this.add(it.next());
		}
	}

	public FArray(int i) {
		super(i);
	}

	public FArray<T> slice(int start, int end) {
		final FArray<T> ret = new FArray<T>();
		for (int i = start; i < end; i++) {
			ret.add(this.get(i));
		}
		return ret;
	}

	public FArray<T> slice(int start) {
		final FArray<T> ret = new FArray<T>();
		for (int i = start; i < size(); i++) {
			ret.add(this.get(i));
		}
		return ret;
	}

	public T reduceLeft(F2<T,T,T> reduceFunc) {
		T result = null;
		T next;
		for (int i = 0; i < this.size() - 1; i++) {
			result = this.get(i);
			next = this.get(i + 1);
			result = reduceFunc.f(result, next);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object reduceRight(F2 reduceFunc) {
		Object result = null;
		T next;
		for (int i = this.size() - 1; i >= 1; i--) {
			result = this.get(i);
			next = this.get(i - 1);
			result = reduceFunc.f(result, next);
		}
		return result;
	}

	public String join(String sep) {
		final StringBuilder sb = new StringBuilder();
		for (T t : this) {
			sb.append(t.toString()).append(sep);
		}
		return sb.toString();
	}
}
