package com.airy.remind.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V) this);
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    protected abstract T createPresenter();

    abstract protected int provideContentViewId();

}
