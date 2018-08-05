package com.airy.remind.ui.view;

import android.view.View;

import com.airy.remind.myview.EmptyRecycleView;

/**
 * Created by Airy on 2018/7/16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

public interface IMainView {

    EmptyRecycleView getRecyclerView();
    View getEmptyView();
}
