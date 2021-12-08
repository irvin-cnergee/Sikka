package com.cnergee.mypage;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.utils.Utils;

import cnergee.sikka.broadband.R;


public class TransResponseBillDesk extends Activity{
	
	TextView mTranscationIdOrderId, mTextMessage, mTxBankRefNo, mPgTxnNo,
	mTxStatus,txrefno;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private String sharedPreferences_name;
	String status="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transresponse);
		initWidget();
		
		linnhome =(LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile =(LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp =(LinearLayout)findViewById(R.id.inn_banner_help);
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseBillDesk.this.finish();
				Intent i = new Intent(TransResponseBillDesk.this, IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseBillDesk.this.finish();
				Intent i = new Intent(TransResponseBillDesk.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseBillDesk.this.finish();
				Intent i = new Intent(TransResponseBillDesk.this, NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TransResponseBillDesk.this.finish();
				Intent i = new Intent(TransResponseBillDesk.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub

		mTranscationIdOrderId = (TextView) findViewById(R.id.txid_orderid);
		mTextMessage = (TextView) findViewById(R.id.txmsg);
		mTxBankRefNo = (TextView) findViewById(R.id.pgtxnno);
		mTxStatus = (TextView) findViewById(R.id.txstatus);
		txrefno = (TextView)findViewById(R.id.txrefno);
		
		Intent i= getIntent();
		status=i.getStringExtra("status");
		Utils.log("TransResponse","BillDesk");
		
		setCCAvenueResponse();
	}

	private void setCCAvenueResponse()
	{
		Intent i= getIntent();
		
		Log.e("transStatus",":"+i.getStringExtra("transStatus"));
		Log.e("order_id",":"+i.getStringExtra("order_id"));
		Log.e("PG_track_id",":"+i.getStringExtra("PG_track_id"));
		Log.e("transaction_msg",":"+i.getStringExtra("transaction_msg"));
		Log.e("bank_ref_no",":"+i.getStringExtra("bank_ref_no"));
		Log.e("amount",":"+i.getStringExtra("amount"));
		Log.e("transaction_msg",":"+i.getStringExtra("transaction_msg"));
		
		mTxStatus.setText(i.getStringExtra("transStatus"));
		mTranscationIdOrderId.setText("Transaction Number: "+i.getStringExtra("order_id")+" |  Order Amount: "+ i.getStringExtra("amount")+" INR");
		mTxBankRefNo.setText(i.getStringExtra("PG_track_id"));
		mTextMessage.setText(i.getStringExtra("transaction_msg"));
		txrefno.setText(i.getStringExtra("bank_ref_no"));
		
		Utils.log("transaction_msg", ":"+i.getStringExtra("transaction_msg"));
		Utils.log("transaction_ref_nog", ":"+i.getStringExtra("transaction_ref_no"));
		
		//mTxRefNo.setText(i.getStringExtra("order_status"));
	
		if(mTxStatus.getText().toString().equalsIgnoreCase("SUCCESS")){
			
			sharedPreferences_name = getString(R.string.shared_preferences_name);
			SharedPreferences sharedPreferences = getApplicationContext()
						.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
				
				Utils.log("second time", ":"+sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true));
				if(sharedPreferences.getBoolean(Utils.APP_SECOND_TIME, true)){
					if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").length()>0){
						
						Utils.log("App rate status", ""+sharedPreferences.getString(Utils.APP_RATE_STATUS, ""));
						
						if(sharedPreferences.getString(Utils.APP_RATE_STATUS, "").equalsIgnoreCase("later")){
							if(Utils.open_once){
								Utils.showAppRater(TransResponseBillDesk.this);
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
							Utils.showAppRater(TransResponseBillDesk.this);
							Utils.open_once=false;
						}
					}
					
				}
				else{
					
				}
			}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	this.finish();
	/*Intent i = new Intent(TransResponsePayu.this,IONHome.class);
	startActivity(i);*/
	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
}
