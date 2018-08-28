package com.airy.remind.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.airy.remind.MyApp;
import com.airy.remind.R;
import com.airy.remind.bean.Remind;
import com.airy.remind.widget.HomeScreenWidget;

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
    public static final int HEAD = 1;
    public static final int NORMAL = 2;
    public static final int FOOT = 3;

    private Context mContext;
    private List<Remind> mList;
    private OnItemClickListener mListener;
    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private View footView;

    public RemindListAdapter(Context context, List<Remind> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + getFootViewCount() > mList.size() ) return FOOT;
        return NORMAL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOT) return new ViewHolder(footView);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.remind_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        runEnterAnimation(holder.itemView,position);
        // footview not to bind
        if (getItemViewType(position) == FOOT) return;

        final Remind item = mList.get(position);
        holder.cb_task_isFinshed.setChecked(item.getIsFinished());
        holder.tv_task_name.setText(item.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("做完这件事了吗？");
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getLayoutPosition();
                        removeData(position);
                        Toast.makeText(mContext,"已删除ε=ε=ε=(~￣▽￣)~",Toast.LENGTH_SHORT).show();

                        //notify widget update
                        Intent intent = new Intent(mContext, HomeScreenWidget.class);
                        intent.setAction("refresh");
                        mContext.sendBroadcast(intent);
                    }
                }).setNegativeButton("还没呢(。・∀・)ノ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        holder.cb_task_isFinshed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getIsFinished()){
                    item.setIsFinished(false);
                    MyApp.getDaoSession().getRemindDao().update(item);
                } else {
                    item.setIsFinished(true);
                    MyApp.getDaoSession().getRemindDao().update(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() + getFootViewCount();
    }


    /**
     * 移除数据
     */
    public void removeData(int position) {
        Remind remind = mList.get(position);
        MyApp.getDaoSession().getRemindDao().delete(remind);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 插入数据
     * @param position
     * @param remind
     */
    public void InsertData(int position,Remind remind){
        mList.add(position,remind);
        notifyItemInserted(position);
    }

    public void addFootView(View footView){
        this.footView = footView;
    }

    public int getFootViewCount() {
        return footView == null ? 0 : 1;
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
        void onItemClick();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.remind_task_check_box)
        CheckBox cb_task_isFinshed;
        @BindView(R.id.remind_task_name)
        TextView tv_task_name;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == footView) return;
            ButterKnife.bind(this, itemView);
        }
    }
}