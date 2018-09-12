package com.devmasterteam.photicker.views.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.views.views.MainActivity;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SocialUtil {

    private static final String HASHTAG = "#";
    private static String TAG = MainActivity.class.getName();
    static String authority = null;

    public static void shareWithWhatsapp(MainActivity mainActivity, ConstraintLayout constraintLayout, View view) {

        PackageManager packageManager = mainActivity.getPackageManager();

        try {
            packageManager.getPackageInfo("com.whatsapp", 0);
            String filename = "temp_" + System.currentTimeMillis() + ".jpg";

            try {
                constraintLayout.setDrawingCacheEnabled(true);
                constraintLayout.buildDrawingCache(true);

                File imageFile = new File(Environment.getExternalStorageDirectory(), filename);
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                constraintLayout.getDrawingCache(true).compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                constraintLayout.setDrawingCacheEnabled(false);
                constraintLayout.destroyDrawingCache();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, HASHTAG);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
                    sendIntent.setType("image/jpg");
                    sendIntent.setPackage("com.whatsapp");
                    view.getContext().startActivity(Intent.createChooser(sendIntent, mainActivity.getString(R.string.share_image)));

                } catch (Exception e) {
                    Toast.makeText(mainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                Toast.makeText(mainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(mainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(mainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void shareWithTwitterOrInstagram(MainActivity activity, View view, ConstraintLayout constraintLayout, Context context, String setPackage) {

        //checar se tem o app desejado instalado
        Boolean appInstalled = findApplication(activity, setPackage);
        if (appInstalled) {
            //capturar a tela
            String filePath = ImageUtil.drawBitmap(constraintLayout);
            Log.i(TAG, "shareWithTwitter: " + filePath);

            //enviar tela via app
            sendIntent(activity, view, filePath, setPackage);
        }
    }

    public static void shareWithFacebook(MainActivity activity, ConstraintLayout constraintLayout){

        Bitmap bitmap = BitmapFactory.decodeFile(ImageUtil.drawBitmap(constraintLayout));
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder().setHashtag(HASHTAG).build())
                .build();
        new ShareDialog(activity).show(content);


    }

    private static Boolean findApplication(MainActivity activity, String packageSearch){

        PackageManager packageManager = activity.getPackageManager();
        try {
            packageManager.getPackageInfo(packageSearch , 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void sendIntent(MainActivity activity, View view, String fileName, String packageSearch) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, HASHTAG);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileName));
        sendIntent.setType("image/jpg");
        sendIntent.setPackage(packageSearch);
        view.getContext().startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.share_image)));
    }

}
