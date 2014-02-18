package com.shuimin.base;

import java.util.Iterator;

import com.shuimin.base.struc.NullIterator;

@SuppressWarnings("rawtypes")
public final class Nothing implements Iterable {

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	private final Iterator nut= new NullIterator();

	@Override
	public Iterator iterator() {
		return nut;
	}
	
}
