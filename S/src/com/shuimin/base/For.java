package com.shuimin.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({"unchecked","rawtypes"})
public class For<T> {
	private final Iterable<?> iterable;

	protected For(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	protected <K> For(Map<K, T> map) {
		this.iterable = map.entrySet();
	}

	protected For(T[] objs) {
		final ArrayList<T> a = new ArrayList<T>();
		for (T o : objs) {
			a.add(o);
		}
		this.iterable = a;
	}

	public <R> For<R> map(F<R, T> mapFunc) {
		Class<?> retClass = iterable.getClass();
		try {
			if (Map.Entry.class.isAssignableFrom(retClass)) {
				final Map<Object, R> newm = (Map<Object, R>) retClass
						.newInstance();
				for (Object entry : iterable) {
					newm.put(((Entry) entry).getKey(),
							mapFunc.f((T) ((Entry) entry).getValue()));
				}
				return new For<R>(newm);
			} else if (Collection.class.isAssignableFrom(retClass)) {
				final Collection<R> newc = (Collection<R>) retClass
						.newInstance();
				for (Object t : iterable) {
					newc.add(mapFunc.f((T) t));
				}
				return new For<R>(newc);
			} else
			/* default convert to arrayList */
			{
				List<R> arrList = new ArrayList<R>();
				final Iterator iter = iterable.iterator();
				while (iter.hasNext()) {
					arrList.add(mapFunc.f((T) iter.next()));
				}
				return new For<R>(arrList);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void each(CB<T> eachFunc) {
		final Iterator iter = iterable.iterator();
		while (iter.hasNext()) {
			eachFunc.f((T) iter.next());
		}
	}

	public For<T> grep(F<Boolean, T> grepFunc) {
		Class<?> retClass = iterable.getClass();
		try {
			if (Map.Entry.class.isAssignableFrom(retClass)) {
				final Map newm = (Map) retClass.newInstance();
				for (Object entry : iterable) {
					final T v = (T) ((Entry) entry).getValue();
					if (grepFunc.f(v)) {
						newm.put(((Entry) entry).getKey(), v);
					}
				}
				return new For<T>(newm);
			} else if (Collection.class.isAssignableFrom(retClass)) {
				final Collection<T> newc = (Collection<T>) retClass
						.newInstance();
				for (Object t : iterable) {
					if ((Boolean) grepFunc.f((T) t)) {
						newc.add((T) t);
					}
				}
				return new For<T>(newc);
			} else
			/* default convert to arrayList */
			{
				List<T> arrList = new ArrayList<T>();
				final Iterator iter = iterable.iterator();
				while (iter.hasNext()) {
					final T v = (T) iter.next();
					if ((Boolean) grepFunc.f(v)) {
						arrList.add(v);
					}
				}
				return new For<T>(arrList);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}

	public <R>Iterable<R> val() {
		return (Iterable<R>) this.iterable;
	}

}
