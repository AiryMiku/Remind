package com.airy.remind.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.airy.remind.R;

public class SlapActivity extends AppCompatActivity {

    public final static String TAG = "SlapActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View contentView = View.inflate(this, R.layout.activity_slap,null);
        setContentView(contentView);
        AlphaAnimation animation = new AlphaAnimation(0.3f,1.0f);
        animation.setDuration(800);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SlapActivity.this,MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(android.R.animator.fade_in,android.R.animator.fade_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        Log.d(TAG,"start animation");
        contentView.startAnimation(animation);
//        Intent intent = new Intent(SlapActivity.this,MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(android.R.animator.fade_in,android.R.animator.fade_out);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

}
