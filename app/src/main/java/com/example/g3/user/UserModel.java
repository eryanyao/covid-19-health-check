package com.example.g3.user;

import java.sql.Timestamp;

public class UserModel {
    String userCountry,userEmail,userFirstName,userId,userAffiliation,
            userLastName,userPhoneNumber,userBarCode,userPhoto;
    Timestamp userDateCreatedAt;

    public UserModel(String userCountry, String userEmail, String userFirstName,
                     String userId, String userAffiliation, String userLastName,
                     String userPhoneNumber, String userBarCode, String userPhoto,
                     Timestamp userDateCreatedAt) {
        this.userCountry = userCountry;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userId = userId;
        this.userAffiliation = userAffiliation;
        this.userLastName = userLastName;
        this.userPhoneNumber = userPhoneNumber;
        this.userBarCode = userBarCode;
        this.userPhoto = userPhoto;
        this.userDateCreatedAt = userDateCreatedAt;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAffiliation() {
        return userAffiliation;
    }

    public void setUserAffiliation(String userAffiliation) {
        this.userAffiliation = userAffiliation;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserBarCode() {
        return userBarCode;
    }

    public void setUserBarCode(String userBarCode) {
        this.userBarCode = userBarCode;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Timestamp getUserDateCreatedAt() {
        return userDateCreatedAt;
    }

    public void setUserDateCreatedAt(Timestamp userDateCreatedAt) {
        this.userDateCreatedAt = userDateCreatedAt;
    }

    @Override public String toString() {
        return "UserModel{" +
                "userCountry='" + userCountry + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userId='" + userId + '\'' +
                ", userAffiliation='" + userAffiliation + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userBarCode='" + userBarCode + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userDateCreatedAt=" + userDateCreatedAt +
                '}';
    }
}
