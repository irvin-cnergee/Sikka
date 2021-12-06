package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.cnergee.mypage.SOAP.GetAtomSignatureSoap;
import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.BeforePaymentInsertCaller;
import com.cnergee.mypage.caller.GetRedirectionDetailsCaller;
import com.cnergee.mypage.caller.InsertBeforeWithTrackCaller;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.caller.PaymentGatewayCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.obj.RedirectionDetailObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.sabpaisa.payment.EDW;
import com.sabpaisa.payment.callback.SabPaisaPG;
import com.sabpaisa.payment.pojo.PaymentResponse;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import cnergee.sikka.broadband.R;

public class MakePaymentSubpaisa extends BaseActivity implements SabPaisaPG {


    LinearLayout linnhome, linnprofile, linnnotification, linnhelp, llClickDetails, ll_addtional_details;
    TextView txtloginid, txtemailid, txtcontactno, txtnewpackagename, txtnewamount, txtnewvalidity, tvDiscountLabel,TextView14,TextView15;

    String SabPaisaTxId,firstName,lastName,payMode,email,mobileNo,transDate,orgTxnAmount,
            spTrackId,spAmount,spPaymentid,spRespStatus,amount,spRespCode,pgRespCode,PGTxnNo,issuerRefNo,reMsg,clientTxnId,res_status,clientCode,password,
            payerAddress,payerContact,payerEmail,payerFirstName,payerLastName,txnAmt,URLfailure,URLsuccess,username,
            key,iv,authIV,authKey;

    CheckBox privacy, terms;
    private String sharedPreferences_name;
    Button btnnb;
    String ServiceTax, UpdateFrom, discount, ClassName;
    public boolean is_member_details = false, is_activity_running = false, trackid_check = false;
    public static boolean Changepack;
    public boolean is_payemnt_detail = false;
    public boolean isDetailShow = false;
    boolean isLogout = false;
    public long memberid;
    String isRenew = "";
    String type = "Renew";

    private ScrollView payNowView, responseScrollLayout;
    String TrackId;
    private InsertBeforeWithTrackId insertBeforeWithTrackId =null;

    public static String adjTrackval = "";
    public static String responseMsg = ""; public static String rslt = "";
    public static Map<String, MemberDetailsObj> mapMemberDetails;
    public static Map<String, RedirectionDetailObj> MapRedirectionDetails;
    private String customername, Email_id;
    private InsertBeforePayemnt InsertBeforePayemnt = null;

    AdditionalAmount additionalAmount;
    MemberDetailsObj memberDetails;

    RedirectionDetailObj detailObj;
    Bundle bundle;
    PaymentsObj paymentsObj = new PaymentsObj();

    private PaymentGateWayDetails getpaymentgatewaysdetails = null;
    private GetMemberDetailWebService getMemberDetailWebService = null;
    private GetRedirectionDetails getRedirectionDetails = null;
    public static Context context;
    Utils utils = new Utils();

