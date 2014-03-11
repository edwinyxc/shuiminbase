/**
 * Face of s-lib
 *
 * @author ed
 *
 */
package com.shuimin.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.shuimin.base.S.function.Callback;
import com.shuimin.base.S.function.Function;
import com.shuimin.base.S.function.Function2;
import com.shuimin.base.f.None;
import com.shuimin.base.f.Option;
import com.shuimin.base.f.Some;
import com.shuimin.base.struc.Cache;
import com.shuimin.base.struc.IterableEnumeration;
import com.shuimin.base.struc.Matrix;
import com.shuimin.base.util.cui.Rect;
import com.shuimin.base.util.logger.Logger;

public class S {

    /**
     * ***************** logger *****************
     */
    private final static Logger logger = Logger.getDefault();

    public static final Logger logger() {
        return logger;
    }

    /**
     * ***************** meta *****************
     */
    public static final String author() {
        return " edwinyxc@gmail.com ";
    }

    public static final String version() {
        return "v0.0.1 2014";
    }

    /**
     * ***************** _ *****************
     */
    public static <T> Some<T> _some(T t) {
        return Option.<T>some(t);
    }

    public static <T> None<T> _none() {
        return Option.<T>none();
    }

    public static void _assert(boolean b) {
        _assert(b, "assert failure,somthing wrong");
    }

    public static void _assert(boolean a, String err) {
        if (a) {
            return;
        }
        throw new RuntimeException(err);
    }

    public static void _lazyThrow(Throwable a) {
        throw new RuntimeException(a);
    }

	// public static <T> _ord(,T t){}
    public static <T> T _fail() {
        throw new RuntimeException("BUG OCCUR, CONTACT ME:" + author());
    }

    public static <T> T _fail(String err) {
        throw new RuntimeException(err);
    }

    public static <T> T _avoidNull(T t, Class<T> clazz) {
        if (t == null) {
            return (T) nothing.of(clazz);// TODO possible to remove the second
            // argument
        }
        return t;
    }

    public static <T> T _notNull(T t) {
        _assert(t, "noNull assert failure");
        return t;
    }

    public static <T> T _notNull(T t, String err) {
        _assert(t, err);
        return t;
    }

    public final static <T> T _notNullElse(T _check, T _else) {
        return _check != null ? _check : _else;
    }

    public final static <E> ForIt<E> _for(Iterable<E> c) {
        return new ForIt<E>(c);
    }

    public final static <E> ForIt<E> _for(E[] c) {
        return new ForIt<E>(c);
    }

    public final static <E> ForIt<E> _for(Enumeration<E> emun) {
        return new ForIt<E>(new IterableEnumeration<E>(emun));
    }

    public final static <K, V> ForMap<K, V> _for(Map<K, V> c) {
        return new ForMap<K, V>(c);
    }

    @SuppressWarnings("unchecked")
    public static <T> T _one(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return (T) clazz.newInstance();
    }

    /**
     * ****************** A ***************
     */
    public static class array {

        public static <T> Iterable<T> to(T[] arr) {
            return list.one(arr);
        }

        public static <T> T[] of(Iterable<T> iterable) {
            return S._for(iterable).join();
        }

        @SuppressWarnings("unchecked")
        public static <T> T[] of(Object[] arr) {
            if (arr.length == 0) {
                return (T[]) arr;
            }
            Class<?> tClass = arr[0].getClass();
            Object array = java.lang.reflect.Array.newInstance(tClass, arr.length);
            for (int i = 0; i < arr.length; i++) {
                java.lang.reflect.Array.set(array, i, arr[i]);
            }
            return (T[]) array;
        }

        /**
         * check if an array contains something
         *
         * @param arr array
         * @param o the thing ...
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
         * @param arr array
         * @param o the thing ...
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
            Object array = java.lang.reflect.Array.newInstance(clazz, list.size());
            for (int i = 0; i < list.size(); i++) {
                java.lang.reflect.Array.set(array, i, list.get(i));
            }
            return array;
        }

        public static Object convertType(Class<?> clazz, Object[] arr) {
            Object array = java.lang.reflect.Array.newInstance(clazz, arr.length);
            for (int i = 0; i < arr.length; i++) {
                java.lang.reflect.Array.set(array, i, arr[i]);
            }
            return array;
        }

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

    /**
     * ***************** B ********************
     */
    /**
     * ***************** C ********************
     */
    // @SuppressWarnings({ "rawtypes", "unchecked" })
    public static class collection {

        public static class set {

