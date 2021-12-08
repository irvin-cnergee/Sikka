package com.cnergee.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import all.interface_.IError;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.BaseActivity;
import com.cnergee.mypage.PaymentHistory;
import com.cnergee.mypage.adapter.PaymentHistoryAdapter;
import com.cnergee.mypage.caller.PaymentDetailsCaller;
import com.cnergee.mypage.caller.PaymentHistoryCaller;
import com.cnergee.mypage.obj.PaymentHistoryObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.BaseActivity.iError;
import static com.cnergee.mypage.BaseActivity.sharedPreferences1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    TextView dtv;
    Utils utils;
    //ImageView btnhomeimg;
    public static Context context;
    public static String rslt = "";
    private boolean flag=true;
    public int ListSize = 0;
    public static ArrayList<PaymentHistoryObj> paymentlist = new ArrayList<PaymentHistoryObj>();
    private boolean isFinish = false;
    private String sharedPreferences_name,sharedPreferences_payment_history;
    private Map paymenthistorymap;
    private ListView paymentListView;
    public String PaymentHistory = null;
    private PaymentListWebService getpaymentlistwebservice = null;
    private GetPaymentDetailsWebService getPaymentDetailsWebService = null;
    private String PaymentDate="";
    public static String paymentDetails = "";
    private boolean isActivityrunning=false;
    TextView tv_marquee;
    private String sharedPreferences_renewal,notificationMsg;

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentHistoryFragment newInstance(String param1, String param2) {
        PaymentHistoryFragment fragment = new PaymentHistoryFragment();
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

        view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        utils = new Utils();
        context = getActivity();
        iError=(IError)context;
        isActivityrunning=true;
        initControls(view);

        return view;
    }

    private void initControls(View view) {

        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        sharedPreferences1 = context.getSharedPreferences(
                getString(R.string.shared_preferences_renewal), 0);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        notificationMsg = sharedPreferences2.getString("NotificationMsg","");
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);
        paymentListView = (ListView)view.findViewById(R.id.paymentListView);

        tv_marquee.setSelected(true);
        tv_marquee.setText(notificationMsg);

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);

        SharedPreferences sharedPreferences1 = context
                .getSharedPreferences(getString(R.string.shared_preferences_payment_history), 0);

        if(sharedPreferences1.getBoolean("payment_history", true))
        {
            Utils.log("Data From server PaymentHistory ","yes"+sharedPreferences1.getBoolean("payment_history", true));
            if(Utils.isOnline(context)){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    getpaymentlistwebservice = new PaymentListWebService();
                    getpaymentlistwebservice .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
                }
                else{
                    getpaymentlistwebservice = new PaymentListWebService();
                    getpaymentlistwebservice.execute((String) null);
                }
            }
            else{
                Toast.makeText(context, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Utils.log("Data From server PaymentHistory","offline"+sharedPreferences1.getBoolean("payment_history", true));

            String versionName="";
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(Double.valueOf(versionName)<1.9){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    getpaymentlistwebservice = new PaymentListWebService();
                    getpaymentlistwebservice .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
                }
                else{
                    getpaymentlistwebservice = new PaymentListWebService();
                    getpaymentlistwebservice.execute((String) null);
                }
            }
            else{
                setOfflinePaymentHistory();
            }


        }

        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                if(Utils.isOnline(context)){
                    PaymentHistoryObj  selectedFromList = (PaymentHistoryObj) paymentListView.getItemAtPosition(position);

                    if( selectedFromList.getPaymentDate() != null){
                        String Paydate = selectedFromList.getPaymentDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd-MM-yyyy");
                        Date myDate = null;
                        try {
                            myDate = dateFormat.parse(Paydate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat timeFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                        PaymentDate = timeFormat.format(myDate);

                        //System.out.println(PaymentDate);

                        getPaymentDetailsWebService = new GetPaymentDetailsWebService();
                        getPaymentDetailsWebService.execute((String) null);

                    }
                }
                else{
                    Toast.makeText(context,
                            "Please connect to internet and try again!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    protected class PaymentListWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener
    {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if(isActivityrunning)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
            Utils.log("Line193 PaymentHistory","OnPreExceAyncTask");
        }
        protected void onPostExecute(Void unused) {
            if(isActivityrunning)
                mProgressHUD.dismiss();
            if (rslt.trim().equalsIgnoreCase("ok")) {


                Utils.log("Line 201 PaymentHistory", "OnPostExecuted");
                //Log.i("",""+complaintList.size());
                ListSize = paymentlist.size();
                PaymentHistoryObj payment_history[] = new PaymentHistoryObj[paymentlist.size()];
                //PaymentHistoryObj complaint_id[] = new PaymentHistoryObj[paymentlist.size()];
                Iterator iter = paymentlist.iterator();
                int i = 0;
                paymenthistorymap = new HashMap();

                sharedPreferences_payment_history = getString(R.string.shared_preferences_payment_history);
                SharedPreferences sharedPreferences1 = context
                        .getSharedPreferences(sharedPreferences_payment_history, 0); // 0 - for private mode

                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("ListSize",paymentlist.size() );
                int j=1;
                //editor.commit();
                if(paymentlist.size()>0){
                    for(int k=0;k<paymentlist.size();k++){
                        PaymentHistoryObj obj1=paymentlist.get(k);
                        //Utils.log("PaymentDate andamount",obj1.getPaymentDate()+" and "+obj1.getAmount());
                        editor.putString("PaymentDate"+j,obj1.getPaymentDate());
                        editor.putString("Amount"+j,obj1.getAmount());
                        j++;
                    }
                }
                else{
                    PaymentHistoryObj obj= new PaymentHistoryObj();
                    obj.setPaymentDate("No Payments")	;
                    obj.setAmount("-");
                    //payment_history[0] = obj;
                }
                while(iter.hasNext()){
                    PaymentHistoryObj obj = (PaymentHistoryObj)iter.next();
                    String id = obj.getPaymentDate();
                    String Amount = obj.getAmount();




                    obj.setPayment(id);
                    paymenthistorymap.put(id,obj);
                    payment_history[i] = obj;
                    //editor.putString("PaymentDate"+j,obj.getPaymentDate());
                    //editor.putString("Amount"+j,obj.getAmount());
                    //payment_history[i] = new PaymentHistoryObj(id);
                    //complaint_id[i] = new ComplaintListObj(Complain);
                    i++;

                }
                editor.putBoolean("payment_history",false );
                editor.commit();
                //System.out.println(payment_history);
                PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(context,R.layout.paymentlistview_row, payment_history);
                paymentListView.setAdapter(adapter);
            } else {
                if(isActivityrunning){
                    //AlertsBoxFactory.showAlert(rslt,context );
                    if (rslt.trim().equalsIgnoreCase("error")) {
                        iError.display();
                    }
                }
                return;
            }
        }
        @Override
        protected Void doInBackground(String... params) {

            try{
                Utils.log("Line 201 PaymentHistory", "On Do inBcknd");
                PaymentHistoryCaller caller = new PaymentHistoryCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_PAYMENTHISTORY));
                caller.setMemberid(Long.parseLong(utils.getMemberId()));

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
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            if(isActivityrunning)
                mProgressHUD.dismiss();
            getpaymentlistwebservice = null;
        }
        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }
    }

    private class GetPaymentDetailsWebService extends
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener
    {

        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            if(isActivityrunning)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        }

        protected void onPostExecute(Void unused) {
            getPaymentDetailsWebService = null;
            //collectBtn.setClickable(true);
            if(isActivityrunning)
                mProgressHUD.dismiss();

            try{
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    //	AlertsBoxFactory.showAlert("PAYMENT DETAILS",paymentDetails,context );
                    if(isActivityrunning){
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



                        dtv = (TextView) dialog.findViewById(R.id.tv1);

                        TextView txt = (TextView) dialog.findViewById(R.id.tv);

                        txt.setText(Html.fromHtml(paymentDetails));

                        Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        //(width/2)+((width/2)/2)
                        //dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }else if (rslt.trim().equalsIgnoreCase("not")) {
                        if(isActivityrunning){
                            mProgressHUD.dismiss();

                            AlertsBoxFactory.showAlert("NO DATA FOUND !!! ",context );
                        }
                    }
                }
            }catch(Exception e){
                Utils.log("Error","is"+e);
                if(isActivityrunning){
                    mProgressHUD.dismiss();

                    AlertsBoxFactory.showAlert(rslt,context );
                }}

        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {

                PaymentDetailsCaller pdCaller = new PaymentDetailsCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GET_MEMBER_PAYMENTS_DETAILS));
                pdCaller.setPaymentDate(PaymentDate);
                pdCaller.setMemberid(Long.parseLong(utils.getMemberId()));
                pdCaller.join();
                pdCaller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            if(isActivityrunning)
                mProgressHUD.dismiss();
			/*collectBtn.setClickable(true);
			getTodaysCollectionWebService = null;*/
        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub

        }

    }

    public void setOfflinePaymentHistory(){
        sharedPreferences_payment_history=getString(R.string.shared_preferences_payment_history);
        SharedPreferences shared_preferencess= context.getSharedPreferences(sharedPreferences_payment_history, 0);
        int i=shared_preferencess.getInt("ListSize", 0);
        //Utils.log("ListSize",""+i);
        PaymentHistoryObj payment_history1[] = new PaymentHistoryObj[i];
        //Utils.log("ListSize",shared_preferencess.getString("PaymentDate"+1, "-"));
        if(i>0){
            for(int j=1;j<=i;j++){
                //Utils.log("ListSize in for",""+j);
                PaymentHistoryObj obj= new PaymentHistoryObj();
                obj.setPaymentDate(shared_preferencess.getString("PaymentDate"+j, "-"))	;
                obj.setAmount(shared_preferencess.getString("Amount"+j, "-"));
                int k=j-1;
                payment_history1[k] = obj;
            }
        }else
        {
            PaymentHistoryObj obj= new PaymentHistoryObj();
            obj.setPaymentDate("No Payments")	;
            obj.setAmount("-");
            //payment_history1[0] = obj;
        }
        //Utils.log("p history","size"+payment_history1.length);
        PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(context,R.layout.paymentlistview_row, payment_history1);
        //adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
        paymentListView.setAdapter(adapter);
    }

}