package com.shuimin.base.util.cui;

import com.shuimin.base.S;
import com.shuimin.base.f.F2;
import com.shuimin.base.f.FArray;
import com.shuimin.base.struc.Matrix;

public class RichLayout {

	public static class matrixHelper {

		
	}

	public static Rect horizontal(Rect... some) {
		int height = 0;
		int width = 0;
		for (int i = 0; i < some.length; i++) {
			Rect x = some[i];
			height = S.math.max(height, x.height);
			width += x.width;
		}

		int[][] out = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				out[i][j] = ' ';
			}
		}

		int colfix = 0;
		for (int i = 0; i < some.length; i++) {
			Rect x = some[i];
			for (int h = 0; h < x.height; h++) {
				int[] row = x.data.getRow(h);
				for (int _ = 0; _ < row.length; _++) {
					out[h][colfix + _] = (char) row[_];
				}
			}
			colfix += x.width;
		}
		return new Rect(new Matrix(out));
	}

	public static String mkStr(Rect r) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < r.height; i++) {
			for (int j = 0; j < r.width; j++) {
				char c = (char) r.data.get(i, j);
				sb.append(c);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		FArray<Integer> arr = new FArray<Integer>(new Integer[] { 1, 1, 2, 3,
				4, 2, 5 });
		final int max = (int) arr
				.reduceLeft(new F2<Integer, Integer, Integer>() {

					@Override
					public Integer f(Integer a, Integer b) {
						return S.math.max(a, b);
					}

				});
		S.echo(max);
		// System.out.println(RichLayout.horizontal(new Rect(new String[] {
		// "123123", "-----------", "sdsds" }), new Rect(new String[] {
		// "123123sdsd", "-----------", "sdsdas", "23123", "sdas" }),
		// new Rect(new String[] { "123123sdsd", "---xxxxsdsd",
		// "sdasd56^&*(" })));
	}
}
