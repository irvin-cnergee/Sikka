package com.cnergee.mypage.sys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/*public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Utils.log("Broadcast ","called");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent myIntent = new Intent(context,MyPageService.class);
            context.startForegroundService(myIntent);
        }
        else {
            Intent myIntent = new Intent(context,MyPageService.class);
            context.startService(myIntent);
        }


	 }
 }*/
