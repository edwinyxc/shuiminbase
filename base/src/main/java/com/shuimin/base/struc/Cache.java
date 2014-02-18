package com.shuimin.base.struc;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.shuimin.base.CB2;
import com.shuimin.base.F;
import com.shuimin.base.struc.cache.DefaultCache;
import com.shuimin.base.struc.cache.LRUCache;

public abstract class Cache<K, V> {
	/**
	 * <p>
	 * Get from cache, if not found, trigger doWhenNothing
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public abstract V get(K key);

	/**
	 * <p>
	 * Get from cache, if not found, trigger doWhenNothing
	 * </p>
	 * 
	 * @param key
	 * @param doWithVal
	 * @return
	 */
	public abstract V get(K key, F<V, V> doWhenEmpty);

	/**
	 * <p>
	 * Raw put
	 * </p>
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public abstract Cache<K, V> put(K key, V val);

	public abstract Cache<K, V> putAll(Map<K, V> m);

	public abstract Cache<K, V> remove(K key);

	public abstract Cache<K, V> removeAll(Iterable<K> key);

	public abstract ConcurrentMap<K, V> asMap();

	public abstract Cache<K, V> onNothingFound(F<V, K> nothingFoundLisener);

	public abstract Cache<K, V> onRemove(CB2<K, V> removeListener);

	public static <K, V> Cache<K, V> lruCache(int max) {
		return new LRUCache<K, V>(max);
	}

	public static <K, V> Cache<K, V> defaultCache(int max) {
		return new DefaultCache<>(max);
	}
}
