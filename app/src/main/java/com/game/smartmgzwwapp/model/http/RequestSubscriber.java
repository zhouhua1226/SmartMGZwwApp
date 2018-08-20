package com.game.smartmgzwwapp.model.http;

import android.util.Log;

import rx.Subscriber;
import rx.exceptions.OnErrorFailedException;

/**
 * Created by zhouh on 2017/7/5.
 */
public abstract class RequestSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }
    @Override
    public void onError(Throwable e) {
        try {
            _onError(e);
            if(e.getMessage()!=null) {
                Log.e( "onError",e.getMessage());
            }
        } catch (OnErrorFailedException e1) {
            if(e1.getMessage()!=null) {
                Log.e("OnErrorFailedException",e1.toString());
            }
        }
    }

    @Override
    public void onNext(T t) {
        _onSuccess(t);
    }

    public abstract void _onSuccess(T t);

    public abstract void _onError(Throwable e);
}
