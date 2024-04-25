package com.bevstudio.wolfbooksapp.view.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.adapters.BookShelfAdapter;
import com.bevstudio.wolfbooksapp.model.BookShelf;
import com.bevstudio.wolfbooksapp.vendor.AddShelfDialogClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShelfFragment extends Fragment  {

    private RecyclerView recyclerView;
    private BookShelfAdapter adapter;
    private DatabaseReference databaseReference;

    Button add_new_shelf;
    public ShelfFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        String titleText = "Book Shelves";
        int titleColor = (Color.parseColor("#EFE0CB"));
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inknut_antiqua_bold);
        SpannableString spannableTitle = new SpannableString(titleText);
        spannableTitle.setSpan(new ForegroundColorSpan(titleColor), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().setTitle(spannableTitle);
        add_new_shelf=view.findViewById(R.id.add_new_shelf);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookShelfAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("BookShelfApp").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("shelves");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookShelf> bookShelfList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String shelfId = snapshot.getKey();
                    String category = snapshot.getValue(String.class);
                    BookShelf bookShelf = new BookShelf(shelfId, category);
                    bookShelfList.add(bookShelf);
                }
                adapter = new BookShelfAdapter(getContext(), bookShelfList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        add_new_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddShelfDialogClass cdd = new AddShelfDialogClass(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                cdd.show();
            }
        });
        return view;
    }

}