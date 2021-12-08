package com.cnergee.mypage.obj;

public class ConfirmationObj {
	private String MemberLoginId;
	private String MobileNoprimary;
	private String CustomerName;
	private String InstallationAddress;
	private String MemberId;
	private String showLogo,dob;
	private String CustomerCareNumber;
	private String EmailId;
	private String IsPostPaid;
	private String ISPId;

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIsPostPaid() {
		return IsPostPaid;
	}

	public void setIsPostPaid(String isPostPaid) {
		IsPostPaid = isPostPaid;
	}

	public String getISPId() {
		return ISPId;
	}

	public void setISPId(String ISPId) {
		this.ISPId = ISPId;
	}

	public String getMemberLoginId() {
		return MemberLoginId;
	}
	public void setMemberLoginId(String memberLoginId) {
		MemberLoginId = memberLoginId;
	}
	public String getMobileNoprimary() {
		return MobileNoprimary;
	}
	public void setMobileNoprimary(String mobileNoprimary) {
		MobileNoprimary = mobileNoprimary;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getInstallationAddress() {
		return InstallationAddress;
	}
	public void setInstallationAddress(String installationAddress) {
		InstallationAddress = installationAddress;
	}
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}
	public String getShowLogo() {
		return showLogo;
	}
	public void setShowLogo(String showLogo) {
		this.showLogo = showLogo;
	}
	
	public String getCustomerCareNumber() {
		return CustomerCareNumber;
	}
	public void setCustomerCareNumber(String customerCareNumber) {
		CustomerCareNumber = customerCareNumber;
	}
	public String getEmailId() {
		return EmailId;
	}
	public void setEmailId(String emailId) {
		EmailId = emailId;
	}
}
