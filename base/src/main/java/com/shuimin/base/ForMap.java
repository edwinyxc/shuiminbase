package com.shuimin.base;

import java.util.Map;
import java.util.Map.Entry;

public final class ForMap<K, V>  {

	private final Map<K, V> map;

	protected ForMap(Map<K, V> map) {
		this.map = map;
	}
	
	public ForMap<K, V> grep(F<Boolean, Entry<K,V>> grepFunc) {
		Class<?> mapClass = map.getClass();
		Map<K, V> newm = S.<Map<K, V>> _one(mapClass);
		for (Entry<K, V> entry : map.entrySet()) {
			if (grepFunc.f(entry))
				newm.put(entry.getKey(), entry.getValue());
		}
		return new ForMap<K, V>(newm);
	}

	public ForMap<K, V> grepByKey(F<Boolean, K> grepFunc) {
		Class<?> mapClass = map.getClass();
		Map<K, V> newm = S.<Map<K, V>> _one(mapClass);
		for (Entry<K, V> entry : map.entrySet()) {
			if (grepFunc.f(entry.getKey()))
				newm.put(entry.getKey(), entry.getValue());
		}
		return new ForMap<K, V>(newm);
	}
	
	public ForMap<K, V> grepByValue(F<Boolean, V> grepFunc) {
		Class<?> mapClass = map.getClass();
		Map<K, V> newm = S.<Map<K, V>> _one(mapClass);
		for (Entry<K, V> entry : map.entrySet()) {
			if (grepFunc.f(entry.getValue()))
				newm.put(entry.getKey(), entry.getValue());
		}
		return new ForMap<K, V>(newm);
	}

	public Entry<K, V> reduce(
			F2<Entry<K, V>, Entry<K, V>, Entry<K, V>> reduceLeft) {
		return S.<Entry<K, V>> Array(map.entrySet()).reduceLeft(reduceLeft);
	}

	public <R> ForMap<K, R> map(F<R, V> mapFunc) {
		Class<?> mapClass = map.getClass();
		Map<K, R> newm = S.<Map<K, R>> _one(mapClass);
		for (Entry<K, V> entry : map.entrySet()) {
			newm.put(entry.getKey(), mapFunc.f(entry.getValue()));
		}
		return new ForMap<K, R>(newm);
	}

	public ForMap<K,V> each(CB<Entry<K, V>> eachFunc) {
		for (Entry<K, V> entry : map.entrySet()) {
			eachFunc.f(entry);
		}
		return this;
	}

	public Map<K,V> val() {
		return map;
	}

}