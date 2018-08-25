package com.airy.remind.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.airy.remind.MyApp;
import com.airy.remind.R;
import com.airy.remind.bean.Remind;
import com.airy.remind.bean.RemindDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WidgetService extends RemoteViewsService {

    public static final String TAG = "WidgetService";

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestory");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        public static final String TAG = "ListRemoteViewsFactory";

        private final Context mContext;
        public List<Remind> mList = new ArrayList<>();
        private RemindDao remindDao = MyApp.getDaoSession().getRemindDao();

        public ListRemoteViewsFactory(Context mContext) {
            this.mContext = mContext;
//            if(Looper.myLooper() == null){
//                Looper.prepare();
//            }
        }

        @Override
        public void onCreate() {
            Log.d(TAG,"onCreate");
            mList.clear();
            mList.addAll(remindDao.loadAll());
            Collections.reverse(mList);
        }

        @Override
        public void onDataSetChanged() {
            mList.clear();
            mList.addAll(remindDao.loadAll());
            Collections.reverse(mList);
        }

        @Override
        public void onDestroy() {
            mList.clear();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if(i >= mList.size())
                return null;
            Remind content = mList.get(i);
            final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.remind_widget_list_item);
            rv.setTextViewText(R.id.widget_list_item_tv, content.getName());
            // 填充Intent，填充在AppWdigetProvider中创建的PendingIntent
            Intent intent = new Intent();
            // 传入点击行的数据
            intent.putExtra("name", content.getName());
            rv.setOnClickFillInIntent(R.id.widget_list_item_tv, intent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
