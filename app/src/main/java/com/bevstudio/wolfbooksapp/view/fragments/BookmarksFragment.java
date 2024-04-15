package com.bevstudio.wolfbooksapp.view.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.adapters.BookmarksRecyclerviewAdapter;
import com.bevstudio.wolfbooksapp.model.db.VolumeBooks;
import com.bevstudio.wolfbooksapp.request.db.DatabaseHelper;
import com.bevstudio.wolfbooksapp.vendor.InternetConnection;
import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView tvBookmarkCount, placeholderTV, headerTV, textTV;
    RecyclerView bookmarksRV;
    ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout bookmarksSRL;
    View noConnectionLL;
    Button errorBTN;
    BookmarksRecyclerviewAdapter bookmarksAdapter;
    List<VolumeBooks> list;
    DatabaseHelper db;
    Button button;
    LuckyWheel lwv;
    List<WheelItem> wheelItems = new ArrayList<>();

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        setHasOptionsMenu(true);

        lwv = view.findViewById(R.id.lwv);
        tvBookmarkCount = view.findViewById(R.id.tv_bookmarks_count);
        bookmarksRV = view.findViewById(R.id.bookmarksRV);
        noConnectionLL = view.findViewById(R.id.noConnectionLL);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        bookmarksSRL = view.findViewById(R.id.bookmarksSRL);
        headerTV = view.findViewById(R.id.headerTV);
        textTV = view.findViewById(R.id.textTV);
        errorBTN = view.findViewById(R.id.errorBTN);
        button = view.findViewById(R.id.start);

        getActivity().setTitle("Spin the Wheel");
        bookmarksSRL.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        bookmarksSRL.setOnRefreshListener(this);
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        wheelItems.add(new WheelItem(Color.BLACK,
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name),
                " "));

        lwv.addWheelItems(wheelItems);
        lwv.setTarget(1);

        checkInternetConnection();
        loadBookmarks();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                lwv.rotateWheelTo(2);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void checkInternetConnection() {
        InternetConnection.isInternetConnected(getContext(),noConnectionLL,bookmarksRV);
        headerTV.setText(R.string.no_internet_header);
        textTV.setText(R.string.no_internet_text);
        errorBTN.setVisibility(View.VISIBLE);
        errorBTN.setText(R.string.try_again);
    }

    private void loadBookmarks() {
        bookmarksSRL.setRefreshing(false);
        db = new DatabaseHelper(getContext());
        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        bookmarksRV.setLayoutManager(layoutManager);
        list = db.getAll();

        if (list.size() == 1) {
//            tvBookmarkCount.setText(list.size()+" item found");
        } else if (list.size() == 0) {
            tvBookmarkCount.setText("");
        } else {
//            tvBookmarkCount.setText(list.size()+" items found");
        }


        if (list.isEmpty()) {
            noConnectionLL.setVisibility(View.VISIBLE);
            headerTV.setText(R.string.no_bookmarks);
            textTV.setText(R.string.no_bookmarks_placeholder);
            errorBTN.setVisibility(View.GONE);
        } else {
            noConnectionLL.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        bookmarksAdapter = new BookmarksRecyclerviewAdapter(getActivity(), list);
        shimmerFrameLayout.setVisibility(View.GONE);
        bookmarksRV.setAdapter(bookmarksAdapter);
        bookmarksAdapter.notifyDataSetChanged();

        if (list.isEmpty()) {
            noConnectionLL.setVisibility(View.VISIBLE);
            bookmarksRV.setVisibility(View.GONE);
            headerTV.setText(R.string.no_bookmarks);
            textTV.setText(R.string.no_bookmarks_placeholder);
            errorBTN.setVisibility(View.GONE);
        } else {
            bookmarksRV.setVisibility(View.VISIBLE);
            noConnectionLL.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        }
//        List<String> bookNames = bookmarksAdapter.getBookNames();
//        List<Bitmap> bookImages = bookmarksAdapter.getBookImages();
//        List<WheelItem> wheelItems = new ArrayList<>();
//        for (int i = 0; i < bookNames.size(); i++) {
//            String bookName = bookNames.get(i);
//            Bitmap bookImage = bookImages.get(i);

//            WheelItem wheelItem = new WheelItem(Color.LTGRAY, bookImage, bookName);
//            wheelItems.add(wheelItem);
//        }
//        lwv.addWheelItems(wheelItems);


// Inside your activity or fragment
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (BookmarksRecyclerviewAdapter.bookNames != null) {
                    for (int i = 0; i < BookmarksRecyclerviewAdapter.bookNames.size(); i++) {
                        String bookName = BookmarksRecyclerviewAdapter.bookNames.get(i);
                        Bitmap bookImage = BookmarksRecyclerviewAdapter.bookImages.get(i);
                        Log.d("names", BookmarksRecyclerviewAdapter.bookNames.size() + "  names");
                        wheelItems.add(new WheelItem(Color.BLACK,
                                BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name),
                                bookName));
                        handler.removeCallbacks(this);
                    }

                    lwv.addWheelItems(wheelItems);
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        bookmarksRV.setVisibility(View.GONE);
        loadBookmarks();
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();
    }
}
