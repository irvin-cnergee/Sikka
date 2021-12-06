package com.cnergee.mypage;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;

import all.interface_.IError;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.fragments.HelpFragment;
import com.cnergee.fragments.HomeFragment;
import com.cnergee.fragments.NewConnFragment;
import com.cnergee.fragments.NotificationListFragment;
import com.cnergee.fragments.ProfileFragment;
import com.cnergee.fragments.Refer_FrndFragment;
import com.cnergee.fragments.RenewPackageFragment;
import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.PAckageDetailSOAP;
import com.cnergee.mypage.SOAP.UpdatePhoneDetailSOAP;
import com.cnergee.mypage.adapter.NotificationInterface;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.obj.PackageDetails;
//import com.cnergee.mypage.sys.AlarmBroadcastReceiver;
import com.cnergee.mypage.sys.ExpiryBroadcastReceiver;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.MyTextView;
import com.cnergee.widgets.ProgressHUD;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.utils.Utils.SID;


@SuppressLint("CommitPrefEdits")
public class IONHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotificationInterface, IError {

	public static Context context;
	Utils utils = new Utils();
//	private boolean flag = true;
//	public static String rslt = "";
//	public String memberloginid, MemberId;
//	TextView txtPackage, txtamount, txtvalidity, txtpkgexpiry, txtUser,
//			txtdayremaing;
//	ImageView btnrenewal;
//	LinearLayout linnhome, linnprofile, linnnotification, linnhelp;

	/*LinearLayout relativepaymenthistory, relativedatausage,
			relativeservicerequest, relativeupgradepackage,
			relativepickuprequest,  relativeTopup;*/

//	RelativeLayout relativepaymenthistory, relativedatausage,
//			relativeservicerequest, relativeupgradepackage,
//			relativepickuprequest, relativeTopup;
//	public boolean check_call = true;
//	//relativerenewal
//	LinearLayout relativerenewal;
//	//ProgressDialog mProgressDialog;
//	ProgressHUD mProgressHUD;
//	private String sharedPreferences_name;
//	private String sharedPreferences_renewal;
//	public static String strXML = "";
//	public static String TAG = "IONHome";
//	public static String rsltUsername = "";
//	public static String Username = "";
//	public static String Password = "";
//	// private boolean isFinish = false;
//	public String s = "http://ionhungama.com/api_login.php";
//
//	static String extStorageDirectory = Environment
//			.getExternalStorageDirectory().toString();
//	final static String xml_folder = "mypage";
//	final static String TARGET_BASE_PATH = extStorageDirectory + "/"
//			+ xml_folder + "/";
//
//	public String xml_file_postFix = "PackageList.xml";
//	public String xml_file;
//	public String xml_file_with_path;
//
//	public static Map<String, PackageDetails> mapPackageDetails;
//	private GetMemberDetailWebService getMemberDetailWebService = null;
//
//	boolean isLogout = false;
//	IntentFilter intentFilter = new IntentFilter();
//	BroadcastReceiver broadcastReceiver;
//	SharedPreferences sharedPreferences1;
//	public boolean show_progress = false;

/*private ResideMenu resideMenu;

	private ResideMenuItem itemProfile;
	private ResideMenuItem itemRenewal;
	private ResideMenuItem itemSelf_Res;
	private ResideMenuItem itemCall_to_CC;
	private ResideMenuItem itemChat;
	private ResideMenuItem itemAlerts;
	private ResideMenuItem itemHelp;
	private ResideMenuItem itemShare;*/

	ImageView ivMenuDrawer;
	String AppVersion = "0",str_UserName,str_PhoneNumber,str_Email;
	public static boolean is_home_running = false;

	public ResideMenu resideMenu;
	public static Boolean showLog = true;


	public ResideMenuItem itemProfile;
	public ResideMenuItem itemRenewal;
	public ResideMenuItem itemSelf_Res;
	public ResideMenuItem itemCall_to_CC;
	//public  ResideMenuItem itemChat;
	public ResideMenuItem itemAlerts;
	public ResideMenuItem itemHelp;
	public ResideMenuItem itemShare;
	public ResideMenuItem itemRefer;

	public ResideMenuItem itemProfile1;
	public ResideMenuItem itemlogout;
	public ResideMenuItem itemlogout1;
	public ResideMenuItem itemRenewal1;
	public ResideMenuItem itemSelf_Res1;
	public ResideMenuItem itemCall_to_CC1;
	//public  ResideMenuItem itemChat1;
	public ResideMenuItem itemHelp1;
	public ResideMenuItem itemShare1;
	public ResideMenuItem itemRefer1;
	public ResideMenuItem itemAlerts1;


	ImageView iv_explore_plans, iv_special_offer;
	TextView txt_UserName,txt_Email,txt_PhoneNumber;
	public boolean is_activity_running = false,is_allow_renewal=false;


    NavigationView navigationView;
    AppBarLayout mAppBarLayout;
    Toolbar toolbar;
    int menu_id;
    MyTextView tv_title;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_myapp_home);
        //isServerReachable(IONHome.this);

         initViews();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(mAppBarLayout, "elevation", 0));
        mAppBarLayout.setStateListAnimator(stateListAnimator);
//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.nav_host_fragment_content_main, new HomeFragment());


//        tx.commit();

        navigationView.setNavigationItemSelectedListener(IONHome.this);

        SharedPreferences sharedPreferences1 = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_preferences_name), 0);
        str_PhoneNumber=sharedPreferences1.getString("MobileNoPrimary", "0");
        str_UserName =sharedPreferences1.getString("MemberName", "0");
        str_Email=sharedPreferences1.getString("EmailId", "0");

        Log.e("str_PhoneNumber: ",str_PhoneNumber);
        Log.e("str_MemberName: ",str_UserName);
        Log.e("str_Email: ",str_Email);

        txt_PhoneNumber.setText(""+str_PhoneNumber);
        txt_UserName.setText(""+str_UserName);
        txt_Email.setText(""+str_Email);

        loadFragment(new HomeFragment());





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


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                Menu m= bottomNavigationView.getMenu();
                menu_id =item.getItemId();

                switch (menu_id) {
                    case R.id.navBottom_Home:

                        fragment = new HomeFragment();
                        updateTitle("DashBoard");
                        loadFragment(fragment);

                        break;
                    case R.id.navBottom_Profile:
                        Log.e("Test","12");
                        fragment = new ProfileFragment();
                        updateTitle("Profile");
                        loadFragment(fragment);
                        break;

                    case R.id.navBottom_Alert:
                        fragment = new NotificationListFragment();
                        updateTitle("Notification");
                        loadFragment(fragment);
                        break;
                    case R.id.navBottom_Help:
                        fragment = new HelpFragment();
                        updateTitle("Help");
                        loadFragment(fragment);
                        break;

                }
                return true;
            }
        });





        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        //boolean url_value = exists("http://150.129.48.28:8005/CCRMToMobileIntegration.asmx?wsdl");
        //Log.e("url_value",":"+url_value);

        Utils.log("Density", "is:" + getResources().getDisplayMetrics().density);

        is_activity_running = true;
        Utils.log("" + this.getClass().getSimpleName(), "is:");
