package com.cnergee.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import all.interface_.IError;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.BaseActivity;
import com.cnergee.mypage.BaseApplication;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.LoginFragmentActivity;
import com.cnergee.mypage.SOAP.ResetPasswordCaller;
import com.cnergee.mypage.SelfResolution;
import com.cnergee.mypage.caller.ComplaintCaller;
import com.cnergee.mypage.caller.ComplaintCategoryListCaller;
import com.cnergee.mypage.caller.ComplaintsStatusCaller;
import com.cnergee.mypage.caller.InsertComplaintCaller;
import com.cnergee.mypage.caller.LogOutCaller;
import com.cnergee.mypage.caller.ReleaseMacCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.ComplaintCategoryList;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.util.ArrayList;
import java.util.Map;

import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.BaseActivity.iError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComplaintsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplaintsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    Context context;

    Utils utils = new Utils();
    public static String rslt = "";
    public static String responseMsg = "";
    public static String strXML = "";
    public String selDateTime = null;
    String SetDateTime = "", otp_password = "";
    EditText comments;
    TextView txtcomplaintno;
    //RelativeLayout btncallcustomercare,btnReleaseMac,btnResetPwd,btnLogout;
    RelativeLayout btncallcustomercare, btnResetPwd, btnChat, btnSelfResl;
    Button btnsubmit, btncancel;
    ImageView btnhome, btnprofile, btnnotification, btnhelp;
    private String sharedPreferences_name;
    public String memberid;
    public static String complaintno = "";
    public static String statusRslt = "";
    public static String statusResponse = "";
    public static String statusResponseForPwd = "We are unable to process your request. \n Please try again later";
    public static String statusResponseForMac = "We are unable to process your request. \n Please try again later";
    public static String statusResponseForLogOut = "We are unable to process your request. \n Please try again later";
    public static String rsltLogOut = "";
    boolean isLogout = false;
    ArrayList<String> ComplaintName;
    ArrayList<String> ComplaintId;
    Spinner spinnerList;
    public static Map<String, ComplaintCategoryList> mapComplaintCategoryList;
    public static ArrayList<ComplaintCategoryList> complaintcategorylist;

    public static Map<String, ComplaintObj> mapComplaintNo;
    /*private GetComplaintNoWebService getComplaintNoWebService = null;*/
    private InsertComplaintWebService InsertComplaintWebService = null;

    GetComplaintNoWebService GetComplaintNoWebService = null;
    //ValidUserWebService validUserWebService;
    ComplaintCategoryListWebService ComplaintCategoryListWebService = null;
    SelectedWebService SelectedWebService = null;
    public boolean isValid = false;
    public static boolean isVaildUser = false;
    //ProgressDialog mainDialog;
    ProgressHUD mProgressHUD;
    String CustomerCareNo = "0";
    String DefaultCustomerCareNo = "+918574412345";
    private static boolean is_running = false;
    LinearLayout ll_24_ol;
    RelativeLayout btn_reset_mac, btn_logout;
    private String sharedPreferences_renewal,notificationMsg;
    TextView tv_marquee;


    public ComplaintsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComplaintsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintsFragment newInstance(String param1, String param2) {
        ComplaintsFragment fragment = new ComplaintsFragment();
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

        view = inflater.inflate(R.layout.fragment_complaints, container, false);
        context = getActivity();
        iError = (IError) context;
        initViews(view);

        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        notificationMsg = sharedPreferences1.getString("NotificationMsg","");
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);

        CustomerCareNo = sharedPreferences.getString("CustomerCareNo", "0");
        otp_password = sharedPreferences.getString("otp_password", "0");
        memberid = (utils.getMemberId());
        Utils.log("CustomerCare Number", ":" + CustomerCareNo);

        SharedPreferences sharedPreferences2 = context.getSharedPreferences(
                this.getString(R.string.shared_preferences_renewal), 0);

        if (sharedPreferences2.getBoolean("is_24ol", true)) {
            btnSelfResl.setVisibility(View.GONE);
            ll_24_ol.setVisibility(View.VISIBLE);
        } else {
            btnSelfResl.setVisibility(View.VISIBLE);
            ll_24_ol.setVisibility(View.GONE);
        }

        btnSelfResl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //Complaints.this.finish();

                Intent i = new Intent(context, SelfResolution.class);
                i.putParcelableArrayListExtra("complaintcategorylist", complaintcategorylist);

                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        btnChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Complaints.this.finish();

                //AlertsBoxFactory.showAlert("This facility is currently not available", Complaints.this);

			/*	Intent i = new Intent(Complaints.this,ChatActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
            }
        });

        btnResetPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
			/*Complaints.this.finish();
			Intent i = new Intent(Complaints.this,ResetPwdActivity.class);
			startActivity(i);*/

                try {
                    if (Utils.isOnline(context)) {


                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //tell the Dialog to use the dialog.xml as it's layout description
                        dialog.setContentView(R.layout.reset_pass);
							/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
							Point size = new Point();
							display.getSize(size);*/
                        int width = 0;
                        int height = 0;


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

                        TextView dtv = (TextView) dialog.findViewById(R.id.txt);

                        TextView txt = (TextView) dialog.findViewById(R.id.commentss);

                        txt.setText(Html.fromHtml("Are you sure you want to Reset the Password?"));

                        Button dialogButtonc = (Button) dialog.findViewById(R.id.btn_res_cancell);
                        Button dialogButtons = (Button) dialog.findViewById(R.id.btn_res_okk);


                        dialogButtons.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                                new ResetPasswordWebService().execute();
                            }
                        });


                        dialogButtonc.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                            }
                        });
                        dialog.show();
                        //(width/2)+((width/2)/2)
                        //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout((width / 2) + (width / 2) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }










					/*	final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					//tell the Dialog to use the dialog.xml as it's layout description
					dialog.setContentView(R.layout.reset_pass);
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					int width = size.x;
					int height = size.y;



					//dtv = (TextView) dialog.findViewById(R.id.tv1);

					TextView txt = (TextView) dialog.findViewById(R.id.tv);

					txt.setText(Html.fromHtml(message));

					Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							dialog.dismiss();
						}
					});

					dialog.show();
					//(width/2)+((width/2)/2)
					//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
				*/


                    else if (rslt.trim().equalsIgnoreCase("not")) {
                        mProgressHUD.dismiss();
                        AlertsBoxFactory.showAlert("NO DATA FOUND !!! ", context);
                    }
                } catch (Exception e) {
                    Utils.log("Error", "is" + e);
                    mProgressHUD.dismiss();
                    AlertsBoxFactory.showAlert(rslt, context);
                }

            }

        });


		/*
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(Html.fromHtml("Are you sure you want to Reset the Password?"))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(Html.fromHtml("Confirmation"))
				.setCancelable(false)

				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
					//Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();

	           }
		     })

		       .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
					//	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
					new ResetPasswordWebService().execute();
		           }
		       });


		AlertDialog alert = builder.create();
		alert.show();

				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
				}

			}
		});
		*/
        btn_reset_mac.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Utils.isOnline(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(Html.fromHtml("Are you sure you want to Release MAC?"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(Html.fromHtml("Confirmation"))
                            .setCancelable(false)

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
                                    Utils.log("mob number", "s" + utils.getMobileNoPrimary());
                                    Utils.log("MemberId", "s" + utils.getMemberId());
                                    Utils.log("MemberLoginId", "s" + utils.getMemberLoginID());
                                    new ReleaseMacWebService().execute();
                                }
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Utils.isOnline(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(Html.fromHtml("Are you sure you want to end your current Internet session from Server?"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(Html.fromHtml("Confirmation"))
                            .setCancelable(false)

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
                                }
                            })

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
                                    Utils.log("mob number", "s" + utils.getMobileNoPrimary());
                                    Utils.log("MemberId", "s" + utils.getMemberId());
                                    Utils.log("MemberLoginId", "s" + utils.getMemberLoginID());
                                    new LogOutWebService().execute();
                                }
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isOnline(context)) {
                    Utils.log("submit Button", "Submiit Button Executed");


                    if (spinnerList.getSelectedItemPosition() != 0) {
                        Utils.log("spinner executed", "Spinner Executed");
                        if (TextUtils.isEmpty(comments.getText().toString().trim())) {
                            Utils.log("spinner  empty Text executed", "Spinner Emptytext Executed");
                            AlertsBoxFactory.showAlert(" Please enter valid comments.", context);

                        } else {
                            Utils.log("on else ", "selet item selsted Executed");

                            if ((statusResponse.equalsIgnoreCase("DC") || statusResponse.equalsIgnoreCase("AR"))) {
                                Utils.log("status Login", "Spinner Executed");
                                if (statusResponse.equalsIgnoreCase("DC")) {
                                    AlertsBoxFactory.showAlertOffer("Package Alert", "Package has been expired. \n Please renew your package.", context);
                                } else if (statusResponse.equalsIgnoreCase("AR") && spinnerList.getSelectedItem().toString().equalsIgnoreCase("Slow Browsing")) {
                                    AlertsBoxFactory.showAlertOffer("Package Alert", "You are on complementary 128kbps plan. \n Please renew your package.", context);
                                } else {
                                    new InsertComplaintWebService().execute();
                                }

                            } else if ((statusResponse.equalsIgnoreCase("AR") || statusResponse.equalsIgnoreCase("DC"))) {
                                if (statusResponse.equalsIgnoreCase("DC"))
                                    AlertsBoxFactory.showAlertOffer("Package Alert", "Package has been expired. \n Please renew your package.", context);
                                else if (statusResponse.equalsIgnoreCase("AR") && spinnerList.getSelectedItem().toString().equalsIgnoreCase("Slow Browsing")) {
                                    AlertsBoxFactory.showAlertOffer("Package Alert", "You are on complementary 128kbps plan. \n Please renew your package.", context);
                                } else {
                                    new InsertComplaintWebService().execute();
                                }
                            } else {
                                InsertComplaintWebService = new InsertComplaintWebService();
                                InsertComplaintWebService.execute((String) null);
                            }

                        }
                    } else {
                        //AlertsBoxFactory.showAlertOffer("Package Alert","Please select Complaint category type.",context);

                        AlertsBoxFactory.showAlert2("Please select Complaint category type.", context);
                    }

                } else {
                    Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btncallcustomercare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
				/*Intent intent = new Intent(Intent.ACTION_CALL);
				if (!CustomerCareNo.equalsIgnoreCase("0") && !CustomerCareNo.equalsIgnoreCase("anyType{}"))
					intent.setData(Uri.parse("tel:" + CustomerCareNo));
				else
					intent.setData(Uri.parse("tel:" + DefaultCustomerCareNo));
				if (ActivityCompat.checkSelfPermission(Complaints.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				startActivity(intent);*/

                //isPermissionGranted();

                SharedPreferences sharedPreferences = context
                        .getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode

                Utils utils=new Utils();
                utils.setSharedPreferences(sharedPreferences);
                String DefaultCustomerCareNo="+918574412345";
                String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
                    intent.setData(Uri.parse("tel:"+CustomerCareNo));
                else
                    intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
                startActivity(intent);
            }
        });

        if(Utils.isOnline(context)){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new ValidUserWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
            else
                new ValidUserWebService().execute();

        }
        else{
            Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
        }
        BaseApplication.getEventBus().register(this);
        // Utils.Last_Class_Name=this.getClass().getSimpleName();






        return view;
    }
