package com.shuimin.base;

import static com.shuimin.base.S._maybeNull;

public class Test {
	static void test(){
	}
	public static void main(String[] s) {
		String a = null;
		char c = _maybeNull(a, String.class).charAt(0);
		System.out.println(c);
	}
}
