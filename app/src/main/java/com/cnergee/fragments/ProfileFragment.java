package com.cnergee.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.Profile;
import com.cnergee.mypage.UpdateProfile;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static Context context;
    private boolean flag=true;
    Utils utils = new Utils();
    public static String rslt = "";
    public long memberid;



    TableRow tgoneActivationd,tgoneDoB,tgoneEmailId,tgoneBilgAd,tgonePermaneAdd;
    Button imgcomplete ,imgupdate;
    TextView lblNameValue,lblLoginIDValue,
            lblAlternateNovalue,lblMobileNovalue,
            lblEmailvalue,lblActivationDateValue,
            lblDOBValue,StatusValue,ParmanentAddressValue,
            BillingAddressValue,PackageValue;
    LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
    private boolean isFinish = false;
    private String sharedPreferences_name;
    public static Map<String, MemberDetailsObj> mapMemberDetails;
    private GetMemberDetailWebService getMemberDetailWebService = null;

    boolean isLogout = false;
    public String sharedPreferences_profile;
    private String sharedPreferences_renewal,notificationMsg;
    TextView tv_marquee;

    View view;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getActivity();
        initControls(view);

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);

        memberid = Long.parseLong(utils.getMemberId());

        /*
         * This SharedPrefernce used to check there is change in profile data
         */


        SharedPreferences sharedPreferences1 = context
                .getSharedPreferences(getString(R.string.shared_preferences_profile), 0);

        if(sharedPreferences1.getBoolean("profile", true))
        {


            Utils.log("Profile"," If");
            //Utils.log("Data From server","yes"+sharedPreferences1.getBoolean("profile", true));

            if(Utils.isOnline(context)){



                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else{
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.execute();
                }
            }

            else{
                Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
            }
        }
        else{

            Utils.log("Profile"," else");
            //Utils.log("Data From server","offline"+sharedPreferences1.getBoolean("profile", true));
            String versionName="";
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setOfflineProfile();
        }
        imgcomplete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                tgoneActivationd.setVisibility(View.VISIBLE);
                tgoneDoB.setVisibility(View.VISIBLE);
                tgoneEmailId.setVisibility(View.VISIBLE);
                tgoneBilgAd.setVisibility(View.VISIBLE);
                tgonePermaneAdd.setVisibility(View.VISIBLE);

                imgcomplete.setText("Profile Summary");



                if(flag){
                    tgoneActivationd.setVisibility(View.VISIBLE);
                    tgoneDoB.setVisibility(View.VISIBLE);
                    tgoneEmailId.setVisibility(View.VISIBLE);
                    tgoneBilgAd.setVisibility(View.VISIBLE);
                    tgonePermaneAdd.setVisibility(View.VISIBLE);




                    flag=false;
                }
                else{

                    tgoneActivationd.setVisibility(View.GONE);
                    tgoneDoB.setVisibility(View.GONE);
                    tgoneEmailId.setVisibility(View.GONE);
                    tgoneBilgAd.setVisibility(View.GONE);
                    tgonePermaneAdd.setVisibility(View.GONE);
                    imgcomplete.setText("Complete Profile");

                    flag=true;
                }

            }
        });

        imgupdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent i = new Intent(context, UpdateProfile.class);
