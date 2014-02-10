package com.shuimin.base.f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class For implements FIterable {
	private final Iterable iterable;

	public For(Iterable iterable) {
		this.iterable = iterable;
	}

	public For(Map map) {
		this.iterable = map.entrySet();
	}

	public For(Object[] objs) {
		final ArrayList a = new ArrayList();
		for (Object o : objs) {
			a.add(o);
		}
		this.iterable = a;
	}

	@Override
	public FIterable map(F mapFunc) {
		Class<?> retClass = iterable.getClass();
		try {
			if (Map.Entry.class.isAssignableFrom(retClass)) {
				final Map newm = (Map) retClass.newInstance();
				for (Object entry : iterable) {
					newm.put(((Entry) entry).getKey(),
							mapFunc.f(((Entry) entry).getValue()));
				}
				return new For(newm);
			} else if (Collection.class.isAssignableFrom(retClass)) {
				final Collection newc = (Collection) retClass.newInstance();
				for (Object t : (Collection) iterable) {
					newc.add(mapFunc.f(t));
				}
				return new For(newc);
			} else
			/* default convert to arrayList */
			{
				List arrList = new ArrayList();
				final Iterator iter = iterable.iterator();
				while (iter.hasNext()) {
					arrList.add(iter.next());
				}
				return new For(arrList);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void each(CB eachFunc) {
		final Iterator iter = iterable.iterator();
		while (iter.hasNext()) {
			eachFunc.f(iter.next());
		}
	}

	@Override
	public FIterable grep(F grepFunc) {
		Class<?> retClass = iterable.getClass();
		try {
			if (Map.Entry.class.isAssignableFrom(retClass)) {
				final Map newm = (Map) retClass.newInstance();
				for (Object entry : iterable) {
					final Object v = ((Entry) entry).getValue();
					if ((Boolean) grepFunc.f(v)){
						newm.put(((Entry) entry).getKey(),v);
					}
				}
				return new For(newm);
			} else if (Collection.class.isAssignableFrom(retClass)) {
				final Collection newc = (Collection) retClass.newInstance();
				for (Object t : (Collection) iterable) {
					if ((Boolean) grepFunc.f(t)) {
						newc.add(t);
					}
				}
				return new For(newc);
			} else
			/* default convert to arrayList */
			{
				List arrList = new ArrayList();
				final Iterator iter = iterable.iterator();
				while (iter.hasNext()) {
					final Object v = iter.next();
					if ((Boolean) grepFunc.f(v)) {
						arrList.add(v);
					}
				}
				return new For(arrList);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Iterable val() {
		return this.iterable;
	}

	// public static void main(String[] args) {
	// new For<String>(new String[] { "aaa", "bbb", "sddd", "ccc" })
	// .map(new F<Integer,String>(){
	// @Override
	// public Integer f(String a) {
	// return 123;
	// }
	// })
	// .map(new F<Integer,Integer>(){
	//
	// @Override
	// public Integer f(Integer a) {
	// return a+1;
	// }
	//
	// })
	// .each(new CB<Integer>() {
	// @Override
	// public void f(Integer t) {
	// System.out.println("," + t);
	// }
	// });
	// }

}
