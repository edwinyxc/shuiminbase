package com.shuimin.base.f;

public abstract class Promise<V, E> {
	/**
	 * The Promise is still pending - it could be created, submitted for
	 * execution, or currently running, but not yet finished.
	 */
	public final static Boolean STATE_PENDING = null;
	/**
	 * The Promise has finished running and a failure occurred. Thus, the
	 * Promise is rejected.
	 * 
	 */
	public final static Boolean STATE_REJECTED = false;
	/**
	 * The Promise has finished running successfully. Thus the Promise is
	 * fulfilled.
	 * 
	 */
	public final static Boolean STATE_FULFILLED = true;

	private Boolean state = null;

	public <_Y, _N> Promise<_Y, _N> then(Function._0<_Y> doneCallback,
			Function._0<_N> failCallback);

	public <_Y, _N> Promise<_Y, _N> then(Function<_Y, V> doneCallback,
			Function<_N, E> failCallback);

	void _resolve(Promise promise , Object x){
		if( x == promise){
			throw new RuntimeException("[TypeError] can not resolve self ");
		}
		if( x instanceof Promise){
			if(((Promise) x).state == STATE_PENDING){
				
			}
			else if(((Promise) x).state == STATE_FULFILLED){
				promise.
			}
		}
		if()
	}
}
