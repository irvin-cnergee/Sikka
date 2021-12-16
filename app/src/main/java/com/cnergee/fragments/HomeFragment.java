package com.cnergee.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.BaseActivity;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.MyappDataUsage;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.PaymentHistory;
import com.cnergee.mypage.PaymentPickup_New;
import com.cnergee.mypage.RenewPackage;
import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.PAckageDetailSOAP;
import com.cnergee.mypage.SOAP.UpdatePhoneDetailSOAP;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.sys.ExpiryBroadcastReceiver;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.utils.Utils.SID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    Utils utils = new Utils();
    private boolean flag = true;
    public static String rslt = "";
    public String memberloginid, MemberId;
    TextView txtPackage, txtamount, txtvalidity, txtpkgexpiry, txtUser,
            txtdayremaing;
    ImageView btnrenewal;
    LinearLayout linnhome, linnprofile, linnnotification, linnhelp;

	/*LinearLayout relativepaymenthistory, relativedatausage,
			relativeservicerequest, relativeupgradepackage,
			relativepickuprequest,  relativeTopup;*/

    RelativeLayout relativepaymenthistory, relativedatausage,
            relativeservicerequest, relativeupgradepackage,
            relativepickuprequest, relativeTopup;
    public boolean check_call = true;
    //relativerenewal
    LinearLayout relativerenewal;
    //ProgressDialog mProgressDialog;
    ProgressHUD mProgressHUD;
    private String sharedPreferences_name;
    private String sharedPreferences_renewal;
    public static String strXML = "";
    public static String TAG = "IONHome";
    public static String rsltUsername = "";
    public static String Username = "";
    public static String Password = "";
    // private boolean isFinish = false;
    public String s = "http://ionhungama.com/api_login.php";

    static String extStorageDirectory = Environment
            .getExternalStorageDirectory().toString();
    final static String xml_folder = "mypage";
    final static String TARGET_BASE_PATH = extStorageDirectory + "/"
            + xml_folder + "/";

    public String xml_file_postFix = "PackageList.xml";
    public String xml_file;
    public String xml_file_with_path;

    public static Map<String, PackageDetails> mapPackageDetails;
    private  GetMemberDetailWebService getMemberDetailWebService = null;

    boolean isLogout = false;
    IntentFilter intentFilter = new IntentFilter();
    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPreferences1;
    public boolean show_progress = false;
    String AppVersion = "0";
    public static boolean is_home_running = false;
    ImageView iv_explore_plans, iv_special_offer;
    TextView tv_explore_plans, tv_special_offer ,tv_marquee;
    public boolean is_activity_running = false,is_allow_renewal=false;
    Context context;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        view = inflater.inflate(R.layout.fragment_home,container,false);
        context = getActivity();
        ((IONHome)getActivity()).updateTitle(getString(R.string.trans_home));

        initControls(view);

        return view;


    }

    public void initControls(View view){
        setHasOptionsMenu(false);



        txtUser = (TextView) view.findViewById(R.id.tv_usertext);
        txtPackage = (TextView) view.findViewById(R.id.txtPackage);
        txtamount = (TextView) view.findViewById(R.id.txtamount);
        txtvalidity = (TextView) view.findViewById(R.id.txtvalidity);
        txtpkgexpiry = (TextView) view.findViewById(R.id.txtpkgexpiry);
        txtdayremaing = (TextView) view.findViewById(R.id.txtdayremainig);

        iv_explore_plans = (ImageView) view.findViewById(R.id.img_explore_plans);
        //	iv_special_offer=(ImageView)findViewById(R.id.img_special);

        iv_explore_plans.setImageResource(R.drawable.img_exploreplans);
//		iv_special_offer.setImageResource(R.drawable.img_specialoffer);

        tv_explore_plans = (TextView) view.findViewById(R.id.tv_explore_plan);
        //	tv_special_offer=(TextView)findViewById(R.id.tv_special_offer);
        tv_marquee= (TextView) view.findViewById(R.id.tv_marquee);

        relativepaymenthistory = (RelativeLayout)view.findViewById(R.id.relativepaymenthistory);
        relativedatausage = (RelativeLayout)view.findViewById(R.id.relativedatausage);
        relativeservicerequest = (RelativeLayout)view.findViewById(R.id.relativeservicerequest);
        relativeupgradepackage = (RelativeLayout)view.findViewById(R.id.relativeupgradepdackage);
        relativepickuprequest = (RelativeLayout)view.findViewById(R.id.relativepickuprequest);
        relativerenewal = (LinearLayout)view.findViewById(R.id.relativerenewal);



        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Your code goes here
                    URL url = null;
                    try {
                        url = new URL("http://150.129.48.28:8005/CCRMToMobileIntegration.asmx?wsdl");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        int respCode = connection.getResponseCode();
                        System.out.println(respCode);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        //boolean url_value = exists("http://150.129.48.28:8005/CCRMToMobileIntegration.asmx?wsdl");
        //Log.e("url_value",":"+url_value);

        Utils.log("Density", "is:" + getResources().getDisplayMetrics().density);

        is_activity_running = true;
        Utils.log("" + this.getClass().getSimpleName(), "is:");
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        tv_marquee.setSelected(true);

        tv_explore_plans.setText("Upgrade Plans");

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            AppVersion = pInfo.versionName;
            Utils.log("Apppversion on Create", ":" + pInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        utils.setSharedPreferences(sharedPreferences);
        sharedPreferences1 = context.getSharedPreferences(
                getString(R.string.shared_preferences_renewal), 0);

        MemberId = utils.getMemberId();
        Intent i = getActivity().getIntent();
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(
                "CNERGEE_BILLING", 0);


        memberloginid = utils.getMemberLoginID();

        Utils.log("MemberId", ":" + memberloginid);

        /*
         * This SharedPrefernce used to check there is change in profile data
         */


        if (sharedPreferences1.getBoolean("renewal", true)) {
            Utils.log("Data From server IONHome ", "yes" + sharedPreferences1.getBoolean("renewal", true));
            show_progress = true;
            if (Utils.isOnline(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.execute((String) null);
                }

            }

        } else {
            Utils.log("Data From server IONHome", "offline " + sharedPreferences1.getBoolean("renewal", true));
            show_progress = false;
            setOfflineRenewalData();
            if (Utils.isOnline(context)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new GetCalciVersion().execute();

                SharedPreferences sharedPreferences_ = context
                        .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
                if (sharedPreferences_.getString("Gcm_reg_id", "").length() > 0) {
                    Utils.log("Reg id ", "SharedPreference:" + sharedPreferences_.getString("Gcm_reg_id", ""));

                } else {
                    Utils.getRegId(context);
                }
            }


            // *********************for expiry service****************

            SharedPreferences sharedPreferences3 = context
                    .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
            // private
            // mode
            if (sharedPreferences3.getBoolean("check_expiry", true)) {
                Intent intentService2 = new Intent(context,
                        ExpiryBroadcastReceiver.class);

                PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
                        intentService2, 0);
                AlarmManager alarm2 = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);

                int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
                Calendar calendar1 = new GregorianCalendar();
                calendar1.set(Calendar.HOUR_OF_DAY, 8);
                calendar1.set(Calendar.MINUTE, 05);
                calendar1.set(Calendar.SECOND, 0);
                calendar1.set(Calendar.MILLISECOND, 0);

                long triggerMillis = calendar1.getTimeInMillis();

                if (calendar1.getTimeInMillis() < Calendar.getInstance()
                        .getTimeInMillis()) {
                    triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
                    //System.out.println("Alarm will go off next day");
                }

                alarm2.cancel(pintent2);
                if (sharedPreferences3.getBoolean("check_expiry", true)) {
                    alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
                            calendar1.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pintent2);
                } else {

                    alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
                            AlarmManager.INTERVAL_DAY, pintent2);
                }
            }
        }


        //relativeTopup = (RelativeLayout) findViewById(R.id.relativeTopup);

