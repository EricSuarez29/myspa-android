package net.kraken.myspa_android.ui.commons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Base64;

public class MySPACommons {
    public static final String MYSPA_SERVER = "http://192.168.100.5:8084/myspa-app";

    public static Bitmap fromBase64(String b64) throws Exception{
        byte[] decodeString = Base64.decode(b64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        return bitmap;
    }
}
