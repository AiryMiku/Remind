package com.airy.remind.ui.fragment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airy.remind.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by airy on 20/8/2018.
 */

public class RemindDialogFragment extends DialogFragment{

    public final static String TAG = "RemindDialogFragment";

    private Context mContext;
    private EditText mRemindNameET;
    private EditText mRemindContentET;
    private TextView mDateTV;
    private TextView mTimeTV;
    private Button mSaveBTN;


    public void onDateClick(final View v) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateString = year+"年"+(month+1)+"月"+dayOfMonth+"日"; //  月份居然是从0开始
                TextView tx = (TextView) v;
                tx.setText(dateString);
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    public void onTimeClick(final View v){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeString = hourOfDay+"时"+minute+"分";
                TextView tx = (TextView) v;
                tx.setText(timeString);
            }
        },hour,minute,true);
        timePicker.show();
    }

    public void initViews(View v) {
        mRemindNameET = v.findViewById(R.id.remind_name_edtx);
        mRemindContentET = v.findViewById(R.id.remind_content_edtx);
        mDateTV = v.findViewById(R.id.remind_date_tv);
        mDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateClick(v);
            }
        });
        mTimeTV = v.findViewById(R.id.remind_time_tv);
        mTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeClick(v);
            }
        });
    }

    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRemindNameET.setText(bundle.getString("name"));
//            mRemindContentET.setText(bundle.getString("content"));
        } else {
            Toast.makeText(mContext,"数据出现了什么差错", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind_edit,container,false);
        initViews(view);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // 全屏dialogfragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0xBFFFFFFF)); // 背景透明度
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    public interface onRemindDialogFragmentComplete{
        void getRemindData(int listPostion, String name, String content, Date date);
    }
}
