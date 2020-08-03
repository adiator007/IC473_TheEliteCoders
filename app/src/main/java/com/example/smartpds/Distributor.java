package com.example.smartpds;

public class Distributor {
    String fname;
    String lname;
    String shopname;
    String email;
    String address;
    String city;
    String state;

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    String shopImage;
    int pincode;
    int walletAmmount;
    String kycDone;
    String accountStatus;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getWalletAmmount() {
        return walletAmmount;
    }

    public void setWalletAmmount(int walletAmmount) {
        this.walletAmmount = walletAmmount;
    }

    public String isKycDone() {
        return kycDone;
    }

    public void setKycDone(String kycDone) {
        this.kycDone = kycDone;
    }

    public String isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        Long mobileno=Long.parseLong(mobile);
        this.mobile = mobileno;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }


    long mobile;

    public Distributor() {
    }
}
