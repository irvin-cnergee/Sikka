package com.cnergee.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
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
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.ChangePackage_NewActivity;
import com.cnergee.mypage.IONHome;
import com.cnergee.mypage.LoginFragmentActivity;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.SOAP.AreaWiseSettingSOAP;
import com.cnergee.mypage.adapter.CustomGridAdapter;
import com.cnergee.mypage.adapter.SpinnerDaysAdapter;
import com.cnergee.mypage.caller.PackgeCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.PackageList;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.PackageListParsing;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import java.io.File;
import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import androidx.fragment.app.FragmentTransaction;
import cnergee.sikka.broadband.R;

import static com.cnergee.mypage.utils.Utils.SID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PackgedetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackgedetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    Boolean is_activity_running=false;
    public static boolean isVaildUser=false;

    Context context;
    private String sharedPreferences_name;
    Utils utils = new Utils();
    String MemberId,memberloginid,otp_password="";
    ProgressDialog mainDialog;
    public static String rslt = "";
    public static String settingResult="";
    public static String strXML = "";
    boolean check_intent=false;


    PackageListWebService packageListWebService = null;
    static String extStorageDirectory = Environment
            .getExternalStorageDirectory().toString();
    final static String xml_folder = "mypage";
    final static String TARGET_BASE_PATH = extStorageDirectory + "/"
            + xml_folder + "/";

    public String xml_file_postFix = "PackageList.xml";
    public String xml_file;
    public String xml_file_with_path,notificationMsg;

    public static Map<String, PackageList> mapPackageList;
    //    List<Integer> unsortList = new ArrayList<Integer>();
    List<Integer> unsortList = new ArrayList<Integer>();

    Spinner planList;