    private static int ACC_ID = 0000;
    private static String SECRET_KEY = "";
    private static String HOST_NAME = "";
    //private static final double PER_UNIT_PRICE = 12.34;
    ArrayList<HashMap<String, String>> custom_post_parameters;
    String atom_url ="",atom_acces_code ="",atom_error="",atom_track_id= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment_subpaisa);
        initControls();

    }

    public void initControls() {
        linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
        linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
        linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
        linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);

        llClickDetails = (LinearLayout) findViewById(R.id.llClickDetails);
        ll_addtional_details = (LinearLayout) findViewById(R.id.ll_addtional_details);

        txtloginid = (TextView) findViewById(R.id.txtloginid);
        txtemailid = (TextView) findViewById(R.id.txtemailid);
        txtcontactno = (TextView) findViewById(R.id.txtcontactno);
        txtnewpackagename = (TextView) findViewById(R.id.txtnewpackagename);
        txtnewamount = (TextView) findViewById(R.id.txtnewamount);
        txtnewvalidity = (TextView) findViewById(R.id.txtnewvalidity);

        tvDiscountLabel = (TextView) findViewById(R.id.tvDiscountLabel);
        TextView14 =  (TextView) findViewById(R.id.TextView14);
        TextView15 =  (TextView) findViewById(R.id.TextView15);

        privacy = (CheckBox) findViewById(R.id.privacy);
        terms = (CheckBox) findViewById(R.id.terms);

        btnnb = (Button) findViewById(R.id.btnnb);

        payNowView = (ScrollView) findViewById(R.id.payNowLayout);
        responseScrollLayout = (ScrollView) findViewById(R.id.responseScrollLayout);

       terms.setChecked(true);
       privacy.setChecked(true);

       terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (compoundButton.isChecked()) {
                   // checked
                   terms.setChecked(true);
                   Log.d("terms",""+b);

               }
               else
               {
                   // not checked
                   Log.d("terms",""+b);
                   AlertsBoxFactory
                           .showAlert(
                                   "Please accept Terms and Conditions.",
                                   MakePaymentSubpaisa.this);
               }
           }
       });

        privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // checked
                    privacy.setChecked(true);
                    Log.d("privacy",""+b);

                }
                else
                {
                    // not checked
                    Log.d("privacy",""+b);
                    AlertsBoxFactory
                            .showAlert(
                                    "Please accept Privacy Policy.",
                                    MakePaymentSubpaisa.this);
                }
            }
        });
        btnnb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    if (Double.parseDouble(txtnewamount.getText().toString()) > 0) {
                        if ((terms.isChecked() == true )&&(privacy.isChecked() == true)) {

                            if(Utils.isOnline(MakePaymentSubpaisa.this)){
                                if(trackid_check){
                                    is_member_details=false;
                                    // TrackId Generated Successfully.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        new InsertBeforePayemnt().executeOnExecutor(
                                                AsyncTask.THREAD_POOL_EXECUTOR,
                                                (String) null);
                                    } else {
                                        new InsertBeforePayemnt().execute((String) null);
                                    }
                                }
                                else{
                                    // TrackId Failed to Generate.
                                    is_member_details=true;
                                    if(Utils.isOnline(MakePaymentSubpaisa.this)){
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                            getpaymentgatewaysdetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

                                        } else {

                                            getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                            getpaymentgatewaysdetails.execute((String) null);
                                        }
                                    }
                                }
                            }
                            else{
                                Toast.makeText(MakePaymentSubpaisa.this,
                                        getString(R.string.app_please_wait_label),
                                        Toast.LENGTH_LONG).show();


                            }


                        } else {
                            AlertsBoxFactory
                                    .showAlert(
                                            "Please accept Terms and Conditions, Privacy Policy.",
                                            MakePaymentSubpaisa.this);


                        }

                    } else {
                        if(is_activity_running)
                            AlertsBoxFactory
                                    .showAlert(
                                            "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                            MakePaymentSubpaisa.this);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                    if(is_activity_running)
                        AlertsBoxFactory
                                .showAlert(
                                        "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                        MakePaymentSubpaisa.this);
                }

            }
        });


        linnhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MakePaymentSubpaisa.this.finish();
                Intent i = new Intent(MakePaymentSubpaisa.this, IONHome.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });

        linnprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MakePaymentSubpaisa.this.finish();
                Intent i = new Intent(MakePaymentSubpaisa.this, Profile.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });

        linnnotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MakePaymentSubpaisa.this.finish();
                Intent i = new Intent(MakePaymentSubpaisa.this,
                        NotificationListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });


        linnhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MakePaymentSubpaisa.this.finish();
                Intent i = new Intent(MakePaymentSubpaisa.this, HelpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                BaseApplication.getEventBus().post(
                        new FinishEvent("RenewPackage"));
                BaseApplication.getEventBus().post(
                        new FinishEvent(Utils.Last_Class_Name));
            }
        });


        Intent intent = getIntent();
        bundle = intent.getExtras();

        txtnewpackagename.setText(bundle.getString("PackageName"));
        txtnewvalidity.setText(bundle.getString("PackageValidity"));
        ServiceTax = bundle.getString("ServiceTax");
        UpdateFrom = bundle.getString("updateFrom");
        discount = bundle.getString("discount");
        ClassName = bundle.getString("ClassName");
        additionalAmount = (AdditionalAmount) bundle.getSerializable("addtional_amount");

        if (bundle.getString("datafrom").equalsIgnoreCase("changepack")) {
            Changepack = true;
            tvDiscountLabel.setVisibility(View.GONE);
        } else {
            Changepack = false;
            Utils.log("Renew", "account");
            tvDiscountLabel.setVisibility(View.VISIBLE);
        }

        if (additionalAmount != null) {
            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    // tvDiscountLabel.setText("You have got "+additionalAmount.getDiscountPercentage()+"% discount for online payment.");
                    tvDiscountLabel.setText("Avail of a "
                            + additionalAmount.getDiscountPercentage()
                            + "% discount by paying online.");
                } else {
                    tvDiscountLabel.setVisibility(View.GONE);
                }
            } else {
                tvDiscountLabel.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                txtnewamount.setText(additionalAmount.getFinalcharges());
            }
            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                is_payemnt_detail = true;
            }
            if (is_payemnt_detail) {
                txtnewamount.setText(additionalAmount.getFinalcharges());
                llClickDetails.setVisibility(View.VISIBLE);
            } else {
                llClickDetails.setVisibility(View.GONE);
            }
        } else {
            tvDiscountLabel.setVisibility(View.GONE);
        }


        txtnewamount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (is_payemnt_detail) {
                    showPaymentDetailsDialog(additionalAmount);
                }
            }
        });


        llClickDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (is_payemnt_detail) {
                    if (isDetailShow) {
                        ll_addtional_details.setVisibility(View.GONE);
                        isDetailShow = false;
                    } else {
                        ll_addtional_details.setVisibility(View.VISIBLE);
                        isDetailShow = true;
                    }
                    showPaymentDetails(additionalAmount);

                } else {
                    ll_addtional_details.setVisibility(View.GONE);
                }
            }
        });

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        utils.setSharedPreferences(sharedPreferences);
        memberid = Long.parseLong(utils.getMemberId());
        isRenew = sharedPreferences.getString(Utils.IS_RENEWAL,"0");


        if (Utils.isOnline(MakePaymentSubpaisa.this)) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

                getMemberDetailWebService = new MakePaymentSubpaisa.GetMemberDetailWebService();
                getMemberDetailWebService.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

            } else {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.execute((String) null);

                getMemberDetailWebService = new MakePaymentSubpaisa.GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);
            }

            payNowView.setVisibility(View.VISIBLE);
            responseScrollLayout.setVisibility(View.GONE);
        } else {
            if (is_activity_running)
                AlertsBoxFactory.showAlert("Please connect to internet !!", MakePaymentSubpaisa.this);
        }

    }
    public void showPaymentDetails(AdditionalAmount additionalAmount) {
        if (additionalAmount != null) {
            // ll_addtional_details.setVisibility(View.VISIBLE);
            LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

            TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

            ll_package_rate = (LinearLayout) findViewById(R.id.ll_package_rate);
            ll_add_amt = (LinearLayout) findViewById(R.id.ll_add_amt);
            ll_add_reason = (LinearLayout) findViewById(R.id.ll_add_reason);
            ll_discount_amt = (LinearLayout) findViewById(R.id.ll_discount_amt);
            ll_fine_amount = (LinearLayout) findViewById(R.id.ll_fine_amt);
            ll_days_fine_amt = (LinearLayout) findViewById(R.id.ll_days_fine_amt);
            ll_discount_per = (LinearLayout) findViewById(R.id.ll_discount_per);
            ll_final_amt = (LinearLayout) findViewById(R.id.ll_final_amount);

            tv_package_rate = (TextView) findViewById(R.id.tv_package_rate);
            tv_add_amt = (TextView) findViewById(R.id.tv_add_amt);
            tv_add_reason = (TextView) findViewById(R.id.tv_add_reason);
            tv_discount_amt = (TextView) findViewById(R.id.tv_discount_amt);
            tv_fine_amount = (TextView) findViewById(R.id.tv_fine_amt);
            tv_days_fine_amt = (TextView) findViewById(R.id.tv_days_fine_amt);
            tv_discount_per = (TextView) findViewById(R.id.tv_discount_per);
            tv_final_amt = (TextView) findViewById(R.id.tv_final_amount);

            if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
                ll_package_rate.setVisibility(View.VISIBLE);
                tv_package_rate.setText(additionalAmount.getPackageRate());
            } else {

                ll_package_rate.setVisibility(View.GONE);

            }

            if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
                ll_add_amt.setVisibility(View.VISIBLE);
                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
            } else {
                ll_add_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getAdditionalAmountType().length() > 0) {
                ll_add_reason.setVisibility(View.GONE);
                tv_add_reason.setText(additionalAmount
                        .getAdditionalAmountType());
            } else {
                ll_add_reason.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                ll_discount_amt.setVisibility(View.VISIBLE);
                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
            } else {
                ll_discount_amt.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                ll_fine_amount.setVisibility(View.VISIBLE);
                tv_fine_amount.setText(additionalAmount.getFineAmount());
            } else {
                ll_fine_amount.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                ll_days_fine_amt.setVisibility(View.VISIBLE);
                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
            } else {
                ll_days_fine_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    ll_discount_per.setVisibility(View.VISIBLE);
                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
                } else {
                    ll_discount_per.setVisibility(View.GONE);
                }
            } else {
                ll_discount_per.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                ll_final_amt.setVisibility(View.VISIBLE);
                tv_final_amt.setText(additionalAmount.getFinalcharges());
            } else {
                ll_final_amt.setVisibility(View.GONE);
            }
        } else {
            ll_addtional_details.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MakePaymentSubpaisa.this.finish();
        Intent i = new Intent(MakePaymentSubpaisa.this, IONHome.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
        BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
    }


    public void showPaymentDetailsDialog(AdditionalAmount additionalAmount) {
        if (additionalAmount != null) {
            final Dialog dialog = new Dialog(MakePaymentSubpaisa.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_additional_amount);

            int width = 0;
            int height = 0;

            Point size = new Point();
            WindowManager w = ((Activity) MakePaymentSubpaisa.this)
                    .getWindowManager();

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

            LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

            TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

            ll_package_rate = (LinearLayout) dialog
                    .findViewById(R.id.ll_package_rate);
            ll_add_amt = (LinearLayout) dialog.findViewById(R.id.ll_add_amt);
            ll_add_reason = (LinearLayout) dialog
                    .findViewById(R.id.ll_add_reason);
            ll_discount_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_discount_amt);
            ll_fine_amount = (LinearLayout) dialog
                    .findViewById(R.id.ll_fine_amt);
            ll_days_fine_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_days_fine_amt);
            ll_discount_per = (LinearLayout) dialog
                    .findViewById(R.id.ll_discount_per);
            ll_final_amt = (LinearLayout) dialog
                    .findViewById(R.id.ll_final_amount);

            tv_package_rate = (TextView) dialog.findViewById(R.id.tv_package_rate);
            tv_add_amt = (TextView) dialog.findViewById(R.id.tv_add_amt);
            tv_add_reason = (TextView) dialog.findViewById(R.id.tv_add_reason);
            tv_discount_amt = (TextView) dialog.findViewById(R.id.tv_discount_amt);
            tv_fine_amount = (TextView) dialog.findViewById(R.id.tv_fine_amt);
            tv_days_fine_amt = (TextView) dialog.findViewById(R.id.tv_days_fine_amt);
            tv_discount_per = (TextView) dialog.findViewById(R.id.tv_discount_per);
            tv_final_amt = (TextView) dialog.findViewById(R.id.tv_final_amount);

            if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
                ll_package_rate.setVisibility(View.VISIBLE);
                tv_package_rate.setText(additionalAmount.getPackageRate());
            } else {
                ll_package_rate.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
                ll_add_amt.setVisibility(View.VISIBLE);
                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
            } else {
                ll_add_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getAdditionalAmountType().length() > 0) {
                ll_add_reason.setVisibility(View.GONE);
                tv_add_reason.setText(additionalAmount.getAdditionalAmountType());
            } else {
                ll_add_reason.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
                ll_discount_amt.setVisibility(View.VISIBLE);
                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
            } else {
                ll_discount_amt.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
                ll_fine_amount.setVisibility(View.VISIBLE);
                tv_fine_amount.setText(additionalAmount.getFineAmount());
            } else {
                ll_fine_amount.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
                ll_days_fine_amt.setVisibility(View.VISIBLE);
                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
            } else {
                ll_days_fine_amt.setVisibility(View.GONE);
            }

            if (additionalAmount.getDiscountPercentage().length() > 0) {
                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
                    ll_discount_per.setVisibility(View.VISIBLE);
                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
                } else {
                    ll_discount_per.setVisibility(View.GONE);
                }
            } else {
                ll_discount_per.setVisibility(View.GONE);
            }

            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
                ll_final_amt.setVisibility(View.VISIBLE);
                tv_final_amt.setText(additionalAmount.getFinalcharges());
            } else {
                ll_final_amt.setVisibility(View.GONE);
            }
            Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

        }
    }

    private class InsertBeforeWithTrackId extends AsyncTask<String,Void,Void> implements DialogInterface.OnCancelListener
    {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        @Override
        protected void onPreExecute() {
            if(is_activity_running){
                mProgressHUD = ProgressHUD
                        .show(MakePaymentSubpaisa.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if(is_activity_running)
                mProgressHUD.dismiss();
            insertBeforeWithTrackId = null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... strings) {
            try {

                InsertBeforeWithTrackCaller caller = new InsertBeforeWithTrackCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_BEFORE_MEMBER_PAY_WITH_TRACKID),true);

                paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
                paymentsObj.setTrackId(TrackId);
                paymentsObj.setAmount(txtnewamount.getText().toString().trim());
                paymentsObj.setPackageName(txtnewpackagename.getText().toString());
                paymentsObj.setServiceTax(ServiceTax);
                paymentsObj.setDiscount_Amount(additionalAmount.getDiscountAmount());
                paymentsObj.setBankcode("SB");


                if(Utils.pg_sms_request){
                    if(Utils.pg_sms_uniqueid.length()>0){
                        paymentsObj.setPg_sms_unique_id(Utils.pg_sms_uniqueid);
                    }
                    else{
                        paymentsObj.setPg_sms_unique_id(null);
                    }
                }
                else{
                    paymentsObj.setPg_sms_unique_id(null);
                }
                paymentsObj.setIs_renew(isRenew);

                caller.setPaymentdata(paymentsObj);

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
                /* AlertsBoxFactory.showAlert(rslt,context ); */
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (is_activity_running)
                mProgressHUD.dismiss();
            insertBeforeWithTrackId = null;
            if (rslt.trim().equalsIgnoreCase("null") || rslt.equals(null)) {
                AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", MakePaymentSubpaisa.this);

            } else {
                if (rslt.trim().equalsIgnoreCase("ok")) {

                    Log.e("RESPONSE TRACKID", ":" + MakeMyPayments_CCAvenue.responseMsg);

                    TrackId = adjTrackval;
                    if (TrackId.equals("null") || TrackId.equals(null)) {
                        AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", MakePaymentSubpaisa.this);

                    } else {

                        if (TrackId != null && TrackId.length() > 0 && !TrackId.equalsIgnoreCase("null")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new Get_SubPaisa_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                            } else {
                                new Get_SubPaisa_Signature().execute();
                            }

                        } else {
                            AlertsBoxFactory.showAlert("TrackId not generated. Please try Again !!!", MakePaymentSubpaisa.this);
                        }

                    }
                } else {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert(rslt, context);
                    return;
                }
            }
        }
    }


    private class   Get_SubPaisa_Signature extends AsyncTask<String , Void , Void> implements DialogInterface.OnCancelListener {

        GetAtomSignatureSoap getAtomSignatureSoap;
        String getatomResult = "";
        String response = "";
        ProgressHUD mProgressHUD;
        @Override
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD
                    .show(MakePaymentSubpaisa.this,
                            getString(R.string.app_please_wait_label), true,
                            true, this);
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(mProgressHUD!=null)
                mProgressHUD.dismiss();
            Utils.log("OnPostAtom","OnPostAtom");
            if(getatomResult!=null&&getatomResult.length()>0){
               if(response!=null&&response.length()>0){
                    try {
                        JSONObject mainobjectJson = new JSONObject(response);
                        if(mainobjectJson.has("NewDataSet")){
                            JSONObject newsetJsonobject = mainobjectJson.getJSONObject("NewDataSet");
                            if(newsetJsonobject.has("Table1")){
                                JSONObject tableJson=newsetJsonobject.getJSONObject("Table1");

                                TrackId = tableJson.getString("txnId");
                                authIV = tableJson.getString("authIV");
                                authKey = tableJson.getString("authKey");
                                clientCode = tableJson.getString("clientCode");
                                password = tableJson.getString("password");
                                payerAddress = tableJson.getString("payerAddress");
                                payerContact = tableJson.getString("payerContact");
                                payerEmail = tableJson.getString("payerEmail");
                                payerFirstName = tableJson.getString("payerFirstName");
                                payerLastName = tableJson.getString("payerLastName");
                                txnAmt = tableJson.getString("txnAmt");
                                URLfailure = tableJson.getString("URLfailure");
                                URLsuccess = tableJson.getString("URLsuccess");
                                username = tableJson.getString("username");

                                key =authKey;
                                iv=authIV;
                            }

                            if (atom_error.equalsIgnoreCase("1")) {
                                if (utils.EmailId == null) {
                                    AlertsBoxFactory.showAlert("Please Update your EmailId from Update Profile.", MakePaymentSubpaisa.this);
                                }
                                AlertsBoxFactory.showAlert("We are facing some technical problem. Please Try Again !!!", MakePaymentSubpaisa.this);

                            }else{
                                MakePaymentSubpaisa.this.finish();

                                HashMap<String,String> hm_subpaisa =new HashMap<>();

                                hm_subpaisa.put("txnAmt",txnAmt);
                                hm_subpaisa.put("payerFirstName",payerFirstName);
                                hm_subpaisa.put("payerLastName",payerLastName);
                                hm_subpaisa.put("payerContact",payerContact);
                                hm_subpaisa.put("payerEmail",payerEmail);
                                hm_subpaisa.put("payerAddress",payerAddress);
                                hm_subpaisa.put("authKey",authKey);
                                hm_subpaisa.put("authIV",authIV);
                                hm_subpaisa.put("clientCode",clientCode);
                                hm_subpaisa.put("txnId",TrackId);
                               hm_subpaisa.put("URLsuccess","http://150.129.48.28:8005/PaymentGateWay/SabPaisa/PaymentStatusTransMobile.aspx");
                               hm_subpaisa.put("URLfailure","http://150.129.48.28:8005/PaymentGateWay/SabPaisa/PaymentStatusTransMobile.aspx");
                               hm_subpaisa.put("spHitUrl","https://securepay.sabpaisa.in/SabPaisa/sabPaisaInit");
                               hm_subpaisa.put("username",username);
                               hm_subpaisa.put("password",password);

                               //EDW edw = new EDW(MakePaymentSubpaisa.this,hm_subpaisa, MakePaymentSubpaisa.this);
                                EDW.initiatePayment(MakePaymentSubpaisa.this,hm_subpaisa,MakePaymentSubpaisa.this);
                            }
                        }else{
                            AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakePaymentSubpaisa.this);
                        }

                    } catch (Exception e) {
                        Utils.log("Error",":"+e);
                    }

                }else{
                    AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakePaymentSubpaisa.this);
                }
            }else {
                AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakePaymentSubpaisa.this);
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            try{
                getAtomSignatureSoap = new GetAtomSignatureSoap(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.SOAP_URL),getString(R.string.METHOD_GET_SubPaisa_SIGNATURE_LIVE));
                getatomResult = getAtomSignatureSoap.GetAtomSignatureResult(memberid,txtnewamount.getText().toString(),TrackId);
                response = getAtomSignatureSoap.getResponse();

            }catch(Exception e){
                Utils.log("Error",":"+e);
            }
            return null;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }

    @Override
    public void paymentResponse(PaymentResponse paymentResponse) {
        System.out.println("---------->"+paymentResponse.getSabPaisaTxId() +"------>" +paymentResponse.getSpRespCode());
        Log.d("Response from subpaisa","-->"+paymentResponse);
        paymentsObj = new PaymentsObj();
        spRespCode = ( checkNullOrNot(paymentResponse.getSpRespCode()));
        SabPaisaTxId = checkNullOrNot(paymentResponse.getSabPaisaTxId());
        spRespStatus = checkNullOrNot(paymentResponse.getSpRespStatus());
        spPaymentid = checkNullOrNot(paymentResponse.getSabPaisaTxId());
        spAmount = checkNullOrNot(paymentResponse.getAmount());
        orgTxnAmount = checkNullOrNot(paymentResponse.getOrgTxnAmount());
        clientTxnId = checkNullOrNot(paymentResponse.getClientTxnId());

        if(spRespCode.equals("0000")){
            reMsg = "Transaction Successful.";
        }if(spRespCode.equals("0300")){
            reMsg = "Transaction Failed.";
        }if(spRespCode.equals("0200")){
            reMsg = "Transaction Aborted.";
        }if(spRespCode.equals("0100")){
            reMsg = "Transaction Initiated.";
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new InsertAfterPayemnt().executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
            Utils.log("InsertPayment", "Insert Pay executed");
        } else {
            new InsertAfterPayemnt().execute();
            Utils.log("InsertPayment", "Insert Pay executed");
        }

    }

    @Override
    public void onError(String s) {
        System.out.println("---------->"+s);
    }

    private String checkNullOrNot(String string){
        if(TextUtils.isEmpty(string)){
            return "";
        }
        return string;
    }




    public class InsertAfterPayemnt extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {

            mProgressHUD = ProgressHUD
                    .show(MakePaymentSubpaisa.this,
                            getString(R.string.app_please_wait_label), true,
                            true, this);

        }

        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();

            // submit.setClickable(true);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();
            // submit.setClickable(true);
            Utils.log("InsertPayment OnPost", "Insert Pay executed ONPost");

            Utils.log("additional amnt", "" + additionalAmount.getFinalcharges());
            Utils.log("Result", "" + rslt);

            // String rslt = "ok";

            Utils.log("OnPostOf InsertPayment", "InsertPayment");

            Utils.log("Result", "" + rslt);

            if (rslt.trim().equalsIgnoreCase("ok")) {
                // Utils.log("Now in renew",":"+additionalAmount.getAdditionalAmountType());

                if (spRespCode.equals("0000") ) {
                    if (additionalAmount.getFinalcharges() != null) {
                        if (additionalAmount.getAdditionalAmountType() != null) {
                            if (additionalAmount.getAdditionalAmountType()
                                    .length() > 0) {
                                if (additionalAmount.getAdditionalAmountType()
                                        .contains("#")) {
                                    String split[] = additionalAmount
                                            .getAdditionalAmountType().split(
                                                    "#");
                                    if (split.length > 0) {
                                        type = split[1];
                                    }
                                }
                            } else {

                            }
                        }
                    } else {
                        Utils.log("Additional Amount ", "is null");
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    {
                        new RenewalProcessWebService().executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                    } else {
                        new RenewalProcessWebService().execute();
                    }

                } else {

                    MakePaymentSubpaisa.this.finish();
                    Intent intent = new Intent(MakePaymentSubpaisa.this, SubpaisaResponse.class);
                    intent.putExtra("spRespStatus", spRespCode);
                    intent.putExtra("SabPaisaTxId", SabPaisaTxId);
                    intent.putExtra("clientTxnId",clientTxnId);
                    intent.putExtra("spPaymentid",spPaymentid);
                    intent.putExtra("transmsg",reMsg);
                    intent.putExtra("spAmount", spAmount);
                    intent.putExtra("orgTxnAmount", orgTxnAmount);
                    intent.putExtra("Trackid",TrackId);
                   // intent.putExtra("statusMsg",paymentsObj.getTxMsg());
                    startActivity(intent);
                    BaseApplication.getEventBus().post(new FinishEvent(MakeMyPaymentsAtom.class.getSimpleName()));
                    BaseApplication.getEventBus().post(new FinishEvent(RenewPackage.class.getSimpleName()));
                    BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));

                }

          } else {
              AlertsBoxFactory.showAlert(rslt, MakePaymentSubpaisa.this);
             return;
           }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());

                AfterInsertPaymentsCaller caller = new AfterInsertPaymentsCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_AFTER_MEMBER_PAYMENTS),
                        true);

                Utils.log("Payment Authcode", "" + paymentsObj.getAuthIdCode());
                Utils.log("Payment TransactionId", "" + paymentsObj.getTrackId());
                Utils.log("Payment TXId", "" + paymentsObj.getTxId());
                paymentsObj.setAuthIdCode(authIV);
                paymentsObj.setTxId(TrackId);
                paymentsObj.setTxStatus(spRespStatus);
                paymentsObj.setPgTxnNo(spPaymentid);
                paymentsObj.setIssuerRefNo(spPaymentid);
                paymentsObj.setTxMsg(spRespStatus);
                caller.setPaymentdata(paymentsObj);

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