//		intentFilter.addAction("com.package.ACTION_LOGOUT");
//		txtUser = (TextView) findViewById(R.id.tv_usertext);
//		txtPackage = (TextView) findViewById(R.id.txtPackage);
//		txtamount = (TextView) findViewById(R.id.txtamount);
//		txtvalidity = (TextView) findViewById(R.id.txtvalidity);
//		txtpkgexpiry = (TextView) findViewById(R.id.txtpkgexpiry);
//		txtdayremaing = (TextView) findViewById(R.id.txtdayremainig);
//
//		iv_explore_plans = (ImageView) findViewById(R.id.img_explore_plans);
//		//	iv_special_offer=(ImageView)findViewById(R.id.img_special);
//
//		iv_explore_plans.setImageResource(R.drawable.img_exploreplans);
////		iv_special_offer.setImageResource(R.drawable.img_specialoffer);
//
//		tv_explore_plans = (TextView) findViewById(R.id.tv_explore_plan);
//		//	tv_special_offer=(TextView)findViewById(R.id.tv_special_offer);
//		tv_marquee= (TextView) findViewById(R.id.tv_marquee);
//		tv_marquee.setSelected(true);
//
//		tv_explore_plans.setText("Upgrade Plans");
//		tv_special_offer.setText("Special Offers");

//		linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
//		linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
//		linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
//		linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);
//		ivMenuDrawer = (ImageView) findViewById(R.id.ivMenuDrawer);
        // btnrenewal = (ImageView)findViewById(R.id.btnrenewal);
        context = this;

//		sharedPreferences_name = getString(R.string.shared_preferences_name);
//		SharedPreferences sharedPreferences = getApplicationContext()
//				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
//		PackageInfo pInfo;
//		try {
//			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//			AppVersion = pInfo.versionName;
//			Utils.log("Apppversion on Create", ":" + pInfo.versionName);
//
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        //**************************APP RATE DIALOG****************************************************//

		/*Utils.log("second time", ":"+sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true));
		if(sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true)){
			if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").length()>0){

				Utils.log("App rate status", ""+sharedPreferences.getString(Utils.APP_RATE_STATUS, ""));

				if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("later")){
					if(Utils.open_once){
						Utils.showAppRater(IONHome.this);
						Utils.open_once=false;
					}
				}
				if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("no")){

				}
				if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("rate")){

				}

			}
			else{
				Utils.log("App rate status", ""+sharedPreferences.getString(Utils.APP_RATE_STATUS, ""));
				if(Utils.open_once){
					Utils.showAppRater(IONHome.this);
					Utils.open_once=false;
				}
			}

		}
		else{

		}*/

        //**************************APP RATE DIALOG****************************************************//

//		utils.setSharedPreferences(sharedPreferences);
//		sharedPreferences1 = getApplicationContext().getSharedPreferences(
//				getString(R.string.shared_preferences_renewal), 0);
//
//		MemberId = utils.getMemberId();
//		setupMenu(IONHome.this, this);
//
//		ivMenuDrawer.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (resideMenu.isOpened()) {
//					Utils.log(this.getClass().getSimpleName() + "", "close");
//					resideMenu.closeMenu();
//				} else {
//					Utils.log(this.getClass().getSimpleName() + "", "open");
//					resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//				}
//			}
//		});
//
//		Intent i = getIntent();
//		SharedPreferences sharedPreferences2 = context.getSharedPreferences(
//				"CNERGEE_BILLING", 0);
        /*
         * if(sharedPreferences2.getBoolean("check_expiry", true)){
         * //Utils.log("start","service home"); Intent startServiceIntent=new
         * Intent(this,CheckExpiryService.class);
         * startService(startServiceIntent); }
         */

//		memberloginid = utils.getMemberLoginID();
//
//		Utils.log("MemberId", ":" + memberloginid);

        /*
         * This SharedPrefernce used to check there is change in profile data
         */


//		if (sharedPreferences1.getBoolean("renewal", true)) {
//			Utils.log("Data From server IONHome ", "yes" + sharedPreferences1.getBoolean("renewal", true));
//			show_progress = true;
//			if (Utils.isOnline(IONHome.this)) {
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//					getMemberDetailWebService = new GetMemberDetailWebService();
//					getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//				} else {
//					getMemberDetailWebService = new GetMemberDetailWebService();
//					getMemberDetailWebService.execute((String) null);
//				}
//
//			}

//    } else {
//        Utils.log("Data From server IONHome", "offline " + sharedPreferences1.getBoolean("renewal", true));
//        show_progress = false;
//        setOfflineRenewalData();
//        if (Utils.isOnline(IONHome.this)) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            else
//                new GetCalciVersion().execute();
//
//            SharedPreferences sharedPreferences_ = getApplicationContext()
//                    .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
//            if (sharedPreferences_.getString("Gcm_reg_id", "").length() > 0) {
//                Utils.log("Reg id ", "SharedPreference:" + sharedPreferences_.getString("Gcm_reg_id", ""));
//
//            } else {
//                Utils.getRegId(IONHome.this);
//            }
        }

    private void initViews() {

        mAppBarLayout=(AppBarLayout)findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


//        setSupportActionBar(toolbar);
        tv_title =  toolbar.findViewById(R.id.tv_title);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
//        navigationView.addHeaderView(headerView);

        View headerView = navigationView.getHeaderView(0);


        txt_PhoneNumber = (TextView)headerView.findViewById(R.id.txt_PhoneNumber);
        txt_Email = headerView.findViewById(R.id.txt_Email);
        txt_UserName = headerView.findViewById(R.id.txt_UserName);


    }


    // *********************for expiry service****************

//			SharedPreferences sharedPreferences3 = getApplicationContext()
//					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
//			// private
//			// mode
//			if (sharedPreferences3.getBoolean("check_expiry", true)) {
//				Intent intentService2 = new Intent(IONHome.this,
//						ExpiryBroadcastReceiver.class);
//
//				PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
//						intentService2, 0);
//				AlarmManager alarm2 = (AlarmManager) context
//						.getSystemService(Context.ALARM_SERVICE);
//
//				int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
//				Calendar calendar1 = new GregorianCalendar();
//				calendar1.set(Calendar.HOUR_OF_DAY, 8);
//				calendar1.set(Calendar.MINUTE, 05);
//				calendar1.set(Calendar.SECOND, 0);
//				calendar1.set(Calendar.MILLISECOND, 0);
//
//				long triggerMillis = calendar1.getTimeInMillis();
//
//				if (calendar1.getTimeInMillis() < Calendar.getInstance()
//						.getTimeInMillis()) {
//					triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
//					//System.out.println("Alarm will go off next day");
//				}
//
//				alarm2.cancel(pintent2);
//				if (sharedPreferences3.getBoolean("check_expiry", true)) {
//					alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
//							calendar1.getTimeInMillis(),
//							AlarmManager.INTERVAL_DAY, pintent2);
//				} else {
//
//					alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
//							AlarmManager.INTERVAL_DAY, pintent2);
//				}
//			}
//		}
//
//
//		// boolean deleted = file.delete();
//
//		linnhome.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//
//			}
//		});
//
//		linnprofile.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, Profile.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
//
//		linnnotification.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				IONHome.this.finish();
//				Intent i = new Intent(IONHome.this,
//						NotificationListActivity.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
//
//		linnhelp.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//
//				IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, HelpActivity.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
//        BaseApplication.getEventBus().register(this);
        // Utils.Last_Class_Name=this.getClass().getSimpleName();
        /*
         * btnrenewal.setOnClickListener(new OnClickListener() { public void
         * onClick(View v) {
         *
         * Intent i = new Intent(IONHome.this,RenewPackage.class);
         * startActivity(i); } });
         */

