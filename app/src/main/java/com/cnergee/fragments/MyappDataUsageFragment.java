package com.cnergee.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnergee.mypage.MyappDataUsage;
import com.cnergee.mypage.caller.DataUsageCaller;
import com.cnergee.mypage.obj.CircularProgressBar;
import com.cnergee.mypage.obj.DataUsageObj;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.cnergee.widgets.ProgressPieView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyappDataUsageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyappDataUsageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Context context;
    Utils utils = new Utils();
    public static String rslt = "";

    private String SharedPreferences_name;
    private boolean flag=true;
    public String memberloginid,notificationMsg;
    public long memberid;
    ImageView btnhome,btnprofile,btnnotification,btnhelp;
    TextView tvUsed,tv_marquee;
    TextView txtused,txtallotedused,txtalotted,txttotalallotted,txtremainig,txttotalremainig;
    private boolean isFinish = false;
    private String sharedPreferences_name;

    public static Map<String , PackageDetails> mapPackageDetails;
    public static Map<String , DataUsageObj> mapdatausage;


    private GetDataUsageWebServices getdatausagewebservice = null;

    //public static Map<String, PackageDetails> mapPackageDetails;
    //public static Map<String, DataUsageObj> mapdatausage;
    //private GetMemberDetailWebService getMemberDetailWebService = null;
    //private GetDataUsageWebServices getdatausagewebservice = null;
    CircularProgressBar circularBar;

    ProgressPieView mProgressPieView;
    boolean isLogout = false;
    private String sharedPreferences_renewal;
    View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyappDataUsageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyappDataUsageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyappDataUsageFragment newInstance(String param1, String param2) {
        MyappDataUsageFragment fragment = new MyappDataUsageFragment();
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

        view = inflater.inflate(R.layout.fragment_myapp_data_usage, container, false);
        context = getActivity();

        initViews(view);

        sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(sharedPreferences_renewal, 0);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        notificationMsg = sharedPreferences1.getString("NotificationMsg","");
        tv_marquee.setSelected(true);
        tv_marquee.setText(notificationMsg);

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

        utils.setSharedPreferences(sharedPreferences);

        memberloginid = utils.getMemberLoginID();
        if(utils.getMemberId().length()>0){
            memberid = Long.valueOf(utils.getMemberId());
        }
		/*if (Utils.isOnline(MyappDataUsage.this)) {
		getdatausagewebservice = new GetDataUsageWebServices();
		getdatausagewebservice.execute((String) null);
		}
		else{
			Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		}*/


        return view;
    }

    private void initViews(View view) {

        tvUsed=(TextView)view.findViewById(R.id.tvUsedPercentage);
        mProgressPieView = (ProgressPieView)view.findViewById(R.id.progressPieViewXml);
        txtused = (TextView)view.findViewById(R.id.txtused);
        txtallotedused = (TextView)view.findViewById(R.id.txtallotedused);
        txtalotted =(TextView)view.findViewById(R.id.txtalloted);
        txttotalallotted = (TextView)view.findViewById(R.id.txttotaslalotted);
        txtremainig = (TextView)view.findViewById(R.id.txtremaining);
        txttotalremainig =(TextView)view.findViewById(R.id.txtallotedremain);
        tv_marquee= (TextView)view.findViewById(R.id.tv_marquee);
        circularBar= (CircularProgressBar)view.findViewById(R.id.circularprogressbar2);

        mProgressPieView.setProgressColor(getResources().getColor(R.color.color_red));
        mProgressPieView.setBackgroundColor(getResources().getColor(R.color.color_green));

        if (Utils.isOnline(context)) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                getdatausagewebservice = new GetDataUsageWebServices();
                getdatausagewebservice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
            }
            else{
                new GetDataUsageWebServices().execute();
            }
        }

    }


    private  class GetDataUsageWebServices extends AsyncTask<String ,Void,Void> implements DialogInterface.OnCancelListener {
        // private ProgressDialog Dialog = new ProgressDialog(MyappDataUsage.this);
        ProgressHUD mProgressHUD ;
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD.show(context,getString(R.string.app_please_wait_label), true,true,this);
            Utils.log("MYAppDataUsage 157" ,"Aynck Task Executed");
        }

        protected void onPostExecute(Void unused) {
            getdatausagewebservice = null;
            mProgressHUD.dismiss();

            try{
                if (rslt.trim().equalsIgnoreCase("ok")) {
                    if (mapdatausage != null) {

                        Set<String> keys = mapdatausage.keySet();
                        String str_keyVal = "";

                        for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                            str_keyVal = (String) i.next();


                            String selItem = str_keyVal.trim();
                            isLogout = false;
                            //finish();
                            DataUsageObj datausage = mapdatausage.get(selItem);

                            //txtallotedtime.setText(datausage.getSessionTime());
                            //txtusedtime.setText(datausage.getActiveTime());
                            //txtusedupload.setText(datausage.getUpLoadData());
                            //txtdownloadused.setText(datausage.getDownLoadData());
                            //txtallotedtime.setText(datausage.getAllotedData());

                            txttotalallotted.setText(datausage.getAllotedData());
                            txtallotedused.setText(datausage.getTotalDataTransfer());
                            txttotalremainig.setText(datausage.getRemainData());

                            Thread.sleep(1000);

                            Double GetPercentage=(Double.parseDouble(datausage.getTotalDataTransfer())*100)/Double.parseDouble(datausage.getAllotedData());

                            mProgressPieView.setMax(100);
                            mProgressPieView.animateProgressFill(GetPercentage
                                    .intValue());
                            mProgressPieView.setText("");

                            tvUsed.setText(GetPercentage
                                    .intValue()+"% Used");


							/*	mProgressPieView.setProgress(GetPercentage
									.intValue());
							mProgressPieView.animateProgressFill(GetPercentage
									.intValue());
							mProgressPieView.setText("");
							tvUsed.setText(GetPercentage
									.intValue()+"%\n used");


							mProgressPieView.setOnProgressListener(new OnProgressListener() {

								@Override
								public void onProgressCompleted() {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProgressChanged(int progress, int max) {
									// TODO Auto-generated method stub
									Utils.log("Progress","is:"+progress);
									tvUsed.setText(progress+"%\n used");
								}
							});*/

                            txttotalallotted.setText(datausage.getAllotedData());
                            txtallotedused.setText(datausage.getTotalDataTransfer());
                            txttotalremainig.setText(datausage.getRemainData());

                            txttotalallotted.invalidate();
                            txtallotedused.invalidate();
                            txttotalremainig.invalidate();
                            //	mProgressPieView.animateProgressFill();

						/*circularBar.animateProgressTo(0, GetPercentage.intValue(), new ProgressAnimationListener() {

							@Override
							public void onAnimationStart() {
							}

							@Override
							public void onAnimationProgress(int progress) {
								circularBar.setTitle(progress + "%");

								tvUsed.setText(progress+"%\n used");
							}

							@Override
							public void onAnimationFinish() {
								circularBar.setSubTitle("used");
							}
						});
						*/
                        }
                    }
                }else if (rslt.trim().equalsIgnoreCase("not")) {
                    //AlertsBoxFactory.showAlert("Subscriber not found !!! ",context );
                    mProgressPieView.setText("NA");
                    mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
                    circularBar.setTitle("NA");
                    circularBar.setSubTitle("");
                }else{
                    circularBar.setTitle("NA");
                    mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
                    circularBar.setSubTitle("");
                    //AlertsBoxFactory.showAlert(rslt,context );
                }
            }catch(Exception e){
                //AlertsBoxFactory.showAlert(rslt,context );
                Utils.log("Error","is:"+e);
                mProgressPieView.setText("NA");
                mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
                circularBar.setTitle("NA");
                circularBar.setSubTitle("");
            }
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                Utils.log("In BackGnd Aync203", "AynckTaskExceuted");
                DataUsageCaller datausagecaller = new DataUsageCaller(
                        context.getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        context.getResources().getString(
                                R.string.SOAP_URL), context
                        .getResources().getString(
                                R.string.METHOD_GETUSAGEDETAILS));

                datausagecaller.memberid = memberid;

                datausagecaller.join();
                datausagecaller.start();
                rslt = "START";

                while(rslt == "START")  {
                    try {

                        Thread.sleep(10);
                    } catch (Exception ex) {

                        ex.printStackTrace();

                        Utils.log("Try","Excuted try");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFinish=true;
            return null;
        }
        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();
            getdatausagewebservice = null;
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
}