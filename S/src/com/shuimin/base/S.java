/**
 * Face of s-lib
 * @author ed
 *
 */
package com.shuimin.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.shuimin.base.f.F2;
import com.shuimin.base.f.FArray;
import com.shuimin.base.struc.Matrix;
import com.shuimin.base.util.cui.Rect;
import com.shuimin.base.util.logger.Logger;

public class S {
	/******************* logger ******************/
	private final static Logger logger = Logger.getDefault();

	public static final Logger logger() {
		return logger;
	}

	/******************* meta ******************/
	public static final String version() {
		return "v0.0.1 2014";
	}

	/******************** A ****************/
	public static class array {
		/**
		 * check if an array contains something
		 * 
		 * @param arr
		 *            array
		 * @param o
		 *            the thing ...
		 * @return -1 if not found or the first index of the object
		 */
		public static int contains(Object[] arr, Object o) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != null && arr[i].equals(o)) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * check if an array contains something
		 * 
		 * @param arr
		 *            array
		 * @param o
		 *            the thing ...
		 * @return -1 if not found or the first index of the object
		 */
		public static int contains(int[] arr, int o) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == o) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * put elements to the left.delete the null ones
		 * 
		 * @return
		 */
		public static Object[] shrink(Object[] arr) {
			int cur = 0;
			int next_val = 0;

			while (next_val < arr.length) {
				if (arr[cur] == null) {
					/* find_next_avaliable */
					for (; next_val < arr.length; next_val++) {
						if (arr[next_val] != null) {
							break;// get the value
						}
					}
					if (next_val >= arr.length) {
						break;
					}
					/* move it to the cur */
					arr[cur] = arr[next_val];
					arr[next_val] = null;
					cur++;
				} else {
					next_val++;
					cur++;
				}
			}
			Object[] ret;
			if (arr[0] != null) {
				Class<?> c = arr[0].getClass();
				ret = (Object[]) java.lang.reflect.Array.newInstance(c, cur);
			} else {
				ret = new Object[cur];
			}

			System.arraycopy(arr, 0, ret, 0, ret.length);
			return ret;
		}

		/**
		 * convert a list to array
		 * 
		 * @param clazz
		 * @param list
		 * @return
		 */
		public static Object fromList(Class<?> clazz, List<?> list) {
			Object array = java.lang.reflect.Array.newInstance(clazz,
					list.size());
			for (int i = 0; i < list.size(); i++) {
				java.lang.reflect.Array.set(array, i, list.get(i));
			}
			return array;
		}

		public static Object convertType(Class<?> clazz, Object[] arr) {
			Object array = java.lang.reflect.Array.newInstance(clazz,
					arr.length);
			for (int i = 0; i < arr.length; i++) {
				java.lang.reflect.Array.set(array, i, arr[i]);
			}
			return array;
		}

	}

	public static void _assert(boolean a, String err) {
		if (a)
			return;
		throw new RuntimeException(err);
	}

	/**
	 * assert
	 * 
	 * @param testee
	 * @param tester
	 * @param err
	 */
	public static void _assert(Object a, String err) {
		if (a == null) {
			throw new RuntimeException(err);
		}
	}

	/**
	 * assert
	 * 
	 * @param testee
	 * @param tester
	 * @param err
	 */
	public static void _assert(Object testee, Object tester, String err) {
		if (tester == null) {
			throw new RuntimeException("empty tester!!! YOU`VE MADE A MISTAKE");
		}

		if (testee == null || tester.equals(testee)) {
			throw new RuntimeException(err);
		}
	}

	/******************* B *********************/
	/******************* C *********************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class collection {
		public static class set {
			public static HashSet hashSet(Object[] arr) {
				final HashSet ret = new HashSet();
				for (Object t : arr) {
					ret.add(t);
				}
				return ret;
			}
		}

		public static class list {
			public ArrayList arrayList(Object[] arr) {
				final ArrayList ret = new ArrayList();
				for (Object t : arr) {
					ret.add(t);
				}
				return ret;
			}

			public LinkedList linkedList(Object[] arr) {
				final LinkedList ret = new LinkedList();
				for (Object t : arr) {
					ret.add(t);
				}
				return ret;
			}
		}
	}

	/******************* D *********************/
	public static class date {
		public static Date fromString(String aDate, String aFormat)
				throws ParseException {
			return new SimpleDateFormat(aFormat).parse(aDate);
		}

		public static Date fromLong(String aDate) {
			return new Date(parse.toLong(aDate));
		}

		public static String fromLong(String aDate, String aFormat) {
			return toString(new Date(parse.toLong(aDate)), aFormat);
		}

		public static String toString(Date aDate, String aFormat) {
			return new SimpleDateFormat(aFormat).format(aDate);
		}

		public static Long stringToLong(String aDate, String aFormat)
				throws ParseException {
			return fromString(aDate, aFormat).getTime();
		}
	}

	/******************* E *********************/

	public static void echo(Object o) {
		logger.echo(echots(o));
	}

	public static String echots(Object o) {
		StringBuilder ret = new StringBuilder();
		if (o.getClass().isPrimitive()) {
			ret.append(o);
		}
		if (o instanceof String) {
			ret.append((String) o);
		} else if (o instanceof List) {
			ret.append("[");
			for (int i = 0; i < ((List<?>) o).size(); i++) {
				ret.append(String.valueOf(((List<?>) o).get(i)));
				if (i != ((List<?>) o).size() - 1)
					ret.append(',');
			}
			ret.append("]");
		} else if (o.getClass().isArray()) {
			ret.append("[");
			for (int i = 0; i < java.lang.reflect.Array.getLength(o); i++) {
				ret.append(String.valueOf(java.lang.reflect.Array.get(o, i)));
				if (i != java.lang.reflect.Array.getLength(o) - 1)
					ret.append(',');
			}
			ret.append("]");
		} else {
			ret.append(o.toString());
		}
		return ret.toString();
	}

	/********************* F ***********************/
	public static class file {
		/**
		 * 
		 * abc.txt => [abc, txt] abc.def.txt => [abc.def, txt] abc. =>
		 * [abc.,null] .abc => [.abc,null] abc => [abc,null]
		 * 
		 * @param file
		 * @return
		 */
		public static String[] splitSuffixName(File file) {
			S._assert(file, "file null");
			String file_name = file.getName();
			int idx_dot = file_name.lastIndexOf('.');
			if (idx_dot <= 0 || idx_dot == file_name.length()) {
				return new String[] { file_name, null };
			}
			return new String[] { file_name.substring(0, idx_dot),
					file_name.substring(idx_dot + 1) };
		}

		public static File mkdir(File par, String name) throws IOException {
			final String path = par.getAbsolutePath() + File.separatorChar
					+ name;
			File f = new File(path);
			f.mkdirs();
			f.createNewFile();
			return f;
		}

		public static File touch(File par, String name) throws IOException {
			final String path = par.getAbsolutePath() + File.separatorChar
					+ name;
			File f = new File(path);
			f.createNewFile();
			return f;
		}

		/**
		 * Delets a dir recursively deleting anything inside it.
		 * 
		 * @param file
		 *            The dir to delete
		 * @return true if the dir was successfully deleted
		 */
		public static boolean rm(File file) {
			if (!file.exists() || !file.isDirectory()) {
				return false;
			}

			String[] files = file.list();
			for (int i = 0, len = files.length; i < len; i++) {
				File f = new File(file, files[i]);
				if (f.isDirectory()) {
					rm(f);
				} else {
					f.delete();
				}
			}
			return file.delete();
		}
	}

	/********************* G ***********************/
	/********************* H ***********************/
	/********************* I ***********************/
	/********************* J ***********************/
	/********************* K ***********************/
	/********************* L ***********************/
	/********************* M ***********************/
	public static class math {
		public static int max(int a, int b) {
			return a > b ? a : b;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class map {
		public HashMap hashMap(Object[][] kv) {
			HashMap ret = new HashMap();
			for (Object[] entry : kv) {
				ret.put(entry[0], entry[1]);
			}
			return ret;
		}
	}

	public static class matrix {
		
		public static Matrix console(int maxLength){
			return new Matrix(0,maxLength);
		}
		
		/**
		 * <p>Print a matrix whose each row as a String.</p> 
		 * @param r
		 * @return
		 */
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

		public static Matrix addHorizontal(Matrix... some) {
			int height = 0;
			int width = 0;
			for (int i = 0; i < some.length; i++) {
				Matrix x = some[i];
				height = S.math.max(height, x.rows());
				width += x.cols();
			}

			int[][] out = new int[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					out[i][j] = ' ';
				}
			}

			int colfix = 0;
			for (int i = 0; i < some.length; i++) {
				Matrix x = some[i];
				for (int h = 0; h < x.rows(); h++) {
					int[] row = x.row(h);
					for (int _ = 0; _ < row.length; _++) {
						out[h][colfix + _] = (char) row[_];
					}
				}
				colfix += x.cols();
			}
			return new Matrix(out);
		}

		public static Matrix fromString(String... s) {

			final int maxLen = ((String) new FArray<String>(s)
					.reduceLeft(new F2<String, String, String>() {

						@Override
						public String f(String a, String b) {
							return a.length() > b.length() ? a : b;
						}

					})).length();

			int[][] ret = new int[s.length][maxLen];
			for (int i = 0; i < s.length; i++) {
				for (int j = 0; j < s[i].length(); j++) {
					ret[i][j] = s[i].charAt(j);
				}
			}
			return new Matrix(ret);
		}
	}

	/********************* N ***********************/
	/********************* O ***********************/
	/********************* P ***********************/
	public static class path {
		public static String rootAbsPath(Object caller) {
			return caller.getClass().getClassLoader().getResource("/")
					.getPath();
		}

		public static String rootAbsPath(Class<?> callerClass) {
			return callerClass.getClassLoader().getResource("/").getPath();
		}
	}

	public static class parse {
		/**
		 * <p>
		 * WARNING!!! ONLY POSITIVE VALUES WILL BE RETURN
		 * </p>
		 * 
		 * @param value
		 * @return above zero
		 */
		public static int toUnsigned(String value) {
			int ret = 0;
			if (value == null || value.isEmpty()) {
				return 0;
			}
			char tmp;
			for (int i = 0; i < value.length(); i++) {
				tmp = value.charAt(i);
				if (!Character.isDigit(tmp)) {
					return 0;
				}
				ret = ret * 10 + ((int) tmp - (int) '0');
			}
			return ret;
		}

		public static long toLong(String value) throws NumberFormatException {
			return Long.parseLong(value);
		}
	}

	/********************* Q ***********************/
	/********************* R ***********************/
	/********************* S ***********************/
	/**
	 * 
	 * @author ed
	 * 
	 */
	public static class stream {
		private static final int BUFFER_SIZE = 8192;

		/**
		 * write from is to os;
		 * 
		 * @param in
		 * @param out
		 * @throws IOException
		 */
		public static void write(final InputStream in, final OutputStream out)
				throws IOException {
			final byte[] buffer = new byte[BUFFER_SIZE];
			int cnt;

			while ((cnt = in.read(buffer)) != -1) {
				out.write(buffer, 0, cnt);
			}
		}
	}

	public static class str {
		public static boolean isBlank(String str) {
			return str == null || "".equals(str.trim()) ? true : false;
		}

		public static boolean notBlank(String str) {
			return str == null || "".equals(str.trim()) ? false : true;
		}

		public static boolean notBlank(String... strings) {
			if (strings == null)
				return false;
			for (String str : strings)
				if (str == null || "".equals(str.trim()))
					return false;
			return true;
		}

		public static boolean notNull(Object... paras) {
			if (paras == null)
				return false;
			for (Object obj : paras)
				if (obj == null)
					return false;
			return true;
		}

		/**
		 * 
		 * @param str
		 * @param idx
		 * @return
		 */
		public static int indexOf(String ori, char ch, int idx) {
			char c;
			int occur_idx = 0;
			for (int i = 0; i < ori.length(); i++) {
				c = ori.charAt(i);
				if (c == ch) {
					if (occur_idx == idx) {
						return i;
					}
					occur_idx++;
				}
			}
			return -1;
		}
	}

	/********************* T ***********************/
	public static long time() {
		return System.currentTimeMillis();
	}

	/********************* U ***********************/
	public static class uuid {
		public static String str() {
			return java.util.UUID.randomUUID().toString();
		}

		public static UUID base() {
			return java.util.UUID.randomUUID();
		}

		public static String vid() {
			UUID uuid = java.util.UUID.randomUUID();
			String vid = uuid.toString().replaceAll("-", "");
			// System.out.println(tmp);
			return vid;
		}
	}
	/********************* V ***********************/
	/********************* W ***********************/
	/********************* X ***********************/
	/********************* Y ***********************/
	/********************* Z ***********************/
}