//		relativepaymenthistory = (RelativeLayout) findViewById(R.id.relativepaymenthistory);
//		relativedatausage = (RelativeLayout) findViewById(R.id.relativedatausage);
//		relativeservicerequest = (RelativeLayout) findViewById(R.id.relativeservicerequest);
//		relativeupgradepackage = (RelativeLayout) findViewById(R.id.relativeupgradepdackage);
//		relativepickuprequest = (RelativeLayout) findViewById(R.id.relativepickuprequest);
//		relativerenewal = (LinearLayout) findViewById(R.id.relativerenewal);
//
//		//relativeTopup = (RelativeLayout) findViewById(R.id.relativeTopup);
//
////		relativeTopup.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				if(sharedPreferences1.getString("isFreePackage" , "false").equalsIgnoreCase("false")){
////					if(Utils.isOnline(IONHome.this)){
////
////						Intent i = new Intent(IONHome.this, SpecialOfferActivity.class);
////
////						startActivity(i);
////						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
////
////						/*if(sharedPreferences1.getBoolean("is_24ol", true)){
////						Intent i = new Intent(IONHome.this, SpecialOfferActivity.class);
////
////						startActivity(i);
////						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
////
////						}
////						else{
////
////							Intent i = new Intent(IONHome.this,TopupActivity.class);
////							i.putExtra("check_intent", false);
////							startActivity(i);
////							overridePendingTransition(R.anim.slide_in_right,
////									R.anim.slide_out_left);
////							Utils.Last_Class_Name=IONHome.this.getClass().getSimpleName();
////						}*/
////					}else{
////
////							Toast.makeText(getApplicationContext(), "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
////						}
////
////
////				}else{
////					AlertDialog.Builder builder = new AlertDialog.Builder(IONHome.this);
////
////				  builder.setMessage("This Facility is not available.")
////					.setTitle("Alert")
////					.setCancelable(false)
////					.setPositiveButton("OK",
////							new DialogInterface.OnClickListener(){
////						public void onClick(
////							DialogInterface dialog,int id){
////						}
////					});
////				AlertDialog alert = builder.create();
////				}
////			}
////		});
//		relativedatausage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				//	IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, MyappDataUsage.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//				Utils.Last_Class_Name = IONHome.this.getClass().getSimpleName();
//
//			}
//		});
//		relativepaymenthistory.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				//IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, PaymentHistory.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				Utils.Last_Class_Name = IONHome.this.getClass().getSimpleName();
//			}
//		});
//
//		relativeservicerequest.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (sharedPreferences1.getString("isFreePackage", "false").equalsIgnoreCase("false")) {
//					if (Utils.isOnline(IONHome.this)) {
//						//IONHome.this.finish();
//						Intent i = new Intent(IONHome.this, Complaints.class);
//						i.putExtra("check_intent", false);
//						startActivity(i);
//						overridePendingTransition(R.anim.slide_in_right,
//								R.anim.slide_out_left);
//						Utils.Last_Class_Name = IONHome.this.getClass().getSimpleName();
//
//					} else {
//
//						Toast.makeText(getApplicationContext(), "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
//					}
//
//				} else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(IONHome.this);
//
//					builder.setMessage("This Facility is not available.")
//							.setTitle("Alert")
//							.setCancelable(false)
//							.setPositiveButton("OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//										}
//									});
//					AlertDialog alert = builder.create();
//				}
//			}
//		});
//
//		relativeupgradepackage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (sharedPreferences1.getString("IsFreePackage", "false")
//						.equalsIgnoreCase("false")) {
//					if (Utils.isOnline(IONHome.this)) {
//						//RenewPackage.this.finish();
//						/*Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
//						i.putExtra("check_intent", true);
//						startActivity(i);
//						overridePendingTransition(R.anim.slide_in_		right, R.anim.slide_out_left);*/
//
//						//						if (!sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO")) {
////							//	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
////							is_allow_renewal = false;
////							Utils.log("Allow NOT GO", ":" + is_allow_renewal);
////							AlertsBoxFactory.showAlert(sharedPreferences1.getString("CheckForRenewal", ""), context);
////						} else {
//////							if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
//////								Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
//////								i.putExtra("check_intent", true);
//////								startActivity(i);
//////								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//////							} else {
//////								AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
//////							}
////
////
//////                            if (sharedPreferences1.getString("CheckForRenewal", "").equalsIgnoreCase("GO") && sharedPreferences1.getInt("isPhonerenew", 0) == 0) {
////                                Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
////                                i.putExtra("check_intent", true);
////                                startActivity(i);
////                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//////                            } else {
//////                                AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
//////                            }
////
////						}
//
//
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
//                            Intent i = new Intent(IONHome.this, PackgedetailActivity.class);
//                            i.putExtra("check_intent", true);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                            } else {
////                                AlertsBoxFactory.showAlert(getResources().getString(R.string.phone_renew), context);
////                            }
//
//
//
//					}else {
//						Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
//					}
//
//					/*if(sharedPreferences1.getBoolean("is_24ol", true)){
//						if(Utils.isOnline(IONHome.this)){
//		            		//RenewPackage.this.finish();
//		            		Intent i = new Intent(IONHome.this,ChangePackage.class);
//		            		i.putExtra("check_intent",true);
//		            		startActivity(i);
//		            		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//		            	}
//		            	else{
//		            		Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
//		            	}
//
//					}
//					else{
//
//						if (Utils.isOnline(IONHome.this)) {
//							//IONHome.this.finish();
//							Intent i = new Intent(IONHome.this,Make_my_plan_Activity.class);
//							i.putExtra("check_intent", false);
//							startActivity(i);
//							overridePendingTransition(R.anim.slide_in_right,
//									R.anim.slide_out_left);
//							Utils.Last_Class_Name=IONHome.this.getClass().getSimpleName();
//						} else {
//							Toast.makeText(getApplicationContext(),
//									"Please connect to internet and try again!!",
//									Toast.LENGTH_LONG).show();
//						}
//					}*/
//
//				} else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							IONHome.this);
//					builder.setMessage("This Facility is not available.")
//							.setTitle("Alert")
//							.setCancelable(false)
//							.setPositiveButton("OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//											// Toast.makeText(NotificationListActivity.this,
//											// ""+selectedFromList.getNotifyId(),
//											// Toast.LENGTH_SHORT).show();
//
//										}
//									});
//					AlertDialog alert = builder.create();
//					alert.show();
//				}
//			}
//		});
//
//		relativepickuprequest.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (sharedPreferences1.getString("IsFreePackage", "false")
//						.equalsIgnoreCase("false")) {
//					if (Utils.isOnline(IONHome.this)) {
//						//IONHome.this.finish();
//						// Intent i = new
//						// Intent(IONHome.this,PymentPickupRequest.class);
//						Intent i = new Intent(IONHome.this,
//								PaymentPickup_New.class);
//						i.putExtra("check_intent", false);
//						startActivity(i);
//						overridePendingTransition(R.anim.slide_in_right,
//								R.anim.slide_out_left);
//
//						Utils.Last_Class_Name = IONHome.this.getClass().getSimpleName();
//						// new firstRunTask().execute();
//					} else {
//						Toast.makeText(getApplicationContext(),
//								"Please connect to internet and try again!!",
//								Toast.LENGTH_LONG).show();
//					}
//				} else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							IONHome.this);
//					builder.setMessage("This Facility is not available.")
//							.setTitle("Alert")
//							.setCancelable(false)
//							.setPositiveButton("OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//											// Toast.makeText(NotificationListActivity.this,
//											// ""+selectedFromList.getNotifyId(),
//											// Toast.LENGTH_SHORT).show();
//										}
//									});
//					AlertDialog alert = builder.create();
//					alert.show();
//				}
//			}
//		});
//
//		relativerenewal.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (sharedPreferences1.getString("IsFreePackage", "false")
//						.equalsIgnoreCase("false")) {
//					//IONHome.this.finish();
//					Utils.pg_sms_request = false;
//					Utils.pg_sms_uniqueid = "";
//					Intent i = new Intent(IONHome.this, RenewPackage.class);
//					i.putExtra("renew", "Home");
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right,
//							R.anim.slide_out_left);
//					Utils.Last_Class_Name = IONHome.this.getClass().getSimpleName();
//				} else {
//					/*AlertDialog.Builder builder = new AlertDialog.Builder(
//							IONHome.this);
//					builder.setMessage("This Facility is not available.")
//							.setTitle("Alert")
//							.setCancelable(false)
//							.setPositiveButton("OK",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//											// Toast.makeText(NotificationListActivity.this,
//											// ""+selectedFromList.getNotifyId(),
//											// Toast.LENGTH_SHORT).show();
//
//										}
//									});
//					AlertDialog alert = builder.create();
//					alert.show();*/
//					AlertsBoxFactory.showAlert2("This Facility is not available.", IONHome.this);
//				}
//			}
//		});
//
//		/*
//		 * Intent intentService = new Intent(IONHome.this,MyPageService.class);
//		 *
//		 * Calendar cal = Calendar.getInstance(); PendingIntent pintent =
//		 * PendingIntent.getService(context, 0, intentService, 0);
//		 *
//		 * AlarmManager alarm =
//		 * (AlarmManager)getSystemService(Context.ALARM_SERVICE); // Start every
//		 * 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
//		 * cal.getTimeInMillis(), 30*1000, pintent); // Start every 1mon..
//		 * alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//		 * 60000*2, pintent);
//		 */
//
//		// ***********************
//
//		SharedPreferences sharedPreferences_gcm = context
//				.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0);
//		String gcm_id = sharedPreferences_gcm.getString("Gcm_reg_id", "");

		/*AlarmManager alarm1 = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		*//*Intent intentService1 = new Intent(IONHome.this,
				AlarmBroadcastReceiver.class);*//*

         *//*PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0,
				intentService1, 0);*//*
		if (gcm_id.length() == 0 || gcm_id.equalsIgnoreCase("NO")) {
			Log.d("Alarm has", "Started");
			Calendar cal = Calendar.getInstance();

			// Start every 30 seconds
			// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
			// 30*1000, pintent);
			// Start every 1mon..
			alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					60000 * 4, pintent1);
			// ********************************
			check_call = false;
		} else {
			Log.d("Alarm has", "cancelled");
			alarm1.cancel(pintent1);
		}*/


    public void updateTitle(String title){
        if(!TextUtils.isEmpty(title) && title != null){
            tv_title.setText(title);
        }else {
            tv_title.setText("");
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

//        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void loadFragment1(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

//        transaction.addToBackStack(null);
        transaction.commit();
    }
    static public boolean isServerReachable(Context context) {
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL("http://150.129.48.28:8005/CCRMToMobileIntegration.asmx?wsdl");
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                //return false;
                e1.printStackTrace();
            } catch (IOException e) {
                //return false;
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        Menu m= navigationView.getMenu();
        menu_id =item.getItemId();

//        for(int i=0 ; i < m.size(); i++){
//            if(m.getItem(i).toString().equalsIgnoreCase(navigationView.getMenu().getItem(menu_id-1).toString())){
//                Log.e("check",":"+m.getItem(menu_id-1).toString());
//                m.getItem(menu_id-1).setChecked(true);
//            }else{
//                m.getItem(i).setChecked(false);
//            }
//        }

//        switch(menu_id){
//
////            case :
////                fragment=new HomeFragment();
////                break;
//            case :
                if (menu_id == R.id.nav_Profile) {
                    updateTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment1(fragment);
                }
                if (menu_id == R.id.nav_Renewal)
                {
                    updateTitle("Renewal");
                    fragment = new RenewPackageFragment();
                    loadFragment(fragment);
                }

                if (menu_id == R.id.nav_Call)
                {
                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode

                    Utils utils=new Utils();
                    utils.setSharedPreferences(sharedPreferences);
                    String DefaultCustomerCareNo="8574412345";
                    String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
                        intent.setData(Uri.parse("tel:"+CustomerCareNo));
                    else
                        intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
                    startActivity(intent);

                }
                if (menu_id == R.id.nav_Alert)
                {
                    updateTitle("Notification");
                    fragment = new NotificationListFragment();
                    loadFragment(fragment);

                }
                if (menu_id == R.id.nav_Help)
                {
                    updateTitle("Help");
                    fragment = new HelpFragment();
                    loadFragment1(fragment);

                }
                if (menu_id == R.id.nav_Share)
                {
                    Intent share = new Intent("android.intent.action.SEND");
                    share.setType("plain/text");
                    share.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
                    Uri imageUri = Uri.parse("android.resource://" + getResources().getString( R.string.playstore_link ));
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(share, "Sikka Broadband App")); }



                if (menu_id == R.id.nav_Logout)
                {
                    try {
//				// clearing app data
                        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
                        } else {
                            String packageName = getApplicationContext().getPackageName();
                            Runtime runtime = Runtime.getRuntime();
                            runtime.exec("pm clear "+packageName);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (menu_id == R.id.nav_ReferFriend)
                {
                    updateTitle("Refer A Friend");
                    fragment = new Refer_FrndFragment();
                    loadFragment(fragment);

                }
//                editor = sharedPreferences.edit();
//                editor.putBoolean(MyUtils.IS_LOGIN,false);
//                editor.putString(MyUtils.User_name, "");
//                editor.putString(MyUtils.Clientaccessid, "");
//                editor.putInt(MyUtils.Entityid, 0);
//                editor.putInt(MyUtils.Userid, 0);
//                editor.putString(MyUtils.Role_id,"");
//                try {
//                    InternalStorage.deleteFile(this,MyUtils.PROFILE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                editor.commit();
//                finish();
//
//                Intent intent =new Intent(IONHome.this,LoginActivity.class);
//                startActivity(intent);
//                break;


//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
//
//        } else {
//            Log.e("MainActivity", "Error in creating fragment");
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

//    private class GetMemberDetailWebService extends
//			AsyncTask<String, Void, Void> implements OnCancelListener {
//		//private ProgressDialog Dialog = new ProgressDialog(IONHome.this);
//
//		protected void onPreExecute() {
//			if (is_activity_running) {
//				if (show_progress)
//					mProgressHUD = ProgressHUD.show(IONHome.this, getString(R.string.app_please_wait_label), true, true, this);
//			}
//		}
//
//		protected void onPostExecute(Void unused) {
//			getMemberDetailWebService = null;
//			if (is_activity_running) {
//				if (show_progress)
//					mProgressHUD.dismiss();
//			}
//
//			try {
//				if (rslt.trim().equalsIgnoreCase("ok")) {
//					if (mapPackageDetails != null) {
//
//						Set<String> keys = mapPackageDetails.keySet();
//						String str_keyVal = "";
//
//						for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
//							str_keyVal = (String) i.next();
//
//						}
//						String selItem = str_keyVal.trim();
//						isLogout = false;
//						// finish();
//
//
//						PackageDetails packageDetails = mapPackageDetails
//								.get(selItem);
//
//						txtUser.setText(packageDetails.getMemberName());
//						txtPackage.setText(packageDetails.getPackageName());
//						txtamount.setText(packageDetails.getAmount());
//						txtvalidity.setText(packageDetails.getPackageValidity() + " Days");
//
//						// String PackageExpiry=packageDetails.getExpiryDate();
//						// String pac
//
//						txtpkgexpiry.setText(packageDetails.getExpiryDate());
//						txtUser.setText(packageDetails.getSubscriberName());
//
//
//						sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
//						SharedPreferences sharedPreferences1 = getApplicationContext()
//								.getSharedPreferences(
//										sharedPreferences_renewal, 0); // 0 -
//						// for
//						// private
//						// mode
//
//						SharedPreferences.Editor editor = sharedPreferences1
//								.edit();
//						editor.putString("SubscriberName",
//								packageDetails.getSubscriberName());
//						NewConnFragment newConnFragment = new NewConnFragment();
//
//						editor.putString("MemberName",
//								packageDetails.getMemberName());
//						editor.putString("PackageName",
//								packageDetails.getPackageName());
//						editor.putString("Amount", packageDetails.getAmount());
//						editor.putString("PackageValidity",
//								packageDetails.getPackageValidity());
//						editor.putString("ExpiryDate",
//								packageDetails.getExpiryDate());
//						editor.putString("DaysRemaining",
//								packageDetails.getDaysRemaining());
//						editor.putString("ServiceTax",
//								packageDetails.getServiceTax());
//						editor.putString("AreaCode",
//								packageDetails.getAreaCode());
//						editor.putString("IsFreePackage",
//								packageDetails.getIsFreePackage());
//						editor.putBoolean("renewal", false);
//						editor.putBoolean("is_24ol", packageDetails.isIs_24ol());
//						editor.putString("ConnectionTypeId", packageDetails.getConnectionTypeId());
//						editor.putInt("CCAvenue", packageDetails.getIsCC_Avenue());
//						Utils.log("Atom", ""+packageDetails.getIsAtom());
//						editor.putInt("PAYTM",packageDetails.getIs_PayTm());
//						editor.putInt("citrus", packageDetails.getIs_citrus());
//						editor.putInt("Atom", packageDetails.getIsAtom());
//						editor.putInt("citrus", packageDetails.getIs_citrus());
//						editor.putInt("icici",packageDetails.getIs_icici());
//						editor.putInt("subpaisa",packageDetails.getIs_subpaisa());
//
//						editor.putInt("isPhonerenew",packageDetails.getIsPhoneRenew());
//						editor.putString("NotificationMsg",packageDetails.getNotificationMsg());
//						editor.putString("CheckForRenewal",packageDetails.getCheckForRenewal());
//                        editor.putInt(SID,packageDetails.getSid());
//
//                        editor.apply();
//						tv_marquee.setText(packageDetails.getNotificationMsg());
//
//                        Utils.log("Atom + subpaisa", ""+packageDetails.getIsAtom() +" atom "+packageDetails.getIsAtom());
//						//editor.commit();
//
//						/*if(sharedPreferences1.getBoolean("is_24ol", true)){
//							iv_explore_plans.setImageResource(R.drawable.upgradepackage);
//							iv_special_offer.setImageResource(R.drawable.img_specialoffer);
//							tv_explore_plans.setText("Upgrade Plans");
//							tv_special_offer.setText("Special Offers");
//						}
//						else{
//							iv_explore_plans.setImageResource(R.drawable.create_plan);
//							iv_special_offer.setImageResource(R.drawable.top_up);
//							tv_explore_plans.setText("Create Your Plan");
//							tv_special_offer.setText("Top Up");
//						}*/
//
//						getRemainigDays();
//
//						/* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//							 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
//						    else
//						    new GetCalciVersion().execute();*/
//
//					}
//				} else if (rslt.trim().equalsIgnoreCase("not")) {
//					if (is_activity_running) {
//						/*if(show_progress)
//						AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context);*/
//					}
//					setOfflineRenewalData();
//
//					if (Utils.isOnline(IONHome.this)) {
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//							new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
//						else
//							new InsertPhoneDetailsWebService().execute();
//
//					}
//
//				} else {
//					if (is_activity_running) {
//					/*if(show_progress)
//					//AlertsBoxFactory.showAlert(rslt, context);
//*/
//					}
//					setOfflineRenewalData();
//				}
//
//			} catch (Exception e) {
//				if (is_activity_running) {
//					if (show_progress)
//						AlertsBoxFactory.showAlert(rslt, context);
//				}
//				setOfflineRenewalData();
//			}
//
//
//			SharedPreferences sharedPreferences_ = getApplicationContext()
//					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
//			if (sharedPreferences_.getString("Gcm_reg_id", "").length() > 0) {
//				Utils.log("Reg id", "is:" + sharedPreferences_.getString("Gcm_reg_id", ""));
//
//			} else {
//				Utils.getRegId(IONHome.this);
//			}
//			/*
//			 * Intent intentService1 = new
//			 * Intent(IONHome.this,ExpiryBroadcastReceiver.class);
//			 *
//			 * SharedPreferences sharedPreferences = getApplicationContext()
//			 * .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
//			 * private mode
//			 *
//			 * PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
//			 * intentService1, 0); AlarmManager alarm2 =
//			 * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//			 *
//			 * int INTERVAL_DAY1 = 24 * 60 * 60 * 1000; Calendar calendar1 = new
//			 * GregorianCalendar(); calendar1.set(Calendar.HOUR_OF_DAY, 8);
//			 * calendar1.set(Calendar.MINUTE, 05);
//			 * calendar1.set(Calendar.SECOND, 0);
//			 * calendar1.set(Calendar.MILLISECOND, 0);
//			 *
//			 * long triggerMillis = calendar1.getTimeInMillis();
//			 *
//			 *
//			 * if (calendar1.getTimeInMillis() < Calendar.getInstance()
//			 * .getTimeInMillis()) { triggerMillis = calendar1.getTimeInMillis()
//			 * + INTERVAL_DAY1;
//			 * System.out.println("Alarm will go off next day"); }
//			 *
//			 * alarm2.cancel(pintent2);
//			 * if(sharedPreferences.getBoolean("check_expiry", true)) {
//			 * alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
//			 * calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
//			 * pintent2); } else {
//			 *
//			 * alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
//			 * AlarmManager.INTERVAL_DAY, pintent2); }
//			 */
//		}
//
//		@Override
//		protected Void doInBackground(String... params) {
//			try {
//				/*PackageDetailCaller packagedetailCaller = new PackageDetailCaller(
//						getApplicationContext().getResources().getString(
//								R.string.WSDL_TARGET_NAMESPACE),
//						getApplicationContext().getResources().getString(
//								R.string.SOAP_URL), getApplicationContext()
//								.getResources().getString(
//										R.string.METHOD_PACKAGE_DETAILS));
//
//				packagedetailCaller.memberloginid = memberloginid;
//
//				packagedetailCaller.join();
//				packagedetailCaller.start();*/
//
//				PAckageDetailSOAP packageDetailSOAP = new PAckageDetailSOAP(
//						getApplicationContext().getResources().getString(
//								R.string.WSDL_TARGET_NAMESPACE),
//						getApplicationContext().getResources().getString(
//								R.string.SOAP_URL), getApplicationContext()
//						.getResources().getString(
//								R.string.METHOD_PACKAGE_DETAILS));
//
//				IONHome.rslt = packageDetailSOAP.CallSearchMemberSOAP(memberloginid);
//				IONHome.mapPackageDetails = packageDetailSOAP.getMapPackageDetails();
//
//				//rslt = "START";
//
//			/*	while (rslt == "START") {
//					try {
//						Thread.sleep(10);
//					} catch (Exception ex) {
//					}
//				}*/
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onCancelled() {
//			if (show_progress)
//				mProgressHUD.dismiss();
//			getMemberDetailWebService = null;
//		}
//
//		/* (non-Javadoc)
//		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
//		 */
//		@Override
//		public void onCancel(DialogInterface dialog) {
//			// TODO Auto-generated method stub
//
//		}
//	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		is_home_running = true;
//		is_activity_running = true;
//		memberloginid = utils.getMemberLoginID();
//		if (memberloginid.length() > 0) {
//
//		} else {
//			this.finish();
//		}
//		try {
//
//
//		/*if(!check_call)
//		Utils.setupMenu(IONHome.this, this);*/
//
//			//	else
//			//		Utils.log("Reside Menu","not null");
//			ImageView ivLogo = (ImageView) findViewById(R.id.imgdvois);
//
//			SharedPreferences sharedPreferences = getApplicationContext()
//					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
//			// private
//			// mode
//		/*if (sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
//			ivLogo.setVisibility(View.VISIBLE);
//		else
//			ivLogo.setVisibility(View.INVISIBLE);*/
//
//
//			// if(sharedPreferences.getBoolean("check_expiry", true))
//			// {
//			Intent intentService1 = new Intent(IONHome.this,
//					ExpiryBroadcastReceiver.class);
//
//			PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
//					intentService1, 0);
//			/*
//			 * Calendar calendar = Calendar.getInstance();
//			 * calendar.set(Calendar.HOUR_OF_DAY, 8); calendar.set(Calendar.MINUTE,
//			 * 05); calendar.set(Calendar.SECOND, 00);
//			 *
//			 * AlarmManager alarm2 =
//			 * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE); //
//			 * Start every 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
//			 * cal.getTimeInMillis(), 30*1000, pintent 24*60*60*1000); // Start
//			 * every 1mon.. //alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
//			 * calendar.getTimeInMillis(), 24*60*60*1000, pintent2);
//			 * alarm2.cancel(pintent2); alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
//			 * calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent2);
//			 */
//
//			AlarmManager alarm2 = (AlarmManager) context
//					.getSystemService(Context.ALARM_SERVICE);
//
//			int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
//			Calendar calendar1 = new GregorianCalendar();
//			calendar1.set(Calendar.HOUR_OF_DAY, 8);
//			calendar1.set(Calendar.MINUTE, 05);
//			calendar1.set(Calendar.SECOND, 0);
//			calendar1.set(Calendar.MILLISECOND, 0);
//
//			long triggerMillis = calendar1.getTimeInMillis();
//
//			if (calendar1.getTimeInMillis() < Calendar.getInstance()
//					.getTimeInMillis()) {
//				triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
//				System.out.println("Alarm will go off next day");
//			}
//
//			alarm2.cancel(pintent2);
//			if (sharedPreferences.getBoolean("check_expiry", true)) {
//				alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
//						calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
//						pintent2);
//			} else {
//				alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
//						AlarmManager.INTERVAL_DAY, pintent2);
//			}
//		} catch (StackOverflowError t) {
//			// more general: catch(Error t)
//			// anything: catch(Throwable t)
//			System.out.println("Caught " + t);
//			t.printStackTrace();
//		}
//	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// this.finish();
		is_home_running = false;
		is_activity_running = false;
	}



	/*
	 *
	 */

//	private void setOfflineRenewalData() {
//		// TODO Auto-generated method stub
//		sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
//		SharedPreferences sharedPreferences2 = getApplicationContext()
//				.getSharedPreferences(sharedPreferences_renewal, 0); // 0 - for
//		// private
//		// mode
//
//		txtPackage.setText(sharedPreferences2.getString("PackageName", "-"));
//		txtUser.setText(sharedPreferences2.getString("SubscriberName", "-"));
//
//		txtamount.setText(sharedPreferences2.getString("Amount", "-"));
//		txtvalidity.setText(sharedPreferences2
//				.getString("PackageValidity", "-") + "  Days");
//
//		// String PackageExpiry=packageDetails.getExpiryDate();
//		txtpkgexpiry.setText(sharedPreferences2.getString("ExpiryDate", "-"));
//
//		txtdayremaing.setText(sharedPreferences2
//				.getString("DaysRemaining", "-"));
//
//		String sharedPreferences_profile = getString(R.string.shared_preferences_profile);
//
//		SharedPreferences sharedPreferences = getApplicationContext()
//				.getSharedPreferences(sharedPreferences_profile, 0); // 0 - for
//		// private
//		// mode
//		//txtUser.setText(sharedPreferences.getString("MemberName", "-"));
//		getRemainigDays();
//		// txtUser.setText(sharedPreferences2.getString("MemberName", "-"));
//		//is_24ol
//
//		/*if(sharedPreferences2.getBoolean("is_24ol", true)){
//			iv_explore_plans.setImageResource(R.drawable.upgradepackage);
//			iv_special_offer.setImageResource(R.drawable.img_specialoffer);
//			tv_explore_plans.setText("Upgrade Plans");
//			tv_special_offer.setText("Special Offers");
//		}
//		else{
//			iv_explore_plans.setImageResource(R.drawable.create_plan);
//			iv_special_offer.setImageResource(R.drawable.top_up);
//			tv_explore_plans.setText("Create Your Plan");
//			tv_special_offer.setText("Top Up");
//
//		}*/
//	}
//
//	public void getRemainigDays() {
//		int expDay, expMonth, expYear;
//		SharedPreferences sharedPreferences6 = context.getSharedPreferences(
//				context.getString(R.string.shared_preferences_renewal), 0);
//		String expDate = sharedPreferences6.getString("ExpiryDate", "0");
//		if (expDate != "0") {
//			String arrDate[] = expDate.split("-");
//			expDay = Integer.valueOf(arrDate[0]);
//			expMonth = Integer.valueOf(arrDate[1]);
//			expYear = Integer.valueOf(arrDate[2]);
//
//			SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
//			java.util.Date d = null;
//			java.util.Date d1 = null;
//			Calendar cal1 = Calendar.getInstance();
//			try {
//				d = dfDate.parse(expDay + "/" + expMonth + "/" + expYear);
//				Utils.log("expiry date", "is:" + d);
//
//				d1 = dfDate.parse(dfDate.format(cal1.getTime()));// Returns
//				// 15/10/2012
//				Utils.log("todays date", "is:" + d1);
//			} catch (java.text.ParseException e) {
//				e.printStackTrace();
//			}
//
//			int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
//
//			Utils.log("Remaining Days", "are:" + diffInDays);
//			if (diffInDays > 0) {
//				txtdayremaing.setText(diffInDays + " Days");
//				txtdayremaing.setTextColor(Color.parseColor("#006633"));
//			} else {
//				txtdayremaing.setText("Expired");
//				txtdayremaing.setTextColor(Color.parseColor("#FF0000"));
//			}
//		}
//	}





//	@Subscribe
//	public void onFinishEvent(FinishEvent event) {
//		Utils.log("" + IONHome.this.getClass().getSimpleName(), ":" + event.getMessage());
//		Utils.log("" + IONHome.this.getClass().getSimpleName(), "::" + Utils.Last_Class_Name);
//		if (IONHome.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())) {
//			IONHome.this.finish();
//		} else {
//
//		}
//
//	}


//	public class GetCalciVersion extends AsyncTask<String, Void, Void> {
//
//		String calc_version_result = "", calc_version_response = "";
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//
//		}
//
//		@Override
//		protected Void doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			GetCurrentVersionSOAP getCurrentVersionsoap = new GetCurrentVersionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CURRENT_VERSION));
//			try {
//				calc_version_result = getCurrentVersionsoap.getCurrentVersion("A", AppVersion, MemberId);
//				calc_version_response = getCurrentVersionsoap.getResponse();
//
//			} catch (SocketTimeoutException e) {
//				// TODO Auto-generated catch block
//				calc_version_result = "Internet is too slow";
//				e.printStackTrace();
//			} catch (SocketException e) {
//				// TODO Auto-generated catch block
//				calc_version_result = "Internet is too slow";
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				calc_version_result = "Please try again!!";
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			SharedPreferences sharedPreferences = getApplicationContext()
//					.getSharedPreferences(sharedPreferences_name, 0);
//
//			if (Utils.isOnline(IONHome.this)) {
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//					getMemberDetailWebService = new GetMemberDetailWebService();
//					getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//				} else {
//					getMemberDetailWebService = new GetMemberDetailWebService();
//					getMemberDetailWebService.execute((String) null);
//				}
//
//			}
//			if (calc_version_result.length() > 0) {
//				if (calc_version_result.equalsIgnoreCase("ok")) {
//					Utils.log("Version", ":" + calc_version_response);
//					Utils.log("APP Version", ":" + AppVersion);
//					String version_response[] = calc_version_response.split("#");
//					if (version_response.length > 1) {
//
//						if (version_response[0].equalsIgnoreCase(AppVersion)) {
//
//						} else {
//							if (version_response[1].equalsIgnoreCase("True")) {
//							/*	SharedPreferences.Editor edit=sharedPreferences.edit();
//								edit.putString("App_Version", version_response[1]);
//								edit.commit();*/
//
//								BaseActivity.isCompulsory = true;
//								showUpdateDialog(true);
//							}
//							if (version_response[1].equalsIgnoreCase("False")) {
//
//								/*SharedPreferences.Editor edit=sharedPreferences.edit();
//								edit.putString("App_Version", version_response[1]);
//								edit.commit();*/
//
//								BaseActivity.isCompulsory = false;
//								showUpdateDialog(false);
//
//							}
//						}
//					}
//				}
//			}
//		}
//
//	}

	public void showUpdateDialog(boolean isCompulsory) {
		final Dialog dialog = new Dialog(IONHome.this);
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
		WindowManager w = ((Activity) IONHome.this).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height = d.getHeight();

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


		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		btnCompulsoryUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
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
		dialog.getWindow().setLayout((width / 2) + (width / 2) / 2, LayoutParams.WRAP_CONTENT);
	}



    @Override
    public void showDelete(ArrayList<String> alNotifyId) {

    }

    @Override
    public void showIcard(String id, String datafrom, Notificationobj notificationobj) {

    }

    @Override
    public void display() {

    }


//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if (v == itemProfile) {
//			Utils.log("Profile", "Clicked");
//			if (!this.getClass().getSimpleName().equalsIgnoreCase("Profile")) {
//				IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, Profile.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//				resideMenu.closeMenu();
//			} else {
//				resideMenu.closeMenu();
//			}
//		}
//		if (v == itemRenewal) {
//			Utils.log("Renewal", "Clicked");
//			if (!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")) {
//				if (sharedPreferences1.getString("IsFreePackage", "false")
//						.equalsIgnoreCase("false")) {
//					//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//					//BaseActivity.this.finish();
//					Intent i = new Intent(IONHome.this, RenewPackage.class);
//					i.putExtra("renew", "Home");
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right,
//							R.anim.slide_out_left);
//				} else {
//					AlertsBoxFactory.showAlert2("This Facility is not available.", IONHome.this);
//				}
//
//				resideMenu.closeMenu();
//			} else {
//				resideMenu.closeMenu();
//			}
//
//		}
//
//		if (v == itemSelf_Res) {
//			Utils.log("Resolution", "Clicked");
//			if (!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")) {
//				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//				//this.finish();
//				Intent i = new Intent(IONHome.this, SelfResolution.class);
//				i.putParcelableArrayListExtra("complaintcategorylist", null);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				resideMenu.closeMenu();
//			} else {
//				resideMenu.closeMenu();
//			}
//		}
//
//		if (v == itemCall_to_CC) {
//            SharedPreferences sharedPreferences = getApplicationContext()
//                    .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
//
//            Utils utils=new Utils();
//            utils.setSharedPreferences(sharedPreferences);
//            String DefaultCustomerCareNo="+918574412345";
//            String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
//                intent.setData(Uri.parse("tel:"+CustomerCareNo));
//            else
//                intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
//            startActivity(intent);
//			/*Utils.log("CC", "Clicked");
//			SharedPreferences sharedPreferences = getApplicationContext()
//					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
//
//			Utils utils = new Utils();
//			utils.setSharedPreferences(sharedPreferences);
//			String DefaultCustomerCareNo = "8574412345";
//			String CustomerCareNo = sharedPreferences.getString("CustomerCareNo", "0");
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			if (!CustomerCareNo.equalsIgnoreCase("0") && !CustomerCareNo.equalsIgnoreCase("anyType{}"))
//				intent.setData(Uri.parse("tel:" + CustomerCareNo));
//			else
//				intent.setData(Uri.parse("tel:" + DefaultCustomerCareNo));
//			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//				// TODO: Consider calling
//				//    ActivityCompat#requestPermissions
//				// here to request the missing permissions, and then overriding
//				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//				//                                          int[] grantResults)
//				// to handle the case where the user grants the permission. See the documentation
//				// for ActivityCompat#requestPermissions for more details.
//				return;
//			}
//			startActivity(intent);*/
//			//isPermissionGranted("C");
//		}
//		/*if(v==itemChat){
//			Utils.log("Chat","Clicked");
//			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//			//	this.finish();
//			Intent i = new Intent(IONHome.this,ChatActivity.class);
//			startActivity(i);
//		}*/
//
//		if(v==itemAlerts){
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
//				this.finish();
//				Intent i = new Intent(IONHome.this,NotificationListActivity.class);
//				startActivity(i);
//				resideMenu.closeMenu();
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//		if(v==itemHelp){
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
//				this.finish();
//				Intent i = new Intent(IONHome.this,HelpActivity.class);
//				startActivity(i);
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//		if(v==itemShare){
//			/*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//			sharingIntent.setType("plain/text");
//			sharingIntent.putExtra(Intent.EXTRA_TEXT, "My text");
//			startActivity(Intent.createChooser(sharingIntent,"My App"));
//			resideMenu.closeMenu();*/
//			Intent share = new Intent("android.intent.action.SEND");
//			share.setType("plain/text");
//			share.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
//			Uri imageUri = Uri.parse("android.resource://" + getResources().getString( R.string.playstore_link ));
//			share.putExtra(Intent.EXTRA_STREAM, imageUri);
//			startActivity(Intent.createChooser(share, "Sikka  Broadband App"));
//			resideMenu.closeMenu();
//		}
//
//		if(v==itemProfile1){
//			Utils.log("Profile","Clicked");
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("Profile")){
//				IONHome.this.finish();
//				Intent i = new Intent(IONHome.this, Profile.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//		if(v==itemRenewal1){
//			Utils.log("Renewal","Clicked");
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")){
//				if (sharedPreferences1.getString("IsFreePackage", "false")
//						.equalsIgnoreCase("false")) {
//					//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//					//BaseActivity.this.finish();
//					Intent i = new Intent(IONHome.this, RenewPackage.class);
//					i.putExtra("renew", "Home");
//					startActivity(i);
//					overridePendingTransition(R.anim.slide_in_right,
//							R.anim.slide_out_left);
//				} else {
//					AlertsBoxFactory.showAlert2("This Facility is not available.", IONHome.this);
//				}
//
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//
//		}
//
//		if(v==itemSelf_Res1){
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")){
//				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//				//this.finish();
//				Intent i = new Intent(IONHome.this,SelfResolution.class);
//				i.putParcelableArrayListExtra("complaintcategorylist",null );
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//
//		if(v==itemCall_to_CC1){
//		SharedPreferences sharedPreferences = getApplicationContext()
//					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
//
//			Utils utils=new Utils();
//			utils.setSharedPreferences(sharedPreferences);
//			String DefaultCustomerCareNo="8574412345";
//			String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
//				intent.setData(Uri.parse("tel:"+CustomerCareNo));
//			else
//				intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
//			startActivity(intent);
//
//			//isPermissionGranted("C");
//		}
////		if(v==itemChat1){
//////			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
//////			//	this.finish();
//////			Intent i = new Intent(IONHome.this,ChatActivity.class);
//////			startActivity(i);
//////		}
//		if(v==itemlogout){
//			try {
//				// clearing app data
//				if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//					((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
//				} else {
//					String packageName = getApplicationContext().getPackageName();
//					Runtime runtime = Runtime.getRuntime();
//					runtime.exec("pm clear "+packageName);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		if(v==itemlogout1 ){
//			try {
//				// clearing app data
//				if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//					((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
//				} else {
//					String packageName = getApplicationContext().getPackageName();
//					Runtime runtime = Runtime.getRuntime();
//					runtime.exec("pm clear "+packageName);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//
//
//		if(v==itemAlerts1){
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
//				this.finish();
//				Intent i = new Intent(IONHome.this,NotificationListActivity.class);
//				startActivity(i);
//				resideMenu.closeMenu();
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//		if(v==itemHelp1){
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
//				this.finish();
//				Intent i = new Intent(IONHome.this,HelpActivity.class);
//				startActivity(i);
//				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//		}
//		if(v==itemShare1){
//		/*	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//			sharingIntent.setType("plain/text");
//			sharingIntent.putExtra(Intent.EXTRA_TEXT, "My text");
//			startActivity(Intent.createChooser(sharingIntent,"My App"));
//			resideMenu.closeMenu();*/
//
//
//			Intent share = new Intent("android.intent.action.SEND");
//			share.setType("plain/text");
//			share.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
//			Uri imageUri = Uri.parse("android.resource://" + getResources().getString( R.string.playstore_link ));
//			share.putExtra(Intent.EXTRA_STREAM, imageUri);
//			startActivity(Intent.createChooser(share, "Sikka  Broadband App"));
//			resideMenu.closeMenu();
//
//		}
//
//		if(v==itemRefer){
//
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("Refer_FrndActivity")){
//				//this.finish();
//				Intent i = new Intent(IONHome.this,Refer_FrndActivity.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//
//		}
//
//		if(v==itemRefer1){
//
//			if(!this.getClass().getSimpleName().equalsIgnoreCase("Refer_FrndActivity")){
//				//	this.finish();
//				Intent i = new Intent(IONHome.this,Refer_FrndActivity.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
//				resideMenu.closeMenu();
//			}
//			else{
//				resideMenu.closeMenu();
//			}
//
//		}
//
//		//resideMenu.closeMenu();
//	}


/*
	public  boolean isPermissionGranted(String param) {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v("TAG","Permission is granted");

				if(param.equalsIgnoreCase("C")) {
					SharedPreferences sharedPreferences = getApplicationContext()
							.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode

					Utils utils = new Utils();
					utils.setSharedPreferences(sharedPreferences);
					String DefaultCustomerCareNo = "8574412345";
					String CustomerCareNo = sharedPreferences.getString("CustomerCareNo", "0");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					if (!CustomerCareNo.equalsIgnoreCase("0") && !CustomerCareNo.equalsIgnoreCase("anyType{}"))
						intent.setData(Uri.parse("tel:" + CustomerCareNo));
					else
						intent.setData(Uri.parse("tel:" + DefaultCustomerCareNo));
					startActivity(intent);
				}
				return true;
			} else {

				Log.v("TAG","Permission is revoked");
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
				return false;
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			Log.v("TAG","Permission is granted");
			return true;
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {

			case 1: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
*/

//	private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
//
//		//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
//		UpdatePhoneDetailSOAP  updatePhoneDetailSOAP;
//		String reg_id="";
//		protected void onPreExecute() {
//			//Dialog.setMessage(getString(R.string.app_please_wait_label));
//			SharedPreferences	sharedPreferences_ = getApplicationContext()
//					.getSharedPreferences(getString(R.string.shared_preferences_name), 0);
//			reg_id=sharedPreferences_.getString("Gcm_reg_id", "");
//		}
//
//
//		protected void onPostExecute(Void unused) {
//				/* if (Utils.isOnline(IONHome.this)) {
//					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//					 {
//						 getMemberDetailWebService = new GetMemberDetailWebService();
//						 getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//					 }
//					 else{
//						 getMemberDetailWebService = new GetMemberDetailWebService();
//						 getMemberDetailWebService.execute((String) null);
//					 }
//
//				}*/
//
//		}
//
//		@Override
//		protected Void doInBackground(String... params) {
//			try {
//
//
//
//				updatePhoneDetailSOAP= new UpdatePhoneDetailSOAP(context.getResources().getString(
//						R.string.WSDL_TARGET_NAMESPACE),
//						context.getResources().getString(
//								R.string.SOAP_URL), context
//						.getResources().getString(
//								R.string.METHOD_UPDATE_PHONE_DETAILS_));
//
//				updatePhoneDetailSOAP.updateDetails(MemberId, Secure.getString(context.getContentResolver(), Secure.ANDROID_ID), reg_id,AppVersion);
//
//
//
//
//			} catch (Exception e) {
////					AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
//			}
//			return null;
//		}
//
//		/* (non-Javadoc)
//		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
//		 */
//		@Override
//		public void onCancel(DialogInterface dialog) {
//			// TODO Auto-generated method stub
//
//		}
//	}
}
