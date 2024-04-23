package com.bevstudio.wolfbooksapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public static String db_path = "LocationMonitorApp";
    public static double cur_lat = 0.0;
    public static double cur_lng = 0.0;

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("LocationMonitorApp");
        db.keepSynced(true);
        return db;
    }

    public static DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference(db_path).child("users");
    public static DatabaseReference LocationReference = FirebaseDatabase.getInstance().getReference(db_path).child("location");
}
