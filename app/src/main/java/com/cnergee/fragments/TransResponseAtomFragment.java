package com.cnergee.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnergee.mypage.utils.Utils;

import cnergee.sikka.broadband.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransResponseAtomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransResponseAtomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView mTranscationIdOrderId, mTextMessage, mTxRefNo, mPgTxnNo,
            mTxStatus,txrefno;

    View view;

    public TransResponseAtomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransResponseAtomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransResponseAtomFragment newInstance(String param1, String param2) {
        TransResponseAtomFragment fragment = new TransResponseAtomFragment();
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
        view = inflater.inflate(R.layout.fragment_trans_response_atom, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {

        mTranscationIdOrderId = (TextView)view.findViewById(R.id.txid_orderid);
        mTextMessage = (TextView)view.findViewById(R.id.txmsg);
        mTxRefNo = (TextView)view.findViewById(R.id.pgtxnno);
        mTxStatus = (TextView)view.findViewById(R.id.txstatus);
        txrefno = (TextView)view.findViewById(R.id.txrefno);
        //setCCAvenueResponse();
        atomresponse();

    }

    private void atomresponse()
    {

		/*Utils.log("order_id",":"+i.getSerializableExtra("order_id"));
		if(i.getStringExtra("transStatus").equals("onbackpressed")){
            mTxStatus.setText("Transaction Cancelled.");
        }else {
            mTxStatus.setText(i.getStringExtra("transStatus"));
        }

		if(i.getStringExtra("order_id").equals("null")){
            mTranscationIdOrderId.setVisibility(View.GONE);
            mTranscationIdOrderId.setText("Order Amount: "+ i.getStringExtra("Amount")+" INR");
        }else {
            mTranscationIdOrderId.setText("Transaction Number: " + i.getStringExtra("order_id") + " | Order Amount: " + i.getStringExtra("Amount") + " INR");

        }
		mTxRefNo.setText(i.getStringExtra("bank_ref_no"));
		//txrefno.setText(i.getStringExtra("AuthCode"));
        if(i.getStringExtra("Track_id").equals("null")){
            txrefno.setVisibility(View.GONE);
        }else {
            txrefno.setText(i.getStringExtra("Track_id"));
        }

		//mTxRefNo.setText(i.getStringExtra("bank_ref_no"));

            mTextMessage.setText(i.getStringExtra("statusMsg"));
*/

        Utils.log("TrnsResponseAmount",""+"Amount");
        Utils.log("TrnsResponseAmount",""+getArguments().getString("Amount"));
        Utils.log("TrnsResponsemTextMessage",""+mTextMessage.getText().toString());
        Utils.log("TrnsResponsemTxRefNo",""+mTxRefNo.getText().toString());
        Utils.log("TrnsResponsetxrefno",""+txrefno.getText().toString());
        Utils.log("TrnsResponsemTranscationIdOrderId",""+mTranscationIdOrderId.getText().toString());
        Utils.log("TrnsResponsemTxStatus",""+mTxStatus.getText().toString());
        Utils.log("TrnsResponsegetting Amount",""+getArguments().getString("Amount"));
        Utils.log("Response Amount",""+mTranscationIdOrderId);



        String amount = getArguments().getString("Amount");
        String statusMsg = getArguments().getString("statusMsg");
        String transStatus = getArguments().getString("transStatus");
        String order_id = getArguments().getString("order_id");

        String order_status = getArguments().getString("order_status");
        String bank_ref_no = getArguments().getString("bank_ref_no");
        String Track_id = getArguments().getString("Track_id");


        if(transStatus.equals("onbackpressed")){
            mTranscationIdOrderId.setText(" Order Amount: " + amount + " INR");
            mTxRefNo.setText("-");
            mTxStatus.setText("Transaction Cancelled.");
            mTextMessage.setText("You have cancelled the transaction.");
        }else {
            mTranscationIdOrderId.setText("Transaction Number: " + order_id + " | Order Amount: " + amount + " INR");
            mTxStatus.setText(transStatus);
            mTextMessage.setText(statusMsg);
            mTxRefNo.setText(bank_ref_no);
        }


        txrefno.setText(Track_id);
    }

}
