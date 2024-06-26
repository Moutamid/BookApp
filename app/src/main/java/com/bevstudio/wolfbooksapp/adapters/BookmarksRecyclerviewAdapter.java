package com.bevstudio.wolfbooksapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.helper.Constant;
import com.bevstudio.wolfbooksapp.model.api.Item;
import com.bevstudio.wolfbooksapp.model.db.VolumeBooks;
import com.bevstudio.wolfbooksapp.request.api.RequestService;
import com.bevstudio.wolfbooksapp.request.api.RetrofitClass;
import com.bevstudio.wolfbooksapp.vendor.NumberFormatter;
import com.bevstudio.wolfbooksapp.view.activity.BookInfoActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarksRecyclerviewAdapter extends RecyclerView.Adapter<BookmarksRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<VolumeBooks> localVolumeBooks;
    private Call<Item> itemCall, itemCall1, itemCall2;
    private RequestService requestService = RetrofitClass.getAPIInstance();

    // Lists to hold names and images of the books
    public static List<String> bookNames = new ArrayList<>();
    public static List<Bitmap> bookImages = new ArrayList<>();

    public BookmarksRecyclerviewAdapter(Context context, List<VolumeBooks> localVolumeBooks) {
        this.context = context;
        this.localVolumeBooks = localVolumeBooks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookmark_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VolumeBooks volumeBooks = localVolumeBooks.get(position);
        itemCall = requestService.getBookItem(volumeBooks.getVolumeId());
        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item = response.body();
                if (response.isSuccessful()) {
                    holder.shimmerFrameLayout.setVisibility(View.GONE);
                    holder.parentLL.setVisibility(View.VISIBLE);
                    holder.RatingRB.setVisibility(View.VISIBLE);
                    holder.titleTV.setText(item.getVolumeInfo().getTitle());
                    bookNames.add(item.getVolumeInfo().getTitle());

                    holder.bookmarkIV.setVisibility(View.GONE);
                    holder.bookmarkActiveIV.setVisibility(View.VISIBLE);
                    try{
                        holder.publisherTV.setText(item.getVolumeInfo().getPublisher());
                    }catch (Exception e) {
                        holder.publisherTV.setText(R.string.dash);
                    }

                    try {
                        Glide.with(context).load(item.getVolumeInfo().getImageLinks().getSmallThumbnail()).centerCrop().into(holder.imageView);
                    }catch (Exception e) {
                        Glide.with(context).load(Constant.N0_IMAGE_PLACEHOLDER)
                                .centerCrop().into(holder.imageView);
                    }

                    try {
                        if (item.getVolumeInfo().getRatingsCount()==1) {
                            holder.ratingsTV.setText(item.getVolumeInfo().getAverageRating()+" avg rating — "+ NumberFormatter.format(item.getVolumeInfo().getRatingsCount())+" rating");
                        }else {
                            holder.ratingsTV.setText(item.getVolumeInfo().getAverageRating()+" avg rating — "+NumberFormatter.format(item.getVolumeInfo().getRatingsCount())+" ratings");
                        }
                    }catch (Exception e) {
                        holder.ratingsTV.setText(R.string.no_reviews);
                    }

                    try {
                        switch (item.getVolumeInfo().getAuthors().size()) {
                            case 1:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0));
                                break;
                            case 2:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1));
                                break;
                            case 3:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1)+", "+item.getVolumeInfo().getAuthors().get(2));
                                break;
                            case 4:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+
                                        item.getVolumeInfo().getAuthors().get(1)+", "+
                                        item.getVolumeInfo().getAuthors().get(2)+", "+item.getVolumeInfo().getAuthors().get(3));
                                break;
                            case 5:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0)+", "+item.getVolumeInfo().getAuthors().get(1)+", "+item.getVolumeInfo().getAuthors().get(2)+
                                        ", "+item.getVolumeInfo().getAuthors().get(3)+", "+item.getVolumeInfo().getAuthors().get(4));
                                break;
                            default:
                                holder.authorTV.setText("By "+item.getVolumeInfo().getAuthors().get(0));
                        }
                    }catch (Exception e) {
                        holder.authorTV.setText(R.string.dash);
                    }

                    // Add book name to the list

                    // Load book image asynchronously
                    // You can use Glide or any other library for this purpose
                    // For simplicity, let's assume you have the image as a resource in the project
                    Bitmap bookImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_name);
                    bookImages.add(bookImage);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return localVolumeBooks.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, bookmarkActiveIV, bookmarkIV;
        TextView publisherTV, titleTV, authorTV, ratingsTV, noRatingTV;
        RatingBar RatingRB;
        View parentLL;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            publisherTV = itemView.findViewById(R.id.publisherTV);
            titleTV = itemView.findViewById(R.id.titleTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            ratingsTV = itemView.findViewById(R.id.ratingsTV);
            RatingRB = itemView.findViewById(R.id.RatingRB);
            noRatingTV = itemView.findViewById(R.id.noRatingTV);
            bookmarkActiveIV = itemView.findViewById(R.id.bookmarkActiveIV);
            bookmarkIV = itemView.findViewById(R.id.bookmarkIV);
            parentLL = itemView.findViewById(R.id.parentLL);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerFrameLayout);
        }
    }

    // Method to retrieve the list of book names
    public List<String> getBookNames() {
        return bookNames;
    }

    // Method to retrieve the list of book images
    public List<Bitmap> getBookImages() {
        return bookImages;
    }
}
