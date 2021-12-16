package com.cnergee.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.BaseApplication;
import com.cnergee.mypage.ChangePackage_NewActivity;
import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.LoginFragmentActivity;
import com.cnergee.mypage.MakeMyPayment_Atom;
import com.cnergee.mypage.MakePaymentSubpaisa;
import com.cnergee.mypage.SOAP.AreaWiseSettingSOAP;
import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.caller.AdjustmentCaller;
import com.cnergee.mypage.caller.PackgeCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.PackageList;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.PackageListParsing;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePackageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePackageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    RadioButton renewOption, adjOption ,renewImmediate;
    //Spinner spinnerList;
    TextView price,validity,servicetax;
    private boolean isFinish = false;
    private String updateFrom="";
    private boolean flag=true;
    public static String rslt = "";
    public static Map<String, PackageList> mapPackageList;
    public static String strXML = "";
    public static String adjStringVal = "";
    String selected_plan="";

    static String extStorageDirectory = Environment
            .getExternalStorageDirectory().toString();
    final static String xml_folder = "mypage";
    final static String TARGET_BASE_PATH = extStorageDirectory + "/"
            + xml_folder + "/";
    public static String final_price;
    public String memberloginid="";
    public String xml_file_postFix = "PackageList.xml";
    public String xml_file;
    public String xml_file_with_path;

    public static Context context;
    public static boolean isAdjOptionClick = false,isVaildUser=false;
    boolean isLogout = false;
    boolean check_intent=false;
    private String sharedPreferences_name;
    public String otp_password="";

    LinearLayout btnpay;
    ImageView btnhome,btnprofile,btnnotification,btnhelp;

    String username;
    String selItem, CurrentPlan;
    String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate;
    double OldPlanRate,NewPlanRate;
    String PackageValidity,ServcieTax;
    ProgressDialog mainDialog;
    String PackageId="";
    boolean is_cheque_bounced=false;
	/*String AreaCode = "21";
	String AreaCodeFilter ="1";*/
    //String previousPlanRate = "";

    String MemberId;
    Utils utils = new Utils();
    AdjustmentWebService adjustmentWebService = null;
    PackageListWebService packageListWebService = null;
    public static String settingResult="";

    Bundle extras1;
    String DiscountPercentage="";
    TextView tvLabelDiscount,tv_pkg_name,tv_offer;
    Button btn_planlist;
    PackageList package_list;
    ArrayList<PackageList> al_package=new ArrayList<PackageList>();


    ArrayAdapter adapterForSpinner;

    public boolean compulsory_immediate=false;
    com.cnergee.mypage.obj.AdditionalAmount additionalAmount;

    String PackageRate,AdditionalAmount,AdditionalAmountType, DaysFineAmount,DiscountAmount,finalcharges,FineAmount;
    LinearLayout ll_addtional_details,llClickDetails;
    boolean is_payemnt_detail=false;
    boolean isDetailShow=false;
    TextView TextView02,final_price_label;
    TextView package_rate;
    LinearLayout ll_package_rate;
    String datafrom="changepack",notificationMsg;
    Dialog pg_dialog;
    TextView tv_marquee ;
    private String sharedPreferences_renewal;
    int ispid;
    boolean ispostpaid;
    boolean is_activity_running=false;
    PackageList packageList;




    public ChangePackageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePackageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePackageFragment newInstance(String param1, String param2) {
        ChangePackageFragment fragment = new ChangePackageFragment();
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

            packageList= (PackageList) getArguments().getSerializable("Selected_Pkg");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_change_package, container, false);
        context = getActivity();

        initView(view);

        xml_file_with_path = TARGET_BASE_PATH + xml_file_postFix;
        File file = new File(xml_file_with_path);
        file.delete();

        adapterForSpinner = new ArrayAdapter(context,android.R.layout.simple_spinner_item);
        is_activity_running=true;
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
        ispid = Integer.parseInt(sharedPreferences.getString("ISPId",""));
        ispostpaid = Boolean.parseBoolean(sharedPreferences.getString("IsPostPaid",""));

        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        notificationMsg = sharedPreferences1.getString("NotificationMsg","");

        utils.setSharedPreferences(sharedPreferences);
        mainDialog= new ProgressDialog(context);
        MemberId = utils.getMemberId();
        //Toast.makeText(ChangePackage_NewActivity.this, MemberId, Toast.LENGTH_LONG).show();

        memberloginid = utils.getMemberLoginID();
        Log.e("Member",":--"+memberloginid);
