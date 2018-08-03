package com.airy.remind.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;

import com.airy.remind.MyApp;
import com.airy.remind.R;
import com.airy.remind.base.BasePresenter;
import com.airy.remind.bean.Remind;
import com.airy.remind.bean.RemindDao;
import com.airy.remind.ui.adapter.RemindListAdapter;
import com.airy.remind.ui.view.IMainView;

import java.util.List;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public class MainActivityPresenter extends BasePresenter<IMainView> {

    private Activity activity;
    private RemindDao remindDao;
    private RecyclerView recyclerView;
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

    public void snackBarAction(final CoordinatorLayout mCoord){
        final EditText editText = new EditText(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("想做些什么呢？");
        builder.setMessage("输入吧");
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                try{
                    Remind remind = new Remind();
                    remind.setName(input);
                    final long key = remindDao.insert(remind);
                    if(key > 0){
                        Snackbar.make(mCoord,"成功添加Remind事件",Snackbar.LENGTH_LONG).setAction("撤回", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                remindDao.deleteByKey(key);
                                adapter.notifyItemRemoved(dataList.size()-1);
                            }
                        }).show();
                        dataList = remindDao.loadAll();
                        adapter.notifyItemInserted(dataList.size()-1);
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

    private void setupRecycleView(){
        dataList = remindDao.loadAll(); // getData
        recyclerView = mainView.getRecyclerView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(activity,DividerItemDecoration.VERTICAL));
        adapter = new RemindListAdapter(recyclerView.getContext(), dataList);
        recyclerView.setAdapter(adapter);
        /**
         * 既然是动画，就会有时间，我们把动画执行时间变大一点来看一看效果
         */
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(500);
        defaultItemAnimator.setRemoveDuration(100);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }

    private void displayAndLoadWithAnimation(){
        if (dataList.isEmpty())
            dataList = remindDao.loadAll(); // getData
        else{
            dataList.clear();
            dataList.addAll(remindDao.loadAll());
        }
        recyclerView = mainView.getRecyclerView();
        final Context recyclerViewContext = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(recyclerViewContext,R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
        recyclerView.startLayoutAnimation();
//        Toast.makeText(activity,"正在载入...",Toast.LENGTH_SHORT).show();
    }

}
