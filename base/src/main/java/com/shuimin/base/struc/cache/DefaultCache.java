package com.shuimin.base.struc.cache;


public final class DefaultCache<K,V> extends AbstractCache<K, V>{

	public DefaultCache(int maxEntries) {
		super(new FixedSizeLinkedHashMap<K,V>(maxEntries));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected V _get(K key) {
		return (V) cache.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void _put(K key, V val) {
		cache.put(key, val);
	}

}