//        check_intent=getActivity().getIntent().getBooleanExtra("check_intent", false);

        tv_marquee.setSelected(true);

        tv_marquee.setText(notificationMsg);

        xml_file = xml_file_postFix;
        otp_password= sharedPreferences.getString("otp_password", "0");
        renewOption.setOnClickListener(updateFromOptionOnClickListener);
        adjOption.setOnClickListener(updateFromOptionOnClickListener);
        renewImmediate.setOnClickListener(updateFromOptionOnClickListener);

        ll_addtional_details.setVisibility(View.GONE);
        llClickDetails.setVisibility(View.GONE);

        llClickDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(is_payemnt_detail){
                    if(isDetailShow){
                        ll_addtional_details.setVisibility(View.GONE);
                        isDetailShow=false;
                    }
                    else{
                        ll_addtional_details.setVisibility(View.VISIBLE);
                        isDetailShow=true;
                    }

                    showPaymentDetails(additionalAmount);
                }
                else{
                    ll_addtional_details.setVisibility(View.GONE);
                }
            }
        });

        /*Log.i("******* XML FILE ******** ", xml_file);*/
        xml_file_with_path =  TARGET_BASE_PATH + xml_file;

        btnpay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.e("adapterForSpinner",":"+adapterForSpinner.getCount());
                //if(adapterForSpinner.getCount()>0){
                    /*if(btn_planlist.getText().toString().equalsIgnoreCase("Select Package"))
                    {
                        Toast.makeText(ChangePackage_NewActivity.this,"Please Select Package from Package list",
                                Toast.LENGTH_LONG).show();

                    }
                    else
                    {*/
                if(!compulsory_immediate){
                    RadioGroup radiogrp = (RadioGroup)view.findViewById(R.id.radioPayMode);
                    int id = radiogrp.getCheckedRadioButtonId();
                    if(id == -1)
                    {
                        Toast.makeText(context,"Please Select Package Update details",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(Double.valueOf(price.getText().toString())>0){
                            if(is_cheque_bounced){
                                AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), context);
                            }
                            else{
                                show_payment_options();
                                //show_pg_dialog("");
									/*Utils.is_BillDesk=false;
									Utils.is_CCAvenue=false;
									Utils.is_BillDesk=true;
									SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
								int	BillDesk=sharedPreferences1.getInt("BillDesk", 0);

								if(BillDesk>0){
									proceed_to_pay();
								}
								else{
									AlertsBoxFactory.showAlert(Utils.BILLDESK_MESSAGE, ChangePackage_NewActivity.this);
								}*/

                            }
                        }
                        else{
                            AlertsBoxFactory.showAlert("Due to selection of downgrade package. Adjustment is not possible", context);
                        }
                    }
                }
                else{
                    if(Double.valueOf(price.getText().toString())>0){
                        if(is_cheque_bounced){
                            AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), context);
                        }
                        else{
                            show_payment_options();
                            //show_pg_dialog("");
								/*Utils.is_BillDesk=true;
								Utils.is_CCAvenue=false;
								SharedPreferences sharedPreferences1 = getApplicationContext()
										.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
								int	BillDesk=sharedPreferences1.getInt("BillDesk", 0);

								if(BillDesk>0){
									proceed_to_pay();
								}
								else{
									AlertsBoxFactory.showAlert(Utils.BILLDESK_MESSAGE, ChangePackage_NewActivity.this);
								}*/
                        }
                    }
                    else{
                        AlertsBoxFactory.showAlert("Due to selection of downgrade package. Adjustment is not possible", context);
                    }
                }
                //}

                /*}
                else{
                    //Toast.makeText(ChangePackage_NewActivity.this, "Please Select Package from Package list !!", Toast.LENGTH_LONG).show();
                    if(btn_planlist.getText().toString().equalsIgnoreCase("Select Package"))
                    {
                        Toast.makeText(ChangePackage_NewActivity.this,"Please Select Package from Package list",
                                Toast.LENGTH_LONG).show();

                    }else{
                        RadioGroup radiogrp = (RadioGroup) findViewById(R.id.radioPayMode);
                        int id = radiogrp.getCheckedRadioButtonId();
                        if(id == -1)
                        {
                            Toast.makeText(ChangePackage_NewActivity.this,"Please Select Package Update details",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }*/
            }
        });
        BaseApplication.getEventBus().register(this);
        tv_pkg_name.setText(packageList.getPlanName());
        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Neuton_Regular.ttf");
        tv_pkg_name.setTypeface(face2);
