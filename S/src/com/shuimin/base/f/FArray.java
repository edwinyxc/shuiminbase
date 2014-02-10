package com.shuimin.base.f;

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

	@SuppressWarnings("unchecked")
	public FArray(FIterable _for) {
		super();
		@SuppressWarnings("rawtypes")
		final Iterator it = _for.val().iterator();
		while (it.hasNext()) {
			this.add((T) it.next());
		}
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
	
	public String join(String sep){
		final StringBuilder sb = new StringBuilder();
		for(T t:this){
			sb.append(t.toString()).append(sep);
		}
		return sb.toString();
	} 
}