//    public  boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG", "Permission is granted");
//
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                if (!CustomerCareNo.equalsIgnoreCase("0") && !CustomerCareNo.equalsIgnoreCase("anyType{}"))
//                    intent.setData(Uri.parse("tel:" + CustomerCareNo));
//                else
//                    intent.setData(Uri.parse("tel:" + DefaultCustomerCareNo));
//
//
//                startActivity(intent);
//                return true;
//            } else {
//
//                Log.v("TAG", "Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return false;
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG", "Permission is granted");
//            return true;
//        }
//    }

    private void initViews(View view) {

        Utils.log("In Compalint", "Executed");
        comments = (EditText)view.findViewById(R.id.comments);
        //txtcomplaintno = (TextView)findViewById(R.id.txtcomplaintno);
        btnsubmit = (Button)view.findViewById(R.id.btnsubmit);
        btnhome = (ImageView)view.findViewById(R.id.btnhome);
        btnprofile = (ImageView)view.findViewById(R.id.btnprofile);
        btnnotification = (ImageView)view.findViewById(R.id.btnnotification);
        btnResetPwd = (RelativeLayout)view.findViewById(R.id.btnResetPwd);
        btnSelfResl = (RelativeLayout)view.findViewById(R.id.selfrsoluton);
        btnChat = (RelativeLayout)view.findViewById(R.id.chat_customer);
        btn_reset_mac = (RelativeLayout)view.findViewById(R.id.btnresetMac);
        btn_logout = (RelativeLayout)view.findViewById(R.id.btn_logoff);
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);
        spinnerList = (Spinner)view.findViewById(R.id.spinnerList);
        ll_24_ol = (LinearLayout)view.findViewById(R.id.ll_24_ol);
        comments = (EditText)view.findViewById(R.id.comments);
        btncallcustomercare = (RelativeLayout)view.findViewById(R.id.btncallcustomercare);


    }

    //*******************************ReleaseMac Web Service*********starts here**************************
    private class ReleaseMacWebService extends AsyncTask<String, Void, Void>  implements DialogInterface.OnCancelListener {
        //private ProgressDialog Dialog1 = new ProgressDialog(
        //	Complaints.this);
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);

        }
        @Override
        protected Void doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                ReleaseMacCaller caller = new ReleaseMacCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_RESET_MAC),true);

                caller.setMemberId(utils.getMemberId());
                caller.setMemberLoginId(utils.getMemberLoginID());
                caller.setMobileNumber(utils.getMobileNoPrimary());
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
						+ e.toString(), Complaints.this);*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //Utils.log("Post Execute called","yes");
            mProgressHUD.dismiss();
            AlertsBoxFactory.showAlert(statusResponseForMac, context);
            //Utils.log("Response for status",""+statusResponse);
        }
        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

