package com.shuimin.base;

import java.util.Set;

final public class Pair<L,R> extends Result {
	
	protected Pair(L left,R right){
		super(new Object[]{left,right});
	}

	@Override
	public boolean isEmpty() {
		return ((Object[])t).length == 0;
	}

	@Override
	public  Set<Object> asSet() {
		return S.collection.set.hashSet(S.array.of((Object[])t));
	}
	
	@SuppressWarnings("unchecked")
	final public L left(){
		return (L)((Object[])t)[0];
	}
	
	@SuppressWarnings("unchecked")
	final public L right(){
		return (L)((Object[])t)[1];
	}

}
