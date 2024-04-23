package com.bevstudio.wolfbooksapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.helper.Constant;
import com.bevstudio.wolfbooksapp.view.activity.Authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView ivLogo;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivLogo = findViewById(R.id.iv_logo);

        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.translate);
        animator.setTarget(ivLogo);
        animator.start();

        //
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();

                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashScreenActivity.this, NavigationActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, Constant.SPLASH_TIME_OUT);
    }
}
