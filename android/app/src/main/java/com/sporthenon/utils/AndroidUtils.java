package com.sporthenon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AndroidUtils {

    public static BitmapDrawable getImage(Activity activity, String alias, String img, Integer id) {
        BitmapDrawable bd = null;
        HttpURLConnection connection = null;
        try {
            File dir = activity.getApplicationContext().getDir("sh", Context.MODE_PRIVATE);
            File file = new File(dir, alias + id + ".png");
            if (file.exists())
                bd = new BitmapDrawable(new FileInputStream(file));
            else if (img != null && img.length() > 0) {
            Log.e("img", "img-" + img);
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
        catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
        }
        return bd;
    }

}