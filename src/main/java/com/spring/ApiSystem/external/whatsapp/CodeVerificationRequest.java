package com.spring.ApiSystem.external.whatsapp;

public class CodeVerificationRequest {
    private String userIdentifier;
    private String verificationCode;


    public CodeVerificationRequest() {
    }


    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
