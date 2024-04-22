package com.bevstudio.wolfbooksapp.view.activity.Authentication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnSuccessListener;
import com.moutamid.locationmonitorapp.MainActivity;
import com.moutamid.locationmonitorapp.Model.UserModel;
import com.moutamid.locationmonitorapp.R;
import com.moutamid.locationmonitorapp.helper.Config;
import com.moutamid.locationmonitorapp.helper.Constants;

import java.util.Calendar;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_GALLERY = 111;
    Calendar myCalendar = Calendar.getInstance();
    EditText name, email, password, phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initComponent();

    }

    public void initComponent() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone_number = findViewById(R.id.phone_number);
    }

    public void login(View view) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }

    public void sign_up(View view) {
        if (validation()) {
            registerRequest();
        }
    }


    private void registerRequest() {

        Dialog lodingbar = new Dialog(SignupActivity.this);
        lodingbar.setContentView(R.layout.loading);
        Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        lodingbar.setCancelable(false);
        lodingbar.show();

        Constants.auth().createUserWithEmailAndPassword(
                email.getText().toString(),
                password.getText().toString()
        ).addOnCompleteListener(authResult -> {
            UserModel userModel = new UserModel();
            userModel.name = name.getText().toString();
            userModel.email = email.getText().toString();
            userModel.phone_number = phone_number.getText().toString();
            userModel.id = authResult.getResult().getUser().getUid();

            Constants.UserReference.child(Objects.requireNonNull(Constants.auth().getCurrentUser().getUid())).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Stash.put("UserDetails", userModel);
                    Stash.put("is_first", true);
                    lodingbar.dismiss();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finishAffinity();
                }
            });
        }).addOnFailureListener(e -> {
            lodingbar.dismiss();
            Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    private boolean validation() {
     if (name.getText().toString().isEmpty()) {
            name.setError("Enter Name");
            name.requestFocus();
            Config.openKeyboard(this);

            return false;
        }else if (email.getText().toString().isEmpty()) {
            email.setError("Enter Email");
            email.requestFocus();
            Config.openKeyboard(this);
            return false;

        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password");
            password.requestFocus();
            Config.openKeyboard(this);

            return false;

        } else if (phone_number.getText().toString().isEmpty()) {
            phone_number.setError("Enter Phone Number");
            phone_number.requestFocus();
            Config.openKeyboard(this);

            return false;

        } else if (password.getText().toString().length()<8) {
         password.setError("Should be minimum 8 digit");
         password.requestFocus();
            Config.openKeyboard(this);

            return false;

        } else if (!Config.isNetworkAvailable(this)) {
            Config.showToast(this, "You are not connected to network");

            return false;
        } else {

            return true;

        }

    }


}