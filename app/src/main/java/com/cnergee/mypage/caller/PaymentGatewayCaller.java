
package com.cnergee.mypage.caller;

import com.cnergee.fragments.MakeMyPayment_AtomFragment;
import com.cnergee.fragments.MakePaymentSubpaisaFragment;
import com.cnergee.mypage.MakeMyPayments;
import com.cnergee.mypage.MakeMyPaymentsTopUp;
import com.cnergee.mypage.MakeMyPaymentsTopUp_CCAvenue;
import com.cnergee.mypage.MakeMyPayments_CCAvenue;
import com.cnergee.mypage.MakePaymentSubpaisa;
import com.cnergee.mypage.SOAP.PaymentGatewaySOAP;
import com.cnergee.mypage.utils.Utils;


public class PaymentGatewayCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private PaymentGatewaySOAP adjustmentSOAP;
	private String  TrackId;
	private String MemberId;
	private String BankName;
	private boolean topup_falg=false;
	public PaymentGatewayCaller() {
	}

	public PaymentGatewayCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,String BankName) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.BankName=BankName;
	}

	public void run() {

		try {
			adjustmentSOAP = new PaymentGatewaySOAP(WSDL_TARGET_NAMESPACE,
					SOAP_URL, METHOD_NAME);
			
			adjustmentSOAP.setMemberId(getMemberId());
            if(Utils.is_atom){
                MakeMyPayment_AtomFragment.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
                MakeMyPayment_AtomFragment.adjTrackval = adjustmentSOAP.getServerMessage();
            }
            if(Utils.is_subpaisa){
                MakePaymentSubpaisaFragment.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
                MakePaymentSubpaisaFragment.adjTrackval = adjustmentSOAP.getServerMessage();
            }
			if(isTopup_falg()){
				/*if(!Utils.is_CCAvenue){
			MakeMyPaymentsTopUp.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
			MakeMyPaymentsTopUp.adjTrackval = adjustmentSOAP.getServerMessage();
				}
				else{
					MakeMyPaymentsTopUp_CCAvenue.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
					MakeMyPaymentsTopUp_CCAvenue.adjTrackval = adjustmentSOAP.getServerMessage();
				}*/

				if(Utils.is_atom){
					MakeMyPayment_AtomFragment.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
					MakeMyPayment_AtomFragment.adjTrackval = adjustmentSOAP.getServerMessage();
				}
                if(Utils.is_subpaisa){
                    MakePaymentSubpaisa.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
                    MakePaymentSubpaisa.adjTrackval = adjustmentSOAP.getServerMessage();
                }
			}
			else{
				if(Utils.is_atom){
					MakeMyPayment_AtomFragment.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
					MakeMyPayment_AtomFragment.adjTrackval = adjustmentSOAP.getServerMessage();
				}
                if(Utils.is_subpaisa){
                    MakePaymentSubpaisaFragment.rslt = adjustmentSOAP.CallAdjustmentAmountSOAP(BankName);
                    MakePaymentSubpaisaFragment.adjTrackval = adjustmentSOAP.getServerMessage();
                }
			}

		} catch (Exception e) {
			//ChangePackage.rslt = e.toString();
			if(isTopup_falg()){
				if(Utils.is_CCAvenue)
					MakeMyPaymentsTopUp_CCAvenue.rslt = e.toString();
				else
				MakeMyPaymentsTopUp.rslt = e.toString();
			}
			else{
				if(!Utils.is_CCAvenue){
				MakeMyPayments.rslt = e.toString();
				}
				else
				MakeMyPayments_CCAvenue.rslt = e.toString();
			}
		}
	}

	

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getTrackId() {
		return TrackId;
	}

	public void setTrackId(String trackId) {
		TrackId = trackId;
	}

	public boolean isTopup_falg() {
		return topup_falg;
	}

	public void setTopup_falg(boolean topup_falg) {
		this.topup_falg = topup_falg;
	}

	
	

}
