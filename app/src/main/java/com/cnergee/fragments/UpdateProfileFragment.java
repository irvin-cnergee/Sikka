package com.cnergee.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.UpdateProfileSoap;
import com.cnergee.mypage.UpdateProfile;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.sikka.broadband.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    String ALtMobileNo = "", DoB = "", Email_Id = "", BillAddress = "" ,PrMobileNo = "";
    DateNumericAdapter DateNumericAdapter;
    WheelView day, month, year;
    String edittext, editheading, editvalue;
    public PopupWindow pdisplay;
    public static Context context;
    private String sharedPreferences_name;
    private boolean flag = true;
    Button btnOK, btnCancel, btnUpdate, btnCancell;
    EditText edMob, edAdd, edEmail;
    ImageView edVdate, edVEmail, edVAdd,ed_primary_mob;
    TextView mobileno, dob, email, Billadd ,mobileno2;
    ImageView ed_alternate_mob;
    public String sharedPreferences_profile;
    public static String rslt = "";
    boolean isLogout = false;
    Utils utils = new Utils();
    private String MemberId,Message,stringType;

    public long memberid;
    private GetMemberDetailWebService getMemberDetailWebService = null;
    public static Map<String, MemberDetailsObj> mapMemberDetails;
    public static final String TAG = "UpdateProfile";
    String print_Dob="";
    String[] months;

    private String sharedPreferences_renewal,notificationMsg;
    TextView tv_marquee;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfileFragment newInstance(String param1, String param2) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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

        view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        context = getActivity();

        initViews(view);
        tv_marquee.setSelected(true);
        tv_marquee.setText(notificationMsg);

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        notificationMsg = sharedPreferences2.getString("NotificationMsg","");
        utils.setSharedPreferences(sharedPreferences);

        memberid = Long.parseLong(utils.getMemberId());

        MemberId =utils.getMemberId();

        SharedPreferences sharedPreferences1 = context.getSharedPreferences(getString(R.string.shared_preferences_profile), 0);

        if(sharedPreferences1.getBoolean("profile", true))
        {
            Utils.log("Profile"," If");
            if(utils.isOnline(context)){
                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);

            }else{
                Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
            }
        }
        else{

            Utils.log("UpdateProfile"," else");
            //Utils.log("Data From server","offline"+sharedPreferences1.getBoolean("profile", true));
            Utils.log("calling setOffline","setOffline Executd");
            setOfflineProfile();
        }

        ed_alternate_mob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowData("Mobile", "Enter your Alternate Mobile No.",
                        ALtMobileNo);
            }
        });

        ed_primary_mob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ShowData("Mobile_pr","Enter Your Primary Mobile No.",PrMobileNo);
            }
        });
        edVAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowData("Address", "Billing Address", BillAddress);
            }
        });

        edVEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowData("Email", "Email Id", Email_Id);
            }
        });

        edVdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                datePopup();
            }
        });


        return view;
    }

    private void initViews(View view) {

        ed_alternate_mob = (ImageView)view.findViewById(R.id.ed_txt_mob);
        ed_primary_mob = (ImageView)view.findViewById(R.id.ed_txt_mob2);
        edVdate = (ImageView)view.findViewById(R.id.rd_text2);
        edVEmail = (ImageView)view.findViewById(R.id.rd_text3);
        edVAdd = (ImageView)view.findViewById(R.id.rd_text14);
        btnCancel = (Button)view.findViewById(R.id.btn_cancel);
        btnOK = (Button)view.findViewById(R.id.btn_ok);
        mobileno = (TextView)view.findViewById(R.id.txt_mobno);
        mobileno2 = (TextView)view.findViewById(R.id.txt_mobno2);
        dob = (TextView)view.findViewById(R.id.ed_mbl22);
        email = (TextView)view.findViewById(R.id.ed_mbl3);
        Billadd = (TextView)view.findViewById(R.id.ed_mbl4);
        btnUpdate = (Button)view.findViewById(R.id.btn_sub);
        btnCancell = (Button)view.findViewById(R.id.btn_cancelll);
        EditText edit = (EditText)view.findViewById(R.id.ed_mobile);

        // eddate = (EditText)findViewById(R.id.ed_dob);

        /*
         * mobileno.setText(ALtMobileNo); dob.setText(DoB);
         * email.setText(Email_Id); Billadd.setText(BillAddress);
         */
        Utils.log("sharedPreferences","shrea Executd");
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        notificationMsg = sharedPreferences2.getString("NotificationMsg","");
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);



    }
    public void setOfflineProfile() {
        sharedPreferences_profile = getString(R.string.shared_preferences_profile);

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_profile, 0); // 0 - for
        // private

        // mode
        Utils.log("sset offline Executesss","setProfileExecutd:"+sharedPreferences.getString("MemberAlternateNo", "-"));
        mobileno2.setText(sharedPreferences.getString("MobileNo","_"));
        mobileno.setText(sharedPreferences.getString("MemberAlternateNo", "-"));
        email.setText(sharedPreferences.getString("EmailId", "-"));
        dob.setText(sharedPreferences.getString("DateofBirth", "-"));
        Billadd.setText(sharedPreferences.getString("InstLocAddressLine1", "-"));

    }

    private class GetMemberDetailWebService extends
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        ProgressHUD mProgressHUD;

        protected void onPreExecute() {

            Utils.log("Member WebsServices","Member Web Services Executed Executd");
            mProgressHUD = ProgressHUD
                    .show(context,
                            getString(R.string.app_please_wait_label), true,
                            true, this);
        }

        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            mProgressHUD.dismiss();

            try {
                if (rslt.trim().equalsIgnoreCase("ok")) {

                    if (mapMemberDetails != null) {

                        Set<String> keys = mapMemberDetails.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                            str_keyVal = (String) i.next();

                        }

                        String selItem = str_keyVal.trim();
                        isLogout = false;

                        // finish();
                        MemberDetailsObj memberDetails = mapMemberDetails
                                .get(selItem);

                        mobileno.setText(memberDetails.getAlternateNo());
                        mobileno2.setText(memberDetails.getMobileNo());
                        email.setText(memberDetails.getEmailId());
                        dob.setText(memberDetails.getDateofBirth());
                        Billadd.setText(memberDetails.getInstLocAddressLine1());

                        sharedPreferences_profile = getString(R.string.shared_preferences_profile);
                        SharedPreferences sharedPreferences1 = context
                                .getSharedPreferences(
                                        sharedPreferences_profile, 0); // 0 -
                        // for
                        // private
                        // mode


                        //editor.putString("MobileNoPrimary", txtmobileno.getText().toString());

                        SharedPreferences.Editor editor = sharedPreferences1
                                .edit();
						/*editor.putString("MemberAlternateNo",
								memberDetails.getAlternateNo());*/


                        editor.putString("MemberAlternateNo", mobileno.getText().toString());
                        editor.putString("MobileNo", mobileno2.getText().toString());
                        editor.putString("EmailId",email.getText().toString());
                        editor.putString("DateofBirth",
                                dob.getText().toString());

                        editor.putBoolean("updateprofile", false);
                        editor.commit();
                        // Utils.log("saving in",""+sharedPreferences1.getString("MemberName",
                        // "not saved"));
                    }
                } else if (rslt.trim().equalsIgnoreCase("not")) {
                    AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",
                            context);
                } else {
                    AlertsBoxFactory.showAlert(rslt, context);
                }

            } catch (Exception e) {
                AlertsBoxFactory.showAlert(rslt, context);
            }
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
                                R.string.METHOD_SUBSCRIBER_DETAILS));

                memberdetailCaller.memberid = memberid;
                memberdetailCaller.setAllData(true);

                memberdetailCaller.join();
                memberdetailCaller.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();
            getMemberDetailWebService = null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }

    private void ShowData( String action_1, String heading, String value) {
        try {
            final String action=action_1;
			/*LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View layout = inflater.inflate(R.layout.mobile_no, null);
			pdisplay = new PopupWindow(layout, LayoutParams.MATCH_PARENT - 22,
					(LayoutParams.WRAP_CONTENT));
			btnCancell = (Button) layout.findViewById(R.id.btn_cancelll);
			btnUpdate = (Button) layout.findViewById(R.id.btn_sub);

			pdisplay.setTouchable(true);
			pdisplay.setFocusable(true);
			pdisplay.showAtLocation(layout, Gravity.CENTER, 0, 0);*/
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.mobile_no);
            int width = 0;
            int height =0;


            Point size = new Point();
            WindowManager w =getActivity().getWindowManager();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                w.getDefaultDisplay().getSize(size);
                width = size.x;
                height = size.y;
            } else {
                Display d = w.getDefaultDisplay();
                width = d.getWidth();
                height   = d.getHeight();;
            }




            Button updateButton = (Button) dialog.findViewById(R.id.btn_sub);
            Button cancelButton = (Button)dialog.findViewById(R.id.btn_cancelll);
            TextView tv = (TextView) dialog.findViewById(R.id.tv1);
            final EditText edit = (EditText) dialog
                    .findViewById(R.id.ed_mobile);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);


            dialog.show();
            tv.setText(heading);
            edit.setText(value);

            if (action.equalsIgnoreCase("Mobile")||action.equalsIgnoreCase("Mobile_pr")) {

                edit.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_CLASS_PHONE);
                if(action.equalsIgnoreCase("Mobile"))

                    if (action.equalsIgnoreCase("Email")) {
                        edit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }
                if (action.equalsIgnoreCase("Address")) {
                    edit.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                }

            }


            Utils.log("Onclicking ","Update pro Executed");

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (action.equalsIgnoreCase("Mobile")){

                        Utils.log("On Clicking Update ", " line 466");

                        Utils.log("On Clicking Update ", " Update Profile line 463");


                        if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                            AlertsBoxFactory
                                    .showAlert(
                                            getString(R.string.app_please_enter_valid_label),
                                            context);
                            return;
                        }


                        else {


                            if (edit.getText().toString().startsWith("0")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("1")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("2")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("3")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("4")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("5")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("6")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }

                            else {
                                if (edit.getText().toString().length() > 10
                                        || edit.getText().toString().length() < 10) {
                                    AlertsBoxFactory.showAlert(
                                            "Please enter 10 digit mobile no.",
                                            context);
                                    return;

                                } else {

                                    Message =edit.getText().toString();

                                    mobileno.setText(Message);

                                    stringType="alt";

                                    new UpdateProfileWebServices().execute();
                                }

                            }
                        }

                    }

                    if(action.equalsIgnoreCase("Address")){

                        if (edit.getText().toString().isEmpty()) {
                            AlertsBoxFactory.showAlert(
                                    "Please enter Your Address", context);
                            return;
                        }else{

                        }
                    }


                    if (action.equalsIgnoreCase("Email")) {

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (edit.getText().toString().trim().matches(emailPattern) && edit.length() > 0)
                        {


                            if (edit.getText().toString().isEmpty()) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid Email-Id .", context);
                                return;
                            } else {

                                Message = edit.getText().toString();

                                email.setText(Message);

                                stringType="eml";
                                new UpdateProfileWebServices().execute();
                            }
                        }else{
                            AlertsBoxFactory.showAlert(
                                    "Please enter valid Email-Id .", context);
                            return;
                        }




                    }
                    if(action.equalsIgnoreCase("Mobile_pr")){


                        Utils.log("On Clicking Update ", " Update Profile line 463");



                        if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                            AlertsBoxFactory
                                    .showAlert(
                                            getString(R.string.app_please_enter_valid_label),
                                            context);
                            return;
                        }


                        else {

                            if (edit.getText().toString().startsWith("0")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("0")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("1")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("2")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("3")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            if (edit.getText().toString().startsWith("4")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("5")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }



                            if (edit.getText().toString().startsWith("6")) {
                                AlertsBoxFactory.showAlert(
                                        "Please enter valid mobile no.",
                                        context);
                                return;
                            }


                            else {
                                if (edit.getText().toString().length() > 10
                                        || edit.getText().toString().length() < 10) {
                                    AlertsBoxFactory.showAlert(
                                            "Please enter 10 digit mobile no.",
                                            context);
                                    return;

                                }

                                else {

                                    Message=edit.getText().toString();

                                    mobileno2.setText(Message);


                                    stringType="mob";
                                    new UpdateProfileWebServices().execute();
                                }

                            }
                        }

                    }

                    dialog.dismiss();

                }
            });


            cancelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void datePopup() {

        try {
			/*LayoutInflater inflater = (LayoutInflater) UpdateProfile.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View layout = inflater.inflate(R.layout.date_birth,
					(ViewGroup) findViewById(R.id.wheel));*/


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.date_birth);
            int width = 0;
            int height =0;


            Point size = new Point();
            WindowManager w = getActivity().getWindowManager();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                w.getDefaultDisplay().getSize(size);
                width = size.x;
                height = size.y;
            } else {
                Display d = w.getDefaultDisplay();
                width = d.getWidth();
                height   = d.getHeight();
            }


            day = (WheelView) dialog.findViewById(R.id.day1);
            month = (WheelView) dialog.findViewById(R.id.month1);
            year = (WheelView) dialog.findViewById(R.id.year1);
            btnOK = (Button) dialog.findViewById(R.id.btn_ok);
            btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            // pdisplay = new PopupWindow(layout,320,300,true);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);

            /*
             * pdisplay = new PopupWindow(layout);
             * pdisplay.setWidth(LayoutParams.WRAP_CONTENT);
             * pdisplay.setHeight(LayoutParams.MATCH_PARENT/2);
             */



            dialog.show();
            Calendar calendar = Calendar.getInstance();
            int curMonth = calendar.get(Calendar.MONTH);
            months = new String[] { "January", "February", "March",
                    "April", "May", "June", "July", "August", "September",
                    "October", "November", "December" };
            month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
            month.setCurrentItem(curMonth);
            month.addChangingListener(listener);
            Utils.log("In Submit 772 ", "Webservice Executed");

            // year
            int curYear = calendar.get(Calendar.YEAR);
            year.setViewAdapter(new DateNumericAdapter(context, curYear - 50,
                    curYear-10, 0));

            year.setCurrentItem(curYear);
            year.addChangingListener(listener);

            // day
            updateDays(year, month, day);
            day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

            //pdisplay.showAtLocation(dialog, Gravity.CENTER, 0, 0);


		/*	SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-day-hh-mm-ss-ssss/");
			SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy 00 00 00");
			try {
			    dob = input.parse(date);                 // parse input
			    tripDate.setText(output.format(oneWayTripDate));    // format output
			} catch (ParseException e) {
			    e.printStackTrace();
			}
			*/

            Utils.log("on Cancel Button", "sline on 789");

            updateDays(year, month, day);

            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {




                    Utils.log("Year", ":" + String.valueOf("Year" + year));
                    Utils.log("month", ":"+String.valueOf("Month"+month));


                    ///dob.setText(Message);
                    stringType="dob";
                    new UpdateProfileWebServices().execute();


                    Utils.log("Day", ":" + String.valueOf("Year" + year));
                    updateDays(year, month, day);

                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.log("line 130", "in lTry catch");

        Utils.log("Day", ":" + String.valueOf("0" + month));

    }

    /*
     * String dateStr = sdfDateTime.format(d);
     *
     * tvDisplayDate.setText(dateStr);
     */

    /*
     * <********************Wheel comes from
     * here*************************************
     */

    void updateDays(final WheelView year, final WheelView month,
                    final WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR)-50) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar
                .get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        String setDay = "", setMonth = "", setYear, setMin = "", setHour = "";
        if (curDay <= 9) {

            Utils.log("Day", ":" + String.valueOf("0" + curDay));
            setDay = String.valueOf("0" + curDay);
        } else {

            Utils.log("Day", ":" + String.valueOf(curDay));
            setDay = String.valueOf(curDay);

        }
        if ((calendar.get(Calendar.MONTH) + 1) <= 9) {

            Utils.log("Month",
                    ":"
                            + "0"
                            + String.valueOf(String.valueOf(calendar
                            .get(Calendar.MONTH) + 1)));
            setMonth = "0"
                    + String.valueOf(String.valueOf(calendar
                    .get(Calendar.MONTH) + 1));
        } else {
            int j = calendar.get(Calendar.MONTH) + 1;
            Utils.log("Month", ":" + String.valueOf(j));
            setMonth = String.valueOf(j);
        }

        // Utils.log("Month",":"+String.valueOf(calendar.get(Calendar.MONTH)+1));
        Utils.log("Year", ":" + calendar.get(Calendar.YEAR));

        setYear = String.valueOf(calendar.get(Calendar.YEAR));
        //	setYear = String.valueOf(calendar.get(Calendar.YEAR));

        Message = setDay +""+ setMonth+""+setYear+"000000";
        Utils.log("Year","is:"+year.getCurrentItem());
        Utils.log("UpDate Date", "Date:" + Message);
        print_Dob=setDay +" "+months[month.getCurrentItem()] +" "+setYear;

    }

    OnWheelChangedListener listener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDays(year, month, day);
        }

    };





    private class UpdateProfileWebServices extends AsyncTask<String,Void,Void> implements DialogInterface.OnCancelListener {


        UpdateProfileSoap getUpdateProfileSoap;
        String UpProfileResult =" " ;

        String Upresponse = " ";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        }

        @Override
        protected void onPostExecute(Void result) {

            Utils.log("On Post ","On Post Executed");
            mProgressHUD.dismiss();
            try {
                Utils.log(TAG+"Result :",""+UpProfileResult);
                Utils.log(TAG+"Response : ",""+Upresponse);

                if(UpProfileResult.length()>0){

                    if(UpProfileResult.equalsIgnoreCase("OK")){
                        if(Upresponse.length()>0){

                            if(Upresponse.contains("#")){
                                String arr[] = Upresponse.split("#");
                                Utils.log(TAG+"Response :",""+ arr[0]);
                                Utils.log(TAG+"Response :",""+arr[1]);
                                String a = arr[1].trim();



                                if(arr[1].equalsIgnoreCase("1")){
                                    AlertsBoxFactory.showAlert(
                                            arr[0],
                                            context);

                                    Utils.log("StringType :",""+stringType);
                                }

                                if(arr[1].equalsIgnoreCase("0")){
                                    AlertsBoxFactory.showAlert(arr[0], context);
                                }


                                if(stringType.equalsIgnoreCase("mob")){


                                    SharedPreferences sharedPreferences1 = context
                                            .getSharedPreferences(
                                                    sharedPreferences_profile, 0);
                                    SharedPreferences.Editor editor = sharedPreferences1
                                            .edit();
                                    editor.putString("MobileNo", mobileno2.getText().toString());
                                    editor.commit();


                                }


                                if(stringType.equalsIgnoreCase("alt")){


                                    SharedPreferences sharedPreferences1 = context
                                            .getSharedPreferences(
                                                    sharedPreferences_profile, 0);
                                    SharedPreferences.Editor editor = sharedPreferences1
                                            .edit();
                                    editor.putString("MemberAlternateNo", mobileno.getText().toString());
                                    editor.commit();

                                }


                                if(stringType.equalsIgnoreCase("eml")){


                                    SharedPreferences sharedPreferences1 = context
                                            .getSharedPreferences(
                                                    sharedPreferences_profile, 0);
                                    SharedPreferences.Editor editor = sharedPreferences1
                                            .edit();
                                    editor.putString("EmailId",email.getText().toString());
                                    editor.commit();

                                }

                                if(stringType.equalsIgnoreCase("dob")){


                                    SharedPreferences sharedPreferences1 = context
                                            .getSharedPreferences(
                                                    sharedPreferences_profile, 0);
                                    SharedPreferences.Editor editor = sharedPreferences1
                                            .edit();
                                    editor.putString("DateofBirth",dob.getText().toString());
                                    editor.commit();
                                    dob.setText(print_Dob);

                                }

                            }

                        }

                    }
                }

                if (rslt.trim().equalsIgnoreCase("ok")) {

                    if (mapMemberDetails != null) {

                        Set<String> keys = mapMemberDetails.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                            str_keyVal = (String) i.next();

                        }

                        String selItem = str_keyVal.trim();
                        isLogout = false;

                        Utils.log("OnPost Set Item","String Executed");
                        // finish();
                        MemberDetailsObj memberDetails = mapMemberDetails
                                .get(selItem);

                        mobileno.setText(memberDetails.getAlternateNo());
                        mobileno2.setText(memberDetails.getMobileNo());
                        email.setText(memberDetails.getEmailId());
                        dob.setText(memberDetails.getDateofBirth());
                        Billadd.setText(memberDetails.getInstLocAddressLine1());


                    } else if (rslt.trim().equalsIgnoreCase("not")) {
                        AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",
                                context);
                    } else {
                        AlertsBoxFactory.showAlert(rslt, context);
                    }
                }
            }catch (Exception e) {
                Utils.log("Error",""+e);
                // TODO: handle exception
            }
        }
        @Override
        protected Void doInBackground(String... params) {

            Utils.log("On  UpdateWeb ser ", " WebserProfile Executed");

            try{

                getUpdateProfileSoap = new UpdateProfileSoap(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.SOAP_URL),getString(R.string.METHOD_UPDATE_PROFILE));
                Utils.log("On  UpdateWeb ser3 ", " WebserProfile Executed3");


                UpProfileResult = getUpdateProfileSoap.GetUpdateProfile(MemberId, Message, stringType);
                Upresponse = getUpdateProfileSoap.getjsonResponse();

            }catch(Exception e){
                Utils.log(TAG+"Error :",""+e);

            }

            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            mProgressHUD.dismiss();
        }

    }



    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }

    }

}