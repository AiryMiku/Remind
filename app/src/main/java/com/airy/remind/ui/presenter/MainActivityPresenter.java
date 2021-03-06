package com.airy.remind.ui.presenter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;

import com.airy.remind.MyApp;
import com.airy.remind.R;
import com.airy.remind.base.BasePresenter;
import com.airy.remind.bean.Remind;
import com.airy.remind.bean.RemindDao;
import com.airy.remind.myview.EmptyRecycleView;
import com.airy.remind.ui.adapter.RemindListAdapter;
import com.airy.remind.ui.fragment.RemindDialogFragment;
import com.airy.remind.ui.view.IMainView;
import com.airy.remind.widget.HomeScreenWidget;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public class MainActivityPresenter extends BasePresenter<IMainView> implements RemindDialogFragment.onRemindDialogFragmentComplete{

    private Activity activity;
    private RemindDao remindDao;
    private EmptyRecycleView recyclerView;
    private RemindListAdapter adapter;
    private List<Remind> dataList;
    private IMainView mainView;

    public MainActivityPresenter(Activity activity){
        this.activity = activity;
        remindDao = MyApp.getDaoSession().getRemindDao();
    }

    private IMainView getMainView() {
        if (isViewAttached()) {
            return getView();
        } else {
            return null;
        }
    }

    public void init(){
        mainView = getMainView();
        if(mainView != null){
            setupRecycleView();
            displayAndLoadWithAnimation();
        }
    }

    public void floatButtonAction(final CoordinatorLayout mCoord){
        final EditText editText = new EditText(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("想做些什么呢？");
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                try{
                    final Remind remind = new Remind();
                    remind.setName(input);
                    final long key = remindDao.insert(remind);
                    if(key > 0){
                        Snackbar.make(mCoord,"成功添加Remind事件",Snackbar.LENGTH_LONG).setAction("撤回", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.removeData(0);
                                notifyWidgetUpdate(); // 通知更新widget
                            }
                        }).show();
                        adapter.InsertData(0,remind);
                        recyclerView.smoothScrollToPosition(0); // 滑动到新增的条目
                        notifyWidgetUpdate(); // 通知更新widget
                    }else{
                        Snackbar.make(mCoord,"添加Remind事件失败",Snackbar.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 初始化recycleView
     */
    private void setupRecycleView(){
        dataList = remindDao.loadAll(); // getData
        View emptyView = mainView.getEmptyView();
        recyclerView = mainView.getRecyclerView();
        recyclerView.setEmptyView(emptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(activity,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new FadeInAnimator());
        adapter = new RemindListAdapter(recyclerView.getContext(), dataList);
        TextView t = new TextView(activity);
        t.setText("—————————————我是有底线的—————————————");
        adapter.addFootView(t);
        ScaleInAnimationAdapter scale = new ScaleInAnimationAdapter(adapter);
        scale.setDuration(500);
        scale.setFirstOnly(false);
        AlphaInAnimationAdapter alpha = new AlphaInAnimationAdapter(scale);
        alpha.setDuration(500);
        alpha.setFirstOnly(false);
//        alpha.setInterpolator(new OvershootInterpolator());
        recyclerView.setAdapter(alpha);


        // 动画效果
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(500);
        defaultItemAnimator.setRemoveDuration(100);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }

    /**
     * 更新主界面的recycleView
     */
    private void displayAndLoadWithAnimation(){
        if (dataList.isEmpty()){
            dataList =  remindDao.loadAll(); // getData
            Collections.reverse(dataList);
        }
        else{
            dataList.clear();
            dataList.addAll(remindDao.loadAll());
            Collections.reverse(dataList);
        }
        recyclerView = mainView.getRecyclerView();

        // 进入呈现的动画
        final Context recyclerViewContext = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(recyclerViewContext,R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();


    }

    public void notifyWidgetUpdate(){
        Intent intent = new Intent(activity, HomeScreenWidget.class);
        intent.setAction("refresh");
        activity.sendBroadcast(intent);
    }

    @Override
    public void getRemindData(int listPostion, String name, String content, Date date) {

    }

    public void displayRemindDialogFragment(int position){
        Remind data = adapter.getAdapterItemData(position);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("name",data.getName());
        bundle.putString("content",data.getContent());
//        bundle.putString("date",data.getDatetime());
        DialogFragment fragment = new RemindDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(activity.getFragmentManager(),activity.getLocalClassName());
    }
}
