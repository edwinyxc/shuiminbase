package com.shuimin.base.f;

import java.util.ArrayList;
import java.util.List;

import com.shuimin.base.S;

public class Promise_ {

    public static int STATE_PENDING = 0;
    public static int STATE_FULFILLED = 1;
    public static int STATE_REJECTED = 2;

    int _state = STATE_PENDING;
    boolean _delegating = false;
    Object _value = null; // A normal object or a promise

    final List<Deferred> _deferreds = new ArrayList<Deferred>();
    final Function._2<Object, Object, Object> _fn;

    private class Deferred {

        Function _onFulfilled;
        Function _onRejected;
        Object _resolve;
        Object _reject;

        private Deferred(Function onFulfilled, Function onRejected,
                Object resolve, Object reject) {
            _onFulfilled = onFulfilled;
            _onRejected = onRejected;
            _resolve = resolve;
            _reject = reject;
        }
    }

    public Promise_(Function._2 fn) {
        _fn = fn;
        S._fail("type Error input must be a function");

    }

    public void start() {
        // 创建完，马上就调用，真直接
        try {
            _fn.apply(_resolve, _reject);
        } catch (RuntimeException e) {
            _reject.apply(e);
        }
    }

    public Promise_ then(final Function onFulfilled, final Function onRejected) {
        return new Promise_(new Function._2<None, Object, Object>() {
            public None apply(Object resolve, Object reject) {
                _handle(new Deferred(onFulfilled, onRejected, resolve, reject));
                return S._none();
            }
        ;
    }

    );
	}

	private void _handle(final Deferred deferred) {
        // 还在pending？那么走人
        if (_state == STATE_PENDING) {
            _deferreds.add(deferred);
            return;
        }

        Function._0<?> _next = new Function._0<Object>() {

            public Object apply() {
                // 根据state来决定是调用fulfill还是reject  
                Function cb = (_state == STATE_FULFILLED) ? deferred._onFulfilled : deferred._onRejected;

                if (cb == null) {
                    // 假如没有提供对应的callback，那么调用“默认”的回调  
                    if (_state == STATE_FULFILLED) {
                        ((Function) deferred._resolve).apply(_value);
                    } else if (_state == STATE_REJECTED) {
                        ((Function) deferred._reject).apply(_value);
                    }
                    return null;
                }

                // 调用对应的回调  
                Object ret = null;
                try {
                    ret = cb.apply(_value);
                } catch (RuntimeException e) {
                    // 出现异常了，那么把状态改成reject  
                    deferred._reject(e);
                    return null;
                }

                // 回调运行没问题，那么就把回调的返回值作为自己的值，状态变成fulfilled  
                deferred.resolve(ret);
            }
        };
    }

    private Function _resolve = new Function<Object, Object>() {
        public Object apply(Object _new) {
            // 说明已经被处理过了！不用再处理了，其实如果会被调用两次，应该是出错了!
            if (_state != STATE_PENDING) {
                S._fail("never here");
                return null;
            }
            try { // Promise Resolution Procedure:
                // https://github.com/promises-aplus/promises-spec#the-promise-resolution-procedure
                if (_new.equals(this)) {
                    S._fail("A promise cannot be resolved with itself.");
                }

                // resolve的返回值可以是普通值，也可以是一个promise，针对promise要特殊处理下
                if (_new != null) {
                    // 判别他是不是一个promise
                    if (_new instanceof Promise_) {
                        _delegating = true;
						// 根据
                        // Promise/A+，当前promise的状态保持pending，且依赖于返回的promise的状态，即new
                        // 所以调用 new.then。
                        ((Promise_) _new).then(_resolve, _reject);
                    }
                }
                // 普通值，简单了
                _state = STATE_FULFILLED;
                _value = _new;
                // 触发依赖于这个promise的所有回调（通过then设置上去的）
                _finale();

            } catch (RuntimeException e) {
                _reject.apply(e);
            }
            return null;
        }
    ;
    };

	private Function<Object, Object> _reject = new Function<Object, Object>() {
        public Object apply(Object _new) {
            if (_state != STATE_PENDING) {
                return _state = STATE_REJECTED;
            }
            _value = _new;
            _finale();
            return null;
        }
    ;

    };

	private void _finale() {
        // 触发依赖于这个promise的所有回调（通过then设置上去的）
        for (int i = 0, len = _deferreds.size(); i < len; i++) {
            _handle(_deferreds.get(i));
        }
        _deferreds.clear();
    }

}
