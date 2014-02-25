package com.shuimin.base.struc.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.shuimin.base.S;
import com.shuimin.base.S.function.Callback;
import com.shuimin.base.S.function.Callback2;
import com.shuimin.base.S.function.Function;
import com.shuimin.base.struc.Cache;

public abstract class AbstractCache<K, V> extends Cache<K, V> {

	@SuppressWarnings("rawtypes")
	protected final Map cache;

	protected Function<V, K> onNothingFound = new Function<V, K>() {
		public V f(K a) {
			return null;
		};
	};

	@Override
	public Cache<K, V> onNotFound(Function<V, K> nothingFoundLisener) {
		onNothingFound = S._notNull(nothingFoundLisener);
		return this;
	}

	protected Callback2<K, V> onRemove = new Callback2<K, V>() {
		@Override
		public void f(K k, V v) {
			// do nothing
		}
	};

	@Override
	public Cache<K, V> onRemove(Callback2<K, V> removeListener) {
		onRemove = S._notNull(removeListener);
		return this;
	}

	protected AbstractCache(Map<K, ?> a) {
		cache = a;
	}

	@Override
	public V get(K key) {
		synchronized (cache) {
			final V ret = _get(key);
			return ret == null ? onNothingFound.f(key) : ret;
		}
	}

	protected abstract V _get(K key);

	@Override
	public V get(K key, Function<V, V> doWithVal) {
		synchronized (cache) {
			return doWithVal.f(get(key));
		}
	}

	@Override
	public Cache<K, V> put(K key, V val) {
		synchronized (cache) {
			_put(key, val);
		}
		return this;
	}

	protected abstract void _put(K key, V val);

	@SuppressWarnings("unchecked")
	@Override
	public ConcurrentMap<K, V> asMap() {
		return new ConcurrentHashMap<K, V>(cache);
	}

	@Override
	public Cache<K, V> remove(K key) {
		synchronized (cache) {
			onRemove.f(key, _get(key));
			cache.remove(key);
			return this;
		}
	}

	@Override
	public Cache<K, V> putAll(Map<K, V> m) {
		synchronized (cache) {
			S._for(m).each(new Callback<Entry<K, V>>() {

				@Override
				public void f(Entry<K, V> t) {
					_put(t.getKey(), t.getValue());
				}

			});
		}
		return this;
	}

	@Override
	public Cache<K, V> removeAll(Iterable<K> keys) {
		synchronized (cache) {
			for (K k : keys) {
				this.remove(k);
			}
			return this;
		}
	}
}
