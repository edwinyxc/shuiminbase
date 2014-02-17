package com.shuimin.base;

import junit.framework.Test;
import static com.shuimin.base.S.echo;
import static com.shuimin.base.S.For;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {

		For(new String[] { "a", "b", "c" })
				.<Integer> map(new F<Integer, String>() {

					@Override
					public Integer f(String a) {
						return a.codePointAt(0);
					}

				}).grep(new F<Boolean, Integer>() {

					@Override
					public Boolean f(Integer a) {
						return a < 100;
					}
				}).each(new CB<Integer>() {

					@Override
					public void f(Integer t) {
						echo(t);
					}

				});

	}
}
