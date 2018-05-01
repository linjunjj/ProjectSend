package com.linjun.projectsend.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.linjun.projectsend.R;
import com.linjun.projectsend.ui.base.CheckPermissActivity;
import com.linjun.projectsend.ui.main.MainActivity;
import com.linjun.projectsend.utils.ActionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Slinjun on 2018/1/5.
 */

public class SplashActivity extends CheckPermissActivity {


    @BindView(R.id.spl_logo)
    ImageView splLogo;
    @BindView(R.id.spl_content)
    ConstraintLayout splContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        setFullScreen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActionUtils.actionStart(SplashActivity.this, MainActivity.class);
                finish();
            }
        }, 2000);
    }

    private void setFullScreen() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = SplashActivity.this.getWindow();
        window.setFlags(flag, flag);
     conversion();

    }

    private void conversion() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             splLogo.setVisibility(View.GONE);
              splContent.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }


}
