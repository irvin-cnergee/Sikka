package com.cnergee.mypage.caller;

import com.cnergee.fragments.ComplaintsFragment;
import com.cnergee.mypage.Complaints;
import com.cnergee.mypage.SOAP.ReleaseMacSOAP;
import com.cnergee.mypage.SelfResolution;

import java.net.SocketException;
import java.net.SocketTimeoutException;


public class ReleaseMacCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private String MemberId,MemberLoginId,MobileNumber;
	boolean is_24_ol;
	
	ReleaseMacSOAP releaseMacSOAP;
	
	public ReleaseMacCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME,boolean is_24_ol) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
		this.is_24_ol= is_24_ol;
	}
public void run() {		
		try{
			releaseMacSOAP = new ReleaseMacSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL,METHOD_NAME);
			if(is_24_ol){
				ComplaintsFragment.rslt = releaseMacSOAP.releaseMac(MemberId,MemberLoginId,MobileNumber);
                ComplaintsFragment.statusResponseForMac=releaseMacSOAP.getResponse();
			}
			else{
			SelfResolution.rslt = releaseMacSOAP.releaseMac(MemberId,MemberLoginId,MobileNumber);
			SelfResolution.statusResponseForMac=releaseMacSOAP.getResponse();
			}
			//Utils.log("Complaint status caller",""+Complaints.statusResponse);
			//Utils.log("Complaint status Result",""+Complaints.statusRslt);
		}catch (SocketException e) {
			e.printStackTrace();
			SelfResolution.rslt = "Internet connection not available!!";
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			SelfResolution.rslt = "Internet connection not available!!";
		}catch (Exception e) {
			SelfResolution.rslt = "Invalid web-service response.<br>"+e.toString();
		}
		
	}
	public void setMemberId(String MemberId){
	this.MemberId=MemberId;
	}
	
	public void setMemberLoginId(String MemberLoginId){
		this.MemberLoginId=MemberLoginId;
	}
	public void setMobileNumber(String MobileNumber){
		this.MobileNumber=MobileNumber;
	}
	
}
