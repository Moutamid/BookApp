package com.bevstudio.wolfbooksapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.model.BookShelf;
import com.bevstudio.wolfbooksapp.view.activity.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookShelfAdapter extends RecyclerView.Adapter<BookShelfAdapter.ViewHolder> {

    private List<BookShelf> bookShelfList;
    public static String name;
    Context context;
    private BookAdapter adapter;
    private List<BookItem> bookItemList;
    private DatabaseReference databaseReference;

    public BookShelfAdapter(Context context, List<BookShelf> bookShelfList) {
        this.bookShelfList = bookShelfList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_shelf, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookShelf bookShelf = bookShelfList.get(position);
        holder.categoryTextView.setText(bookShelf.getCategory());
        Log.d("dataaa", bookShelf.getCategory() + "  dataa");

        holder.ad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = bookShelfList.get(position).getCategory();
                Intent toSearchIntent = new Intent(context, SearchActivity.class);
                context.startActivity(toSearchIntent);
            }
        });

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        List<BookItem> bookItemList = new ArrayList<>();
        BookAdapter adapter = new BookAdapter(bookItemList, context);
        holder.recyclerView.setAdapter(adapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("BookShelfApp")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Shelf")
                .child(bookShelf.getCategory());

        retrieveDataFromFirebase(databaseReference, bookItemList, adapter);
    }

    @Override
    public int getItemCount() {
        return bookShelfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        ImageView ad_button;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.shelf_name);
            ad_button = itemView.findViewById(R.id.ad_button);
            recyclerView = itemView.findViewById(R.id.recyclerview);

        }
    }

    private void retrieveDataFromFirebase(DatabaseReference databaseReference, List<BookItem> bookItemList, BookAdapter adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookItem bookItem = snapshot.getValue(BookItem.class);
                    bookItemList.add(bookItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
