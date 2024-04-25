package com.bevstudio.wolfbooksapp.vendor;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.helper.Constants;
import com.bevstudio.wolfbooksapp.view.activity.Authentication.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class AddShelfDialogClass extends Dialog {

    public Activity c;
    EditText add_new_shelf;
    Button save;
    String id;
    DatabaseReference mDatabase;

    public AddShelfDialogClass(Activity a, String id) {
        super(a);
        // TODO Auto-generated constructor stub
        this.id = id;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.add_shelf_dailogue);
        add_new_shelf = findViewById(R.id.add_new_shelf);
        save = findViewById(R.id.save);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog lodingbar = new Dialog(c);
                lodingbar.setContentView(R.layout.loading);
                Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
                lodingbar.setCancelable(false);
                lodingbar.show();
                String name = add_new_shelf.getText().toString();
                if (name.isEmpty()) {
                    add_new_shelf.setError("required");
                    return;
                }


                Constants.UserReference.child(id).child("shelves").push().setValue(name)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    lodingbar.dismiss();
                                    dismiss();
                                } else {
                                    lodingbar.dismiss();
                                    Toast.makeText(c, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });

            }
        });

    }


}