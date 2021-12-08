package com.cnergee.myapplication;


import cnergee.sikka.broadband.R;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.sabpaisa.payment.callback.SabPaisaPG;
import com.sabpaisa.payment.pojo.PaymentResponse;


import java.util.HashMap;

public class SubTestActivity extends AppCompatActivity{//} implements SabPaisaPG {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtest);

       /* HashMap<String,String> hm_subpaisa =new HashMap<>();
        hm_subpaisa.put("username","anish_24");
        hm_subpaisa.put("password","LINKP_SP24");
        hm_subpaisa.put("txnId","SB010000141335SB");
        hm_subpaisa.put("clientCode","SABPAISA");
        hm_subpaisa.put("authKey","0ZRSHMozMgcBUFD4");
        hm_subpaisa.put("authIV","9wkjg1c5vijxjBej");
        hm_subpaisa.put("txnAmt","10.00");
        hm_subpaisa.put("URLsuccess","https://securepay.sabpaisa.in/SabPaisa/sabPaisaInit ");
        hm_subpaisa.put("URLfailure","https://securepay.sabpaisa.in/SabPaisa/sabPaisaInit ");
        hm_subpaisa.put("payerFirstName","Jyoti");
        hm_subpaisa.put("payerLastName","Sukre");
        hm_subpaisa.put("payerContact","8082325909");
        hm_subpaisa.put("payerEmail","josukre@cnergee.com");
        hm_subpaisa.put("payerAddress","Ghansoli");*/

      /*  HashMap<String,String> hm_subpaisa =new HashMap<>();
        hm_subpaisa.put("username","Sik35@sp");
        hm_subpaisa.put("password","UnnxB16At1");
        hm_subpaisa.put("txnId","SB010000141350SB");
        hm_subpaisa.put("clientCode","SBPL1");
        hm_subpaisa.put("authKey","LEb9SzXNAIsgHLdt");
        hm_subpaisa.put("authIV","Z4IPryT4DkxeQBF3");
        hm_subpaisa.put("txnAmt","10.00");
        hm_subpaisa.put("URLsuccess","https://myaccount.sikkanet.com/PaymentGateWay/SabPaisa/PaymentStatusTransMobile.aspx");
        hm_subpaisa.put("URLfailure","https://myaccount.sikkanet.com/PaymentGateWay/SabPaisa/PaymentStatusTransMobile.aspx");
        hm_subpaisa.put("payerFirstName","Jyoti");
        hm_subpaisa.put("payerLastName","Sukre");
        hm_subpaisa.put("payerContact","8082325909");
        hm_subpaisa.put("payerEmail","josukre@cnergee.com");
        hm_subpaisa.put("payerAddress","Ghansoli");


        EDW edw = new EDW(hm_subpaisa, SubTestActivity.this);
        edw.initiatePayment();*/

    }



   /* @Override
    public void success(PaymentResponse paymentResponse) {

    }

    @Override
    public void failure(String s) {

    }

    @Override
    public void paymentResponse(PaymentResponse paymentResponse) {

    }

    @Override
    public void onError(String s) {

    }*/
}
