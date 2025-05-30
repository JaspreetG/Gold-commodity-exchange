package io.goldexchange.auth_service.dto;

public class VerifyTotpRequest {
    private String phoneNumber;
    private String totp;
    private String deviceFingerprint;

    public VerifyTotpRequest() {}

    public VerifyTotpRequest(String phoneNumber, String totp, String deviceFingerprint) {
        this.phoneNumber = phoneNumber;
        this.totp = totp;
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTotp() {
        return totp;
    }

    public void setTotp(String totp) {
        this.totp = totp;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }
}
