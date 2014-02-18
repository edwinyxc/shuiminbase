package com.shuimin.base;


import java.util.Collection;
import java.util.Iterator;


public final class ForIt<E>  {

		final Iterable<E> iterable;
		
		int size = -1;
		
		public ForIt(Collection<E> e) {
			iterable = e;
			size = e.size();
		}

		public ForIt(Iterable<E> e) {
			iterable = e;
		}

		public ForIt(E[] e) {
			iterable = S.<E> Array(e);
			size = e.length;
		}

		public <R> ForIt<R> map(final F<R, E> mapFunc) {
			final Class<?> itClass = iterable.getClass();
			final Collection<R> c;
			if (Collection.class.isAssignableFrom(itClass))
				c = S.<Collection<R>> _one(itClass);
			else
				c = S.<R> Array();
			each(new CB<E>(){

				@Override
				public void f(E e) {
					c.add(mapFunc.f(e));
				}
				
			});
			return new ForIt<R>(c);
		}

		public ForIt<E> each(CB<E> eachFunc) {
			int icnt = 0;
			for (E e : iterable) {
				icnt++;
				eachFunc.f(e);
			}
			this.size = icnt;
			return this;
		}

		public ForIt<E> grep(final F<Boolean, E> grepFunc) {
			final Class<?> itClass = iterable.getClass();
			final Collection<E> c;
			if (Collection.class.isAssignableFrom(itClass))
				c = S.<Collection<E>> _one(itClass);
			else
				c = S.<E> Array();
			each(new CB<E>(){

				@Override
				public void f(E e) {
					if (grepFunc.f(e))
						c.add(e);	
				}
				
			});
			return new ForIt<E>(c);
		}

		public E reduce(final F2<E, E, E> reduceLeft) {
			return S.<E> Array(iterable).reduceLeft(reduceLeft);
		}

		public Iterable<E> val() {
			return iterable;
		}
		
		public E[] join(){
			S._assert(size != -1," size = -1");
			Object[] arr = new Object[size];
			int cnt = 0;
			final Iterator<E> it = iterable.iterator();
			while(it.hasNext()){
				arr[cnt++] = it.next();
			}
			return S.array.<E>of(arr);
		}

	}
