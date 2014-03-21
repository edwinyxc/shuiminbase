package com.shuimin.base.java8;

import com.shuimin.base.S;
import java.util.List;
import java.util.Map;

public class LambdaTest {

	public static void main(String[] args) {
		Map<Integer, String> testMap = S.map.hashMap(new Object[][]{{1, "one"}, {2, "two"}});
		S._for(testMap).<String>map((s) -> {
			return s + "_new";
		}).each((entry) -> {
			S.echo(entry.getValue());
		});

		for (int i = 0; i < 100; i++) {
			testMap.put(i, String.valueOf(i));
		}

		S._for(testMap).grep((entry) -> (entry.getKey() > 50)).each((t) -> {
			S.echo(t);
		});

		List<Integer> list = S.collection.list.arrayList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 23, 23, 2, 2, 3, 23, 22});

	}
}
