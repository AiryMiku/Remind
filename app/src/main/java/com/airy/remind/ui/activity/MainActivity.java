package com.airy.remind.ui.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airy.remind.R;
import com.airy.remind.base.BaseActivity;
import com.airy.remind.myview.EmptyRecycleView;
import com.airy.remind.ui.fragment.RemindDialogFragment;
import com.airy.remind.ui.presenter.MainActivityPresenter;
import com.airy.remind.ui.view.IMainView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<IMainView,MainActivityPresenter> implements IMainView {

    final public static String TAG = "MainActivity";

    @BindView(R.id.main_content)
    CoordinatorLayout mCoord;

    @BindView(R.id.remind_task_list)
    EmptyRecycleView recyclerView;

    @BindView(R.id.empty_view)
    View emptyView;

    @OnClick(R.id.add_remind_float_button)
    public void OnClick(){
        presenter.floatButtonAction(mCoord);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info,menu);

        menu.add(1,Menu.FIRST,1,"DialogFragment");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case Menu.FIRST:
                final DialogFragment dialogFragment = new RemindDialogFragment();
                dialogFragment.show(getFragmentManager(),getLocalClassName());
                return true;
            default:
                return false;
        }
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
    public EmptyRecycleView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public View getEmptyView() {
        return emptyView;
    }
}
