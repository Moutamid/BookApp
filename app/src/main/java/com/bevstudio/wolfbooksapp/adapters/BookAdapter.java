package com.bevstudio.wolfbooksapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.view.activity.BookInfoActivity;
import com.bumptech.glide.Glide;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<BookItem> bookList;
    private Context context;

    public BookAdapter(List<BookItem> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookItem bookItem = bookList.get(position);
        holder.bookTitle.setText(bookItem.getName());
        Glide.with(context).load(bookItem.getImage()).into(holder.bookImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookInfoActivity.class);
                intent.putExtra("volume_id", bookItem.getVolumeId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }
    }
}
