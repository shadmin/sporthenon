package com.sporthenon.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;

import com.sporthenon.android.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AndroidUtils {

    public static BitmapDrawable getImage(Activity activity, String img) {
        BitmapDrawable bd = null;
        HttpURLConnection connection = null;
        try {
            if (img != null && img.length() > 0) {
                File dir = activity.getApplicationContext().getDir("sh", Context.MODE_PRIVATE);
                File file = new File(dir, img.substring(img.lastIndexOf("/") + 1));
                if (file.exists())
                    bd = new BitmapDrawable(new FileInputStream(file));
                else if (!img.contains("noimage.png")) {
                    img = img.replaceAll("localhost", "10.0.2.2"); // TEST
                    connection = (HttpURLConnection) new URL(img) .openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bd = new BitmapDrawable(BitmapFactory.decodeStream(input));
                    connection.disconnect();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bd.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] arr = stream.toByteArray();
                    FileOutputStream output = new FileOutputStream(file);
                    output.write(arr);
                    output.close();
                }
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        return bd;
    }

    public static boolean notEmpty(String s) {
        return (s != null && s.trim().length() > 0);
    }

    public static LinearLayout.LayoutParams getImageSize(Context context, String url) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int n1 = Math.round(30 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        int n2 = Math.round(30 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        int n3 = Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return (notEmpty(url) && url.contains("/5-") ? new LinearLayout.LayoutParams(n1, n1) : new LinearLayout.LayoutParams(n2, n3));
    }

    public static int getMonthRes(int m) {
        switch (m) {
            case 1 : return R.string.month_1;
            case 2 : return R.string.month_2;
            case 3 : return R.string.month_3;
            case 4 : return R.string.month_4;
            case 5 : return R.string.month_5;
            case 6 : return R.string.month_6;
            case 7 : return R.string.month_7;
            case 8 : return R.string.month_8;
            case 9 : return R.string.month_9;
            case 10 : return R.string.month_10;
            case 11 : return R.string.month_11;
            case 12 : return R.string.month_12;
        }
        return 0;
    }

}