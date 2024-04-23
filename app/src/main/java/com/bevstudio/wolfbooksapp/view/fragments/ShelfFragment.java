package com.bevstudio.wolfbooksapp.view.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.adapters.BestBooksRecyclerviewAdapter;
import com.bevstudio.wolfbooksapp.model.api.Books;
import com.bevstudio.wolfbooksapp.request.api.RequestService;
import com.bevstudio.wolfbooksapp.request.api.RetrofitClass;
import com.bevstudio.wolfbooksapp.vendor.InternetConnection;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import retrofit2.Call;

public class ShelfFragment extends Fragment  {



    public ShelfFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        return view;
    }

}