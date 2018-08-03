package com.airy.remind;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.airy.remind.bean.DaoMaster;
import com.airy.remind.bean.DaoSession;

/**
 * Created by Airy on 2018/7/20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 * Control greenDao
 */

public class MyApp extends Application{

    final public static String TAG = "MyApp";

    private Context mContext;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setDataBase();
        Log.d(TAG,"onCreate");
    }

    public Context getmContext() {
        return mContext;
    }

    private void setDataBase(){
        devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "remind_app.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    static public DaoSession getDaoSession() {
        return daoSession;
    }
}