/* AlertsBoxFactory.showAlert(rslt,context ); */

            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }

    private class RenewalProcessWebService extends
            AsyncTask<String, Void, Void> implements OnCancelListener {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        protected void onPreExecute() {

            Utils.log("OnRenewalExecuted", "OnRenewalExe");
            mProgressHUD = ProgressHUD
                    .show(MakePaymentSubpaisa.this,
                            getString(R.string.app_please_wait_label), true,
                            false, this);
        }

        @Override
        protected void onCancelled() {

            mProgressHUD.dismiss();
        }

        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {

                MakePaymentSubpaisa.this.finish();;
                Intent intent = new Intent(MakePaymentSubpaisa.this, SubpaisaResponse.class);
                intent.putExtra("spRespStatus", spRespCode);
                intent.putExtra("SabPaisaTxId", SabPaisaTxId);
                intent.putExtra("clientTxnId",clientTxnId);
                intent.putExtra("spPaymentid",spPaymentid);
                intent.putExtra("transmsg",reMsg);
                intent.putExtra("spAmount", spAmount);
                intent.putExtra("orgTxnAmount", orgTxnAmount);
                intent.putExtra("Trackid",TrackId);
                startActivity(intent);
                BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
                BaseApplication.getEventBus().post(new FinishEvent(MakeMyPaymentsAtom.class.getSimpleName()));
                BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
                BaseApplication.getEventBus().post(new FinishEvent(RenewPackage.class.getSimpleName()));

            } else {
                AlertsBoxFactory.showAlert(rslt, MakePaymentSubpaisa.this);
                return;
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            try {

                RenewalCaller caller = new RenewalCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL),
                        getApplicationContext().getResources().getString(
                                R.string.METHOD_RENEWAL_REACTIVATE_METHOD));

                paymentsObj.setMobileNumber(utils.getMobileNoPrimary());
                paymentsObj.setSubscriberID(utils.getMemberLoginID());
                paymentsObj.setPlanName(txtnewpackagename.getText().toString());
                paymentsObj.setPaidAmount(Double.parseDouble(additionalAmount.getFinalcharges()));
               // paymentsObj.setTrackId(clientTxnId);
                paymentsObj.setTrackId(TrackId);
                paymentsObj.setIsChangePlan(Changepack);
                paymentsObj.setActionType(UpdateFrom);
                paymentsObj.setPaymentId(spPaymentid);
                paymentsObj.setTxMsg(spRespStatus);

                caller.setPaymentdata(paymentsObj);
                caller.setRenewalType(type);

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

            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }





    private class GetMemberDetailWebService extends
            AsyncTask<String, Void, Void> implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakePaymentSubpaisa.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            Utils.log("1 Progress", "start");
        }

        @Override
        protected Void doInBackground(String... params) {
            {
                try {
                    MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
                            getApplicationContext().getResources().getString(
                                    R.string.WSDL_TARGET_NAMESPACE),
                            getApplicationContext().getResources().getString(
                                    R.string.SOAP_URL), getApplicationContext()
                            .getResources().getString(
                                    R.string.METHOD_SUBSCRIBER_DETAILS));

                    memberdetailCaller.memberid = memberid;

                    memberdetailCaller.setAllData(false);
                    memberdetailCaller.setTopup_flag(false);
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
        }


        protected void onPostExecute(Void unused) {
            getMemberDetailWebService = null;
            if (is_activity_running)
                mProgressHUD.dismiss();
            Utils.log("1 Progress", "end");
            try {
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (mapMemberDetails != null) {

                        Set<String> keys = mapMemberDetails.keySet();
                        Utils.log("KEY_SET", ":" + keys.size());

                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                            str_keyVal = (String) i.next();
                        }
                        String selItem = str_keyVal.trim();
                        isLogout = false;
                        // finish();
                        memberDetails = mapMemberDetails.get(selItem);
                        txtloginid.setText(memberDetails.getMemberLoginId());
                        txtemailid.setText(memberDetails.getEmailId());
                        txtcontactno.setText(memberDetails.getMobileNo());
                        customername = memberDetails.getMemberName();
                        Utils.log("customername", ":" + customername);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							getpaymentgatewaysdetails = new PaymentGateWayDetails();
							getpaymentgatewaysdetails.executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
						} else {
							getpaymentgatewaysdetails = new PaymentGateWayDetails();
							getpaymentgatewaysdetails.execute((String) null);
						}
                    }
                } else if (rslt.trim().equalsIgnoreCase("not")) {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert("Subscriber Not Found !!! ", context);
                } else {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert(rslt, context);
                }
            } catch (Exception e) {
                if (is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
            }
        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();
            getMemberDetailWebService = null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }

    private class PaymentGateWayDetails extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakePaymentSubpaisa.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            Utils.log("2 Progress", "start");
            Utils.log("Atom", ":" + Utils.is_atom);


        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();

            getpaymentgatewaysdetails = null;

        }

        protected void onPostExecute(Void unused) {
            Utils.log("2 Progress", "end");

            if (is_activity_running) mProgressHUD.dismiss();
            getpaymentgatewaysdetails = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                try {
                    TrackId = adjTrackval;
                    clientTxnId = adjTrackval;

                   Utils.log("TrackId", ":" + TrackId);
                    if (TrackId.length() > 0) {
                        trackid_check = true;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new InsertBeforePayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                        } else {
                            new InsertBeforePayemnt().execute((String) null);
                        }
                    }

                    // Log.i(">>>>TrackId<<<<", TrackId);
                    Utils.log("trackid_check", ":" + trackid_check);
                    if (is_member_details) {

                    }
                } catch (NumberFormatException nue) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
                    radioGroup.clearCheck();
                    // Log.i(">>>>>New PLan Rate<<<<<<", adjTrackval);
                }

            } else {

            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                PaymentGatewayCaller adjCaller = new PaymentGatewayCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_GET_TRANSACTIONID_WITH_BANK_NAME), "SB");
                adjCaller.setMemberId(utils.getMemberId());
                // adjCaller.setAreaCode(AreaCode);
                // adjCaller.setAreaCodeFilter(AreaCodeFilter);
                adjCaller.setTopup_falg(false);

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
                /*
                 * AlertsBoxFactory.showErrorAlert("Error web-service response "
                 * + e.toString(), context);
                 */
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }

    private class InsertBeforePayemnt extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        protected void onPreExecute() {
            Utils.log("3 Progress", "start");
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakePaymentSubpaisa.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);

        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();
            InsertBeforePayemnt = null;
            // submit.setClickable(true);
        }

        protected void onPostExecute(Void unused) {
            if (is_activity_running)
                mProgressHUD.dismiss();
            Utils.log("3 Progress", "end");
            Utils.log("Response", ":" + rslt);
            // submit.setClickable(true);
            InsertBeforePayemnt = null;
            if (rslt.trim().equalsIgnoreCase("null") || rslt.equals(null)) {
                AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", MakePaymentSubpaisa.this);

            } else
            if (rslt.trim().equalsIgnoreCase("ok")) {
                 {

                     if(paymentsObj.getMemberId() != 0) {
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                             new Get_SubPaisa_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                         } else {
                             new Get_SubPaisa_Signature().execute();
                         }
                     }else {

                         AlertsBoxFactory.showAlert("Error occured.",context );

                     }
                }

            } else {
                if (is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
                return;
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());

                BeforePaymentInsertCaller caller = new BeforePaymentInsertCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(R.string.METHOD_BEFORE_MEMBER_PAYMENTS_NEW), true);

                paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
                paymentsObj.setTrackId(TrackId);
                paymentsObj.setAmount(txtnewamount.getText().toString().trim());
                paymentsObj.setPackageName(txtnewpackagename.getText().toString());
                paymentsObj.setServiceTax(ServiceTax);
                paymentsObj.setDiscount_Amount(additionalAmount.getDiscountAmount());
                if (Utils.pg_sms_request) {
                    if (Utils.pg_sms_uniqueid.length() > 0) {
                        paymentsObj.setPg_sms_unique_id(Utils.pg_sms_uniqueid);
                    } else {
                        paymentsObj.setPg_sms_unique_id(null);
                    }
                } else {
                    paymentsObj.setPg_sms_unique_id(null);
                }
                caller.setPaymentdata(paymentsObj);

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
                /* AlertsBoxFactory.showAlert(rslt,context ); */
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }
    }

    private class GetRedirectionDetails extends AsyncTask<String, Void, Void>
            implements OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            Utils.log("3 Progress", "start");
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(MakePaymentSubpaisa.this,
                                getString(R.string.app_please_wait_label), true,
                                true, this);

        }

        @Override
        protected void onCancelled() {
            if (is_activity_running)
                mProgressHUD.dismiss();
        }

        protected void onPostExecute(Void unused) {
            if (is_activity_running)
                mProgressHUD.dismiss();
            Utils.log("3 Progress", "end");
            Utils.log("Response", ":" + rslt);


            try {
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (MapRedirectionDetails != null) {

                        Set<String> keys = MapRedirectionDetails.keySet();
                        Utils.log("KEY_SET", ":" + keys.size());

                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                            str_keyVal = (String) i.next();
                        }
                        String selItem = str_keyVal.trim();
                        isLogout = false;
                        // finish();
                        detailObj = MapRedirectionDetails.get(selItem);


                        if(detailObj.getTremsAndConditions().equals("") || detailObj.getTremsAndConditions().equals("null") ){
                            AlertsBoxFactory.showAlert("Please contact customer care for Terms and Conditions .",context );
                        }else {

                            String tandc = "I have read and accepted the " +
                                    String.format("<font color=\"#0000FF\"><a href=\"%s\">Terms and Conditions.</a></font> ", detailObj.getTremsAndConditions()) +
                                    " We as a merchant shall be under no liability whatsoever in respect of any loss or damage arising directly or indirectly out of the decline of authorization for any Transaction, on Account of the Cardholder having exceeded the preset limit mutually agreed by us with our acquiring bank from time to time. ";

                            TextView14.setAutoLinkMask(0);
                            TextView14.setText(Html.fromHtml(tandc));
                            TextView14.setMovementMethod(LinkMovementMethod.getInstance());
                        }

                        if((!detailObj.getPrivacyPolicy().equals("")) || (!detailObj.getPrivacyPolicy().equals("null")) ){
                            if(!detailObj.getCancellationAndRefundPolicy().equals("")  ||  (!detailObj.getCancellationAndRefundPolicy().equals("null"))) {
                                String pvandc = "I have read and accepted the " +
                                        String.format("<font color=\"#0000FF\"><a href=\"%s\" >Privacy Policy </a></font> ",  detailObj.getPrivacyPolicy()) +
                                        String.format("<font color=\"#0000FF\"><a href=\"%s\">Cancellation and Refund Policy </a></font> ", detailObj.getCancellationAndRefundPolicy());

                                TextView15.setAutoLinkMask(0);
                                TextView15.setText(Html.fromHtml(pvandc));
                                TextView15.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                            else{
                                AlertsBoxFactory.showAlert("Please contact customer care for Cancellation and Refund Policy.",context );
                            }
                        }else {
                            AlertsBoxFactory.showAlert("Please contact customer care for Privacy Policy.",context );

                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());

//                GetRedirectionDetailsCaller caller = new GetRedirectionDetailsCaller(
//                        getApplicationContext().getResources().getString(
//                                R.string.WSDL_TARGET_NAMESPACE),
//                        getApplicationContext().getResources().getString(R.string.SOAP_URL), getApplicationContext()
//                        .getResources().getString(R.string.METHOD_GETREDIRECTIONDETAILS));
//
//                caller.join();
//                caller.start();
//                rslt = "START";
//
//                while (rslt == "START") {
//                    try {
//                        Thread.sleep(10);
//                    } catch (Exception ex) {
//                    }
//                }


                GetRedirectionDetailsCaller caller = new GetRedirectionDetailsCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(R.string.METHOD_GETREDIRECTIONDETAILS));

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
                /* AlertsBoxFactory.showAlert(rslt,context ); */
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            if (is_activity_running)
                mProgressHUD.dismiss();
        }


    }

}
