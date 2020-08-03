package com.example.smartpds.benificiary;

import android.net.Uri;

public interface OnUploadListner {

    public  void onComplete(Uri uploadeduri);
    public  void onFailed();
}