//*******************************ReleaseMac Web Service*********ends here**************************

//*******************************ResetPassword Web Service*********starts here**************************

    private class ResetPasswordWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        //private ProgressDialog Dialog1 = new ProgressDialog(
        //	Complaints.this);
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);

        }
        @Override
        protected Void doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                ResetPasswordCaller caller = new ResetPasswordCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_RESET_PASSWORD),"complaints");

                caller.setMemberId(utils.getMemberId());
                caller.setMemberLoginId(utils.getMemberLoginID());

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
						+ e.toString(), Complaints.this);*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //Utils.log("Post Execute called","yes");
            mProgressHUD.dismiss();
            AlertsBoxFactory.showAlert(statusResponseForPwd, context);
            //Utils.log("Response for status",""+statusResponse);
        }
        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

//*******************************ResetPassword Web Service*********ends here**************************



    //*******************************Check Valid User Web Service*********starts here**************************
    protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        // final AlertDialog alert =new
        // AlertDialog.Builder(Login.this).create();

        //private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);

        protected void onPreExecute() {
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);

        }

        @SuppressLint("CommitPrefEdits")
        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {

                if (isVaildUser) {
                    ComplaintCategoryListWebService =new ComplaintCategoryListWebService();
                    ComplaintCategoryListWebService.execute((String) null);
                } else {

                    BaseApplication.getEventBus().post(
                            new FinishEvent(IONHome.class.getSimpleName()));

                    mProgressHUD.dismiss();
                    SharedPreferences sharedPreferences1 = context
                            .getSharedPreferences(sharedPreferences_name, 0); // 0
                    // -
                    // for
                    // private
                    // mode
                    SharedPreferences.Editor edit1 = sharedPreferences1.edit();
                    edit1.clear();
                    edit1.commit();

                    SharedPreferences sharedPreferences2 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_renewal),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit2 = sharedPreferences2.edit();
                    edit2.clear();
                    edit2.commit();
                    SharedPreferences sharedPreferences3 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_notification_list),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit3 = sharedPreferences3.edit();
                    edit3.clear();
                    edit3.commit();
                    SharedPreferences sharedPreferences4 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_payment_history),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit4 = sharedPreferences4.edit();
                    edit4.clear();
                    edit4.commit();
                    SharedPreferences sharedPreferences5 = context
                            .getSharedPreferences(
                                    getString(R.string.shared_preferences_profile),
                                    0); // 0 - for private mode
                    SharedPreferences.Editor edit5 = sharedPreferences5.edit();
                    edit5.clear();
                    edit5.commit();
                    //Utils.log("Data","cleared");

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
                mProgressHUD.dismiss();
                if(rslt.trim().equalsIgnoreCase("error")){
                    iError.display();
                }
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
                smsCaller.MemberId = memberid;
                smsCaller.OneTimePwd=otp_password;
                //smsCaller.setAllData(true);
                smsCaller.setCallData("complaints");
                smsCaller.join();
                smsCaller.start();
                rslt = "START";

                while (rslt == "START") {
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

        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

    }
    //*******************************Check Valid User Web Service*********ends here**************************


    //*******************************Logout Web Service*********starts here**************************
    private class LogOutWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        //private ProgressDialog Dialog1 = new ProgressDialog(
        //	Complaints.this);
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);

        }
        @Override
        protected Void doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                LogOutCaller caller = new LogOutCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_LOG_OUT_BROWSER),true);

                caller.setMemberId(utils.getMemberId());
                caller.setMemberLoginId(utils.getMemberLoginID());

                caller.join();
                caller.start();
                rsltLogOut = "START";

                while (rsltLogOut == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception e) {
						/*AlertsBoxFactory.showErrorAlert("Error web-service response "
								+ e.toString(), Complaints.this);*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            Utils.log("Log outPost Execute called","yes");
            mProgressHUD.dismiss();
            AlertsBoxFactory.showAlert(statusResponseForLogOut, context);
            //Utils.log("Response for status",""+statusResponse);
        }
        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

    private class GetComplaintNoWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        //private ProgressDialog Dialog = new ProgressDialog(
        //		Complaints.this);

        protected void onPreExecute() {
            //Dialog.setMessage(getString(R.string.app_please_wait_label));
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        }

        protected void onPostExecute(Void unused) {

            //DiaUtils.dismiss();
            GetComplaintNoWebService = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                try {
                    txtcomplaintno.setText(complaintno);

                    ComplaintCategoryListWebService =new ComplaintCategoryListWebService();
                    ComplaintCategoryListWebService.execute((String) null);

                } catch (NumberFormatException nue) {


                }

            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }
            this.cancel(true);






			/*GetComplaintNoWebService = null;
			DiaUtils.dismiss();

			Log.i(">>>>.MemberDetails<<<<<<", mapComplaintNo.toString());
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapComplaintNo != null) {

					Set<String> keys = mapComplaintNo.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					ComplaintObj ComplaintNo = mapComplaintNo.get(selItem);


					txtcomplaintno.setText(ComplaintNo.getMemberComplaintNo());

				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){AlertsBoxFactory.showAlert(rslt,context );}	*/
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                ComplaintCaller complaintnoCaller = new ComplaintCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GET_COMPLAINTNO)
                );

                complaintnoCaller.memberid = memberid;
                //memberdetailCaller.setAllData(true);

                complaintnoCaller.join();
                complaintnoCaller.start();
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
            //DiaUtils.dismiss();
            GetComplaintNoWebService = null;
        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }


    private class InsertComplaintWebService extends AsyncTask<String, Void, Void>implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        ComplaintObj complaintobj = new ComplaintObj();

        protected void onPreExecute() {

            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true, this);
        }

        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();
            InsertComplaintWebService = null;
            //submit.setClickable(true);
        }


        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();
            //submit.setClickable(true);
            InsertComplaintWebService = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {

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

                txt.setText(Html.fromHtml(responseMsg));
                dtv.setText(Html.fromHtml("Confirmation"));
                Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        getActivity().finish();
                        Intent i = new Intent(context, IONHome.class);
                        startActivity(i);
                    }
                });

                dialog.show();
                //(width/2)+((width/2)/2)
                //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);

            } else {
                AlertsBoxFactory.showAlert(rslt,context );
                return;
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Log.i("ComplaintId",""+ComplaintId.get(spinnerList.getSelectedItemPosition()));
                //complaintobj.setMemberComplaintNo(txtcomplaintno.getText().toString());
                complaintobj.setComplaintId(ComplaintId.get(spinnerList.getSelectedItemPosition()));
                complaintobj.setMemberId(memberid);
                complaintobj.setMessage(comments.getText().toString());

                InsertComplaintCaller caller = new InsertComplaintCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_INSERT_COMPLAINTS),"complaint");


                caller.setcomplaintobj(complaintobj);

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
                //AlertsBoxFactory.showAlert(rslt,context );

                Utils.log("Error Complaint","596"+e);
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

    private class ComplaintCategoryListWebService extends AsyncTask<String, Void, Void> {
        //private ProgressDialog Dialog = new ProgressDialog(
        //	Complaints.this);

        protected void onPreExecute() {
            //mainDialog.setMessage(getString(R.string.app_please_wait_label));
            //mainDialog.show();
        }
        @Override
        protected void onCancelled() {
            //DiaUtils.dismiss();
            ComplaintCategoryListWebService = null;
        }
        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();
            ComplaintCategoryListWebService = null;
            //DiaUtils.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {
                ComplaintName = new ArrayList<String>();
                ComplaintId = new ArrayList<String>();
                for(int i=0; i< complaintcategorylist.size(); i++ )
                {
                    ComplaintName.add(complaintcategorylist.get(i).getComnplaintName());
                    ComplaintId.add(complaintcategorylist.get(i).getComplaintId());
                }
                ArrayAdapter ComplainArray = new ArrayAdapter(context, android.R.layout.simple_spinner_item, ComplaintName);
                spinnerList.setAdapter(ComplainArray);
            } else {
                if(is_running)
                    AlertsBoxFactory.showAlert("Invalid web-service response "
                            + rslt, context);
            }
            //new SelectedWebService().execute();
            SelectedWebService= new SelectedWebService();
            SelectedWebService.execute((String) null);
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                ComplaintCategoryListCaller caller = new ComplaintCategoryListCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_COMPLAINT_CATEGORY_LIST));
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

    }


    private class SelectedWebService extends AsyncTask<String, Void, Void> {
        //private ProgressDialog Dialog1 = new ProgressDialog(
        //	Complaints.this);
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	Dialog1.setCancelable(false);
            //	Dialog1.setMessage(getString(R.string.app_please_wait_label));
            //	Dialog1.show();

        }
        @Override
        protected Void doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                ComplaintsStatusCaller caller = new ComplaintsStatusCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GET_CURRENT_STATUS));

                caller.setMemberIdCaller(utils.getMemberId());
                caller.join();
                caller.start();
                statusRslt = "START";

                while (statusRslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception e) {
			/*	AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //Utils.log("Post Execute called","yes");
            mProgressHUD.dismiss();

            //Utils.log("Response for status",""+statusResponse);
        }
    }


}