//    GridView gridview,gridview1;
    SortedMap<Integer,ArrayList<PackageList>> hm_packge_list = new TreeMap<>();
    ImageView btnhome,btnprofile,btnnotification,btnhelp;
    SpinnerDaysAdapter spinnerDaysAdapter;
    private int previousSelectedPosition = -1;
    boolean is_selected = true;
    TextView tv_marquee;
    private String sharedPreferences_renewal;
    int ispid,sid;
    boolean ispostpaid;
    ExpandableListView exlv_PackageList;



    public PackgedetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PackgedetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PackgedetailFragment newInstance(String param1, String param2) {
        PackgedetailFragment fragment = new PackgedetailFragment();
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
        view = inflater.inflate(R.layout.fragment_packgedetail, container, false);
        context = getActivity();
        ((IONHome)getActivity()).updateTitle(getString(R.string.trans_UpgradePlan));

        initViews(view);

        xml_file_with_path = TARGET_BASE_PATH + xml_file_postFix;
        File file = new File(xml_file_with_path);
        file.delete();
        is_activity_running=true;
        is_activity_running=true;
        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        ispid = Integer.parseInt(sharedPreferences.getString("ISPId",""));
        ispostpaid = Boolean.parseBoolean(sharedPreferences.getString("IsPostPaid",""));
        sid = sharedPreferences.getInt(SID,0);
        Utils.log("ispid"," ispid: "+ispid);
        Utils.log(""," ispostpaid: "+ispostpaid);

        utils.setSharedPreferences(sharedPreferences);
        mainDialog= new ProgressDialog(context);
        MemberId = utils.getMemberId();
        mapPackageList = new HashMap<String, PackageList>();
        xml_file = xml_file_postFix;
        xml_file_with_path =  TARGET_BASE_PATH + xml_file;
        memberloginid = utils.getMemberLoginID();
        otp_password= sharedPreferences.getString("otp_password", "0");
        tv_marquee.setSelected(true);
        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        notificationMsg = sharedPreferences1.getString("NotificationMsg","");

        tv_marquee.setText(notificationMsg);

        if(Utils.isOnline(context)){
            //packageListWebService = new PackageListWebService();
            //packageListWebService.execute((String)null);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                Utils.log("Both Thread"," executed");
                new ValidUserWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
            }
            else{
                Utils.log("ValidUser Thread"," executed");
                new ValidUserWebService().execute();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setMessage(
                    "Please connect to internet and try again!!")
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setPositiveButton("Confirm",
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



        return view;
    }

    private void initViews(View view) {

        tv_marquee= (TextView) view.findViewById(R.id.tv_marquee);
        exlv_PackageList = view.findViewById(R.id.exlv_PackageList);



    }

    protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {
        ProgressHUD mProgressHUD;
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
                    WindowManager w = getActivity().getWindowManager();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                        w.getDefaultDisplay().getSize(size);
                        width = size.x;
                        height = size.y;
                    } else {
                        Display d = w.getDefaultDisplay();
                        width = d.getWidth();
                        height   = d.getHeight();;
                    }


                    TextView dtv = (TextView) dialog.findViewById(R.id.tv1);

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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                smsCaller.setCallData("changepack");
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

            AreaWiseSettingSOAP areaWiseSettingSOAP= new AreaWiseSettingSOAP(
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

            if(is_activity_running) {
                if (mProgressHUD != null)
                    mProgressHUD.dismiss();
            }
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

    private class PackageListWebService extends AsyncTask<String, Void, Void>implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        String ConnectionTypeId="",AreaId="";

        protected void onPreExecute() {
            if(is_activity_running)
                mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label),true,true,this);
            SharedPreferences sharedPreferences1 = context
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

          /*  if (rslt.trim().equalsIgnoreCase("ok")) {
                writeXMLFile();
            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }*/
            //Log.e("rslt",":"+rslt);
            Log.e("XML File",":"+PackgedetailActivity.strXML);
            setPackageList(PackgedetailFragment.strXML);


            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
//                PackgeCaller caller = new PackgeCaller(getApplicationContext()
//                        .getResources().getString(
//                                R.string.WSDL_TARGET_NAMESPACE),
//                        getApplicationContext().getResources().getString(
//                                R.string.SOAP_URL), getApplicationContext()
//                        .getResources().getString(
//                                R.string.METHOD_PACKAGELIST_BY_CONNECTIONTYPEID));


                PackgeCaller caller = new PackgeCaller(context
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_BINDPACKAGES));
                caller.setMemberId(Long.parseLong(MemberId));
                //caller.setConnectionTypeId(ConnectionTypeId);
                // caller.setAreaCode(AreaId);
                caller.setUserLoginName(memberloginid);
                caller.setIspid(ispid);
                caller.setAreaCode((AreaId));
                caller.setConnectionTypeId((ConnectionTypeId));
                caller.setPostPaid(ispostpaid);


//                PackgeCaller caller = new PackgeCaller(getApplicationContext()
//                        .getResources().getString(
//                                R.string.WSDL_TARGET_NAMESPACE),
//                        getApplicationContext().getResources().getString(
//                                R.string.SOAP_URL), getApplicationContext()
//                        .getResources().getString(
//                                R.string.METHOD_PACKAGELIST));
//                caller.setMemberId(Long.parseLong(MemberId));
//                caller.setConnectionTypeId(ConnectionTypeId);
//                caller.setAreaCode(AreaId);
//                caller.setSid(sid);
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

    private void setPackageList(String str_xml ) {

        try {
            //String str_xml = readPackageListXML();
            //Utils.log("Parsing String XML", ":"+str_xml);
            PackageListParsing packageList = new PackageListParsing(str_xml);
            Utils.log("packageList", ":" + packageList.getMapPackageList());
            mapPackageList = packageList.getMapPackageList();
            Utils.log("mapPackageList", ":" + mapPackageList.size());


//            List<Map.Entry<String, PackageList>> list =
//                    new LinkedList<Map.Entry<String, PackageList>>(mapPackageList.entrySet());
//
//            Collections.sort(list, new Comparator<Map.Entry<String, PackageList>>() {
//                public int compare(Map.Entry<String, PackageList> o1, Map.Entry<String, PackageList> o2) {
//                    return (o1.getValue().getPackagevalidity()).compareTo(o2.getValue().getPackagevalidity());
//                }
//            });
            Iterator it = mapPackageList.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //Integer key = (Integer) pair.getKey();
                PackageList package_List = (PackageList) pair.getValue();

                Log.e("pair.getValue()", ":--" +pair.getValue());

                if (hm_packge_list.containsKey(package_List.getPackagevalidity())) {
                    ArrayList<PackageList> al_pkg_list = hm_packge_list.get(package_List.getPackagevalidity());
                    al_pkg_list.add(package_List);
                    Log.e("al_pkg_list12", ":--" +al_pkg_list.get(0).getPackageRate());

                  /*  Collections.sort(al_pkg_list, new Comparator<PackageList>() {
                        @Override
                        public int compare(PackageList lhs, PackageList rhs) {

                            Log.e("lhs1", ":--" + lhs.getPackagevalidity()+"\nrhs:-"+rhs.getPackagevalidity());

                            return lhs.getPackagevalidity().compareTo(rhs.getPackagevalidity());
                        }
                    });*/

                    Log.e("al_pkg_list", ":--" + al_pkg_list);

                    Log.e("1234", ":--" + package_List);

                    Log.e("getPackagevalidity", ":--" + package_List.getPackagevalidity());
                    hm_packge_list.put(package_List.getPackagevalidity(), al_pkg_list);
                    Log.e("hm_packge_list12", ":--" + hm_packge_list.get(package_List.getPackagevalidity()));


                } else {
                    ArrayList<PackageList> al_package_list = new ArrayList<>();
                    al_package_list.add(package_List);


                   /* Collections.sort(al_package_list, new Comparator<PackageList>() {
                        @Override
                        public int compare(PackageList lhs, PackageList rhs) {
                            Log.e("lhs", ":--" + lhs.getPackagevalidity()+"\nrhs:-"+rhs.getPackagevalidity());

                            return lhs.getPackagevalidity().compareTo(rhs.getPackagevalidity());
                        }
                    });*/
//                    Collections.sort(al_package_list,Comparator.<PackageList>reverseOrder());
//                    Collections.sort(al_package_list);





                    Log.e("al_package_list", ":--" + al_package_list);
                    Log.e("123", ":--" + package_List);

                    Log.e("getPackagevalidity1", ":--" + package_List.getPackagevalidity());
                    hm_packge_list.put(package_List.getPackagevalidity(), al_package_list);
                    Log.e("hm_packge_list", ":--" + hm_packge_list.get(package_List.getPackagevalidity()).get(0).getPackageRate());


                }
//                Collections.sort((List<Comparable>) hm_packge_list);


//                Iterator iterator = hm_packge_list.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry me2 = (Map.Entry) iterator.next();
//                    //System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
//                    String key =  (String) me2.getKey();
//                    ArrayList<PackageList> al_apckage =  ( ArrayList<PackageList>) me2.getValue();
//
//                    Log.e("key",":"+key);
//                    for (int i = 0; i <al_apckage.size() ; i++) {
//                        Log.e("value",":"+al_apckage.get(i).getPlanName());
//                        Log.e("value",":"+al_apckage.get(i).getPackageRate());
//                        Log.e("value",":"+al_apckage.get(i).getPackagevalidity());
//
//                    }
//
//                }
//                sort(List<T> list,
//                        Comparator<? super T> c)

                it.remove(); // avoids a ConcurrentModificationException
            }

            Set<Integer> keys = hm_packge_list.keySet();

            Iterator<Integer> i = keys.iterator();
            Log.e("keys", ":--" + keys);

            while (i.hasNext()) {
                Integer key = i.next();
                if (!unsortList.contains(key))
                    unsortList.add(key);
            }


//            Collections.sort(unsortList,Collections.<Integer>reverseOrder());

//                Collections.sort(unsortList,Comparator.naturalOrder());

//            Collections.sort(unsortList);
//            Collections.sort((List<Comparable>) hm_packge_list);
//            unsortList.sort(Comparator.<Integer>reverseOrder());


            Log.e("Tab1", ":--" + unsortList);
            Log.e("Tab", ":--" + hm_packge_list.get(unsortList.get(0)));
            exlv_PackageList.setAdapter(  new CustomGridAdapter( context,unsortList, hm_packge_list));
            exlv_PackageList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


//                    Intent intent =new Intent(context, ChangePackage_NewActivity.class);
//                    intent.putExtra("Selected_Pkg", (Serializable) hm_packge_list.get(unsortList.get(groupPosition)).get(childPosition));
//                    startActivity(intent);
                    Fragment fragment = new ChangePackageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Selected_Pkg", (Serializable) hm_packge_list.get(unsortList.get(groupPosition)).get(childPosition));
                    fragment.setArguments(bundle);
                    loadFragment(fragment);

                    return false;
                }
            });
            //            setGrid("");
            //            for(Integer temp: unsortList){
