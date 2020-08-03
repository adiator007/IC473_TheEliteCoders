package com.example.smartpds.model;

public class Benificiary {
    String bname;
    String bdob;
    String photouri;
    String adharuri;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status;

    public Benificiary() {
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBdob() {
        return bdob;
    }

    public void setBdob(String bdob) {
        this.bdob = bdob;
    }

    public String getPhotouri() {
        return photouri;
    }

    public void setPhotouri(String photouri) {
        this.photouri = photouri;
    }

    public String getAdharuri() {
        return adharuri;
    }

    public void setAdharuri(String adharuri) {
        this.adharuri = adharuri;
    }
}
