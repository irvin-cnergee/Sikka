package com.cnergee.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import all.interface_.IError;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.BaseApplication;
import com.cnergee.mypage.IdCardActivity;
import com.cnergee.mypage.NotificationListActivity;
import com.cnergee.mypage.PGSMS_Webview_Activity;
import com.cnergee.mypage.adapter.NotificationAdapter;
import com.cnergee.mypage.adapter.NotificationInterface;
import com.cnergee.mypage.caller.DeleteNotificationCaller;
import com.cnergee.mypage.caller.NotificationCaller;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cnergee.sikka.broadband.R;
import common.SOAP.CommonAsyncTask;
import common.SOAP.CommonSOAP;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationListFragment extends Fragment  implements NotificationInterface, OnCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    Utils utils;
    public static Context context;
    public static String rslt = "";
    public static String Deleterslt = "";
    public static String DeleteResponse = "";

    public static String TAG = "NotifiactaionListActivity";
    private boolean flag = true;
    boolean isLogout = false;
    private String logtag = getClass().getSimpleName();
    private String sharedPreferences_name,
            sharedPreferences_payment_notification;
    private ListView notificationlistview;
    public String Complain = null;
    public String Memberid = null;
    public static int ComplaintCount = 0;
    public int ListSize = 0;
    public static ArrayList<Notificationobj> notificationtList = new ArrayList<Notificationobj>();
    private boolean isFinish = false;
    private Map notificationMap;
    LinearLayout linnhome, linnprofile, linnnotification, linnhelp;
    String Notifyid = "";
    ArrayList<String> deltlist;
    CheckBox checkBox ,checkbx;
    Button deltbtn;
    LinearLayout llDeleteBtn;
    static int notListSize = 0;
    public boolean is_activity_running=false;
    IError iError;

    private String sharedPreferences_renewal,notificationMsg;
    TextView tv_marquee;

    public NotificationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationListFragment newInstance(String param1, String param2) {
        NotificationListFragment fragment = new NotificationListFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        context = getActivity();
        iError=(IError)context;
        is_activity_running=true;
        initControls(view);


        notificationlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Utils.log("Item ","Clicked");
                Notificationobj notificationobj=(Notificationobj) arg0.getItemAtPosition(arg2);

                //	Intent i1 = new  Intent(NotificationListActivity.this, IdCardActivity.class);
                //	startActivity(i1);
                if(notificationobj.getDataFrom()!=null){
                    if(notificationobj.getDataFrom().length()>0){
                        if(notificationobj.getDataFrom().equalsIgnoreCase("Identity Notification")){

                            String id[]=	notificationobj.getNotificationMessage().split("/=");
                            Utils.log("id","is"+id[1]);

                            Intent i = new  Intent(context, IdCardActivity.class);
                            i.putExtra("icard_id", id[1]);
                            startActivity(i);

							/*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ccrm.i-on.in/Idcard/identity.aspx?id=353318"));
							startActivity(browserIntent);*/
                        }

                        if(notificationobj.getDataFrom().equalsIgnoreCase("SMSPG")){

                            if(notificationobj.isIs_red()){
                                AlertsBoxFactory.showAlert("This link is expired", context);
                            }
                            else{
                                check_valid_pg_sms_request( notificationobj);
                            }

                        }
                    }
                }
            }
        });

        return view;
    }

    private void initControls(View view) {

        BaseApplication.getEventBus().register(this);
        checkBox = (CheckBox)view.findViewById(R.id.chkbx);
        llDeleteBtn=(LinearLayout)view.findViewById(R.id.llDeleteBtn);
        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        notificationMsg = sharedPreferences2.getString("NotificationMsg","");
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);
        tv_marquee.setSelected(true);
        tv_marquee.setText(notificationMsg);

        notificationlistview = (ListView)view.findViewById(R.id.notificationListView);
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences =context
                .getSharedPreferences(sharedPreferences_name, 0);
        utils = new Utils();
        utils.setSharedPreferences(sharedPreferences);



        SharedPreferences sharedPreferences1 = context
                .getSharedPreferences(
                        getString(R.string.shared_preferences_notification_list),
                        0);

        if (sharedPreferences1.getBoolean("notification_list", true)) {
            Utils.log("Data From server Notification_list ", "yes"
                    + sharedPreferences1.getBoolean("notification_list", true));
            if (Utils.isOnline(context)){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new NotificationListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new NotificationListWebService().execute();
            }
            else{
                setOfflineNotificationList();
                Toast.makeText(context,
                        "Please connect to internet and try again!!",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Utils.log("Data From server Notification_list","offline :"+sharedPreferences1.getBoolean("notification_list", true));
            setOfflineNotificationList();
            String versionName="";
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setOfflineNotificationList();
        }


    }



    protected class NotificationListWebService extends
            AsyncTask<String, Void, Void>  implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;

        //	private ProgressDialog Dialog = new ProgressDialog(
        //		NotificationListActivity.this);

        protected void onPreExecute() {
		/*	Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);
			Dialog.show();*/
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        }

        protected void onPostExecute(Void unused) {
            if(is_activity_running)
                mProgressHUD.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {

                boolean is_red_done=false;
                Notificationobj notification_data[] = new Notificationobj[notificationtList
                        .size()];

                Iterator iter = notificationtList.iterator();
                int i = 0;
                notificationMap = new HashMap();

                sharedPreferences_payment_notification = getString(R.string.shared_preferences_notification_list);
                SharedPreferences sharedPreferences1 = context
                        .getSharedPreferences(
                                sharedPreferences_payment_notification, 0); // 0
                // -
                notificationlistview.setVisibility(View.VISIBLE);													// for
                // private
                // mode
                //sharedPreferences1.edit().clear();
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("NotificationListSize", notificationtList.size());
                editor.putBoolean("notification_list", false);


                Utils.log("Check","notification value "+sharedPreferences1.getBoolean("notification_list", true));
                int j = 1;
                // editor.commit();
                notListSize = notificationtList.size();
                for (int k = 0; k < notificationtList.size(); k++) {
                    Notificationobj obj1 = notificationtList.get(k);
                    // Utils.log("PaymentDate and amount",obj1.getNotifyId()+" and "+obj1.getNotificationMessage());
                    editor.putString("NotifyId" + j, obj1.getNotifyId());
                    editor.putString("NotificationMessage" + j,
                            obj1.getNotificationMessage());
                    editor.putString("CreatedBy" + j,
                            obj1.getIcard_id());

                    editor.putString("dataFrom" + j,
                            obj1.getDataFrom());

                    boolean is_red=false;

                    if(obj1.getDataFrom().equalsIgnoreCase("SMSPG")){
                        if(!is_red_done){
                            is_red_done=true;
                            obj1.setIs_red(false);
                            is_red=false;
                        }
                        else{
                            obj1.setIs_red(true);
                            is_red=true;
                        }
                    }
                    else{
                        obj1.setIs_red(false);
                        is_red=false;
                    }
                    Utils.log("set is_red"+j, ""+is_red);
                    editor.putBoolean("is_red" + j,
                            is_red);
                    j++;
                }

                editor.commit();

                while (iter.hasNext()) {
                    Notificationobj obj = (Notificationobj) iter.next();
                    String id = obj.getNotifyId();
                    Complain = obj.getNotificationMessage();

                    String MemberId = id + "," + Complain;
                    // Log.i(" >> TEST <<< ",""+MemberId);

                    obj.setNotification(id);
                    notificationMap.put(id, obj);
                    // notification_data[i] = new Notificationobj(id);
                    notification_data[i] = obj;
                    // complaint_id[i] = new ComplaintListObj(Complain);

                    // System.out.println("Noti--"+(Notificationobj)notificationMap.get(notification_data[i]).);
                    i++;
                }
                NotificationAdapter adapter = new NotificationAdapter(
                        context,
                        R.layout.notification_listview_item_row,
                        notification_data);
                notificationlistview.setAdapter(adapter);

                setOfflineNotificationList();
            } else {
                if(is_activity_running)
                    AlertsBoxFactory.showAlert(rslt, context);
                return;
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            Utils.log("Get Notification", "Do in bg");
            try {
                NotificationCaller caller = new NotificationCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.MTHOD_GET_NOTIFICATION));

                caller.setMemberId(utils.getMemberId());

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
                if(is_activity_running)
                    AlertsBoxFactory.showErrorAlert(e.toString(), context);

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





    public void setOfflineNotificationList() {
        sharedPreferences_payment_notification = getString(R.string.shared_preferences_notification_list);
        SharedPreferences shared_preferencess = context
                .getSharedPreferences(sharedPreferences_payment_notification, 0);
        int i = shared_preferencess.getInt("NotificationListSize", 0);
        // Utils.log("NotificationListSize",""+i);
        Notificationobj[] notificationobj1 = null;
        // Utils.log("ListSize",shared_preferencess.getString("NotifyId"+1, "-"));
        if (i > 0) {
            notificationobj1= new Notificationobj[i];
            for (int j = 1; j <= i; j++) {
                Utils.log("ListSize in for",""+j);
                Notificationobj obj = new Notificationobj();
                obj.setNotifyId(shared_preferencess.getString("NotifyId" + j,
                        "-"));
                obj.setNotificationMessage(shared_preferencess.getString(
                        "NotificationMessage" + j, "-"));

                obj.setIs_red(shared_preferencess.getBoolean(
                        "is_red" + j, false));

                obj.setDataFrom(shared_preferencess.getString(
                        "dataFrom" + j, "-"));

                Utils.log("Notification Message",""+shared_preferencess.getString(
                        "NotificationMessage" + j, "-"));

                Utils.log("Is Red"+j,""+(shared_preferencess.getBoolean(
                        "is_red"+ j, false)));
                int k = j - 1;
                if(obj.getNotificationMessage().equalsIgnoreCase("-")){
                    notificationobj1=null;
                    Utils.log("NotificationListSize","null");
                }
                else{
                    Utils.log("NotificationListSize","not null");
                    notificationobj1[k] = obj;
                }
            }
        } else {
            Notificationobj obj = new Notificationobj();
            obj.setNotifyId("0");
            obj.setNotificationMessage("No Payments");
            // notificationobj1[0] = obj;
        }
        // Utils.log("p history","size"+notificationobj1.length);
        if(notificationobj1!=null){
            if(notificationobj1.length>0){

                Utils.log("Notification array","size"+notificationobj1.length);

                NotificationAdapter adapter = new NotificationAdapter(
                        context,
                        R.layout.notification_listview_item_row, notificationobj1);
                adapter.notifyDataSetChanged();
                notificationlistview.setAdapter(adapter);
                notificationlistview.setVisibility(View.VISIBLE);
            }
        }
        else{
            notificationlistview.setVisibility(View.GONE);
        }
        // adapter.setNotifyOnChange(true);


    }

    public class DeleteNotificationWebService extends
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        //ProgressDialog prg = new ProgressDialog(NotificationListActivity.this);
        ProgressHUD	mProgressHUD ;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

		/*	prg.show();
			prg.setMessage(getString(R.string.app_please_wait_label));
			prg.setCancelable(false);
*/
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                DeleteNotificationCaller caller = new DeleteNotificationCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL),context
                        .getResources().getString(
                                R.string.METHOD_DELETE_NOTIFICATION));

                caller.setNotifyIdCaller(Notifyid);
                caller.setMemberIdCaller(utils.getMemberId());

                caller.join();
                caller.start();
                Deleterslt = "START";

                while (Deleterslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception e) {
                AlertsBoxFactory.showErrorAlert(e.toString(), context);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressHUD.dismiss();
            if (DeleteResponse.equalsIgnoreCase("Updated Sucessfully")) {
                new NotificationListWebService().execute();
            } else {
                // Toast.makeText(NotificationListActivity.this, "Not Deleted",
                // Toast.LENGTH_LONG).show();
            }
        }

        /* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            mProgressHUD.dismiss();
        }
    }







    @Override
    public void showDelete(ArrayList<String> alNotifyId) {
        // TODO Auto-generated method stub
        deltlist = alNotifyId;


        if(alNotifyId!=null){
            Utils.log("Size","::"+alNotifyId.size());
            if(alNotifyId.size()>0){
                Utils.log("In Visible","Show Delete Executed");
                llDeleteBtn.setVisibility(View.VISIBLE);

            }
            else{

                Utils.log("In Gone ","Show Delt Executed");
                llDeleteBtn.setVisibility(View.GONE);
            }
        }
        else{
            llDeleteBtn.setVisibility(View.GONE);
        }


        llDeleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for(int i=0;i<deltlist.size();i++){
                    if(Notifyid.length()>0){
                        Notifyid+=","+deltlist.get(i);
                    }
                    else{
                        Notifyid=deltlist.get(i);
                    }
                }
                Utils.log("Notify Id are",":"+Notifyid);
                if(Utils.isOnline(context)){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new DeleteNotificationWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        new DeleteNotificationWebService().execute();
                }
                else{
                    Toast.makeText(context,
                            "Please connect to internet and try again!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void showIcard(String id,String datafrom,Notificationobj notificationobj) {
        // TODO Auto-generated method stub
        Utils.log("Id Card","id:"+id);
        if(datafrom.equalsIgnoreCase("Identity Notification")){
            Intent i = new  Intent(context, IdCardActivity.class);
            i.putExtra("icard_id", id);
            startActivity(i);
        }

        if(datafrom.equalsIgnoreCase("SMSPG")){
            if(!notificationobj.isIs_red())
                check_valid_pg_sms_request(notificationobj);
            else
                AlertsBoxFactory.showAlert("This link is expired."+"\n"+" Please try another link", context);
        }
    }

    public void check_valid_pg_sms_request(final Notificationobj notificationobj){

        CommonSOAP commonSOAP= new CommonSOAP(
                context.getResources().getString(
                        R.string.SOAP_URL),
                context.getResources().getString(
                        R.string.WSDL_TARGET_NAMESPACE), context
                .getResources().getString(
                        R.string.METHOD_CHECK_VALID_SMS_PG_REQUEST));

        SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_CHECK_VALID_SMS_PG_REQUEST));

        PropertyInfo pi = new PropertyInfo();
        pi.setName("MemberId");
        pi.setValue(utils.getMemberId());
        pi.setType(String.class);
        request.addProperty(pi);
        commonSOAP.setRequest(request);

        pi = new PropertyInfo();
        pi.setName(AuthenticationMobile.CliectAccessName);
        pi.setValue(AuthenticationMobile.CliectAccessId);
        pi.setType(String.class);
        request.addProperty(pi);
        commonSOAP.setRequest(request);



        commonSOAP.setRequest(request);

        //final ProgressDialog prgDialog= new ProgressDialog(NotificationListActivity.this);
        final ProgressHUD	prg_bar = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
        new CommonAsyncTask(context){


            @Override
            protected void onPostExecute(ArrayList<String> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                prg_bar.dismiss();
                if(result.get(0).equalsIgnoreCase("OK")){

                    if(result.get(1).length()>0){
                        if(result.get(1).equalsIgnoreCase("0")){
                            Utils.pg_sms_uniqueid="";
                            Utils.pg_sms_request=false;
                            AlertsBoxFactory.showAlert("This Link is Expired"+"\n"+"Please call customer care to get a new link.", context);
                        }
                        else{

                            String sms_link="";
                            String arr_split[]=notificationobj.getNotificationMessage().split(" ");
                            for(int i=0;i<arr_split.length;i++){
                                if(arr_split[i].contains("http:")){
                                    sms_link=arr_split[i];
                                }
                            }
                            if(sms_link.length()>0){
                                Intent renew_intent=new Intent(context, PGSMS_Webview_Activity.class);
                                renew_intent.putExtra("SMS_LINK", sms_link);
                                startActivity(renew_intent);
                                Utils.pg_sms_request=true;
                                Utils.pg_sms_uniqueid=result.get(1);
                            }
                            else{
                                Utils.pg_sms_request=false;
                                Utils.pg_sms_uniqueid="";
                            }

                            //int indx=notificationobj.getNotificationMessage().indexOf("http");


                        }

                    }
                    else{
                        Utils.pg_sms_request=false;
                        Utils.pg_sms_uniqueid="";
                    }



                }
                else{
                    if(result.get(0).equalsIgnoreCase("slow")){
                        iError.display();
                    }
                    else{
                        AlertsBoxFactory.showAlert("We are unable to process"+"\n"+"Please try again!", context);
                    }
                    Utils.pg_sms_request=false;
                    Utils.pg_sms_uniqueid="";

                }
            }
        }.execute(commonSOAP);


    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }
    @Subscribe
    public void	onFinishEvent(FinishEvent event){
        Utils.log(""+this.getClass().getSimpleName(),"finish");
        Utils.log(""+this.getClass().getSimpleName(),"::"+Utils.Last_Class_Name);
        if(this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){


        }

    }

}