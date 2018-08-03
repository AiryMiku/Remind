package com.airy.remind.ui.presenter;

import android.app.Activity;

import com.airy.remind.base.BasePresenter;
import com.airy.remind.ui.view.ISplashView;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public class SplashPresenter extends BasePresenter<ISplashView> {

    private Activity activity;

    public SplashPresenter(Activity activity){
        this.activity = activity;
    }


}
