package com.linjun.projectsend.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.linjun.projectsend.R;
import com.linjun.projectsend.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by linjun on 2018/1/5.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.spl_logo)
    ImageView splLogo;
    @BindView(R.id.spl_content)
    ConstraintLayout splContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        setFullScreen();
    }

    private void setFullScreen() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = SplashActivity.this.getWindow();
        window.setFlags(flag, flag);

    }
    private void conversion() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              splLogo.setVisibility(View.GONE);
              splContent.setVisibility(View.VISIBLE);
            }
        },200);
    }


}
