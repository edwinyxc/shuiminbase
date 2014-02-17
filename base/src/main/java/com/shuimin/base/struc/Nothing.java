package com.shuimin.base.struc;

import java.util.Iterator;

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
