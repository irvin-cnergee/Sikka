package com.cnergee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    private boolean flag=true;
    String file_name="";
    String heading="";
    TextView titleheader;
    WebView wv;

    public HelpDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpDetailFragment newInstance(String param1, String param2) {
        HelpDetailFragment fragment = new HelpDetailFragment();
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

            file_name= getArguments().getString("file_name");
            heading= getArguments().getString("heading");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help_detail, container, false);
        initViews(view);

        titleheader.setText(heading);
        if(file_name!=null){
            wv.loadUrl("file:///android_asset/"+file_name);   // now it will not fail here
        }
        else{
            wv.loadUrl("file:///android_asset/help.htm");
        }


        return view;
    }

    private void initViews(View view) {
        wv = (WebView)view.findViewById(R.id.webView1);
        titleheader= (TextView)view.findViewById(R.id.titleheader);
    }
}