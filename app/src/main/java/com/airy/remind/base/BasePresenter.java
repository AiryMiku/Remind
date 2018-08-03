package com.airy.remind.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public class BasePresenter<T> {

    private Reference<T> mViewRef;

    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
    }

    protected T getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView(){
        if (mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