//        if(packageList.getOffervalidityc().equalsIgnoreCase("0")){
//            tv_offer.setVisibility(View.GONE);
//        }else{
//            tv_offer.setVisibility(View.VISIBLE);
//            String offer_text = "\n" +
//                    "You will get Offer on this package.Validity of package is "+"<b>" + packageList.getOffervalidityc() + "</b> "+ " days and policy is "+ packageList.getOfferpackagedesc();
//            tv_offer.setText(Html.fromHtml(offer_text));
//            tv_offer.setTypeface(face2);
//            // blink();
//
//            SpannableString ss1=  new SpannableString(tv_offer.getText().toString());
//            ss1.setSpan(new RelativeSizeSpan(1.2f), 58,61, 0); // set size
//            ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.circular_progress_default_progress)), 58, 61, 0);
//            tv_offer.setText(ss1);
//        }

        price.setText(Double.toString(Double.parseDouble(String.valueOf(packageList.getPackageRate()))));
        validity.setText(""+packageList.getPackagevalidity());
        servicetax.setText(packageList.getServiceTax());
        final_price=price.getText().toString();

        //new GetFinalDeductedAmtAsyncTask().execute();

        if(!tv_pkg_name.getText().toString().trim().equalsIgnoreCase("Select Package")){
            setPlanDetails();
            Utils.log("PackageId", ":"+PackageId);
            price.setText(Double.toString(NewPlanRate));
            final_price=price.getText().toString();
            validity.setText(""+packageList.getPackagevalidity());
            servicetax.setText(packageList.getServiceTax());
            tv_pkg_name.setText(packageList.getPlanName());

            new GetFinalDeductedAmtAsyncTask().execute();

        }else{
            Toast.makeText(context,
                    "Please select valid package from the list",
                    Toast.LENGTH_LONG).show();
            price.setText("0");
            validity.setText("0");
            servicetax.setText("0");
        }



        return view;
    }

    private void initView(View view) {

        mapPackageList = new HashMap<String, PackageList>();
        //spinnerList = (Spinner) this.findViewById(R.id.planList);
        price = (TextView)view.findViewById(R.id.price);
        validity = (TextView)view.findViewById(R.id.validity);
        servicetax = (TextView)view.findViewById(R.id.servicetax);
        tvLabelDiscount=(TextView) view.findViewById(R.id.tvLabelDiscount);
        TextView02=(TextView)view.findViewById(R.id.TextView02);
        final_price_label=(TextView)view.findViewById(R.id.TextView03);
        ll_package_rate=(LinearLayout)view.findViewById(R.id.ll_package_rate);
        package_rate=(TextView)view.findViewById(R.id.package_rate);
        tv_pkg_name=(TextView)view.findViewById(R.id.tv_pkg_name);
        tv_offer=(TextView)view.findViewById(R.id.tv_offer);
        renewOption = (RadioButton) view.findViewById(R.id.radioRenew);
        adjOption = (RadioButton) view.findViewById(R.id.radioAdj);
        renewImmediate= (RadioButton) view.findViewById(R.id.radioImmediate);
        ll_addtional_details=(LinearLayout)view.findViewById(R.id.ll_addtional_details);
        llClickDetails=(LinearLayout)view.findViewById(R.id.llClickDetails);


        tv_marquee= (TextView) view.findViewById(R.id.tv_marquee);

        btnpay = (LinearLayout)view.findViewById(R.id.btnpay);

    }

    RadioButton.OnClickListener updateFromOptionOnClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            String selecte_plan = tv_pkg_name.getText().toString();


            // if(adapterForSpinner.getCount()>0){
            if(selecte_plan.equalsIgnoreCase("Select Package")){
                Toast.makeText(context,
                        "Please select valid package from the list",
                        Toast.LENGTH_LONG).show();
                renewOption.setChecked(false);
                renewImmediate.setChecked(false);
                adjOption.setChecked(false);
                return;
            }

            if (renewOption.isChecked()) {
                updateFrom = "S";
                isAdjOptionClick = false;
                //setPlanDetails();
                price.setText(Double.toString(NewPlanRate));
                validity.setText(PackageValidity);
                servicetax.setText(ServcieTax);
                new GetFinalDeductedAmtAsyncTask().execute();
                //new GetFinalPriceAsynctask().execute();
                // new UpdatePrice().execute();

            }  else if (adjOption.isChecked()) {
                updateFrom = "I";
                //new AdjustmentWebService().execute();
                AlertsBoxFactory.showAlert("This Feature will <b>Discontinue</b> The Current Package and New Package will be Applied from Today.<br/> Are you Sure? ", context);
                adjustmentWebService = new AdjustmentWebService();
                adjustmentWebService.execute((String)null);

                //new GetFinalPriceAsynctask().execute();
            }else if(renewImmediate.isChecked()){
                updateFrom = "I";
                //new AdjustmentWebService().execute();
                AlertsBoxFactory.showAlert("This Feature will <b>Discontinue</b> the Current Package and New Package will be Applied from Today.<br/> Are you Sure? ", context);

                price.setText(Double.toString(NewPlanRate));
                validity.setText(PackageValidity);
                servicetax.setText(ServcieTax);
                new GetFinalDeductedAmtAsyncTask().execute();

            }
        }
        // }
    };


    private void setPackageList() {
        // ProgressDialog progressDialog =
        // ProgressDialog.show(ChangePackgeActivity.this,
        // "Loading Package List","Please Wait...");

        try {
            String str_xml = readPackageListXML();
            Utils.log("Parsing String XML", ":"+str_xml);
            PackageListParsing packageList = new PackageListParsing(str_xml);
            Utils.log("packageList", ":"+packageList);
            mapPackageList = packageList.getMapPackageList();
            Utils.log("mapPackageList", ":"+mapPackageList.size());

			/*String key = AreaCode + "~" + AreaCodeFilter + "~" + CurrentPlan;
			if (!mapPackageList.containsKey(key)) {
				PackageList init_packageList = new PackageList();
				init_packageList.setAreaCode(AreaCode);
				init_packageList.setAreaCodeFilter(AreaCodeFilter);
				init_packageList.setPackageRate("" + PlanRate);
				init_packageList.setPlanName(CurrentPlan);
				mapPackageList.put(key, init_packageList);
			}*/

        } catch (Exception e) {
            e.printStackTrace();
            AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
                    context);
        }
        try {
            ArrayAdapter adapterForSpinner = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item);
            //spinnerList.setAdapter(adapterForSpinner);
            Set<String> keys = mapPackageList.keySet();
            Iterator<String> i = keys.iterator();
            List<String> unsortList  = new ArrayList<String>();

            while (i.hasNext()) {
                String key = (String) i.next();
                PackageList pl = (PackageList) mapPackageList.get(key);

                if(!pl.getPlanName().equals(CurrentPlan))
                    unsortList.add(pl.getPlanName());
            }

            adapterForSpinner.add("Select Package");

            //sort the list
            Collections.sort(unsortList);
            for(String temp: unsortList){
                CharSequence textHolder = "" + temp;



                adapterForSpinner.add(textHolder);

            }

            int defaultPosition = adapterForSpinner.getPosition("Select Package");
            // set the default according to value
            //spinnerList.setSelection(defaultPosition);

            // android.os.Debug.waitForDebugger();
            // AlertsBoxFactory.showAlert(">>>  "+tmp_val,context );

        } catch (Exception e) {
            e.printStackTrace();
        }
        // progressDiaUtils.dismiss();
    }

    private String readPackageListXML() {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String str_xml = "";

        try {
            br = new BufferedReader(new FileReader(new File(xml_file_with_path)));
            // br = new BufferedReader(new
            // InputStreamReader(getAssets().open("xml/PackageList.xml")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
            str_xml = sb.toString();
            // Log.i(">>>XML <<< ", str_xml);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "PackageList File Not Found.", Toast.LENGTH_LONG).show();
            Toast.makeText(context,
                    "Create file mypage/PackageList.xml", Toast.LENGTH_LONG)
                    .show();

        } finally {
            try {
                if(br != null)
                    br.close(); // stop reading
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return str_xml;
    }

    private void writeXMLFile() {
        // Log.i(">>>XML <<< ", strXML);
        try {
            File direct = new File(extStorageDirectory + "/" + xml_folder);
            if (!direct.exists()) {
                direct.mkdirs(); // directory is created;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        FileOutputStream fOut = null;
        OutputStreamWriter xmlOutWriter = null;

        try {
            File xmlFile = new File(xml_file_with_path);
            xmlFile.createNewFile();
            fOut = new FileOutputStream(xmlFile);
            xmlOutWriter = new OutputStreamWriter(fOut);
            xmlOutWriter.append(strXML);

			/*Toast.makeText(getBaseContext(),
					"Done writing '/mypage/'"+xml_file+" file.",
					Toast.LENGTH_LONG).show();*/
            // new ReadPackageListXML().execute();

        }catch(IOException io){
            io.printStackTrace();
            Toast.makeText(context, io.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } finally {
            try {
                if(xmlOutWriter != null){
                    xmlOutWriter.flush();
                    xmlOutWriter.close();
                }
                if(xmlOutWriter != null){
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException io) {
            }
            strXML = null;
        }
        setPackageList();
    }

    private class PackageListWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        String ConnectionTypeId="",AreaId="";

        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label),true,true,this);
            SharedPreferences sharedPreferences1 = context.getApplicationContext()
                    .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
            ConnectionTypeId=sharedPreferences1.getString("ConnectionTypeId", "0");
            AreaId=sharedPreferences1.getString("AreaCode", "0");
        }
        @Override
        protected void onCancelled() {
            if(is_activity_running)
                mProgressHUD.dismiss();
            packageListWebService = null;
        }
        protected void onPostExecute(Void unused) {


            packageListWebService = null;
            if(is_activity_running)
                mProgressHUD.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {
                writeXMLFile();
            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
               /* PackgeCaller caller = new PackgeCaller(getApplicationContext()
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_PACKAGELIST_BY_CONNECTIONTYPEID));*/
                PackgeCaller caller = new PackgeCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_BINDPACKAGES));
                caller.setMemberId(Long.parseLong(MemberId));
                //  caller.setConnectionTypeId(ConnectionTypeId);
                //  caller.setAreaCode(AreaId);
                caller.setUserLoginName(memberloginid);
                caller.setIspid(ispid);
                caller.setAreaCode((AreaId));
                caller.setConnectionTypeId((ConnectionTypeId));
                caller.setPostPaid(ispostpaid);
                caller.setSid(0);
                caller.join();
                caller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
            }
            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

    }

    private class AdjustmentWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        ProgressHUD mProgressHUD;



        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,false, this);

        }
        @Override
        protected void onCancelled() {
            if(is_activity_running)
                mProgressHUD.dismiss();
            adjustmentWebService = null;

        }
        protected void onPostExecute(Void unused) {
            if(is_activity_running)
                mProgressHUD.dismiss();
            adjustmentWebService = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                try {
                    NewPlanRate = Double.parseDouble(adjStringVal);
                    if(NewPlanRate!=0){
                        if(additionalAmount!=null){
                            if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
                                NewPlanRate=(NewPlanRate+Double.valueOf(additionalAmount.getAdditionalAmount()));
                            }
                            if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
                                NewPlanRate=(NewPlanRate-Double.valueOf(additionalAmount.getDiscountAmount()));
                            }
                            additionalAmount.setFinalcharges(String.valueOf(NewPlanRate));
                            price.setText(Double.toString(NewPlanRate));
                            validity.setText(PackageValidity);
                            servicetax.setText(ServcieTax);
                            isAdjOptionClick = true;
                        }
                        else{
                            RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioPayMode);
                            radioGroup.clearCheck();
                            /*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/

                            AlertsBoxFactory.showAlert("Conversion is not possible.", context);
                        }
                    }
                    else{
                        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioPayMode);
                        radioGroup.clearCheck();
                        /*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/

                        AlertsBoxFactory.showAlert("Conversion is not possible.", context);
                    }
                } catch (NumberFormatException nue) {
                    RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioPayMode);
                    radioGroup.clearCheck();
                    /*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/
                    if(adjStringVal.equalsIgnoreCase("anyType{}")){
                        AlertsBoxFactory.showAlert("Conversion is not possible.", context);
                    }else{
                        AlertsBoxFactory.showAlert(adjStringVal, context);
                    }
                }

            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }
            isFinish=true;
            //this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                AdjustmentCaller adjCaller = new AdjustmentCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL),
                        context.getResources().getString(
                                R.string.METHOD_PACKAGEADJUSTMENT));
                adjCaller.setMemberId(Long.parseLong(MemberId));
                setPlanDetails();
                adjCaller.setNewPlan(packageList.getPlanName());
                //adjCaller.setAreaCode(AreaCode);
                //adjCaller.setAreaCodeFilter(AreaCodeFilter);

                adjCaller.join();
                adjCaller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
            }
            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
        }

    }


    private String str_planname;
    private void setPlanDetails() {

        //String selecte_plan = btn_planlist.getText().toString();
        String map_kye = selected_plan;

        PackageList packageList = (PackageList) mapPackageList.get(map_kye);
        if (packageList != null) {
            str_planname = packageList.getPlanName();
            //str_planrate = packageList.getPackageRate();
            try{
                NewPlanRate = Double.parseDouble(String.valueOf(packageList.getPackageRate()));
                PackageValidity = String.valueOf((packageList.getPackagevalidity()));
                ServcieTax = (packageList.getServiceTax());
                PackageId= (packageList.getPackageId());
                //Utils.log("PackageId", ":"+packageList.getPackageId());
                Utils.log("PackageName", ":"+packageList.getPlanName());

            }catch(NumberFormatException nu){
                NewPlanRate = 0;
                PackageValidity = "0";
                ServcieTax = "0";
                PackageId="";
            }
        } else {
            str_planname = CurrentPlan;
            //str_planrate = PlanRate + "";
            NewPlanRate = 0;
            PackageValidity = "0";
            ServcieTax = "0";
            PackageId="";
        }

    }

    //*******************************Check Valid User Web Service*********starts here**************************

    protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {


        ProgressHUD mProgressHUD;


        // final AlertDialog alert =new
        // AlertDialog.Builder(Login.this).create();

        //private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);

        protected void onPreExecute() {

            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true, this);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                Utils.log("Valid User","Progress Not Showing");
            }
            else{

            }

        }

        @SuppressLint("CommitPrefEdits")
        protected void onPostExecute(Void unused) {
            if(is_activity_running)
                mProgressHUD.dismiss();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            }
            else{


            }
            if (rslt.trim().equalsIgnoreCase("ok")) {

                if (isVaildUser) {
                    //new PackageListWebService().execute();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        new SettingResultAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
                    }
                    else{
                        new SettingResultAsyncTask().execute();
                    }
                } else {
                    //mProgressHUD.dismiss();
                    SharedPreferences sharedPreferences1 = context
                            .getSharedPreferences(sharedPreferences_name, 0); // 0
                    // -

                    SharedPreferences.Editor edit1 = sharedPreferences1.edit();
                    edit1.clear();

                    SharedPreferences sharedPreferences2 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_renewal),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit2 = sharedPreferences2.edit();
                    edit2.clear();

                    SharedPreferences sharedPreferences3 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_notification_list),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit3 = sharedPreferences3.edit();
                    edit3.clear();

                    SharedPreferences sharedPreferences4 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_payment_history),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit4 = sharedPreferences4.edit();
                    edit4.clear();

                    SharedPreferences sharedPreferences5 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_profile),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit5 = sharedPreferences5.edit();
                    edit5.clear();
                    //	Utils.log("Data","cleared");
                    sharedPreferences1.edit().clear().commit();

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //tell the Dialog to use the dialog.xml as it's layout description
                    dialog.setContentView(R.layout.dialog_box);
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



                    TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

                    TextView txt = (TextView) dialog.findViewById(R.id.tv);

                    txt.setText(Html.fromHtml("You are allowed to use app only on one device"));
                    dtv.setText(Html.fromHtml("Alert"));
                    Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().finish();
                            Intent intent = new Intent(
                                    context,
                                    LoginFragmentActivity.class);
                            intent.putExtra("from", "2");
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    //(width/2)+((width/2)/2)
                    //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);

                }
            } else {
                //mProgressHUD.dismiss();
            }

        }



        @Override
        protected Void doInBackground(String... params) {
            try {

                //	Log.i("START",">>>>>>>START<<<<<<<<");
                SMSAuthenticationCaller smsCaller = new SMSAuthenticationCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GET_SMS_AUTHENTICATION)
                );
                smsCaller.PhoneUniqueId= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                smsCaller.MemberId = MemberId;
                smsCaller.OneTimePwd=otp_password;
                //smsCaller.setAllData(true);
                smsCaller.setCallData("newchange");
                smsCaller.join();
                smsCaller.start();
                rslt = "START";

                while (rslt.equalsIgnoreCase("START")){
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {

            }
            return null;
        }
        @Override
        protected void onCancelled() {
            //mProgressHUD.dismiss();
            mProgressHUD.dismiss();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

/*		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}*/

    }

