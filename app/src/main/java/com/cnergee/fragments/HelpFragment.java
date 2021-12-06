package com.cnergee.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnergee.mypage.Help;
import com.cnergee.mypage.HelpActivity;
import com.cnergee.mypage.adapter.HelpAdapter;
import com.cnergee.mypage.obj.HelpObject;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    Context context;
    private boolean flag=true;
    ListView lvHelp;
    ArrayList<HelpObject> alHelp= new ArrayList<HelpObject>();
    HelpAdapter helpAdapter;
    SharedPreferences sharedPreferences ;

    private String sharedPreferences_renewal,notificationMsg;
    TextView tv_marquee;
    SharedPreferences sharedPreferences1;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        view = inflater.inflate(R.layout.fragment_help, container, false);
        context = getActivity();
        initControls(view);

        sharedPreferences1 = context.getSharedPreferences(
                getString(R.string.shared_preferences_renewal), 0);
        helpAdapter= new HelpAdapter(context, R.layout.help_item, alHelp);

        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        notificationMsg = sharedPreferences2.getString("NotificationMsg","");
        tv_marquee.setSelected(true);
        tv_marquee.setText(notificationMsg);

        lvHelp.setAdapter(helpAdapter);
        AddHelpObject();
        lvHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                HelpObject helpObject=(HelpObject)arg0.getItemAtPosition(arg2);
                String file_name=helpObject.getFile_name();
                String heading=helpObject.getText_name();
//                Intent i= new Intent(context, Help.class);
//                i.putExtra("file_name", file_name);
//                i.putExtra("heading", heading);
//                startActivity(i);

                Fragment fragment = new HelpDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("file_name", file_name);
                bundle.putString("heading", heading);

                fragment.setArguments(bundle);
                loadFragment(fragment);



            }
        });

        return view;
    }

    private void initControls(View view) {

        lvHelp = (ListView)view.findViewById(R.id.lvHelp);
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);


    }


    public void AddHelpObject(){
        alHelp.add(new HelpObject("Renew Sikka Broadband Account",R.drawable.help_pay,R.color.help_renew,"Renew.htm"));
        alHelp.add(new HelpObject("Upgrade Existing Broadband Account",R.drawable.help_upgrade,R.color.help_upgrade,"Upgrade.htm"));
        alHelp.add(new HelpObject("Request Payment Pickup",R.drawable.help_pp,R.color.help_pay_pickup,"pickup.htm"));
        alHelp.add(new HelpObject("Register a Complaint",R.drawable.help_ser_req,R.color.help_service_req,"complaint.htm"));
        if(sharedPreferences1.getBoolean("is_24ol", true)){

        }
        else{
            alHelp.add(new HelpObject("Self Resolution",R.drawable.help_self,R.color.help_self,"self_resolution.htm"));
        }

        alHelp.add(new HelpObject("Renewal History",R.drawable.help_ren_his,R.color.help_renew_his,"renewalhistory.htm"));

        if(sharedPreferences1.getBoolean("is_24ol", true)){

        }
        else{
            //	alHelp.add(new HelpObject("Create Your Own Plan",R.drawable.help_calu,R.color.help_calc,"create_your_plan.htm"));
        }

        if(sharedPreferences1.getBoolean("is_24ol", true)){

        }
        else{
            //	alHelp.add(new HelpObject("Top-Up For Your Existing Plan",R.drawable.help_top,R.color.help_topup,"topup.htm"));
        }

        alHelp.add(new HelpObject("Check Your Data Usage",R.drawable.help_data,R.color.help_data,"data_usage.htm"));
        alHelp.add(new HelpObject("Get Notifications",R.drawable.alerts_help,R.color.help_alerts,"notifications.htm"));
        //alHelp.add(new HelpObject("Chat with Customer Care",R.drawable.chat_2,R.color.help_chat,"chat.htm"));
        alHelp.add(new HelpObject("FAQS",R.drawable.help_faq,R.color.help_faq,"FAQ.htm"));

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.addToBackStack(null);
        transaction.commit();
    }
}