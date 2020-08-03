package com.example.smartpds.model;

public class Cart {

   protected String DistributerId;
  protected   boolean Cartstatus;

    public Cart(String distributerId, boolean cartStatus) {
        DistributerId = distributerId;
        Cartstatus = cartStatus;
    }

    public Cart() {
    }

    public String getDistributerId() {
        return DistributerId;
    }

    public void setDistributerId(String distributerId) {
        DistributerId = distributerId;
    }

    public boolean getCartstatus() {
        return Cartstatus;
    }

    public void setCartstatus(boolean cartStatus) {
        Cartstatus = cartStatus;
    }
}
