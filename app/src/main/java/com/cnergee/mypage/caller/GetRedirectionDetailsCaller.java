package com.cnergee.mypage.caller;

import com.cnergee.fragments.MakeMyPayment_AtomFragment;
import com.cnergee.mypage.MakePaymentSubpaisa;
import com.cnergee.mypage.SOAP.GetRedirectionDetailsSOAP;
import com.cnergee.mypage.utils.Utils;

public class GetRedirectionDetailsCaller extends Thread{
    public GetRedirectionDetailsSOAP getRedirectionDetailsSOAP;

    private String WSDL_TARGET_NAMESPACE;
    private String SOAP_URL;
    private String METHOD_NAME;





    public GetRedirectionDetailsCaller(){}

    public GetRedirectionDetailsCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL, String METHOD_NAME) {

        this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
        this.SOAP_URL = SOAP_URL;
        this.METHOD_NAME = METHOD_NAME;

    }

    public void run() {

        try {
            getRedirectionDetailsSOAP = new GetRedirectionDetailsSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
            if(Utils.is_subpaisa){
                MakePaymentSubpaisa.rslt = getRedirectionDetailsSOAP.CallComplaintNoSOAP();
                MakePaymentSubpaisa.MapRedirectionDetails = getRedirectionDetailsSOAP.getMapRedictionDetail();
            }else if(Utils.is_atom){
                MakeMyPayment_AtomFragment.rslt = getRedirectionDetailsSOAP.CallComplaintNoSOAP();
                MakeMyPayment_AtomFragment.MapRedirectionDetails = getRedirectionDetailsSOAP.getMapRedictionDetail();

            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    public GetRedirectionDetailsSOAP getBeforepaymentsoap() {
        return getRedirectionDetailsSOAP;
    }

    public void setRedirectionDetailsSOAP(GetRedirectionDetailsSOAP getRedirectionDetailsSOAP) {
        this.getRedirectionDetailsSOAP = getRedirectionDetailsSOAP;
    }




}
