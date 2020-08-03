package com.example.smartpds.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;


public  class DownloadQrBAckground extends AsyncTask<Object, Object, Object> {
    private static Context mContext;
    private String requestUrl, imagename_;
    private Bitmap bitmap;
    private FileOutputStream fos;
    onDownloadListner onDownloadListner;


    public DownloadQrBAckground(String requestUrl , String _imagename_ , Context context , onDownloadListner onDownloadListner) {
        this.requestUrl = requestUrl;
        this.imagename_ = _imagename_;
        mContext = context;
        this.onDownloadListner =onDownloadListner;

    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        saveQr(bitmap, imagename_);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);

    }



    public static String getFilename(String folderPath, String imagename) {
//            File file = new File(Environment.getExternalStorageDirectory().getPath(), folderPath + "/images/sent");
        File file = new File(Environment.getExternalStorageDirectory().getPath(), folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + imagename + ".jpg");
        //System.currentTimeMillis()
        return uriSting;
    }



    String saveQr(Bitmap bitmap, String imagename_) {


        String stored = null;

        String filename = getFilename("RATIONKHATA" + "/" , imagename_);
        File file = new File(filename) ;
        if (file.exists())
            return stored ;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
                this.onDownloadListner.onDownload(filename);
            Toast.makeText(mContext , "QR Download at : " + filename , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }
}

