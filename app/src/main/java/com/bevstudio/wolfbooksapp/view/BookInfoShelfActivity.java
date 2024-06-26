package com.bevstudio.wolfbooksapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.adapters.BookShelfAdapter;
import com.bevstudio.wolfbooksapp.helper.Constant;
import com.bevstudio.wolfbooksapp.model.api.Item;
import com.bevstudio.wolfbooksapp.model.api.SaleInfo;
import com.bevstudio.wolfbooksapp.model.api.VolumeInfo;
import com.bevstudio.wolfbooksapp.model.db.VolumeBooks;
import com.bevstudio.wolfbooksapp.request.api.RequestService;
import com.bevstudio.wolfbooksapp.request.api.RetrofitClass;
import com.bevstudio.wolfbooksapp.request.db.DBManager;
import com.bevstudio.wolfbooksapp.request.db.DatabaseHelper;
import com.bevstudio.wolfbooksapp.vendor.CurrencyConverter;
import com.bevstudio.wolfbooksapp.vendor.NumberFormatter;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookInfoShelfActivity extends AppCompatActivity implements View.OnClickListener {

    RoundedImageView bookImageIV;
    TextView publisherTV, titleTV, authorTV, noRatingPlaceholderTV, descriptionTV, categoriesTV,
            publishedDateTV, pageCountTV, languageTV, isbnsTV, ratingsTV, maturityRatingTV, previewBTN, printTypeTV2,
            printTypeTV, heightTV, widthTV, thicknessTV;
    ShimmerFrameLayout shimmer_view_container;
    ConstraintLayout layout_parent;
    Button buyLinkBTN, activeBookmark, inactiveBookmark;
    RatingBar averageRatingRB;
    RequestService requestService;
    Call<Item> singleItemCall;
    String title = "", volume_id = "";
    //    String bookmarked = "";
    int bookmark_id = 0;
    List<VolumeBooks> list = new ArrayList<>();
    DatabaseHelper db;
    DBManager dbManager;
    ProgressDialog dialog;
    String thumbnails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_shelf);

        requestService = RetrofitClass.getAPIInstance();
        bookImageIV = findViewById(R.id.bookImageIV);
        ratingsTV = findViewById(R.id.ratingsTV);
        publisherTV = findViewById(R.id.publisherTV);
        titleTV = findViewById(R.id.titleTV);
        authorTV = findViewById(R.id.authorTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        categoriesTV = findViewById(R.id.categoriesTV);
        publishedDateTV = findViewById(R.id.publishedDateTV);
        noRatingPlaceholderTV = findViewById(R.id.noRatingPlaceholderTV);
        isbnsTV = findViewById(R.id.isbnsTV);
        pageCountTV = findViewById(R.id.pageCountTV);
        languageTV = findViewById(R.id.languageTV);
        previewBTN = findViewById(R.id.previewBTN);
        buyLinkBTN = findViewById(R.id.buyLinkBTN);
        averageRatingRB = findViewById(R.id.averageRatingRB);
        maturityRatingTV = findViewById(R.id.maturityRatingTV);
        printTypeTV2 = findViewById(R.id.printTypeTV2);
        printTypeTV = findViewById(R.id.printTypeTV);
        heightTV = findViewById(R.id.heightTV);
        widthTV = findViewById(R.id.widthTV);
        thicknessTV = findViewById(R.id.thicknessTV);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        layout_parent = findViewById(R.id.layout_parent);
        activeBookmark = findViewById(R.id.activeBookmark);
        inactiveBookmark = findViewById(R.id.inactiveBookmark);

        dialog = new ProgressDialog(BookInfoShelfActivity.this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            volume_id = bundle.getString("volume_id");
//            bookmarked = bundle.getString("is_bookmarked");
            displayBookItem(volume_id);

            try {
                bookmark_id = bundle.getInt("bookmark_id");
            } catch (Exception e) {
                Toast.makeText(BookInfoShelfActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }

        activeBookmark.setOnClickListener(this);
        inactiveBookmark.setOnClickListener(this);
//        DatabaseReference child = FirebaseDatabase.getInstance().getReference("BookShelfApp").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Shelf").child(BookShelfAdapter.name);
        checkVolumeIdExists(volume_id);
//                if (exists) {
//                    activeBookmark.setVisibility(View.VISIBLE);
//                    inactiveBookmark.setVisibility(View.GONE);
//                } else {
//                    activeBookmark.setVisibility(View.GONE);
//                    inactiveBookmark.setVisibility(View.VISIBLE);
//                }



    }

    private void addToBookmark() {
        dbManager = new DBManager(this);
        dbManager.open();
        db = new DatabaseHelper(this);
        VolumeBooks volumeBooks = new VolumeBooks(volume_id, true, titleTV.getText().toString(), previewBTN.getText().toString(), thumbnails);
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("BookShelfApp").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Shelf").child(BookShelfAdapter.name);
        child.child(volume_id).setValue(volumeBooks);
        inactiveBookmark.setVisibility(View.GONE);
        activeBookmark.setVisibility(View.VISIBLE);
        Toast.makeText(BookInfoShelfActivity.this, "Book has been added to Shelf.", Toast.LENGTH_LONG).show();
    }

    private void removeFromBookmark() {
        dbManager = new DBManager(this);
        dbManager.open();
        db = new DatabaseHelper(this);
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("BookShelfApp").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Shelf").child(BookShelfAdapter.name);
        child.child(volume_id).removeValue();
//        db.removeBookmark(bookmark_id);
        inactiveBookmark.setVisibility(View.VISIBLE);
        activeBookmark.setVisibility(View.GONE);
        Toast.makeText(BookInfoShelfActivity.this, "Books has been removed from wheel.", Toast.LENGTH_LONG).show();
    }

    void displayBookItem(String id) {
        singleItemCall = requestService.getBookItem(id);
        singleItemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item = response.body();
                VolumeInfo volume = response.body().getVolumeInfo();
                SaleInfo saleInfo = response.body().getSaleInfo();
                if (response.isSuccessful()) {
                    shimmer_view_container.setVisibility(View.GONE);
                    layout_parent.setVisibility(View.VISIBLE);

                    db = new DatabaseHelper(BookInfoShelfActivity.this);
                    list = db.getAll();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getVolumeId().equals(volume_id)) {
//                            activeBookmark.setVisibility(View.VISIBLE);
//                            inactiveBookmark.setVisibility(View.GONE);
                            break;
                        } else {
//                            activeBookmark.setVisibility(View.GONE);
//                            inactiveBookmark.setVisibility(View.VISIBLE);
                        }
                    }

                    title = volume.getTitle();
                    setTitle(volume.getTitle() + "");
                    titleTV.setText(volume.getTitle());

                    languageTV.setText(volume.getLanguage().toUpperCase());

                    try {
                        publisherTV.setText(volume.getPublisher());
                    } catch (Exception e) {
                        publisherTV.setText(R.string.dash);
                    }

                    try {
                        publishedDateTV.setText(volume.getPublishedDate());
                    } catch (Exception e) {
                        publishedDateTV.setText(R.string.dash);
                    }

                    try {
                        pageCountTV.setText(volume.getPageCount() + " pages");
                    } catch (Exception e) {
                        pageCountTV.setText(R.string.dash);
                    }

                    try {
                        noRatingPlaceholderTV.setText(volume.getAverageRating() + " avg rating — " + NumberFormatter.format(volume.getRatingsCount()) + " ratings");
                    } catch (Exception e) {
                        noRatingPlaceholderTV.setText(R.string.no_reviews);
                    }

                    String categories = "", authors = "", isbns = "";

                    try {
                        for (int i = 0; i < volume.getCategories().size(); i++) {
                            categories = "" + volume.getCategories().get(i) + "\n";
                            categoriesTV.append("" + categories);
                        }
                    } catch (Exception e) {
                        categoriesTV.setText(R.string.dash);
                    }

                    try {
                        printTypeTV2.setText(volume.getPrintType());
                        printTypeTV.setText(volume.getPrintType());
                    } catch (Exception e) {
                        printTypeTV2.setText(R.string.dash);
                        printTypeTV.setText(R.string.dash);
                    }

                    try {
                        String result = "";
                        //loop to get authors
                        for (int j = 0; j < volume.getAuthors().size(); j++) {
                            if (volume.getAuthors().size() == 1) {
                                authors = "" + volume.getAuthors().get(j);
                            } else {
                                authors = "" + volume.getAuthors().get(j) + ", ";
                            }
                            ratingsTV.append("" + authors);
                        }

                        if (volume.getAuthors().size() == 1) {
                            result = ratingsTV.getText().toString().trim();
                        } else {
                            result = ratingsTV.getText().toString().substring(0, ratingsTV.getText().toString().length() - 2);
                        }
                        authorTV.setText("By " + result);

                    } catch (Exception e) {
                        authorTV.setText(R.string.dash);
                    }

                    try {
                        for (int k = 0; k < volume.getIndustryIdentifiers().size(); k++) {
                            isbns = "" + volume.getIndustryIdentifiers().get(k).getIdentifier() + "\n";
                            isbnsTV.append("" + isbns);
                        }
                    } catch (Exception e) {
                        isbnsTV.setText(R.string.dash);
                    }

                    try {
                        heightTV.setText(volume.getDimension().getHeight());
                        widthTV.setText(volume.getDimension().getWidth());
                        thicknessTV.setText(volume.getDimension().getThickness());
                    } catch (Exception e) {
                        heightTV.setText(R.string.dash);
                        widthTV.setText(R.string.dash);
                        thicknessTV.setText(R.string.dash);
                    }

                    try {
                        thumbnails = volume.getImageLinks().getThumbnail();
                        Glide.with(BookInfoShelfActivity.this).load(volume.getImageLinks().getThumbnail()).placeholder(R.color.colorShimmer).into(bookImageIV);
                    } catch (Exception e) {
                        Glide.with(BookInfoShelfActivity.this).load(Constant.N0_IMAGE_PLACEHOLDER)
                                .into(bookImageIV);
                    }

                    try {
                        maturityRatingTV.setText(volume.getMaturityRating());
                    } catch (Exception e) {
                        maturityRatingTV.setText(R.string.dash);
                    }
                }

                try {
                    String webReaderLink = item.getAccessInfo().getWebReaderLink();
                    previewBTN.setText(webReaderLink);
                    previewBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getAccessInfo().getWebReaderLink()));
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    previewBTN.setVisibility(View.INVISIBLE);
                }


                if (item.getSaleInfo().getSaleability().equals("FOR_SALE")) {

                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

                    try {
                        Double price = 0.00;

                        if (saleInfo.getIsEbook()) {
                            buyLinkBTN.setText("Ebook " + CurrencyConverter.convertCurrency(saleInfo.getRetailPrice().getCurrencyCode()) + decimalFormat.format(saleInfo.getRetailPrice().getAmount()));
                        } else {
                            buyLinkBTN.setText("Book " + CurrencyConverter.convertCurrency(saleInfo.getRetailPrice().getCurrencyCode()) + decimalFormat.format(saleInfo.getRetailPrice().getAmount()));
                        }
                    } catch (Exception e) {
//                        if (saleInfo.getIsEbook()) {
//                            buyLinkBTN.setText("Ebook "+CurrencyConverter.convertCurrency(saleInfo.getRetailPrice().getCurrencyCode())+saleInfo.getRetailPrice().getAmount());
//                        }else {
//                            buyLinkBTN.setText("Book "+CurrencyConverter.convertCurrency(saleInfo.getRetailPrice().getCurrencyCode())+saleInfo.getRetailPrice().getAmount());
//                        }
                    }

                    buyLinkBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(saleInfo.getBuyLink()));
                            startActivity(intent);
                        }
                    });

                } else {
                    buyLinkBTN.setText("Not For Sale");
                    buyLinkBTN.setBackground(getResources().getDrawable(R.drawable.button_bg_disabled));
                }

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        descriptionTV.setText(Html.fromHtml(volume.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        descriptionTV.setText(Html.fromHtml(volume.getDescription()));
                    }
                } catch (Exception e) {
                    descriptionTV.setText(R.string.dash);
                }

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activeBookmark:
                removeFromBookmark();
                break;
            case R.id.inactiveBookmark:
                addToBookmark();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private void checkVolumeIdExists(String volumeId) {
        DatabaseReference volumeRef = FirebaseDatabase.getInstance().getReference("BookShelfApp").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Shelf").child(BookShelfAdapter.name);

        // Query for the specific volume ID
        Query query = volumeRef.orderByChild("volumeId").equalTo(volumeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Volume ID exists in the database
                    activeBookmark.setVisibility(View.VISIBLE);
                    inactiveBookmark.setVisibility(View.GONE);

                    Log.d("VolumeID", "Volume ID " + volumeId + " exists in the database");
                } else {
                    // Volume ID does not exist in the database
                    activeBookmark.setVisibility(View.GONE);
                    inactiveBookmark.setVisibility(View.VISIBLE);
                    Log.d("VolumeID", "Volume ID " + volumeId + " does not exist in the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("Firebase", "Error querying volume ID: " + databaseError.getMessage());
            }
        });
    }

}