            public static <E> HashSet<E> hashSet(Iterable<E> arr) {
                final HashSet<E> ret = new HashSet<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }

            public static <E> HashSet<E> hashSet(E[] arr) {
                final HashSet<E> ret = new HashSet<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }
        }

        public static class list {

            public <E> ArrayList<E> arrayList(Iterable<E> arr) {
                final ArrayList<E> ret = new ArrayList<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }

            public <E> ArrayList<E> arrayList(E[] arr) {
                final ArrayList<E> ret = new ArrayList<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }

            public <E> LinkedList<E> linkedList(Iterable<E> arr) {
                final LinkedList<E> ret = new LinkedList<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }

            public <E> LinkedList<E> linkedList(E[] arr) {
                final LinkedList<E> ret = new LinkedList<E>();
                for (E t : arr) {
                    ret.add(t);
                }
                return ret;
            }
        }
    }

    /**
     * ***************** D ********************
     */
    public static class date {

        public static Date fromString(String aDate, String aFormat) throws ParseException {
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

        public static Long stringToLong(String aDate, String aFormat) throws ParseException {
            return fromString(aDate, aFormat).getTime();
        }
    }

    /**
     * ***************** E ********************
     */
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
                if (i != ((List<?>) o).size() - 1) {
                    ret.append(',');
                }
            }
            ret.append("]");
        } else if (o.getClass().isArray()) {
            ret.append("[");
            for (int i = 0; i < java.lang.reflect.Array.getLength(o); i++) {
                ret.append(String.valueOf(java.lang.reflect.Array.get(o, i)));
                if (i != java.lang.reflect.Array.getLength(o) - 1) {
                    ret.append(',');
                }
            }
            ret.append("]");
        } else {
            ret.append(o.toString());
        }
        return ret.toString();
    }

    /**
     * ******************* F **********************
     */
    public final static class ForMap<K, V> {

        private final Map<K, V> map;

        protected ForMap(Map<K, V> map) {
            this.map = map;
        }

        public ForMap<K, V> grep(Function<Boolean, Entry<K, V>> grepFunc) {
            Map<K, V> newm = S.map.hashMap(null);
            for (Entry<K, V> entry : map.entrySet()) {
                if (grepFunc.f(entry)) {
                    newm.put(entry.getKey(), entry.getValue());
                }
            }
            return new ForMap<K, V>(newm);
        }

        public ForMap<K, V> grepByKey(Function<Boolean, K> grepFunc) {
            Map<K, V> newm = S.map.hashMap(null);
            for (Entry<K, V> entry : map.entrySet()) {
                if (grepFunc.f(entry.getKey())) {
                    newm.put(entry.getKey(), entry.getValue());
                }
            }
            return new ForMap<K, V>(newm);
        }

        public ForMap<K, V> grepByValue(Function<Boolean, V> grepFunc) {
            Map<K, V> newm = S.map.hashMap(null);
            for (Entry<K, V> entry : map.entrySet()) {
                if (grepFunc.f(entry.getValue())) {
                    newm.put(entry.getKey(), entry.getValue());
                }
            }
            return new ForMap<K, V>(newm);
        }

        public Entry<K, V> reduce(Function2<Entry<K, V>, Entry<K, V>, Entry<K, V>> reduceLeft) {
            return list.one(map.entrySet()).reduceLeft(reduceLeft);
        }

        public <R> ForMap<K, R> map(Function<R, V> mapFunc) {
            Map<K, R> newm = S.map.hashMap(null);
            for (Entry<K, V> entry : map.entrySet()) {
                newm.put(entry.getKey(), mapFunc.f(entry.getValue()));
            }
            return new ForMap<K, R>(newm);
        }

        public ForMap<K, V> each(Callback<Entry<K, V>> eachFunc) {
            for (Entry<K, V> entry : map.entrySet()) {
                eachFunc.f(entry);
            }
            return this;
        }

        public Map<K, V> val() {
            return map;
        }

    }

    public final static class ForIt<E> {

        final Iterable<E> iter;
        int size = -1;

        public ForIt(Collection<E> e) {
            iter = e;
            size = e.size();
        }

        public ForIt(Iterable<E> e) {
            iter = e;
        }

        public ForIt(E[] e) {
            iter = list.one(e);
            size = e.length;
        }

        private <R> Collection<R> _initCollection(Class<?> itClass) {
            Collection<R> re;
            if (Collection.class.isAssignableFrom(itClass)) {
                try {
                    re = _one(itClass);
                } catch (InstantiationException e) {
                    re = list.one();
                } catch (IllegalAccessException e) {
                    re = list.one();
                }
            } else {
                re = list.one();
            }
            return re;
        }

        public <R> ForIt<R> map(final Function<R, E> mapper) {
            final Class<?> itClass = iter.getClass();
            final Collection<R> result = _initCollection(itClass);
            each(new Callback<E>() {
                public void f(E element) {
                    result.add(mapper.f(element));
                }
            });
            return new ForIt<R>(result);
        }

        public ForIt<E> each(Callback<E> eachFunc) {
            int size = 0;
            for (E e : iter) {
                size++;
                eachFunc.f(e);
            }
            this.size = size;
            return this;
        }

        public ForIt<E> grep(final Function<Boolean, E> grepFunc) {
            final Class<?> itClass = iter.getClass();
            final Collection<E> c = _initCollection(itClass);
            each(new Callback<E>() {
                public void f(E e) {
                    if (grepFunc.f(e)) {
                        c.add(e);
                    }
                }
            });
            return new ForIt<E>(c);
        }

        public E reduce(final Function2<E, E, E> reduceLeft) {
            return list.one(iter).reduceLeft(reduceLeft);
        }

        public Iterable<E> val() {
            return iter;
        }

        public E first() {
            Iterator<E> it = iter.iterator();
            if (it.hasNext()) {
                return it.next();
            }
            return null;
        }

        public E[] join() throws RuntimeException {
            _assert(size != -1, " size = -1");
            Object[] arr = new Object[size];
            int cnt = 0;
            final Iterator<E> it = iter.iterator();
            while (it.hasNext()) {
                arr[cnt++] = it.next();
            }
            return array.<E>of(arr);
        }
    }

    /**
     * Functions
     */
    public static interface function {

        public static interface Callback<T> {

            void f(T t);
        }

        public static interface Callback0 {

            void f();
        }

        public static interface Callback2<A, B> {

            void f(A a, B b);
        }

        public static interface Callback3<A, B, C> {

            void f(A a, B b);
        }

        @Deprecated
        public static interface Callback4<A, B, C, D> {

            void f(A a, B b, C c, D d);
        }

        @Deprecated
        public static interface Callback5<A, B, C, D, E> {

            void f(A a, B b, C c, D d, E e);
        }

        public static interface Function<R, A> {

            R f(A a);
        }

        public static interface Function0<R> {

            R f();
        }

        public static interface Function2<R, A, B> {

            R f(A a, B b);
        }

        public static interface Function3<R, A, B, C> {

            R f(A a, B b, C c);
        }

        @Deprecated
        public static interface Function4<R, A, B, C, D> {

            R f(A a, B b, C c, D d);
        }

        @Deprecated
        public static interface Function5<R, A, B, C, D, E> {

            R f(A a, B b, C c, D d, E e);
        }
    }

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
                return new String[]{file_name, null};
            }
            return new String[]{file_name.substring(0, idx_dot), file_name.substring(idx_dot + 1)};
        }

        public static File mkdir(File par, String name) throws IOException {
            final String path = par.getAbsolutePath() + File.separatorChar + name;
            File f = new File(path);
            f.mkdirs();
            f.createNewFile();
            return f;
        }

        public static File touch(File par, String name) throws IOException {
            final String path = par.getAbsolutePath() + File.separatorChar + name;
            File f = new File(path);
            f.createNewFile();
            return f;
        }

        /**
         * Delets a dir recursively deleting anything inside it.
         *
         * @param file The dir to delete
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

    /**
     * ******************* G **********************
     */
    /**
     * ******************* H **********************
     */
    /**
     * ******************* I **********************
     */
    /**
     * ******************* J **********************
     */
    /**
     * ******************* K **********************
     */
    /**
     * ******************* L **********************
     */
    final static public class list {

        @SuppressWarnings("serial")
        final static public class FList<T> extends ArrayList<T> {

            protected FList() {
                super();
            }

            protected FList(T[] a) {
                super();
                for (T t : a) {
                    this.add(t);
                }
            }

            protected FList(List<T> a) {
                super(a);
            }

            protected FList(Iterable<T> iter) {
                super();
                final Iterator<T> it = iter.iterator();
                while (it.hasNext()) {
                    this.add(it.next());
                }
            }

            protected FList(int i) {
                super(i);
            }

            public FList<T> slice(int start, int end) {
                final FList<T> ret = new FList<T>();
                for (int i = start; i < end; i++) {
                    ret.add(this.get(i));
                }
                return ret;
            }

            public FList<T> slice(int start) {
                final FList<T> ret = new FList<T>();
                for (int i = start; i < size(); i++) {
                    ret.add(this.get(i));
                }
                return ret;
            }

            public T reduceLeft(Function2<T, T, T> reduceFunc) {
                if (this.size() == 1) {
                    return this.get(0);
                }
                T result = null;
                T next;
                for (int i = 0; i < this.size() - 1; i++) {
                    result = this.get(i);
                    next = this.get(i + 1);
                    result = reduceFunc.f(result, next);
                }
                return result;
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            public Object reduceRight(Function2 reduceFunc) {
                if (this.size() == 1) {
                    return this.get(0);
                }
                Object result = null;
                T next;
                for (int i = this.size() - 1; i >= 1; i--) {
                    result = this.get(i);
                    next = this.get(i - 1);
                    result = reduceFunc.f(result, next);
                }
                return result;
            }

            public String join(String sep) {
                final StringBuilder sb = new StringBuilder();
                for (T t : this) {
                    sb.append(t.toString()).append(sep);
                }
                return sb.toString();
            }
        }

        public static <E> FList<E> one() {
            return new FList<E>();
        }

        public static <E> FList<E> one(Iterable<E> iterable) {
            return new FList<E>(iterable);
        }

        public static <E> FList<E> one(E[] arr) {
            return new FList<E>(arr);
        }

        public static <E> FList<E> one(List<E> list) {
            return new FList<E>(list);
        }
    }

    /**
     * ******************* M **********************
     */
    public static class math {

        public static int max(int a, int b) {
            return a > b ? a : b;
        }
    }

    public static class map {

        @SuppressWarnings("unchecked")
        public static <K, V> HashMap<K, V> hashMap(Object[][] kv) {
            if (kv == null) {
                return new HashMap<K, V>();
            }
            HashMap<K, V> ret = new HashMap<K, V>();
            for (Object[] entry : kv) {
                if (entry.length >= 2) {
                    ret.put((K) entry[0], (V) entry[1]);
                }
            }
            return ret;
        }
    }

    public static class matrix {

        public static Matrix console(int maxLength) {
            return new Matrix(0, maxLength);
        }

        /**
         * <p>
         * Print a matrix whose each row as a String.
         * </p>
         *
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
            S.echo(s);
            final int maxLen = ((String) list.one(S.array.<String>of(S.array.shrink(s))).reduceLeft(
                    new Function2<String, String, String>() {

                        public String f(String a, String b) {
                            if (a == null || b == null) {
                                return "";
                            }
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

    /**
     * ******************* N **********************
     */
    final public static class nothing {

        public final static Boolean _boolean = new Boolean(false);
        public final static Character _char = new Character((char) '\0');
        public final static Byte _byte = new Byte((byte) 0);
        public final static Integer _int = new Integer(0);
        public final static Short _short = new Short((short) 0);// ???
        public final static Long _long = new Long(0);
        public final static Float _float = new Float(0);
        public final static Double _double = new Double(0);

        final static Cache<Class<?>, Object> _nothingValues = Cache.<Class<?>, Object>defaultCache().onNotFound(
                new Function<Object, Class<?>>() {

                    @Override
                    public Object f(Class<?> a) {
                        return Enhancer.create(_notNull(a), new MethodInterceptor() {

                            @Override
                            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                            throws Throwable {
								// nothing value must do
                                // nothing;
                                return null;
                            }

                        });
                    }
                });

        static {

            _nothingValues.put(String.class, new String("")).put(Boolean.class, _boolean).put(Integer.class, _int)
                    .put(Byte.class, _byte).put(Character.class, _char).put(Short.class, _short).put(Long.class, _long)
                    .put(Float.class, _float).put(Double.class, _double).put(Object.class, new Object());
        }
        final private Class<?> proxyClass;

        protected nothing(Class<?> clazz) {
            proxyClass = clazz;
        }

        protected Object proxy() {
            return _nothingValues.get(proxyClass);
        }

        @SuppressWarnings("unchecked")
        public static <T> T of(Class<T> t) {
            return (T) new nothing(t).proxy();
        }

    }

    /**
     * ******************* O **********************
     */
    /**
     * ******************* P **********************
     */
    @SuppressWarnings("unchecked")
    final public static class proxy<T> {

        final private Class<T> _clazz;
        final private T _t;
        final Map<String, Function<Object, Object[]>> _mm = new HashMap<String, Function<Object, Object[]>>(5);

        private proxy(Class<T> clazz, T t) {
            _clazz = clazz;
            _t = t;
        }

        public static <T> proxy<T> one(Class<T> clazz) throws InstantiationException, IllegalAccessException {
            return new proxy<T>(clazz, S.<T>_one(clazz));
        }

        public static <T> proxy<T> of(T t) {
            return new proxy<T>((Class<T>) t.getClass(), t);
        }

        public T origin() {
            return _t;
        }

        public Class<T> originClass() {
            return _clazz;
        }

        public proxy<T> method(String methodName, Function<Object, Object[]> method) {
            _mm.put(methodName, method);
            return this;
        }

        public T create() {
            return (T) Enhancer.create(_clazz, new MethodInterceptor() {

                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return _avoidNull(_mm.get(method.getName()), Function.class).f(args);
                }

            });

        }
    }

    public static class path {

        private static String webRoot;

        public static String rootAbsPath(Object caller) {
            return caller.getClass().getClassLoader().getResource("/").getPath();
        }

        public static String rootAbsPath(Class<?> callerClass) {
            return callerClass.getClassLoader().getResource("/").getPath();
        }

        @SuppressWarnings("rawtypes")
        public static String get(Class clazz) {
            String path = clazz.getResource("").getPath();
            return new File(path).getAbsolutePath();
        }

        public static String get(Object object) {
            String path = object.getClass().getResource("").getPath();
            return new File(path).getAbsolutePath();
        }

        public static String rootClassPath() {
            try {
                String path = S.class.getClassLoader().getResource("").toURI().getPath();
                return new File(path).getAbsolutePath();
            } catch (Exception e) {
                String path = S.class.getClassLoader().getResource("").getPath();
                return new File(path).getAbsolutePath();
            }
        }

        public static String packageOf(Object object) {
            Package p = object.getClass().getPackage();
            return p != null ? p.getName().replaceAll("\\.", "/") : "";
        }

        public static String webRoot() {
            if (webRoot == null) {
                webRoot = detectWebRootPath();
            }
            return webRoot;
        }

        public static void webRoot(String webRootPath) {
            if (webRootPath == null) {
                return;
            }

            if (webRootPath.endsWith(File.separator)) {
                webRootPath = webRootPath.substring(0, webRootPath.length() - 1);
            }
            S.path.webRoot = webRootPath;
        }

        private static String detectWebRootPath() {
            try {
                String path = S.class.getResource("/").toURI().getPath();
                return new File(path).getParentFile().getParentFile().getCanonicalPath();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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

    /**
     * ******************* Q **********************
     */
    /**
     * ******************* R **********************
     */
    /**
     * ******************* S **********************
     */
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
        public static void write(final InputStream in, final OutputStream out) throws IOException {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int cnt;

            while ((cnt = in.read(buffer)) != -1) {
                out.write(buffer, 0, cnt);
            }
        }
    }

    public static class str {

        public static final String NEWLINE;

        static {
            String newLine;

            try {
                newLine = new Formatter().format("%n").toString();
            } catch (Exception e) {
                newLine = "\n";
            }

            NEWLINE = newLine;
        }

        public static boolean isBlank(String str) {
            return str == null || "".equals(str.trim()) ? true : false;
        }

        public static boolean notBlank(String str) {
            return str == null || "".equals(str.trim()) ? false : true;
        }

        public static boolean notBlank(String... strings) {
            if (strings == null) {
                return false;
            }
            for (String str : strings) {
                if (str == null || "".equals(str.trim())) {
                    return false;
                }
            }
            return true;
        }

        public static boolean notNull(Object... paras) {
            if (paras == null) {
                return false;
            }
            for (Object obj : paras) {
                if (obj == null) {
                    return false;
                }
            }
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

    /**
     * ******************* T **********************
     */
    final public static class Tuple<A, B> {

        public A a;
        public B b;

        public Tuple(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    final public static class Tuple3<A, B, C> {

        public A a;
        public B b;
        public C c;

        public Tuple3(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    final public static class Tuple4<A, B, C, D> {

        public A a;
        public B b;
        public C c;
        public D d;

        public Tuple4(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }

    public static long time() {
        return System.currentTimeMillis();
    }

    /**
     * ******************* U **********************
     */
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
    /**
     * ******************* V **********************
     */
    /**
     * ******************* W **********************
     */
    /**
     * ******************* X **********************
     */
    /**
     * ******************* Y **********************
     */
    /**
     * ******************* Z **********************
     */
}