//*******************************Check Valid User Web Service*********ends here**************************

    //******************************* Web Service*********starts here**************************
    public class SettingResultAsyncTask extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        String Arearslt="";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,false, this);
            super.onPreExecute();
            settingResult="";
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            AreaWiseSettingSOAP areaWiseSettingSOAP= new  AreaWiseSettingSOAP(
                    context.getResources().getString(
                            R.string.WSDL_TARGET_NAMESPACE),
                    context.getResources().getString(
                            R.string.SOAP_URL), context
                    .getResources().getString(
                            R.string.METHOD_GET_AREA_SETTING)
            );
            SharedPreferences sharedPreferences1 = context
                    .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
            String areaCode=sharedPreferences1.getString("AreaCode", "0");

            try {
                Arearslt=	areaWiseSettingSOAP.getAreaWiseSetting(areaCode, "UP");
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(is_activity_running)
                mProgressHUD.dismiss();
            super.onPostExecute(result);
            if(Arearslt.length()>0){
                if(Arearslt.equalsIgnoreCase("ok")){
                    if(settingResult.equalsIgnoreCase("0")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                context);
                        builder.setMessage(
                                "This Facility is not available !! ")
                                .setTitle("Alert")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                // Toast.makeText(NotificationListActivity.this,
                                                // ""+selectedFromList.getNotifyId(),
                                                // Toast.LENGTH_SHORT).show();

                                                getActivity().finish();
                                                Intent intent = new Intent(
                                                        context,
                                                        IONHome.class);

                                                startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                            new PackageListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                            new PackageListWebService().execute();
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            context);
                    builder.setMessage(
                            "Please try again!! ")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // Toast.makeText(NotificationListActivity.this,
                                            // ""+selectedFromList.getNotifyId(),
                                            // Toast.LENGTH_SHORT).show();

                                            getActivity().finish();
                                            Intent intent = new Intent(
                                                    context,
                                                    IONHome.class);

                                            startActivity(intent);
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                builder.setMessage(
                        "Please try again!!  ")
                        .setTitle("Alert")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // Toast.makeText(NotificationListActivity.this,
                                        // ""+selectedFromList.getNotifyId(),
                                        // Toast.LENGTH_SHORT).show();

                                        getActivity().finish();
                                        Intent intent = new Intent(
                                                context,
                                                IONHome.class);

                                        startActivity(intent);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }



    }
    //******************************* Web Service*********starts here**************************

    public class GetFinalDeductedAmtAsyncTask extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        String getResponse="",getResult="";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            ll_addtional_details.setVisibility(View.GONE);
            llClickDetails.setVisibility(View.GONE);
            Utils.log("GetFinalDeductedAmtAsyncTask","started");
            mProgressHUD=ProgressHUD.show(context, getString(R.string.app_please_wait_label), true, false, this);
        }
        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            GetAdditionalAmountSOAP getAdditionalAmountSOAP= new GetAdditionalAmountSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ADDITIONAL_AMT));
            try {
                getResult=getAdditionalAmountSOAP.getAddtionalAmount(memberloginid, packageList.getPackageId(), "0");
                //Utils.log("PackageID",":"+PackageId);
                getResponse=getAdditionalAmountSOAP.getResponse();
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressHUD.dismiss();
            if(getResult.length()>0){
                if(getResult.equalsIgnoreCase("ok")){
                    if(getResponse.length()>0){
                        Utils.log("Get Additional",":"+getResponse);
                        parseAdditionalAmtData(getResponse);
                    }
                }
            }
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }

    }

    public void parseAdditionalAmtData(String json){
        JSONObject mainJson;
        try {
            mainJson = new JSONObject(json);
            JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
            if(NewDataSetJson.has("AdditionAmount")){

                JSONObject json_add_amt=NewDataSetJson.getJSONObject("AdditionAmount");
                PackageRate=json_add_amt.optString("PackageRate","0");
                NewPlanRate=Double.valueOf(json_add_amt.optString("finalcharges","0"));
                AdditionalAmount=json_add_amt.optString("AdditionalAmount","0");
                AdditionalAmountType=json_add_amt.optString("AdditionalAmountType","");
                DaysFineAmount=json_add_amt.optString("DaysFineAmount","0");
                DiscountAmount=json_add_amt.optString("DiscountAmount","0");
                finalcharges=json_add_amt.optString("finalcharges","0");
                FineAmount=json_add_amt.optString("FineAmount","0");
                DiscountPercentage=json_add_amt.optString("OnlineDiscountInPer","0");
                is_cheque_bounced	 =json_add_amt.optBoolean("IsChequeBounce",false);
                additionalAmount= new AdditionalAmount(PackageRate,AdditionalAmount, AdditionalAmountType, DaysFineAmount, DiscountAmount, finalcharges, FineAmount, DiscountPercentage,is_cheque_bounced);
                if(DiscountPercentage.length()>0){
                    if(Double.valueOf(DiscountPercentage)>0)
                        // tvLabelDiscount.setText("You have got "+DiscountPercentage+" % discount for online payment.");
                        tvLabelDiscount.setText("Avail of a "+DiscountPercentage+"% discount by paying online.");
                }
                if(AdditionalAmountType.length()>0){

                    renewOption.setVisibility(View.GONE);
                    adjOption.setVisibility(View.GONE);
                    TextView02.setVisibility(View.GONE);
                    compulsory_immediate=true;

                }
                else{
                    renewOption.setVisibility(View.VISIBLE);
                    adjOption.setVisibility(View.GONE);
                    TextView02.setVisibility(View.VISIBLE);
                    compulsory_immediate=false;

                    String	sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
                    SharedPreferences sharedPreferences2 = context
                            .getSharedPreferences(sharedPreferences_renewal, 0);
                    if(!sharedPreferences2.getString("PackageName", "-").equalsIgnoreCase(str_planname)){
                        adjOption.setVisibility(View.GONE);
                        datafrom="changepack";
                    }
                    else{
                        adjOption.setVisibility(View.GONE);
                        datafrom="";
                    }

                }

                if(Double.valueOf(additionalAmount.getFinalcharges())>0){
                    price.setText(additionalAmount.getFinalcharges());

                }
                if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
                    is_payemnt_detail=true;
                }
                if(Double.valueOf(additionalAmount.getFineAmount())>0){
                    is_payemnt_detail=true;
                }
                if(Double.valueOf(additionalAmount.getDiscountPercentage())>0){
                    ll_package_rate.setVisibility(View.VISIBLE);
                    is_payemnt_detail=true;
                }
                else{
                    ll_package_rate.setVisibility(View.GONE);
                    final_price_label.setText("Package Rate");
                }
                if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
                    is_payemnt_detail=true;

                }
                if(is_payemnt_detail){
                    price.setText(additionalAmount.getFinalcharges());
                    llClickDetails.setVisibility(View.VISIBLE);
                }
                else{
                    llClickDetails.setVisibility(View.GONE);
                }

                if(Double.valueOf(additionalAmount.getPackageRate())>0){

                    package_rate.setText(additionalAmount.getPackageRate());
                }
                else{

                }



                if(Double.valueOf(finalcharges)>0.0){


					/*Intent i = new Intent(ChangePackage_NewActivity.this,MakeMyPayments.class);
					i.putExtra("PackageName", spinnerList.getSelectedItem().toString());
					i.putExtra("PackageAmount", PackageRate);
					i.putExtra("PackageValidity", PackageValidity);
					i.putExtra("updateFrom", "I");
					i.putExtra("ServiceTax", servicetax.getText().toString());
					i.putExtra("datafrom", "Renew");
					i.putExtra("discount", DiscountPercentage);
					i.putExtra("addtional_amount", additionalamt_obj);
					i.putExtra("ClassName", ChangePackage_NewActivity.this.getClass().getSimpleName());
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

					/*i.putExtra("AdditionalAmount", AdditionalAmount);
					i.putExtra("AdditionalAmountType", AdditionalAmountType);
					i.putExtra("DaysFineAmount", DaysFineAmount);
					i.putExtra("DiscountAmount", DiscountAmount);
					i.putExtra("finalcharges", finalcharges);
					i.putExtra("DaysFineAmount", DaysFineAmount);
					i.putExtra("finalcharges", finalcharges);
					i.putExtra("FineAmount", FineAmount);*/

                    //RenewPackage.this.finish();

                    //showFinalDialog();
                }
                else{
                    AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void showPaymentDetails(AdditionalAmount additionalAmount){
        if(additionalAmount!=null){
            //ll_addtional_details.setVisibility(View.VISIBLE);
            LinearLayout ll_package_rate,ll_add_amt,ll_add_reason,ll_discount_amt,ll_fine_amount,ll_days_fine_amt,ll_discount_per,ll_final_amt;

            TextView tv_package_rate,tv_add_amt,tv_add_reason,tv_discount_amt,tv_fine_amount,tv_days_fine_amt,tv_discount_per,tv_final_amt;

            ll_package_rate=(LinearLayout)view.findViewById(R.id.ll_package_rate);
            ll_add_amt=(LinearLayout)view.findViewById(R.id.ll_add_amt);
            ll_add_reason=(LinearLayout)view.findViewById(R.id.ll_add_reason);
            ll_discount_amt=(LinearLayout)view.findViewById(R.id.ll_discount_amt);
            ll_fine_amount=(LinearLayout)view.findViewById(R.id.ll_fine_amt);
            ll_days_fine_amt=(LinearLayout)view.findViewById(R.id.ll_days_fine_amt);
            ll_discount_per=(LinearLayout)view.findViewById(R.id.ll_discount_per);
            ll_final_amt=(LinearLayout) view.findViewById(R.id.ll_final_amount);

            tv_package_rate=(TextView) view.findViewById(R.id.tv_package_rate);
            tv_add_amt=(TextView) view.findViewById(R.id.tv_add_amt);
            tv_add_reason=(TextView) view.findViewById(R.id.tv_add_reason);
            tv_discount_amt=(TextView) view.findViewById(R.id.tv_discount_amt);
            tv_fine_amount=(TextView) view.findViewById(R.id.tv_fine_amt);
            tv_days_fine_amt=(TextView)view. findViewById(R.id.tv_days_fine_amt);
            tv_discount_per=(TextView) view.findViewById(R.id.tv_discount_per);
            tv_final_amt=(TextView) view.findViewById(R.id.tv_final_amount);

            if(Double.valueOf(additionalAmount.getPackageRate())>0){
                ll_package_rate.setVisibility(View.VISIBLE);
                tv_package_rate.setText(additionalAmount.getPackageRate());
            }
            else{

                ll_package_rate.setVisibility(View.GONE);

            }

            if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
                ll_add_amt.setVisibility(View.VISIBLE);
                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
            }
            else{

                ll_add_amt.setVisibility(View.GONE);

            }

            if(additionalAmount.getAdditionalAmountType().length()>0){
                ll_add_reason.setVisibility(View.GONE);
                tv_add_reason.setText(additionalAmount.getAdditionalAmountType());
            }
            else{

                ll_add_reason.setVisibility(View.GONE);

            }

            if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
                ll_discount_amt.setVisibility(View.VISIBLE);
                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
            }
            else{

                ll_discount_amt.setVisibility(View.GONE);

            }

            if(Double.valueOf(additionalAmount.getFineAmount())>0){
                ll_fine_amount.setVisibility(View.VISIBLE);
                tv_fine_amount.setText(additionalAmount.getFineAmount());
            }
            else{

                ll_fine_amount.setVisibility(View.GONE);

            }

            if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
                ll_days_fine_amt.setVisibility(View.VISIBLE);
                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
            }
            else{

                ll_days_fine_amt.setVisibility(View.GONE);

            }
            if(additionalAmount.getDiscountPercentage().length()>0){
                if(Double.valueOf(additionalAmount.getDiscountPercentage())>0){
                    ll_discount_per.setVisibility(View.VISIBLE);
                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
                }
                else{
                    ll_discount_per.setVisibility(View.GONE);
                }
            }
            else{
                ll_discount_per.setVisibility(View.GONE);
            }

            if(Double.valueOf(additionalAmount.getFinalcharges())>0){
                ll_final_amt.setVisibility(View.VISIBLE);
                tv_final_amt.setText(additionalAmount.getFinalcharges());
            }
            else{
                ll_final_amt.setVisibility(View.GONE);
            }


        }
        else{
            ll_addtional_details.setVisibility(View.GONE);
        }
    }


    public void show_pg_dialog() {
        pg_dialog = new Dialog(context);
        pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pg_dialog.setContentView(R.layout.dialog_payment_gateway);

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
        RelativeLayout rl_payment_gateway_1=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_1);
        RelativeLayout rl_payment_gateway_2=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_2);

        // rl_payment_gateway_1.setVisibility(View.GONE);

        //rl_payment_gateway_2.setVisibility(View.GONE);

        rl_payment_gateway_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pg_dialog.dismiss();
                SharedPreferences sharedPreferences1 = context
                        .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
                int	atom=sharedPreferences1.getInt("Atom", 1);

                if(atom>0){
                    Utils.is_subpaisa=false;
                    Utils.is_atom=true;
                    proceed_to_pay();

                }
                else{
//					AlertsBoxFactory.showAlert(Utils.CAvenue_Message, ChangePackage.this);

                    AlertsBoxFactory.showAlert(Utils.Atom_Message, context);
                }

            }
        });

        rl_payment_gateway_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pg_dialog.dismiss();
                SharedPreferences sharedPreferences1 = context
                        .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);

                int subpaisa=sharedPreferences1.getInt("subpaisa", 1);
                if(subpaisa>0){
                    Utils.is_subpaisa=true;
                    Utils.is_atom=false;
                    proceed_to_pay();
                }
                else{
                    AlertsBoxFactory.showAlert(Utils.Atom_Message, context);
                }

            }
        });

        pg_dialog.show();
        pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pg_dialog.getWindow().setLayout((width/2)+(width/2), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void proceed_to_pay(){

        Utils.log("Compulsory Immediate",":"+compulsory_immediate);
        if(compulsory_immediate){
            Intent i = null;
            if(Utils.is_subpaisa) {
//                i = new Intent(context, MakePaymentSubpaisa.class);
//                i.putExtra("PackageName", tv_pkg_name.getText().toString());
//                i.putExtra("PackageAmount", price.getText().toString());
//                i.putExtra("PackageValidity", packageList.getPackagevalidity());
//                i.putExtra("updateFrom", "I");
//                i.putExtra("ServiceTax", servicetax.getText().toString());
//                i.putExtra("datafrom", datafrom);
//                i.putExtra("ClassName",context.getClass().getSimpleName());
//                i.putExtra("addtional_amount", additionalAmount);
//                startActivity(i);
//                BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));


                Fragment fragment = new MakePaymentSubpaisaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PackageName", tv_pkg_name.getText().toString());
                bundle.putString("PackageAmount", price.getText().toString());
                bundle.putInt("PackageValidity", packageList.getPackagevalidity());
                bundle.putString("updateFrom", "I");
                bundle.putString("ServiceTax", servicetax.getText().toString());
                bundle.putString("datafrom", datafrom);
                bundle.putString("ClassName", context.getClass().getSimpleName());
                bundle.putSerializable("addtional_amount", additionalAmount);
                fragment.setArguments(bundle);
                loadFragment(fragment);


            }
            else {
//                i = new Intent(context, MakeMyPayment_Atom.class);
//                Log.e("Validity1",":-"+packageList.getPackagevalidity());
//
//                i.putExtra("PackageName", tv_pkg_name.getText().toString());
//                i.putExtra("PackageAmount", price.getText().toString());
//                i.putExtra("PackageValidity", packageList.getPackagevalidity());
//                i.putExtra("updateFrom", "I");
//                i.putExtra("ServiceTax", servicetax.getText().toString());
//                i.putExtra("datafrom", datafrom);
//                i.putExtra("ClassName", context.getClass().getSimpleName());
//                i.putExtra("addtional_amount", additionalAmount);
//                startActivity(i);


                Fragment fragment = new MakeMyPayment_AtomFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PackageName", tv_pkg_name.getText().toString());
                bundle.putString("PackageAmount", price.getText().toString());
                bundle.putInt("PackageValidity", packageList.getPackagevalidity());
                bundle.putString("updateFrom", "I");
                bundle.putString("ServiceTax", servicetax.getText().toString());
                bundle.putString("datafrom", datafrom);
//                bundle.putString("ClassName", context.getClass().getSimpleName());
                bundle.putSerializable("addtional_amount", additionalAmount);
                fragment.setArguments(bundle);
                loadFragment(fragment);

            }
        }
        else{
            RadioGroup radiogrp = (RadioGroup) view.findViewById(R.id.radioPayMode);
            int id = radiogrp.getCheckedRadioButtonId();
            Utils.log("RadioGroupid","is:"+id);
            if(id == -1)
            {
                Toast.makeText(context,"Please Select Package Update details",
                        Toast.LENGTH_LONG).show();
            }
            else{
                Intent i =null;
                if(Utils.is_subpaisa) {
                    i = new Intent(context, MakePaymentSubpaisa.class);
                    i.putExtra("PackageName", tv_pkg_name.getText().toString());
                    i.putExtra("PackageAmount", price.getText().toString());
                    i.putExtra("PackageValidity", packageList.getPackagevalidity());
                    i.putExtra("updateFrom", updateFrom);
                    i.putExtra("ServiceTax", servicetax.getText().toString());
                    i.putExtra("datafrom", datafrom);
                    i.putExtra("ClassName", context.getClass().getSimpleName());
                    i.putExtra("addtional_amount", additionalAmount);

                    startActivity(i);
                    BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
                }
                else {
//                    Toast.makeText(context, "Testf", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new MakeMyPayment_AtomFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PackageName", tv_pkg_name.getText().toString());
                    bundle.putString("PackageAmount", price.getText().toString());
                    bundle.putInt("PackageValidity", packageList.getPackagevalidity());
                    bundle.putString("updateFrom", "I");
                    bundle.putString("ServiceTax", servicetax.getText().toString());
                    bundle.putString("datafrom", datafrom);
//                    bundle.putString("ClassName", context.getClass().getSimpleName());
                    bundle.putSerializable("addtional_amount", additionalAmount);
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                }
            }
        }

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
    public void show_payment_options(){
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
        show_pg_dialog();
//        int CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);
//        int Atom=sharedPreferences1.getInt("Atom", 1);
//        int	paytm=sharedPreferences1.getInt("PAYTM", 0);


       /* int subpaisa=sharedPreferences1.getInt("subpaisa", 0);
        int atom=sharedPreferences1.getInt("Atom", 0);


        Utils.log("subpaisa", ":"+subpaisa);
        Utils.log("atom", ":"+atom);

        if(subpaisa>0&&icici>0){
            show_pg_dialog();
        }
        else if(subpaisa>0){
            Utils.is_subpaisa=true;
            Utils.is_icici=false;
            proceed_to_pay();
        }
        else if(icici>0){
            Utils.is_subpaisa=false;
            Utils.is_icici=true;
            proceed_to_pay();
        }*/
        /*else{
            AlertsBoxFactory.showAlert(Utils.Atom_Message, ChangePackage_NewActivity.this);
        }*/
    }




}