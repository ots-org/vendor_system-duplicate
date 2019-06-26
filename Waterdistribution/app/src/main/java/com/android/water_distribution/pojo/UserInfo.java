package com.android.water_distribution.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {

    String userId;
    String firstName;
    String lastName;
    String emailId;
    String usrStatus;
    String usrPassword;
    String address1;
    String address2;
    String pincode;
    String profilePic;
    String registrationId;
    String userRoleId;
    String status;
    String phonenumber;
    String mappedTo;
    String productId;
    String password;
    String deviceToken;
    String distributorId;
    String contactNo;

    protected UserInfo(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        emailId = in.readString();
        usrStatus = in.readString();
        usrPassword = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        pincode = in.readString();
        profilePic = in.readString();
        registrationId = in.readString();
        userRoleId = in.readString();
        status = in.readString();
        phonenumber = in.readString();
        mappedTo = in.readString();
        productId = in.readString();
        password = in.readString();
        deviceToken = in.readString();
        distributorId = in.readString();
        contactNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(emailId);
        dest.writeString(usrStatus);
        dest.writeString(usrPassword);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(pincode);
        dest.writeString(profilePic);
        dest.writeString(registrationId);
        dest.writeString(userRoleId);
        dest.writeString(status);
        dest.writeString(phonenumber);
        dest.writeString(mappedTo);
        dest.writeString(productId);
        dest.writeString(password);
        dest.writeString(deviceToken);
        dest.writeString(distributorId);
        dest.writeString(contactNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsrStatus() {
        return usrStatus;
    }

    public void setUsrStatus(String usrStatus) {
        this.usrStatus = usrStatus;
    }

    public String getUsrPassword() {
        return usrPassword;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getMappedTo() {
        return mappedTo;
    }

    public void setMappedTo(String mappedTo) {
        this.mappedTo = mappedTo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}