package com.cnergee.fragments;

import android.annotation.SuppressLint;
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

import all.interface_.IError;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.MakeMyPayment_Atom;
import com.cnergee.mypage.SOAP.GetAtomSignatureSoap;
import com.cnergee.mypage.WebView_AtomPayments;
import com.cnergee.mypage.caller.BeforePaymentInsertCaller;
import com.cnergee.mypage.caller.GetRedirectionDetailsCaller;
import com.cnergee.mypage.caller.InsertBeforeWithTrackCaller;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.caller.PaymentGatewayCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.obj.RedirectionDetailObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.BaseActivity.iError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeMyPayment_AtomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeMyPayment_AtomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    TextView txtloginid, txtemailid, txtcontactno, txtnewpackagename, txtnewamount, txtnewvalidity, tvDiscountLabel, TextView14, TextView15;
    LinearLayout ll_addtional_details, llClickDetails;
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

    private ScrollView payNowView, responseScrollLayout;
    String TrackId;
    private InsertBeforeWithTrackId insertBeforeWithTrackId =null;

    public static String adjTrackval;
    public static String responseMsg = "";
    public static String rslt = "";
    public static Map<String, MemberDetailsObj> mapMemberDetails;
    public static Map<String, RedirectionDetailObj> MapRedirectionDetails;
    private String customername, Email_id;
    RedirectionDetailObj detailObj;
    AdditionalAmount additionalAmount;
    MemberDetailsObj memberDetails;

        private PaymentGateWayDetails getpaymentgatewaysdetails = null;
    private InsertBeforePayemnt InsertBeforePayemnt = null;
    private GetMemberDetailWebService getMemberDetailWebService = null;
    private GetRedirectionDetails getRedirectionDetails = null;
    Utils utils = new Utils();

    private static int ACC_ID = 0000;
    private static String SECRET_KEY = "";
    private static String HOST_NAME = "";
    //private static final double PER_UNIT_PRICE = 12.34;
    ArrayList<HashMap<String, String>> custom_post_parameters;
    String atom_url = "", atom_acces_code = "", atom_error = "", atom_track_id = "";
    Context context;


    public MakeMyPayment_AtomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeMyPayment_AtomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeMyPayment_AtomFragment newInstance(String param1, String param2) {
        MakeMyPayment_AtomFragment fragment = new MakeMyPayment_AtomFragment();
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

            ServiceTax = getArguments().getString("ServiceTax");
            UpdateFrom = getArguments().getString("updateFrom");
            discount = getArguments().getString("discount");
//            ClassName = getArguments().getString("ClassName");
            additionalAmount = (AdditionalAmount) getArguments().getSerializable("addtional_amount");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_make_my_payment_atom, container, false);
        context = getActivity();

        iError = (IError) context;
        initControls(view);

        return view;
    }

    public void initControls(View view) {

        llClickDetails = (LinearLayout) view.findViewById(R.id.llClickDetails);
        ll_addtional_details = (LinearLayout) view.findViewById(R.id.ll_addtional_details);

        txtloginid = (TextView) view.findViewById(R.id.txtloginid);
        txtemailid = (TextView) view.findViewById(R.id.txtemailid);
        txtcontactno = (TextView) view.findViewById(R.id.txtcontactno);
        txtnewpackagename = (TextView) view.findViewById(R.id.txtnewpackagename);
        txtnewamount = (TextView) view.findViewById(R.id.txtnewamount);
        txtnewvalidity = (TextView) view.findViewById(R.id.txtnewvalidity);

        tvDiscountLabel = (TextView) view.findViewById(R.id.tvDiscountLabel);

        privacy = (CheckBox) view.findViewById(R.id.privacy);
        terms = (CheckBox) view.findViewById(R.id.terms);
        TextView14 = (TextView) view.findViewById(R.id.TextView14);
        TextView15 = (TextView) view.findViewById(R.id.TextView15);
        btnnb = (Button) view.findViewById(R.id.btnnb);

        payNowView = (ScrollView) view.findViewById(R.id.payNowLayout);
        responseScrollLayout = (ScrollView) view.findViewById(R.id.responseScrollLayout);
        terms.setChecked(true);
        privacy.setChecked(true);

//        Intent intent = getIntent();
//        bundle = intent.getExtras();

        txtnewpackagename.setText(getArguments().getString("PackageName"));
        txtnewvalidity.setText("" + getArguments().getInt("PackageValidity"));

        if (Utils.isOnline(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

            } else {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.execute((String) null);

                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);
            }
        }


        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // checked
                    terms.setChecked(true);
                    Log.d("terms", "" + b);

                } else {
                    // not checked
                    Log.d("terms", "" + b);
                    AlertsBoxFactory
                            .showAlert(
                                    "Please accept Terms and Conditions.",
                                    context);
                }
            }
        });

        privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // checked
                    privacy.setChecked(true);
                    Log.d("privacy", "" + b);

                } else {
                    // not checked
                    Log.d("privacy", "" + b);
                    AlertsBoxFactory
                            .showAlert(
                                    "Please accept Privacy Policy.",
                                    context);
                }
            }
        });
        btnnb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    if (Double.parseDouble(txtnewamount.getText().toString()) > 0) {
                        if ((terms.isChecked() == true)
                                && (privacy.isChecked() == true)) {

                            if (Utils.isOnline(context)) {
                                if (trackid_check) {
                                    is_member_details = false;
                                    // TrackId Generated Successfully.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        new InsertBeforePayemnt().executeOnExecutor(
                                                AsyncTask.THREAD_POOL_EXECUTOR,
                                                (String) null);
                                    } else {
                                        new InsertBeforePayemnt().execute((String) null);
                                    }
                                } else {
                                    // TrackId Failed to Generate.
                                    is_member_details = true;
                                    if (Utils.isOnline(context)) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            if (!utils.getMemberId().equals(0)) {
                                                getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                                getpaymentgatewaysdetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                                            }
                                        } else {
                                            if (!utils.getMemberId().equals(0)) {
                                                getpaymentgatewaysdetails = new PaymentGateWayDetails();
                                                getpaymentgatewaysdetails.execute((String) null);
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context,
                                        getString(R.string.app_please_wait_label),
                                        Toast.LENGTH_LONG).show();


                            }


                        } else {
                            AlertsBoxFactory
                                    .showAlert(
                                            "Please accept Terms and Conditions, Privacy Policy.",
                                            context);

                        }

                    } else {
                        if (is_activity_running)
                            AlertsBoxFactory
                                    .showAlert(
                                            "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                            context);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    if (is_activity_running)
                        AlertsBoxFactory
                                .showAlert(
                                        "Due to some data mismatch we are unable to process your request\n Please contact admin",
                                        context);

                }
            }
        });


        if (getArguments().getString("datafrom").equalsIgnoreCase("changepack")) {
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
//                    showPaymentDetails(additionalAmount);

                } else {
                    ll_addtional_details.setVisibility(View.GONE);
                }
            }
        });

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        utils.setSharedPreferences(sharedPreferences);
        memberid = Long.parseLong(utils.getMemberId());
        isRenew = sharedPreferences.getString(Utils.IS_RENEWAL, "0");


        if (Utils.isOnline(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

            } else {
                getRedirectionDetails = new GetRedirectionDetails();
                getRedirectionDetails.execute((String) null);

                getMemberDetailWebService = new GetMemberDetailWebService();
                getMemberDetailWebService.execute((String) null);
            }


            payNowView.setVisibility(View.VISIBLE);
            responseScrollLayout.setVisibility(View.GONE);
        } else {
            if (is_activity_running)
                AlertsBoxFactory.showAlert("Please connect to internet !!", context);
        }

    }

    private class InsertBeforeWithTrackId extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        @Override
        protected void onPreExecute() {
            if (is_activity_running) {
                mProgressHUD = ProgressHUD
                        .show(context,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (is_activity_running)
                mProgressHUD.dismiss();
            insertBeforeWithTrackId = null;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());
                Log.e("Strings", ":");
                Log.e("Strings", ":" + strings.toString());
                InsertBeforeWithTrackCaller caller = new InsertBeforeWithTrackCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_BEFORE_MEMBER_PAY_WITH_TRACKID), true);

                paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
                paymentsObj.setTrackId(TrackId);
                paymentsObj.setAmount(txtnewamount.getText().toString().trim());
                paymentsObj.setPackageName(txtnewpackagename.getText().toString());
                paymentsObj.setServiceTax(ServiceTax);
                paymentsObj.setDiscount_Amount(additionalAmount.getDiscountAmount());
                paymentsObj.setBankcode("AT");
                paymentsObj.setRenewaltype(UpdateFrom);


                if (Utils.pg_sms_request) {
                    if (Utils.pg_sms_uniqueid.length() > 0) {
                        paymentsObj.setPg_sms_unique_id(Utils.pg_sms_uniqueid);
                    } else {
                        paymentsObj.setPg_sms_unique_id(null);
                    }
                } else {
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
            InsertBeforePayemnt = null;

            if (is_activity_running)
                mProgressHUD.dismiss();
            insertBeforeWithTrackId = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {

                Log.e("RESPONSE TRACKID", ":" + MakeMyPayment_AtomFragment.responseMsg);
                TrackId = adjTrackval;

                if (TrackId != null && TrackId.length() > 0 && !TrackId.equalsIgnoreCase("null")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new Get_Atom_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                    } else {
                        new Get_Atom_Signature().execute();
                    }

                } else {
                    AlertsBoxFactory.showAlert("TrackId not generated. Please try Again !!!", context);
                }

            } else {
                if (is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
                return;
            }
        }
    }

    public void showPaymentDetailsDialog(AdditionalAmount additionalAmount) {
        if (additionalAmount != null) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_additional_amount);

            int width = 0;
            int height = 0;

            Point size = new Point();

            WindowManager w = ((Activity) context)
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

