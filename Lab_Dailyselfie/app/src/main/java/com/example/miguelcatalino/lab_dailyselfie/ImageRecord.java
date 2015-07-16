package com.example.miguelcatalino.lab_dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by miguelcatalino on 7/11/15.
 */
public class ImageRecord {
    private String name;
    private String path;
    private Bitmap selfie;
    private String time;

    public ImageRecord(String name, String path) {
        this.name = name.trim();
        this.path = path.trim();
        this.selfie = createImage();
        setTime(name);
    }

    public ImageRecord() {
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getSelfie() {
        return selfie;
    }

    public String getTime() {
        return time;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBitmap(Bitmap selfie) {
        this.selfie = selfie;
    }

    public void setTime(String time) {
        Pattern p = Pattern.compile("_.*_.*.jpg$");
        Matcher m = p.matcher(time);
        while (m.find()) {
            String[] t = m.group().replaceFirst("_", "").replaceFirst(".jpg", "").split("_");
            SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            android.text.format.DateFormat format = new android.text.format.DateFormat();
            this.time = String.format("Day: %s Time: %s", t[0], t[1]);
        }

    }

    private Bitmap createImage() {
        File image = new File(this.path);
        Bitmap result = null;

        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = calculateInSampleSize(bmOptions, 100, 80);
            bmOptions.inJustDecodeBounds = false;

            if (image.exists()) {
                result = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions),
                        36, 36, false);

            }

        } catch (Exception e) {
        }
        return result;

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
