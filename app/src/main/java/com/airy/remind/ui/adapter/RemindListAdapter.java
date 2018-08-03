package com.airy.remind.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;

import com.airy.remind.MyApp;
import com.airy.remind.R;
import com.airy.remind.bean.Remind;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Airy on 2018/7/20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public class RemindListAdapter extends RecyclerView.Adapter<RemindListAdapter.ViewHolder> {

    private static final String TAG = RemindListAdapter.class.getSimpleName();
    private Context mContext;
    private List<Remind> mList;
    private OnItemClickListener mListener;
    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public RemindListAdapter(Context context, List<Remind> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.remind_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        runEnterAnimation(holder.itemView,position);
        final Remind item = mList.get(position);
        holder.cb_task_isFinshed.setChecked(item.getIsFinished());
        holder.tv_task_name.setText(item.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                removeData(position);
            }
        });

        holder.cb_task_isFinshed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getIsFinished()){
                    item.setIsFinished(false);
                } else {
                    item.setIsFinished(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    /**
     * 移除数据
     */
    public void removeData(int position) {
        Remind remind = mList.get(position);
        MyApp.getDaoSession().getRemindDao().delete(remind);
        mList.remove(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_main, null);
        CoordinatorLayout layout = view.findViewById(R.id.main_content);
        Snackbar.make(layout,"已删除",Snackbar.LENGTH_SHORT).show();
        notifyItemRemoved(position);//注意这里
    }

    private void runEnterAnimation(View view, int position) {

        if (animationsLocked) return;
        //animationsLocked是布尔类型变量，一开始为false
        //确保仅屏幕一开始能够容纳显示的item项才开启动画

        if (position > lastAnimatedPosition) {
            //lastAnimatedPosition是int类型变量，默认-1，
            //这两行代码确保了recyclerview滚动式回收利用视图时不会出现不连续效果
            lastAnimatedPosition = position;
            view.setTranslationY(500);     //Item项一开始相对于原始位置下方500距离
            view.setAlpha(0.f);           //item项一开始完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原始位置

            view.animate()
                    .translationY(0).alpha(1.f)                                //设置最终效果为完全不透明
                    //并且在原来的位置
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间
                    //达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))     //设置动画位移先快后慢的效果
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                            //确保仅屏幕一开始能够显示的item项才开启动画
                            //也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Remind item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.remind_task_check_box)
        CheckBox cb_task_isFinshed;
        @BindView(R.id.remind_task_name)
        TextView tv_task_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}