//    public void showPaymentDetails(AdditionalAmount additionalAmount) {
//        if (additionalAmount != null) {
//            // ll_addtional_details.setVisibility(View.VISIBLE);
//            LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;
//
//            TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;
//
//            ll_package_rate = (LinearLayout)view.findViewById(R.id.ll_package_rate);
//            ll_add_amt = (LinearLayout)view.findViewById(R.id.ll_add_amt);
//            ll_add_reason = (LinearLayout)view.findViewById(R.id.ll_add_reason);
//            ll_discount_amt = (LinearLayout)view.findViewById(R.id.ll_discount_amt);
//            ll_fine_amount = (LinearLayout)view.findViewById(R.id.ll_fine_amt);
//            ll_days_fine_amt = (LinearLayout)view.findViewById(R.id.ll_days_fine_amt);
//            ll_discount_per = (LinearLayout)view.findViewById(R.id.ll_discount_per);
//            ll_final_amt = (LinearLayout)view.findViewById(R.id.ll_final_amount);
//
//            tv_package_rate = (TextView)view.findViewById(R.id.tv_package_rate);
//            tv_add_amt = (TextView)view.findViewById(R.id.tv_add_amt);
//            tv_add_reason = (TextView)view.findViewById(R.id.tv_add_reason);
//            tv_discount_amt = (TextView)view.findViewById(R.id.tv_discount_amt);
//            tv_fine_amount = (TextView)view.findViewById(R.id.tv_fine_amt);
//            tv_days_fine_amt = (TextView)view.findViewById(R.id.tv_days_fine_amt);
//            tv_discount_per = (TextView)view.findViewById(R.id.tv_discount_per);
//            tv_final_amt = (TextView)view.findViewById(R.id.tv_final_amount);
//
//            if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
//                ll_package_rate.setVisibility(View.VISIBLE);
//                tv_package_rate.setText(additionalAmount.getPackageRate());
//            } else {
//
//                ll_package_rate.setVisibility(View.GONE);
//
//            }
//
//            if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
//                ll_add_amt.setVisibility(View.VISIBLE);
//                tv_add_amt.setText(additionalAmount.getAdditionalAmount());
//            } else {
//                ll_add_amt.setVisibility(View.GONE);
//            }
//
//            if (additionalAmount.getAdditionalAmountType().length() > 0) {
//                ll_add_reason.setVisibility(View.GONE);
//                tv_add_reason.setText(additionalAmount
//                        .getAdditionalAmountType());
//            } else {
//                ll_add_reason.setVisibility(View.GONE);
//            }
//
//            if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
//                ll_discount_amt.setVisibility(View.VISIBLE);
//                tv_discount_amt.setText(additionalAmount.getDiscountAmount());
//            } else {
//                ll_discount_amt.setVisibility(View.GONE);
//            }
//
//            if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
//                ll_fine_amount.setVisibility(View.VISIBLE);
//                tv_fine_amount.setText(additionalAmount.getFineAmount());
//            } else {
//                ll_fine_amount.setVisibility(View.GONE);
//            }
//
//            if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
//                ll_days_fine_amt.setVisibility(View.VISIBLE);
//                tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
//            } else {
//                ll_days_fine_amt.setVisibility(View.GONE);
//            }
//
//            if (additionalAmount.getDiscountPercentage().length() > 0) {
//                if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
//                    ll_discount_per.setVisibility(View.VISIBLE);
//                    tv_discount_per.setText(additionalAmount.getDiscountPercentage());
//                } else {
//                    ll_discount_per.setVisibility(View.GONE);
//                }
//            } else {
//                ll_discount_per.setVisibility(View.GONE);
//            }
//
//            if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
//                ll_final_amt.setVisibility(View.VISIBLE);
//                tv_final_amt.setText(additionalAmount.getFinalcharges());
//            } else {
//                ll_final_amt.setVisibility(View.GONE);
//            }
//        } else {
//            ll_addtional_details.setVisibility(View.GONE);
//        }
//    }

    private class GetMemberDetailWebService extends
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(context,
                                getString(R.string.app_please_wait_label), true,
                                true, this);
            Utils.log("1 Progress", "start");
        }

        @Override
        protected Void doInBackground(String... params) {
            {
                try {
                    MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
                            context.getResources().getString(
                                    R.string.WSDL_TARGET_NAMESPACE),
                            context.getResources().getString(
                                    R.string.SOAP_URL), context
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
            implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(context,
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
            if (rslt.trim().equalsIgnoreCase("null") || rslt.equals(null)) {
                AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", context);

            } else {
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    try {
                        TrackId = adjTrackval;
                        trackid_check = true;
                        Utils.log("TrackId", ":" + TrackId);
                        if (TrackId.equalsIgnoreCase("null") || TrackId.equals(null)) {
                            AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", context);

                        } else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new InsertBeforePayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                            } else {
                                new InsertBeforePayemnt().execute((String) null);
                            }


                            Utils.log("trackid_check", ":" + trackid_check);
                            if (is_member_details) {

                            }
                        }
                    } catch (NumberFormatException nue) {
                        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioPayMode);
                        radioGroup.clearCheck();
                        // Log.i(">>>>>New PLan Rate<<<<<<", adjTrackval);
                    }

                } else {
                    if (is_activity_running)
                        iError.display();
                }
            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                PaymentGatewayCaller adjCaller = new PaymentGatewayCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GET_TRANSACTIONID_WITH_BANK_NAME), "AT");
                adjCaller.setMemberId(utils.getMemberId());
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
            implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        PaymentsObj paymentsObj = new PaymentsObj();

        protected void onPreExecute() {
            Utils.log("3 Progress", "start");
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(context,
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
                AlertsBoxFactory.showAlert("Something went wrong. Please try Again !!!", context);

            } else {

                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (memberid > 0) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                            new Get_Atom_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

                        } else {

                            new Get_Atom_Signature().execute();

                        }
                    } else {

                        AlertsBoxFactory.showAlert("Error occured.", context);

                    }
                } else {
                    if (is_activity_running)
                        AlertsBoxFactory.showAlert(rslt, context);
                    return;
                }

            }

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // setCurrDateTime();
                // Log.i(" >>>>> ",getCurrDateTime());

                BeforePaymentInsertCaller caller = new BeforePaymentInsertCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(R.string.SOAP_URL), context
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


    private class Get_Atom_Signature extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        GetAtomSignatureSoap getAtomSignatureSoap;
        String getatomResult = "";
        String response = "";
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD
                    .show(context,
                            getString(R.string.app_please_wait_label), true,
                            true, this);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mProgressHUD != null)
                mProgressHUD.dismiss();
            Utils.log("OnPostAtom", "OnPostAtom");
            if (getatomResult != null && getatomResult.length() > 0) {
                if (response != null && response.length() > 0) {

                    try {
                        JSONObject mainobjectJson = new JSONObject(response);
                        if (mainobjectJson.has("NewDataSet")) {
                            JSONObject newsetJsonobject = mainobjectJson.getJSONObject("NewDataSet");
                            if (newsetJsonobject.has("Table1")) {
                                JSONObject tableJson = newsetJsonobject.getJSONObject("Table1");

                                TrackId = tableJson.getString("TrackId");
                                atom_acces_code = tableJson.getString("AccessCode");
                                atom_url = tableJson.getString("EncRequest");
                                atom_error = tableJson.getString("IsError");
                                atom_track_id = tableJson.getString("TrackId");

                                Utils.log("Atom URL", "" + atom_url);
                                Utils.log("Atom AccessCode", "" + atom_acces_code);
                                Utils.log("Atom error", "" + atom_error);
                                Utils.log("Atom TrackId", "" + atom_track_id);

                            }

                            if (atom_error.equalsIgnoreCase("1")) {
                                if (utils.EmailId == null) {
                                    AlertsBoxFactory.showAlert("Please Update your EmailId from Update Profile.", context);
                                }
                                AlertsBoxFactory.showAlert("We are facing some technical problem. Please Try Again !!!", context);

                            } else {
                                Intent i = new Intent(context, WebView_AtomPayments.class);
                                i.putExtra("returnUrl", atom_url);
                                i.putExtra("trackId", atom_track_id);
                                i.putExtra("Error", atom_error);
                                i.putExtra("AccessCode", atom_acces_code);
                                i.putExtra(com.avenues.lib.utility.AvenuesParams.ACCESS_CODE, atom_acces_code);
                                i.putExtra(com.avenues.lib.utility.AvenuesParams.AMOUNT, txtnewamount.getText().toString());
                                i.putExtra("TrackId", TrackId);
                                i.putExtra("additional_amount", additionalAmount);
                                i.putExtra("Changepack", Changepack);
                                i.putExtra("UpdateFrom", UpdateFrom);
                                i.putExtra("PackageName", txtnewpackagename.getText().toString());
                                startActivity(i);
                            }
                        } else {
                            AlertsBoxFactory.showAlert("We are unable to initiate Payment.", context);
                        }

                    } catch (Exception e) {
                        Utils.log("Error", ":" + e);
                    }

                } else {
                    AlertsBoxFactory.showAlert("We are unable to initiate Payment.", context);
                }
            } else {
                AlertsBoxFactory.showAlert("We are unable to initiate Payment.", context);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Utils.log("Amount", "" + txtnewamount.getText().toString());
                getAtomSignatureSoap = new GetAtomSignatureSoap(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ATOM_SIGNATURE_LIVE));
                getatomResult = getAtomSignatureSoap.GetAtomSignatureResult(memberid, txtnewamount.getText().toString(), TrackId);
                response = getAtomSignatureSoap.getResponse();
                Utils.log("Amount", "" + txtnewamount.getText().toString());
            } catch (Exception e) {
                Utils.log("Error", ":" + e);
            }
            return null;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }

    private class GetRedirectionDetails extends AsyncTask<String, Void, Void>
            implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            Utils.log("3 Progress", "start");
            if (is_activity_running)
                mProgressHUD = ProgressHUD
                        .show(context,
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


                        if (detailObj.getTremsAndConditions().equals("") || detailObj.getTremsAndConditions().equals("null")) {
                            AlertsBoxFactory.showAlert("Please contact customer care for Terms and Conditions .", context);
                        } else {

                            String tandc = "I have read and accepted the " +
                                    String.format("<font color=\"#0000FF\"><a href=\"%s\">Terms and Conditions.</a></font> ", detailObj.getTremsAndConditions()) +
                                    " We as a merchant shall be under no liability whatsoever in respect of any loss or damage arising directly or indirectly out of the decline of authorization for any Transaction, on Account of the Cardholder having exceeded the preset limit mutually agreed by us with our acquiring bank from time to time. ";

                            TextView14.setAutoLinkMask(0);
                            TextView14.setText(Html.fromHtml(tandc));
                            TextView14.setMovementMethod(LinkMovementMethod.getInstance());
                        }

                        if ((!detailObj.getPrivacyPolicy().equals("")) || (!detailObj.getPrivacyPolicy().equals("null"))) {
                            if (!detailObj.getCancellationAndRefundPolicy().equals("") || (!detailObj.getCancellationAndRefundPolicy().equals("null"))) {
                                String pvandc = "I have read and accepted the " +
                                        String.format("<font color=\"#0000FF\"><a href=\"%s\" >Privacy Policy </a></font> ", detailObj.getPrivacyPolicy()) +
                                        String.format("<font color=\"#0000FF\"><a href=\"%s\">Cancellation and Refund Policy </a></font> ", detailObj.getCancellationAndRefundPolicy());

                                TextView15.setAutoLinkMask(0);
                                TextView15.setText(Html.fromHtml(pvandc));
                                TextView15.setMovementMethod(LinkMovementMethod.getInstance());
                            } else {
                                AlertsBoxFactory.showAlert("Please contact customer care for Cancellation and Refund Policy.", context);
                            }
                        } else {
                            AlertsBoxFactory.showAlert("Please contact customer care for Privacy Policy.", context);

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
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(R.string.SOAP_URL), context
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




