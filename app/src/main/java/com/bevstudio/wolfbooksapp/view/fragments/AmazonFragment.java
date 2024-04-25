package com.bevstudio.wolfbooksapp.view.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bevstudio.wolfbooksapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AmazonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmazonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AmazonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AmazonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AmazonFragment newInstance(String param1, String param2) {
        AmazonFragment fragment = new AmazonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_amazon, container, false);
        String titleText = "Amazon";
        int titleColor = (Color.parseColor("#EFE0CB"));
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inknut_antiqua_bold);
        SpannableString spannableTitle = new SpannableString(titleText);
        spannableTitle.setSpan(new ForegroundColorSpan(titleColor), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().setTitle(spannableTitle);

        return  view;
    }
}