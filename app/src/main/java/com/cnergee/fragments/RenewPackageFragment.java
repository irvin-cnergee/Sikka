package com.cnergee.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.MakeMyPayment_Atom;
import com.cnergee.mypage.MakePaymentSubpaisa;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.PaymentPickup_New;
import com.cnergee.mypage.RenewPackage;
import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.SOAP.GetFinalPackageSOAP;
import com.cnergee.mypage.caller.PackageDetailCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RenewPackageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RenewPackageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    RelativeLayout pickuprequest,upgradepack;
    RelativeLayout creditdebit,netbanking;
    public String memberloginid="";
    private boolean flag= true;
    ProgressDialog mProgressDialog;
    public static String checkValue = "";
    public static String CheckForRenewal = "";
    //ImageView btnhomeimg;
    RelativeLayout rlSpecialOffer;
    String PackageName="",PackageRate="",ServiceTax="",PackageValidity="";
    public static Map<String, PackageDetails> mapPackageDetails;

    boolean isLogout = false;
    private boolean isFinish = false;
    public static Context context;
    Utils utils = new Utils();
    public static String rslt = "";
    public long memberid;
    private String sharedPreferences_name;


    private GetMemberDetailWebService getMemberDetailWebService = null;

    public static Map<String, MemberDetailsObj> mapMemberDetails;
    public boolean is_member_check=false;
    public boolean is_allow_renewal=false;

    String DiscountPercentage="",notificationMsg="";
    String meesage_restrict="";
    int count_run=1,isPhonerenew=0;;
    public static boolean  is_renew_running=false;
    TextView tv_marquee;
    Dialog pg_dialog;
    PackageDetails packageDetails;

    public RenewPackageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RenewPackage.
     */
    // TODO: Rename and change types and number of parameters
    public static RenewPackageFragment newInstance(String param1, String param2) {
        RenewPackageFragment fragment = new RenewPackageFragment();
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

        view = inflater.inflate(R.layout.fragment_renew_package, container, false);
        context = getActivity();
        ((IONHome)getActivity()).updateTitle(getString(R.string.trans_Renew));


        initViews(view);
        is_renew_running=true;

        if(Utils.isOnline(context)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
            }
            else{
                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);
            }
            checkValue="initial";
        }
        else{
            Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
        }


        sharedPreferences_name = getString(R.string.shared_preferences_name);
        final SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);
        memberloginid = utils.getMemberLoginID();

        SharedPreferences sharedPreferences1 = context
                .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0); // 0 - for private mode

        PackageName = sharedPreferences1.getString("PackageName", "-");
        PackageRate = sharedPreferences1.getString("Amount", "-");
        PackageValidity = sharedPreferences1.getString("PackageValidity", "-");
        ServiceTax = sharedPreferences1.getString("ServiceTax", "-");

        pickuprequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(Utils.isOnline(context)){
                    //new firstRunTask().execute();
                    Intent i = new Intent(context, PaymentPickup_New.class);
                    i.putExtra("intent_check", true);
                    startActivity(i);
                }
                else{
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //new GetFinalDeductedAmtAsyncTask().execute();

		/*btnhomeimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			RenewPackage.this.finish();
			Intent i = new Intent(RenewPackage.this,IONHome.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});*/

        upgradepack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(Utils.isOnline(context)) {
                    //RenewPackage.this.finish();
					/*Intent i = new Intent(RenewPackage.this,PackgedetailActivity.class);
					i.putExtra("check_intent",true);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                    if (!packageDetails.getCheckForRenewal().equalsIgnoreCase("GO") && isPhonerenew == 0) {
                        //	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
                        is_allow_renewal = false;
                        Utils.log("Allow NOT GO", ":" + is_allow_renewal);
                        meesage_restrict = packageDetails.getCheckForRenewal();
                        if (is_renew_running)
                            AlertsBoxFactory.showAlert(packageDetails.getCheckForRenewal(), context);
                    } else {
                        if (isPhonerenew == 0) {
//                            Intent i = new Intent(context, PackgedetailActivity.class);
//                            i.putExtra("check_intent", true);
//                            startActivity(i);

                            Fragment fragment = new PackgedetailFragment();
                            loadFragment(fragment);

                        } else {
                            AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        netbanking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Utils.isOnline(context)) {

                    checkValue = "netbanking";
                    if (!packageDetails.getCheckForRenewal().equalsIgnoreCase("GO") && isPhonerenew == 0) {
                        //	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
                        is_allow_renewal = false;
                        Utils.log("Allow NOT GO", ":" + is_allow_renewal);
                        meesage_restrict = packageDetails.getCheckForRenewal();
                        if (is_renew_running)
                            AlertsBoxFactory.showAlert(packageDetails.getCheckForRenewal(), context);
                    } else {
                        if (isPhonerenew == 0) {
                            show_payment_options("netbanking");
                        } else {
                            AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });


        creditdebit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Utils.isOnline(context)){

					/*SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
					int	Atom=sharedPreferences1.getInt("Atom", 0);

					int PAYTM = sharedPreferences.getInt("PAYTM", 0);

					Log.e("",":"+PAYTM);


					if(Atom>0){
						checkValue="creditdebit";
						if(!is_member_check){
							getMemberDetailWebService = new GetMemberDetailWebService();
							getMemberDetailWebService.execute((String) null);
						}
						else{
							Utils.log("Allow",":"+is_allow_renewal);
							if(is_allow_renewal){
							new GetFinalDeductedAmtAsyncTask().execute();
							}
							else{
								if(is_renew_running)
								AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
							}
						}
						Intent i = new Intent(RenewPackage.this,MakeMyPayments.class);
						i.putExtra("PackageName", PackageName);
						i.putExtra("PackageAmount", PackageRate);
						i.putExtra("PackageValidity", PackageValidity);
						i.putExtra("updateFrom", "I");
						i.putExtra("ServiceTax", ServiceTax);
						i.putExtra("datafrom", "Renew");
						startActivity(i);
						RenewPackage.this.finish();
					}else if(PAYTM>0){
                        checkValue="creditdebit";
                        if(!is_member_check){
                            getMemberDetailWebService = new GetMemberDetailWebService();
                            getMemberDetailWebService.execute((String) null);
                        }
                        else{
                            Utils.log("Allow",":"+is_allow_renewal);
                            if(is_allow_renewal){
                                new GetFinalDeductedAmtAsyncTask().execute();
                            }
                            else{
                                if(is_renew_running)
                                    AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
                            }
                        }
                        Intent i = new Intent(RenewPackage.this,MakeMyPayment_PayTmActivity.class);
                        i.putExtra("PackageName", PackageName);
                        i.putExtra("PackageAmount", PackageRate);
                        i.putExtra("PackageValidity", PackageValidity);
                        i.putExtra("updateFrom", "I");
                        i.putExtra("ServiceTax", ServiceTax);
                        i.putExtra("datafrom", "Renew");
                        startActivity(i);
                        RenewPackage.this.finish();
                    }
					else{
						Toast.makeText(getApplicationContext(), Utils.Atom_Message, Toast.LENGTH_LONG).show();
					}*/
                    checkValue="creditdebit";
                    if (!packageDetails.getCheckForRenewal().equalsIgnoreCase("GO") && isPhonerenew == 0) {
                        //	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
                        is_allow_renewal = false;
                        Utils.log("Allow NOT GO", ":" + is_allow_renewal);
                        meesage_restrict = packageDetails.getCheckForRenewal();
                        if (is_renew_running)
                            AlertsBoxFactory.showAlert(packageDetails.getCheckForRenewal(), context);
                    }
                    else {
                        if (isPhonerenew == 0) {
                            show_payment_options("creditdebit");
                        } else {
                            AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
                        }
                    }

                }
                else{
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void initViews(View view) {

        pickuprequest = (RelativeLayout)view.findViewById(R.id.pickuprequest);

        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);
        tv_marquee.setSelected(true);
        creditdebit = (RelativeLayout)view.findViewById(R.id.creditdebit);
        netbanking = (RelativeLayout)view.findViewById(R.id.netbanking);
        //rlSpecialOffer = (RelativeLayout)findViewById(R.id.rlSpecialOffer);
        upgradepack = (RelativeLayout)view.findViewById(R.id.upgradepack);


    }

    private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
            Utils.log("count","is:"+count_run);
            count_run++;
        }

        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            mProgressHUD.dismiss();

            try{
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (mapPackageDetails != null) {

                        Utils.log("Allow onPost",":"+is_allow_renewal);
                        Set<String> keys = mapPackageDetails.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                            str_keyVal = (String) i.next();

                        }
                        String selItem = str_keyVal.trim();
                        isLogout = false;
                        //finish();
                        packageDetails = mapPackageDetails.get(selItem);

                        is_member_check=true;

					/*txtloginid.setText(Details.getMemberLoginId());
					txtregisterdate.setText(packageDetails.getMemberRegsiterDate());
					txtexpdate.setText(packageDetails.getExpiryDate());
					txtpackage.setText(packageDetails.getPackageName());
					txtipaddress.setText(packageDetails.getIpAddress());*/

                        PackageName = packageDetails.getPackageName();
                        PackageRate = packageDetails.getAmount();
                        ServiceTax = packageDetails.getServiceTax();
                        PackageValidity = packageDetails.getPackageValidity();
                        CheckForRenewal = packageDetails.getCheckForRenewal();
                        isPhonerenew = packageDetails.getIsPhoneRenew();
                        notificationMsg=packageDetails.getNotificationMsg();
                        tv_marquee.setText(packageDetails.getNotificationMsg());

                        if(!packageDetails.getCheckForRenewal().equalsIgnoreCase("GO") && isPhonerenew == 0){
                            //	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
                            is_allow_renewal=false;
                            Utils.log("Allow NOT GO",":"+is_allow_renewal);
                            meesage_restrict=packageDetails.getCheckForRenewal();
                            if(is_renew_running)
                                AlertsBoxFactory.showAlert(packageDetails.getCheckForRenewal(),context );
                        }
                        else
                        {
                            is_allow_renewal=true;
                            if(checkValue.equalsIgnoreCase("initial")){
                                Utils.log("Allow Initial",":"+is_allow_renewal);
                            }
                            if(checkValue.equalsIgnoreCase("netbanking")){
                                Utils.log("Allow Initial",":"+is_allow_renewal);
                                new GetFinalDeductedAmtAsyncTask().execute();
                                new GetFinalPriceAsynctask().execute();
                            }
                            if(checkValue.equalsIgnoreCase("creditdebit")){
                                new GetFinalDeductedAmtAsyncTask().execute();
                                new GetFinalPriceAsynctask().execute();
                            }
                        }
                    }
                }else if (rslt.trim().equalsIgnoreCase("not")) {
                    if(is_renew_running)
                        AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
                }else{
                    if(is_renew_running)
                        AlertsBoxFactory.showAlert2(rslt,context );
                }
            }catch(Exception e){
                if(is_renew_running)
                    AlertsBoxFactory.showAlert(rslt,context );}
            isFinish=true;
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                PackageDetailCaller packagedetailCaller = new PackageDetailCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_PACKAGE_DETAILS)
                );

                packagedetailCaller.memberloginid = memberloginid;


                packagedetailCaller.join();
                packagedetailCaller.start();
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
            mProgressHUD.dismiss();
            getMemberDetailWebService = null;
        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

    public class GetFinalPriceAsynctask extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        ProgressHUD mProgressHUD;
        String getFinalPriceResult="",getFinalPriceResponse="";
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true, this);
        }
        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            GetFinalPackageSOAP getFinalPackageSOAP= new GetFinalPackageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_CALCULATOR_FINAL_PRICE));
            try {
                getFinalPriceResult=getFinalPackageSOAP.getFinalPrice(PackageName, PackageRate);
                getFinalPriceResponse=getFinalPackageSOAP.getResponse();

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
            if(getFinalPriceResult.length()>0){
                if(getFinalPriceResult.equalsIgnoreCase("ok")){
                    if(getFinalPriceResponse.length()>0){
                        String[] final_price_discount=getFinalPriceResponse.split("#");
                        if(final_price_discount.length>1){
                            PackageRate=final_price_discount[0];
                            DiscountPercentage=final_price_discount[1];
                            if(Double.valueOf(PackageRate)>0.0){
                                if(is_renew_running){

									/*Intent i = new Intent(RenewPackage.this,MakeMyPayment_PayTmActivity.class);
									i.putExtra("PackageName", PackageName);
									i.putExtra("PackageAmount", PackageRate);
									i.putExtra("PackageValidity", PackageValidity);
									i.putExtra("updateFrom", "S");
									i.putExtra("ServiceTax", ServiceTax);
									i.putExtra("datafrom", "Renew");
									i.putExtra("discount", DiscountPercentage);
									i.putExtra("ClassName", RenewPackage.this.getClass().getSimpleName());
									startActivity(i);
									RenewPackage.this.finish();
									overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                    //showFinalDialog();
                                }
                            }
                            else{
                                AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
                            }

                        }
                        else{
                            AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
                        }
                    }
                    else{
                        AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
                    }
                }
                else{
                    AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
                }
            }
            else{
                AlertsBoxFactory.showAlert(getString(R.string.fail_response), context);
            }
        }
        @Override
        public void onCancel(DialogInterface arg0) {
            // TODO Auto-generated method stub

        }
        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();


        }
    }


    public class GetFinalDeductedAmtAsyncTask extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        String getResponse="",getResult="";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Utils.log("GetFinalDeductedAmtAsyncTask","started");
            mProgressHUD=ProgressHUD.show(context, getString(R.string.app_please_wait_label), true, false, this);
        }
        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            GetAdditionalAmountSOAP getAdditionalAmountSOAP= new GetAdditionalAmountSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ADDITIONAL_AMT));
            try {
                getResult=getAdditionalAmountSOAP.getAddtionalAmount(memberloginid, "0", "0");
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
                String PackageRate=json_add_amt.optString("PackageRate","0");
                String AdditionalAmount=json_add_amt.optString("AdditionalAmount","0");
                String AdditionalAmountType=json_add_amt.optString("AdditionalAmountType","");
                String DaysFineAmount=json_add_amt.optString("DaysFineAmount","0");
                String DiscountAmount=json_add_amt.optString("DiscountAmount","0");
                String finalcharges=json_add_amt.optString("finalcharges","0");
                String FineAmount=json_add_amt.optString("FineAmount","0");
                DiscountPercentage=json_add_amt.optString("OnlineDiscountInPer","0");
                Boolean is_cheque_bounce=json_add_amt.optBoolean("IsChequeBounce",true);
                com.cnergee.mypage.obj.AdditionalAmount additionalamt_obj= new AdditionalAmount(PackageRate,AdditionalAmount, AdditionalAmountType, DaysFineAmount, DiscountAmount, finalcharges, FineAmount, DiscountPercentage,is_cheque_bounce);


                if(Double.valueOf(finalcharges)>0.0){

                    if(is_cheque_bounce){
                        AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), context);
                    }else{

                        proceed_to_pay(additionalamt_obj);

				/*//	Intent i = new Intent(RenewPackage.this,MakeMyPayments_CCAvenue.class);

					Intent i = new Intent(RenewPackage.this,MakeMyPayment_Atom.class);

					i.putExtra("PackageName", PackageName);
					i.putExtra("PackageAmount", PackageRate);
					i.putExtra("PackageValidity", PackageValidity);
					i.putExtra("updateFrom", "S");
					i.putExtra("ServiceTax", ServiceTax);
					i.putExtra("datafrom", "Renew");
					i.putExtra("discount", DiscountPercentage);
					i.putExtra("addtional_amount", additionalamt_obj);


					i.putExtra("ClassName", RenewPackage.this.getClass().getSimpleName());
					startActivity(i);
					//RenewPackage.this.finish();
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					//showFinalDialog();*/
                    }
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

    public void show_pg_dialog() {
        pg_dialog = new Dialog(context);
        pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pg_dialog.setContentView(R.layout.dialog_payment_gateway);

        int width = 0;
        int height =0;


        Point size = new Point();
        WindowManager w =((Activity)context).getWindowManager();

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

        //  rl_payment_gateway_2.setVisibility(View.GONE);
        // rl_payment_gateway_1.setVisibility(View.GONE);


        rl_payment_gateway_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // TODO Auto-generated method stub
                pg_dialog.dismiss();
                SharedPreferences sharedPreferences1 = context
                        .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);

                int atom=sharedPreferences1.getInt("Atom", 1);
                if(atom>0){
                    //proceed_to_pay();
                    Utils.is_subpaisa=false;
                    Utils.is_atom=true;
                    checkValue="netbanking";
                    if(!is_member_check){
                        getMemberDetailWebService = new GetMemberDetailWebService();
                        getMemberDetailWebService.execute((String) null);
                    }
                    else{

                        Utils.log("Allow",":"+is_allow_renewal);
                        if(is_allow_renewal){
                            new GetFinalDeductedAmtAsyncTask().execute();
                        }
                        else{
                            if(is_renew_running)
                                AlertsBoxFactory.showAlert(meesage_restrict, context);
                        }
                    }
                }else{
                    AlertsBoxFactory.showAlert(Utils.Paytm_Message, context);
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
                    //proceed_to_pay();

                    checkValue="netbanking";
                    if(!is_member_check){
                        getMemberDetailWebService = new GetMemberDetailWebService();
                        getMemberDetailWebService.execute((String) null);
                    }
                    else{
                        Utils.log("Allow",":"+is_allow_renewal);
                        if(is_allow_renewal){
                            new GetFinalDeductedAmtAsyncTask().execute();
                        }
                        else{
                            if(is_renew_running)
                                AlertsBoxFactory.showAlert(meesage_restrict, context);
                        }
                    }
                }
                else{
                    AlertsBoxFactory.showAlert(Utils.Paytm_Message, context);
                }
            }
        });

        pg_dialog.show();
        pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pg_dialog.getWindow().setLayout((width/2)+(width/2), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void proceed_to_pay(AdditionalAmount additionalAmount){
        Intent i =null;
        if(Utils.is_subpaisa) {
            i = new Intent(context, MakePaymentSubpaisa.class);
            i.putExtra("PackageName", PackageName);
            i.putExtra("PackageAmount", PackageRate);
            i.putExtra("PackageValidity", PackageValidity);
            i.putExtra("updateFrom", "S");
            i.putExtra("ServiceTax", ServiceTax);
            i.putExtra("datafrom", "Renew");
            i.putExtra("discount", DiscountPercentage);
            i.putExtra("addtional_amount", additionalAmount);

            i.putExtra("ClassName", context.getClass().getSimpleName());
            startActivity(i);
            //RenewPackage.this.finish();
        }else
            Log.e("PackageValidity",":--"+PackageValidity);

//            i = new Intent(context, MakeMyPayment_Atom.class);
//        i.putExtra("PackageName", PackageName);
//        i.putExtra("PackageAmount", PackageRate);
//        i.putExtra("PackageValidity", PackageValidity);
//        i.putExtra("updateFrom", "S");
//        i.putExtra("ServiceTax", ServiceTax);
//        i.putExtra("datafrom", "Renew");
//        i.putExtra("discount", DiscountPercentage);
//        i.putExtra("addtional_amount", additionalAmount);
//
//        i.putExtra("ClassName", context.getClass().getSimpleName());
//        startActivity(i);
        //RenewPackage.this.finish();

        Fragment fragment = new MakeMyPayment_AtomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PackageName", PackageName);
        bundle.putString("PackageAmount", PackageRate);
        bundle.putInt("PackageValidity", Integer.parseInt(PackageValidity));
        bundle.putString("updateFrom", "S");
        bundle.putString("ServiceTax", ServiceTax);
        bundle.putString("datafrom", "Renew");
        bundle.putString("discount", DiscountPercentage);
        bundle.putSerializable("addtional_amount", additionalAmount);
        fragment.setArguments(bundle);
        loadFragment(fragment);



    }

    public void show_payment_options(String check){
        SharedPreferences sharedPreferences1 = context
                .getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
        show_pg_dialog();
        //int PAYTM=sharedPreferences1.getInt("PAYTM", 0);

        //int Atom=sharedPreferences1.getInt("Atom", 0);
		/*int icici=sharedPreferences1.getInt("icici", 0);
		int subpaisa=sharedPreferences1.getInt("subpaisa", 0);


		Utils.log("subpaisa", ":"+subpaisa);
		Utils.log("PAYTM", ":"+icici);

		if(icici>0&&subpaisa>0){
			show_pg_dialog(check);
		}
		else if(icici>0){
			Utils.is_icici=true;
			Utils.is_subpaisa=false;
			start_payment_process();
		}

		else if(subpaisa>0){
			Utils.is_icici=false;
			Utils.is_subpaisa=true;
			start_payment_process();
		}
		else{
			AlertsBoxFactory.showAlert("Payment Gateways", RenewPackage.this);
		}*/
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();
    }

}