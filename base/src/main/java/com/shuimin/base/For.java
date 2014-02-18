package com.shuimin.base;

import java.util.Map;

public final class For{
	
	protected final static <E> ForIt<E> _(Iterable<E> c) {
		return new ForIt<E>(c);
	}

	protected final static <E> ForIt<E> _(E[] c) {
		return new ForIt<E>(c);
	}

	protected final static <K, V> ForMap<K, V> _(Map<K, V> c) {
		return new ForMap<K, V>(c);
	}

}