//                CharSequence textHolder = "" + temp;
//                adapterForSpinner.add(textHolder);
//            }
//
//            planList.setAdapter(adapterForSpinner);
//            gridViewSetting(unsortList);
//            setGrid("");
//            final List<Integer> final_pkg_list1 = unsortList;
//
//
//            gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    CardView tv = (CardView) view.findViewById(R.id.card_view);
//                    tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
//
//                    LinearLayout previousSelectedView = (LinearLayout) gridview1.getChildAt(previousSelectedPosition);
//                    gridview1.getChildAt(previousSelectedPosition);
//
//                        if (previousSelectedPosition != -1)
//                        {
//                           // previousSelectedView.setSelected(false);
//                            CardView cardView = (CardView) previousSelectedView.findViewById(R.id.card_view);
//                            cardView.setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
//                            tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
//                        }
//
//                    previousSelectedPosition = position;
//
//                    if(position!=0) {
//                        is_selected = false;
//                    }
//                    else
//                        is_selected =true;
//
//
//                    if(is_selected)
//                     gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.pkg6));
//                    else
//                        gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
//                    setGrid(((TextView) view.findViewById( R.id.tv_days )) .getText().toString());
//                }
//
//            });
//
//
//
//
//    }

        }  catch (Exception e) {
            e.printStackTrace();
            AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
                    context);
        }
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();
    }

}