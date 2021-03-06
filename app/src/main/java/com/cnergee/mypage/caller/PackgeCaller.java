
package com.cnergee.mypage.caller;

import android.util.Log;

import com.cnergee.fragments.PackgedetailFragment;
import com.cnergee.mypage.PackgedetailActivity;
import com.cnergee.mypage.SOAP.PackgeSOAP;

public class PackgeCaller extends Thread {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private long MemberId;
	public String username;
	public String UserLoginName,password;
	PackgeSOAP packageSOAP;
	int Ispid,sid;
	private String ConnectionTypeId,AreaCode,SubscriberID;
	boolean IsPostPaid;

	public PackgeCaller() {
	}

	public PackgeCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
						String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {

		try {
			packageSOAP = new PackgeSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			packageSOAP.setMemberId(getMemberId());
			packageSOAP.setConnectionTypeId(getConnectionTypeId());
			packageSOAP.setAreaCode(getAreaCode());
			packageSOAP.setIspid(getIspid());
			packageSOAP.setPostPaid(isPostPaid());
			packageSOAP.setSid(getSid());
			packageSOAP.setUserLoginName(getUserLoginName());
			PackgedetailFragment.rslt = packageSOAP.CallPackageSOAP();
			PackgedetailFragment.strXML = packageSOAP.getXMLData();
            Log.e("Packages:","2:-"+PackgedetailActivity.rslt);
            Log.e("Packages:","3:-"+PackgedetailActivity.strXML);

		} catch (Exception e) {
			PackgedetailFragment.rslt = e.toString();
            Log.e("Packages:",":-"+e.getMessage());
            Log.e("Packages:","1:-"+e.toString());

		}
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}

	public String getUserLoginName() {
		return UserLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
	}

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public int getIspid() {
		return Ispid;
	}

	public void setIspid(int ispid) {
		Ispid = ispid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public boolean isPostPaid() {
		return IsPostPaid;
	}

	public void setPostPaid(boolean postPaid) {
		IsPostPaid = postPaid;
	}
	/*private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	private long MemberId;
	public String username;
	public String password;
	PackgeSOAP packageSOAP;
	String ConnectionTypeId="",AreaCode="";
	private String SubscriberID;

	public PackgeCaller() {
	}

	public PackgeCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
						String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public void run() {

		try {
			packageSOAP = new PackgeSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);
			packageSOAP.setMemberId(getMemberId());
			packageSOAP.setConnectionTypeId(getConnectionTypeId());
			packageSOAP.setAreaCode(getAreaCode());
			PackgedetailActivity.rslt = packageSOAP.CallPackageSOAP();
			PackgedetailActivity.strXML = packageSOAP.getXMLData();

		} catch (Exception e) {
			PackgedetailActivity.rslt = e.toString();
		}
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
*/



}
