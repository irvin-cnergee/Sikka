package com.cnergee.mypage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import cnergee.sikka.broadband.R;

public class SubpaisaResponse extends Activity {
    TextView mTranscationIdOrderId, mTextMessage, mTxRefNo, mPgTxnNo,
            mTxStatus,txrefno;
    String type ,id,transmsg,status,amount,orgTxnAmount,Trackid,clientTxnId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subpaisa_response);

        mTranscationIdOrderId = (TextView) findViewById(R.id.txid_orderid);
        mTextMessage = (TextView) findViewById(R.id.txmsg);
        mTxRefNo = (TextView) findViewById(R.id.pgtxnno);
        mTxStatus = (TextView) findViewById(R.id.txstatus);
        txrefno = (TextView)findViewById(R.id.txrefno);


        Intent i = getIntent();

        status = i.getStringExtra("spRespStatus");
        id = i.getStringExtra("spPaymentid");
        amount = i.getStringExtra("spAmount");
        orgTxnAmount = i.getStringExtra("orgTxnAmount");
        Trackid = i.getStringExtra("Trackid");
        transmsg = i.getStringExtra("transmsg");
        clientTxnId= i.getStringExtra("clientTxnId");


        mTranscationIdOrderId.setText("Transaction Number: "+Trackid+" |  Order Amount: "+ amount+" "+" INR");
        mTextMessage.setText(transmsg);
        txrefno.setText(clientTxnId);
        mTxRefNo.setText(id);
        mTxStatus.setText(transmsg);
    }


}
