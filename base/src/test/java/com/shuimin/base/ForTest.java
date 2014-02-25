package com.shuimin.base;

import static com.shuimin.base.S.echo;

import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import com.shuimin.base.S.function.Callback;
import com.shuimin.base.S.function.Function;

public class ForTest extends TestCase {
	public static void testArray() {
		S._for(new String[] { "a", "b", "c" })
				.<Integer> map(new Function<Integer, String>() {

					@Override
					public Integer f(String a) {
						return a.codePointAt(0);
					}

				}).grep(new Function<Boolean, Integer>() {

					@Override
					public Boolean f(Integer a) {
						return a < 100;
					}
				}).each(new Callback<Integer>() {

					@Override
					public void f(Integer t) {
						echo(t);
					}
				});

	}

	public static void testMap() {
		Map<String, Integer> map = S.map
				.<String, Integer> hashMap(new Object[][] { { "one", 1 },
						{ "two", 2 } });
		S._for(map).<String> map(new Function<String, Integer>() {

			@Override
			public String f(Integer a) {
				return "" + a;
			}

		}).grepByValue(new Function<Boolean, String>() {

			@Override
			public Boolean f(String a) {
				S.echo(a);
				return true;
			}
		}).each(new Callback<Entry<String,String>>() {

			@Override
			public void f(Entry<String, String> t) {
				S.echo(t.getKey());
			}
		});

	}

	public static void main(String[] args) {
		testMap();
	}

}