//		relativeTopup.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(sharedPreferences1.getString("isFreePackage" , "false").equalsIgnoreCase("false")){
//					if(Utils.isOnline(IONHome.this)){
//
//						Intent i = new Intent(IONHome.this, SpecialOfferActivity.class);
//
//						startActivity(i);
//						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//
//						/*if(sharedPreferences1.getBoolean("is_24ol", true)){
//						Intent i = new Intent(IONHome.this, SpecialOfferActivity.class);
//
//						startActivity(i);
//						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//
//						}
//						else{
//
//							Intent i = new Intent(IONHome.this,TopupActivity.class);
//							i.putExtra("check_intent", false);
//							startActivity(i);
//							overridePendingTransition(R.anim.slide_in_right,
//									R.anim.slide_out_left);
//							Utils.Last_Class_Name=IONHome.this.getClass().getSimpleName();
//						}*/
//					}else{
//
//							Toast.makeText(getApplicationContext(), "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
//						}
//
//
//				}else{
//					AlertDialog.Builder builder = new AlertDialog.Builder(IONHome.this);
//
//				  builder.setMessage("This Facility is not available.")
//					.setTitle("Alert")
//					.setCancelable(false)
//					.setPositiveButton("OK",
//							new DialogInterface.OnClickListener(){
//						public void onClick(
//							DialogInterface dialog,int id){
//						}
//					});
//				AlertDialog alert = builder.create();
//				}
//			}
//		});
        relativedatausage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //	IONHome.this.finish();