//                i.putExtra("Mobile_no",lblAlternateNovalue.getText().toString());
//                i.putExtra("DoB",lblDOBValue.getText().toString());
//                i.putExtra("Email-Id", lblEmailvalue.getText().toString());
//                i.putExtra("Address", BillingAddressValue.getText().toString());
//                i.putExtra("MobileNo",lblMobileNovalue.getText().toString());
//                startActivity(i);

                Fragment fragment = new UpdateProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Mobile_no",lblAlternateNovalue.getText().toString());
                bundle.putString("DoB",lblDOBValue.getText().toString());
                bundle.putString("Email-Id", lblEmailvalue.getText().toString());
                bundle.putString("Address", BillingAddressValue.getText().toString());
                bundle.putString("MobileNo",lblMobileNovalue.getText().toString());
                fragment.setArguments(bundle);
                loadFragment(fragment);
               

            }
        });



        return view;
    }

    private void initControls(View view) {

        imgcomplete = (Button)view.findViewById(R.id.img_cmp_profile);
        imgupdate = (Button)view.findViewById(R.id.img_upd_profile);
        lblNameValue = (TextView)view.findViewById(R.id.txtname);
        lblLoginIDValue = (TextView)view.findViewById(R.id.txtloginid);
        lblAlternateNovalue = (TextView)view.findViewById(R.id.txtalternateno);
        lblMobileNovalue = (TextView)view.findViewById(R.id.txtmobno);
        lblEmailvalue = (TextView)view.findViewById(R.id.txtemail);
        lblActivationDateValue = (TextView)view.findViewById(R.id.txtactivationdate);
        lblDOBValue = (TextView)view.findViewById(R.id.txtdob);
        StatusValue = (TextView)view.findViewById(R.id.txtstatus);
        BillingAddressValue = (TextView)view.findViewById(R.id.txtaddress);
        PackageValue = (TextView)view.findViewById(R.id.txtpackage);
        ParmanentAddressValue =(TextView)view.findViewById(R.id.txtparmanentadd);
        linnhome = (LinearLayout)view.findViewById(R.id.inn_banner_home);
        linnprofile = (LinearLayout)view.findViewById(R.id.inn_banner_profile);
        linnnotification = (LinearLayout)view.findViewById(R.id.inn_banner_notification);
        tgoneActivationd =(TableRow)view.findViewById(R.id.tableRow3);
        tgoneDoB = (TableRow)view.findViewById(R.id.tableRow4);
        tgoneBilgAd = (TableRow)view.findViewById(R.id.tableRow9);
        tgoneEmailId = (TableRow)view.findViewById(R.id.tableRow7);
        tgonePermaneAdd = (TableRow)view.findViewById(R.id.tableRow11);


    }

    private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(
                context);

        protected void onPreExecute() {
            Dialog.setMessage(getString(R.string.app_please_wait_label));
            Dialog.setCancelable(false);
            Dialog.show();
        }


        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            Dialog.dismiss();

            if((lblNameValue.length()>0)){
                Utils.log("Onpost", "Visibility Executed");
                imgcomplete.setVisibility(View.VISIBLE);
                lblActivationDateValue.setEnabled(true);
                lblDOBValue.setEnabled(false);
            }
            else{
                Utils.log("Onpost", "Visibility else Executed");
                tgoneActivationd.setVisibility(View.VISIBLE);
                lblActivationDateValue.setEnabled(true);
                lblDOBValue.setEnabled(false);

            }



            //
            try{
                if (rslt.trim().equalsIgnoreCase("ok")) {

                    if (mapMemberDetails != null) {

                        Set<String> keys = mapMemberDetails.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                            str_keyVal = (String) i.next();

                        }


                        String selItem = str_keyVal.trim();
                        isLogout = false;


                        //finish();
                        MemberDetailsObj memberDetails = mapMemberDetails.get(selItem);


/*
					String datedob = memberDetails.getDateofBirth();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
			                "dd-MM-yyyy");
					*/

                        lblNameValue.setText(memberDetails.getMemberName());
                        lblLoginIDValue.setText(memberDetails.getMemberLoginId());
                        lblAlternateNovalue.setText(memberDetails.getAlternateNo());

                        lblMobileNovalue.setText(memberDetails.getMobileNo());
                        lblEmailvalue.setText(memberDetails.getEmailId());
				/*
					Date date = new Date(memberDetails.getActivationDate());
					java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
					lblActivationDateValue.setText("Time: " + dateFormat.format(date));*/

                        String ActivationDate=memberDetails.getActivationDate();
                        lblActivationDateValue.setText(memberDetails.getActivationDate());
                        lblDOBValue.setText(memberDetails.getDateofBirth());


                        StatusValue.setText(memberDetails.getStatus());
                        BillingAddressValue.setText(memberDetails.getInstLocAddressLine1());
                        PackageValue.setText(memberDetails.getPackageName());
                        ParmanentAddressValue.setText(memberDetails.getInstLocAddressLine2());



                        sharedPreferences_profile = getString(R.string.shared_preferences_profile);
                        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferences_profile, 0); // 0 - for private mode

                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putString("MemberName",memberDetails.getMemberName());
                        editor.putString("MemberLoginId", memberDetails.getMemberLoginId());
                        editor.putString("MemberAlternateNo",memberDetails.getAlternateNo());

                        editor.putString("EmailId",memberDetails.getEmailId() );
                        editor.putString("MobileNo",memberDetails.getMobileNo() );
                        editor.putString("ActivationDate", memberDetails.getActivationDate());
                        //		editor.putString("ActivationDate", memberDetails.getActivationDate());

                        editor.putString("DateofBirth",memberDetails.getDateofBirth());

                        editor.putString("Status",memberDetails.getStatus() );
                        editor.putString("InstLocAddressLine1", memberDetails.getInstLocAddressLine1());
                        editor.putString("InstLocAddressLine2", memberDetails.getInstLocAddressLine2());
                        editor.putString("PackageName",memberDetails.getPackageName());
                        editor.putBoolean("profile", false);
                        editor.commit();
                        //Utils.log("saving in",""+sharedPreferences1.getString("MemberName", "not saved"));
                    }
                }else if (rslt.trim().equalsIgnoreCase("not")) {
                    AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
                }else{
                    AlertsBoxFactory.showAlert(rslt,context );
                }

            }catch(Exception e){
                AlertsBoxFactory.showAlert(rslt,context );}
        }





        @Override
        protected Void doInBackground(String... params) {
            try {
                MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_SUBSCRIBER_DETAILS)
                );

                memberdetailCaller.memberid = memberid;
                memberdetailCaller.setAllData(true);

                memberdetailCaller.join();
                memberdetailCaller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            getMemberDetailWebService = null;
        }
    }





    public void setOfflineProfile(){
        sharedPreferences_profile = getString(R.string.shared_preferences_profile);

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_profile
                        , 0); // 0 - for private mode
        lblNameValue.setText(sharedPreferences.getString("MemberName", "-"));
        lblLoginIDValue.setText(sharedPreferences.getString("MemberLoginId", "-"));
        lblAlternateNovalue.setText(sharedPreferences.getString("MemberAlternateNo", "-"));
        lblMobileNovalue.setText(sharedPreferences.getString("MobileNo", "-"));
        lblEmailvalue.setText(sharedPreferences.getString("EmailId", "-"));
        lblActivationDateValue.setText(sharedPreferences.getString("ActivationDate", "-"));
        lblDOBValue.setText(sharedPreferences.getString("DateofBirth", "-"));
        StatusValue.setText(sharedPreferences.getString("Status", "-"));
        BillingAddressValue.setText(sharedPreferences.getString("InstLocAddressLine1", "-"));
        PackageValue.setText(sharedPreferences.getString("PackageName", "-"));
        ParmanentAddressValue.setText(sharedPreferences.getString("InstLocAddressLine2", "-"));


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