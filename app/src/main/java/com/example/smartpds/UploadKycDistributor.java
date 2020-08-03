package com.example.smartpds;

public class UploadKycDistributor {
    public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadKycDistributor() {
    }

    public UploadKycDistributor(String name, String url) {
        this.name = name;
        this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