//                Intent i = new Intent(context, MyappDataUsage.class);
//                startActivity(i);


                Fragment fragment = new MyappDataUsageFragment();
                loadFragment(fragment);

//                overridePendingTransition(R.anim.slide_in_right,
//                        R.anim.slide_out_left);
                Utils.Last_Class_Name = context.getClass().getSimpleName();

            }
        });
        relativepaymenthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                  Fragment fragment = new PaymentHistoryFragment();
                  loadFragment(fragment);
//                Intent i = new Intent(context, PaymentHistory.class);
//                startActivity(i);
////                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                Utils.Last_Class_Name = context.getClass().getSimpleName();
            }
        });

        relativeservicerequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPreferences1.getString("isFreePackage", "false").equalsIgnoreCase("false")) {
                    if (Utils.isOnline(context)) {
                        //IONHome.this.finish();
//                        Intent i = new Intent(context, Complaints.class);
//                        i.putExtra("check_intent", false);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in_right,
//                                R.anim.slide_out_left);

                        Fragment fragment = new ComplaintsFragment();
                        loadFragment(fragment);

                        Utils.Last_Class_Name = context.getClass().getSimpleName();

                    } else {

                        Toast.makeText(context, "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("This Facility is not available.")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                        }
                                    });
                    AlertDialog alert = builder.create();
                }
            }
        });

        relativeupgradepackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences1.getString("IsFreePackage", "false")
                        .equalsIgnoreCase("false")) {
                    if (Utils.isOnline(context)) {
                        //RenewPackage.this.finish();
						/*Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
						i.putExtra("check_intent", true);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_		right, R.anim.slide_out_left);*/

                        //						if (!sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO")) {
//							//	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
//							is_allow_renewal = false;
//							Utils.log("Allow NOT GO", ":" + is_allow_renewal);
//							AlertsBoxFactory.showAlert(sharedPreferences1.getString("CheckForRenewal", ""), context);
//						} else {
////							if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
////								Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
////								i.putExtra("check_intent", true);
////								startActivity(i);
////								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////							} else {
////								AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
////							}
//
//
////                            if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
//                                Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
//                                i.putExtra("check_intent", true);
//                                startActivity(i);
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                            } else {
////                                AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
////                            }
//
//						}


//							if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
//								Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
//								i.putExtra("check_intent", true);
//								startActivity(i);
//								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//							} else {
//								AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
//							}


//                            if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
//                        Intent i = new Intent(context, PackgedetailActivity.class);
//                        i.putExtra("check_intent", true);
//                        startActivity(i);

                        Fragment fragment = new PackgedetailFragment();
                        loadFragment(fragment);

//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                            } else {
//                                AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
//                            }



                    }else {
                        Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                    }

					/*if(sharedPreferences1.getBoolean("is_24ol", true)){
						if(Utils.isOnline(IONHome.this)){
		            		//RenewPackage.this.finish();
		            		Intent i = new Intent(IONHome.this,ChangePackage.class);
		            		i.putExtra("check_intent",true);
		            		startActivity(i);
		            		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		            	}
		            	else{
		            		Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		            	}

					}
					else{

						if (Utils.isOnline(IONHome.this)) {
							//IONHome.this.finish();
							Intent i = new Intent(IONHome.this,Make_my_plan_Activity.class);
							i.putExtra("check_intent", false);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							Utils.Last_Class_Name=IONHome.this.getClass().getSimpleName();
						} else {
							Toast.makeText(getApplicationContext(),
									"Please connect to internet and try again!!",
									Toast.LENGTH_LONG).show();
						}
					}*/

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            context);
                    builder.setMessage("This Facility is not available.")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // Toast.makeText(NotificationListActivity.this,
                                            // ""+selectedFromList.getNotifyId(),
                                            // Toast.LENGTH_SHORT).show();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        relativepickuprequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences1.getString("IsFreePackage", "false")
                        .equalsIgnoreCase("false")) {
                    if (Utils.isOnline(context)) {
                        //IONHome.this.finish();
                        // Intent i = new
                        // Intent(IONHome.this,PymentPickupRequest.class);
                        Intent i = new Intent(context,
                                PaymentPickup_New.class);
                        i.putExtra("check_intent", false);
                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in_right,
//                                R.anim.slide_out_left);

                        Utils.Last_Class_Name = context.getClass().getSimpleName();
                        // new firstRunTask().execute();
                    } else {
                        Toast.makeText(context,
                                "Please connect to internet and try again!!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            context);
                    builder.setMessage("This Facility is not available.")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // Toast.makeText(NotificationListActivity.this,
                                            // ""+selectedFromList.getNotifyId(),
                                            // Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        relativerenewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences1.getString("IsFreePackage", "false")
                        .equalsIgnoreCase("false")) {
                    //IONHome.this.finish();
                    Fragment fragment = new RenewPackageFragment();
                    loadFragment(fragment);
//                    overridePendingTransition(R.anim.slide_in_right,
//                            R.anim.slide_out_left);
                    Utils.Last_Class_Name = context.getClass().getSimpleName();
                } else {
					/*AlertDialog.Builder builder = new AlertDialog.Builder(
							IONHome.this);
					builder.setMessage("This Facility is not available.")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();*/
                    AlertsBoxFactory.showAlert2("This Facility is not available.", context);
                }
            }
        });

        /*
         * Intent intentService = new Intent(IONHome.this,MyPageService.class);
         *
         * Calendar cal = Calendar.getInstance(); PendingIntent pintent =
         * PendingIntent.getService(context, 0, intentService, 0);
         *
         * AlarmManager alarm =
         * (AlarmManager)getSystemService(Context.ALARM_SERVICE); // Start every
         * 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
         * cal.getTimeInMillis(), 30*1000, pintent); // Start every 1mon..
         * alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
         * 60000*2, pintent);
         */

        // ***********************

        SharedPreferences sharedPreferences_gcm = context
                .getSharedPreferences(context.getString(R.string.shared_preferences_name), 0);
        String gcm_id = sharedPreferences_gcm.getString("Gcm_reg_id", "");



    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        is_home_running = true;
        is_activity_running = true;
        memberloginid = utils.getMemberLoginID();
        if (memberloginid.length() > 0) {

        } else {

        }
        try {


		/*if(!check_call)
		Utils.setupMenu(IONHome.this, this);*/

            //	else
            //		Utils.log("Reside Menu","not null");
            ImageView ivLogo = (ImageView)view.findViewById(R.id.imgdvois);

            SharedPreferences sharedPreferences = context
                    .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
            // private
            // mode
		/*if (sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/


            // if(sharedPreferences.getBoolean("check_expiry", true))
            // {
            Intent intentService1 = new Intent(context,
                    ExpiryBroadcastReceiver.class);

            PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
                    intentService1, 0);
            /*
             * Calendar calendar = Calendar.getInstance();
             * calendar.set(Calendar.HOUR_OF_DAY, 8); calendar.set(Calendar.MINUTE,
             * 05); calendar.set(Calendar.SECOND, 00);
             *
             * AlarmManager alarm2 =
             * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE); //
             * Start every 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
             * cal.getTimeInMillis(), 30*1000, pintent 24*60*60*1000); // Start
             * every 1mon.. //alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
             * calendar.getTimeInMillis(), 24*60*60*1000, pintent2);
             * alarm2.cancel(pintent2); alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
             * calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent2);
             */

            AlarmManager alarm2 = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
            Calendar calendar1 = new GregorianCalendar();
            calendar1.set(Calendar.HOUR_OF_DAY, 8);
            calendar1.set(Calendar.MINUTE, 05);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            long triggerMillis = calendar1.getTimeInMillis();

            if (calendar1.getTimeInMillis() < Calendar.getInstance()
                    .getTimeInMillis()) {
                triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
                System.out.println("Alarm will go off next day");
            }

            alarm2.cancel(pintent2);
            if (sharedPreferences.getBoolean("check_expiry", true)) {
                alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                        pintent2);
            } else {
                alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
                        AlarmManager.INTERVAL_DAY, pintent2);
            }
        } catch (StackOverflowError t) {
            // more general: catch(Error t)
            // anything: catch(Throwable t)
            System.out.println("Caught " + t);
            t.printStackTrace();
        }
    }


    private void setOfflineRenewalData() {
        // TODO Auto-generated method stub
        sharedPreferences_renewal = context.getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences2 = context
                .getSharedPreferences(sharedPreferences_renewal, 0); // 0 - for
        // private
        // mode

        txtPackage.setText(sharedPreferences2.getString("PackageName", "-"));
        txtUser.setText(sharedPreferences2.getString("SubscriberName", "-"));

        txtamount.setText(sharedPreferences2.getString("Amount", "-"));
        txtvalidity.setText(sharedPreferences2
                .getString("PackageValidity", "-") + "  Days");

        // String PackageExpiry=packageDetails.getExpiryDate();
        txtpkgexpiry.setText(sharedPreferences2.getString("ExpiryDate", "-"));

        txtdayremaing.setText(sharedPreferences2
                .getString("DaysRemaining", "-"));

        String sharedPreferences_profile = context.getString(R.string.shared_preferences_profile);

        SharedPreferences sharedPreferences =context
                .getSharedPreferences(sharedPreferences_profile, 0); // 0 - for
        // private
        // mode
        //txtUser.setText(sharedPreferences.getString("MemberName", "-"));
        getRemainigDays();
        // txtUser.setText(sharedPreferences2.getString("MemberName", "-"));
        //is_24ol

		/*if(sharedPreferences2.getBoolean("is_24ol", true)){
			iv_explore_plans.setImageResource(R.drawable.upgradepackage);
			iv_special_offer.setImageResource(R.drawable.img_specialoffer);
			tv_explore_plans.setText("Upgrade Plans");
			tv_special_offer.setText("Special Offers");
		}
		else{
			iv_explore_plans.setImageResource(R.drawable.create_plan);
			iv_special_offer.setImageResource(R.drawable.top_up);
			tv_explore_plans.setText("Create Your Plan");
			tv_special_offer.setText("Top Up");

		}*/
    }

    public void getRemainigDays() {
        int expDay, expMonth, expYear;
        SharedPreferences sharedPreferences6 = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_renewal), 0);
        String expDate = sharedPreferences6.getString("ExpiryDate", "0");
        if (expDate != "0") {
            String arrDate[] = expDate.split("-");
            expDay = Integer.valueOf(arrDate[0]);
            expMonth = Integer.valueOf(arrDate[1]);
            expYear = Integer.valueOf(arrDate[2]);

            SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date d = null;
            java.util.Date d1 = null;
            Calendar cal1 = Calendar.getInstance();
            try {
                d = dfDate.parse(expDay + "/" + expMonth + "/" + expYear);
                Utils.log("expiry date", "is:" + d);

                d1 = dfDate.parse(dfDate.format(cal1.getTime()));// Returns
                // 15/10/2012
                Utils.log("todays date", "is:" + d1);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));

            Utils.log("Remaining Days", "are:" + diffInDays);
            if (diffInDays > 0) {
                txtdayremaing.setText(diffInDays + " Days");
                txtdayremaing.setTextColor(Color.parseColor("#006633"));
            } else {
                txtdayremaing.setText("Expired");
                txtdayremaing.setTextColor(Color.parseColor("#FF0000"));
            }
        }
    }


    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        Utils.log("" +context.getClass().getSimpleName(), ":" + event.getMessage());
        Utils.log("" + context.getClass().getSimpleName(), "::" + Utils.Last_Class_Name);
        if (context.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())) {
//            context.finish();
        } else {

        }

    }


    public class GetCalciVersion extends AsyncTask<String, Void, Void> {

        String calc_version_result = "", calc_version_response = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            GetCurrentVersionSOAP getCurrentVersionsoap = new GetCurrentVersionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CURRENT_VERSION));
            try {
                calc_version_result = getCurrentVersionsoap.getCurrentVersion("A", AppVersion, MemberId);
                calc_version_response = getCurrentVersionsoap.getResponse();

            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                calc_version_result = "Internet is too slow";
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                calc_version_result = "Internet is too slow";
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                calc_version_result = "Please try again!!";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            SharedPreferences sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences(sharedPreferences_name, 0);

            if (Utils.isOnline(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                    getMemberDetailWebService = new GetMemberDetailWebService();
                    getMemberDetailWebService.execute((String) null);
                }

            }
            if (calc_version_result.length() > 0) {
                if (calc_version_result.equalsIgnoreCase("ok")) {
                    Utils.log("Version", ":" + calc_version_response);
                    Utils.log("APP Version", ":" + AppVersion);
                    String version_response[] = calc_version_response.split("#");
                    if (version_response.length > 1) {

                        if (version_response[0].equalsIgnoreCase(AppVersion)) {

                        } else {
                            if (version_response[1].equalsIgnoreCase("True")) {
							/*	SharedPreferences.Editor edit=sharedPreferences.edit();
								edit.putString("App_Version", version_response[1]);
								edit.commit();*/

                                BaseActivity.isCompulsory = true;
                                showUpdateDialog(true);
                            }
                            if (version_response[1].equalsIgnoreCase("False")) {

								/*SharedPreferences.Editor edit=sharedPreferences.edit();
								edit.putString("App_Version", version_response[1]);
								edit.commit();*/

                                BaseActivity.isCompulsory = false;
                                showUpdateDialog(false);

                            }
                        }
                    }
                }
            }
        }

    }

    public void showUpdateDialog(boolean isCompulsory) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.update_dialog);
		/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);*/


        int width = 0;
        int height = 0;
        dialog.setCancelable(false);

        Point size = new Point();
        WindowManager w = ((Activity) context).getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
            ;
        }

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);

        TextView txt = (TextView) dialog.findViewById(R.id.tvMessage);
        LinearLayout llUpdate = (LinearLayout) dialog.findViewById(R.id.llUpdate);
        LinearLayout llCompulsoryUpdate = (LinearLayout) dialog.findViewById(R.id.llCompulsoryUpdate);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnCompulsoryUpdate = (Button) dialog.findViewById(R.id.btnCompulsoryUpdate);

        if (isCompulsory) {
            tvTitle.setText(Html.fromHtml("Mandatory Update"));
            txt.setText((getString(R.string.compulsory_update_msg)));
            llUpdate.setVisibility(View.GONE);
            llCompulsoryUpdate.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
        } else {
            tvTitle.setText(Html.fromHtml("INFO"));
            txt.setText((getString(R.string.update_msg)));
            llCompulsoryUpdate.setVisibility(View.GONE);
            llUpdate.setVisibility(View.VISIBLE);
            dialog.setCancelable(true);
        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnCompulsoryUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        dialog.show();
        //(width/2)+((width/2)/2)
        //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((width / 2) + (width / 2) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        //private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
        UpdatePhoneDetailSOAP updatePhoneDetailSOAP;
        String reg_id="";
        protected void onPreExecute() {
            //Dialog.setMessage(getString(R.string.app_please_wait_label));
            SharedPreferences	sharedPreferences_ = context
                    .getSharedPreferences(getString(R.string.shared_preferences_name), 0);
            reg_id=sharedPreferences_.getString("Gcm_reg_id", "");
        }


        protected void onPostExecute(Void unused) {
				/* if (Utils.isOnline(IONHome.this)) {
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					 {
						 getMemberDetailWebService = new GetMemberDetailWebService();
						 getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

					 }
					 else{
						 getMemberDetailWebService = new GetMemberDetailWebService();
						 getMemberDetailWebService.execute((String) null);
					 }

				}*/

        }

        @Override
        protected Void doInBackground(String... params) {
            try {



                updatePhoneDetailSOAP= new UpdatePhoneDetailSOAP(context.getResources().getString(
                        R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_UPDATE_PHONE_DETAILS_));

                updatePhoneDetailSOAP.updateDetails(MemberId, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID), reg_id,AppVersion);




            } catch (Exception e) {
//					AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
            }
            return null;
        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

    private class GetMemberDetailWebService extends
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        //private ProgressDialog Dialog = new ProgressDialog(IONHome.this);

        protected void onPreExecute() {
            if (is_activity_running) {
                if (show_progress)
                    mProgressHUD = ProgressHUD.show(context, getString(R.string.app_please_wait_label), true, true, this);
            }
        }

        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            if (is_activity_running) {
                if (show_progress)
                    mProgressHUD.dismiss();
            }

            try {
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (mapPackageDetails != null) {

                        Set<String> keys = mapPackageDetails.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                            str_keyVal = (String) i.next();

                        }
                        String selItem = str_keyVal.trim();
                        isLogout = false;
                        // finish();


                        PackageDetails packageDetails = mapPackageDetails
                                .get(selItem);

                        txtUser.setText(packageDetails.getMemberName());
                        txtPackage.setText(packageDetails.getPackageName());
                        txtamount.setText(packageDetails.getAmount());
                        txtvalidity.setText(packageDetails.getPackageValidity() + " Days");

                        // String PackageExpiry=packageDetails.getExpiryDate();
                        // String pac

                        txtpkgexpiry.setText(packageDetails.getExpiryDate());
                        txtUser.setText(packageDetails.getSubscriberName());


                        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
                        SharedPreferences sharedPreferences1 = context
                                .getSharedPreferences(
                                        sharedPreferences_renewal, 0); // 0 -
                        // for
                        // private
                        // mode

                        SharedPreferences.Editor editor = sharedPreferences1
                                .edit();
                        editor.putString("SubscriberName",
                                packageDetails.getSubscriberName());
                        NewConnFragment newConnFragment = new NewConnFragment();

                        editor.putString("MemberName",
                                packageDetails.getMemberName());
                        editor.putString("PackageName",
                                packageDetails.getPackageName());
                        editor.putString("Amount", packageDetails.getAmount());
                        editor.putString("PackageValidity",
                                packageDetails.getPackageValidity());
                        editor.putString("ExpiryDate",
                                packageDetails.getExpiryDate());
                        editor.putString("DaysRemaining",
                                packageDetails.getDaysRemaining());
                        editor.putString("ServiceTax",
                                packageDetails.getServiceTax());
                        editor.putString("AreaCode",
                                packageDetails.getAreaCode());
                        editor.putString("IsFreePackage",
                                packageDetails.getIsFreePackage());
                        editor.putBoolean("renewal", false);
                        editor.putBoolean("is_24ol", packageDetails.isIs_24ol());
                        editor.putString("ConnectionTypeId", packageDetails.getConnectionTypeId());
                        editor.putInt("CCAvenue", packageDetails.getIsCC_Avenue());
                        Utils.log("Atom", ""+packageDetails.getIsAtom());
                        editor.putInt("PAYTM",packageDetails.getIs_PayTm());
                        editor.putInt("citrus", packageDetails.getIs_citrus());
                        editor.putInt("Atom", packageDetails.getIsAtom());
                        editor.putInt("citrus", packageDetails.getIs_citrus());
                        editor.putInt("icici",packageDetails.getIs_icici());
                        editor.putInt("subpaisa",packageDetails.getIs_subpaisa());

                        editor.putInt("isPhonerenew",packageDetails.getIsPhoneRenew());
                        editor.putString("NotificationMsg",packageDetails.getNotificationMsg());
                        editor.putString("CheckForRenewal",packageDetails.getCheckForRenewal());
                        editor.putInt(SID,packageDetails.getSid());

                        editor.apply();
                        tv_marquee.setText(packageDetails.getNotificationMsg());

                        Utils.log("Atom + subpaisa", ""+packageDetails.getIsAtom() +" atom "+packageDetails.getIsAtom());
                        //editor.commit();

						/*if(sharedPreferences1.getBoolean("is_24ol", true)){
							iv_explore_plans.setImageResource(R.drawable.upgradepackage);
							iv_special_offer.setImageResource(R.drawable.img_specialoffer);
							tv_explore_plans.setText("Upgrade Plans");
							tv_special_offer.setText("Special Offers");
						}
						else{
							iv_explore_plans.setImageResource(R.drawable.create_plan);
							iv_special_offer.setImageResource(R.drawable.top_up);
							tv_explore_plans.setText("Create Your Plan");
							tv_special_offer.setText("Top Up");
						}*/

                        getRemainigDays();

						/* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						    else
						    new GetCalciVersion().execute();*/

                    }
                } else if (rslt.trim().equalsIgnoreCase("not")) {
                    if (is_activity_running) {
						/*if(show_progress)
						AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context);*/
                    }
                    setOfflineRenewalData();

                    if (Utils.isOnline(context)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                        else
                            new InsertPhoneDetailsWebService().execute();

                    }

                } else {
                    if (is_activity_running) {
					/*if(show_progress)
					//AlertsBoxFactory.showAlert(rslt, context);
*/
                    }
                    setOfflineRenewalData();
                }

            } catch (Exception e) {
                if (is_activity_running) {
                    if (show_progress)
                        AlertsBoxFactory.showAlert(rslt, context);
                }
                setOfflineRenewalData();
            }


            SharedPreferences sharedPreferences_ = context
                    .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
            if (sharedPreferences_.getString("Gcm_reg_id", "").length() > 0) {
                Utils.log("Reg id", "is:" + sharedPreferences_.getString("Gcm_reg_id", ""));

            } else {
                Utils.getRegId(context);
            }
            /*
             * Intent intentService1 = new
             * Intent(IONHome.this,ExpiryBroadcastReceiver.class);
             *
             * SharedPreferences sharedPreferences = getApplicationContext()
             * .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
             * private mode
             *
             * PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
             * intentService1, 0); AlarmManager alarm2 =
             * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
             *
             * int INTERVAL_DAY1 = 24 * 60 * 60 * 1000; Calendar calendar1 = new
             * GregorianCalendar(); calendar1.set(Calendar.HOUR_OF_DAY, 8);
             * calendar1.set(Calendar.MINUTE, 05);
             * calendar1.set(Calendar.SECOND, 0);
             * calendar1.set(Calendar.MILLISECOND, 0);
             *
             * long triggerMillis = calendar1.getTimeInMillis();
             *
             *
             * if (calendar1.getTimeInMillis() < Calendar.getInstance()
             * .getTimeInMillis()) { triggerMillis = calendar1.getTimeInMillis()
             * + INTERVAL_DAY1;
             * System.out.println("Alarm will go off next day"); }
             *
             * alarm2.cancel(pintent2);
             * if(sharedPreferences.getBoolean("check_expiry", true)) {
             * alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
             * calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
             * pintent2); } else {
             *
             * alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
             * AlarmManager.INTERVAL_DAY, pintent2); }
             */
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
				/*PackageDetailCaller packagedetailCaller = new PackageDetailCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_PACKAGE_DETAILS));

				packagedetailCaller.memberloginid = memberloginid;

				packagedetailCaller.join();
				packagedetailCaller.start();*/

                PAckageDetailSOAP packageDetailSOAP = new PAckageDetailSOAP(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_PACKAGE_DETAILS));

                rslt = packageDetailSOAP.CallSearchMemberSOAP(memberloginid);
                mapPackageDetails = packageDetailSOAP.getMapPackageDetails();

                //rslt = "START";

			/*	while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            if (show_progress)
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

}