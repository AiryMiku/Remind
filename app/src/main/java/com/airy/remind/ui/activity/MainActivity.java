package com.airy.remind.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.airy.remind.R;
import com.airy.remind.base.BaseActivity;
import com.airy.remind.ui.presenter.MainActivityPresenter;
import com.airy.remind.ui.view.IMainView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<IMainView,MainActivityPresenter> implements IMainView {

    final public static String TAG = "MainActivity";

    @BindView(R.id.main_content)
    CoordinatorLayout mCoord;

    @BindView(R.id.remind_task_list)
    RecyclerView recyclerView;

    @OnClick(R.id.add_remind_float_button)
    public void OnClick(){
        presenter.snackBarAction(mCoord);
    }


//    final Handler.Callback callback = new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            return false;
//        }
//    };
//
//    Handler handler = new Handler(callback){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected MainActivityPresenter createPresenter() {
        Log.d(TAG,"create presenter");
        return new MainActivityPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
