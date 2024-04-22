package com.bevstudio.wolfbooksapp.view.activity.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.moutamid.locationmonitorapp.MainActivity;
import com.moutamid.locationmonitorapp.R;

public class SplashActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ImageView imageViewLogo;
    Animation animation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imageViewLogo = findViewById(R.id.imageView);

//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageViewLogo, "y", -190);
//
//        objectAnimator.setDuration(1500);
//
//        AnimatorSet animatorSet=new AnimatorSet();
//
//        animatorSet.playTogether(objectAnimator);
//
//        objectAnimator.start();

        int splashInterval = 1700;

        new Handler().postDelayed(this::goToApp, splashInterval);


    }

    public void goToApp() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }


    }


}