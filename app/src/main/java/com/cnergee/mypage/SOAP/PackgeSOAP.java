/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */

package com.cnergee.mypage.SOAP;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.PackageList;
import com.cnergee.mypage.utils.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Map;

public class PackgeSOAP<E> {
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private String SubscriberID;
	private Map<String, PackageList> mapPackageDetails;
	private String   AreaCodeFilter;
	private long MemberId;
	private ArrayList<PackageList> packagelist = new ArrayList<PackageList>();
	String UserLoginName,AreaCode,ConnectionTypeId;
	int Ispid,sid;
	boolean IsPostPaid;



	public PackgeSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
					  String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallPackageSOAP() throws SocketException,SocketTimeoutException,Exception {



		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(getMemberId());
		pi.setType(String.class);
		request.addProperty(pi);


		pi = new PropertyInfo();
		pi.setName("UserLoginName");
		pi.setValue(getUserLoginName());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("Ispid");
		pi.setValue(getIspid());
		pi.setType(int.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AreaCode");
		pi.setValue(getAreaCode());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("ConnectionTypeId");
		pi.setValue(getConnectionTypeId());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("IsPostPaid");
		pi.setValue(isPostPaid());
		pi.setType(boolean.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("sid");
		pi.setValue(getSid());
		pi.setType(int.class);
		request.addProperty(pi);


		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<", request.toString());
		Utils.log("Package list request",":"+request.toString());


		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;


		try {

			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log(this.getClass().getSimpleName()+" Request:",""+androidHttpTransport.requestDump);
			Utils.log(this.getClass().getSimpleName()+" Response:",""+androidHttpTransport.responseDump);
			String ss = androidHttpTransport.responseDump;

			setXMLData(ss);

			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	private String strXML;

	public void setXMLData(String strXML) {
		this.strXML = strXML;
	}

	public String getXMLData() {
		return strXML;
	}

	public long getMemberId() {
		return MemberId;
	}

	public void setMemberId(long memberId) {
		MemberId = memberId;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	public String getConnectionTypeId() {
		return ConnectionTypeId;
	}

	public void setConnectionTypeId(String connectionTypeId) {
		ConnectionTypeId = connectionTypeId;
	}

	public String getUserLoginName() {
		return UserLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		UserLoginName = userLoginName;
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
/*
	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;

	private String METHOD_NAME;
	private String SubscriberID;
	private Map<String, PackageList> mapPackageDetails;
	private String  AreaCode, AreaCodeFilter;
	private long MemberId;
	private ArrayList<PackageList> packagelist = new ArrayList<PackageList>();
	String ConnectionTypeId="";




	public PackgeSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
					  String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}

	public String CallPackageSOAP() throws SocketException,SocketTimeoutException,Exception {



		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(getMemberId());
		pi.setType(String.class);
		request.addProperty(pi);


		pi = new PropertyInfo();
		pi.setName("ConnectionTypeId");
		pi.setValue(getConnectionTypeId());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("AreaCode");
		pi.setValue(getAreaCode());
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		//Log.i(">>>>>Request<<<<<", request.toString());
		Utils.log("Package list request",":"+request.toString());


		envelope.encodingStyle = SoapSerializationEnvelope.ENC;

		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;


		try {

			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log(this.getClass().getSimpleName()+" Request:",""+androidHttpTransport.requestDump);
			Utils.log(this.getClass().getSimpleName()+" Response:",""+androidHttpTransport.responseDump);
			String ss = androidHttpTransport.responseDump;

			setXMLData(ss);

			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	private String strXML;

	public void setXMLData(String strXML) {
		this.strXML = strXML;
	}

	public String getXMLData() {
		return strXML;
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
