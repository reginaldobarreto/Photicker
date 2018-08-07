package com.devmasterteam.photicker.views.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.devmasterteam.photicker.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getName();

    public static List<Integer> getImage() {

        List<Integer> images = new ArrayList<>();

        Field[] draws = R.drawable.class.getFields();
        for (Field f : draws) {
            try {
                if (f.getName().startsWith("st_")) {
                    images.add(f.getInt(f.getName()));
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao listar imagens: " + e.toString());
            }
        }
        return images;
    }

    public static void handleZoomIn(ImageView imageSelected) {


        if (imageSelected.getHeight() > 1100) {
            Log.i(TAG, "ZoomIn return " + imageSelected.getHeight());
            return;
        } else {
            ViewGroup.LayoutParams params = imageSelected.getLayoutParams();
            params.height = (int) (imageSelected.getHeight() + (imageSelected.getHeight() * 0.05));
            params.width = (int) (imageSelected.getWidth() + (imageSelected.getWidth() * 0.05));
            imageSelected.setLayoutParams(params);

            Log.i(TAG, "ZoomIn " + imageSelected.getHeight());
        }
    }

    public static void handleZoomOut(ImageView imageSelected) {

        if (imageSelected.getHeight() < 150) {
            Log.i(TAG, "ZoomOut return " + imageSelected.getHeight());
            return;
        } else {
            ViewGroup.LayoutParams params = imageSelected.getLayoutParams();
            params.height = (int) (imageSelected.getHeight() - (imageSelected.getHeight() * 0.05));
            params.width = (int) (imageSelected.getWidth() - (imageSelected.getWidth() * 0.05));
            imageSelected.setLayoutParams(params);

            Log.i(TAG, "ZoomOut " + imageSelected.getHeight());
        }
    }

    public static void handleRotateLeft(ImageView imageSelected) {

        imageSelected.setRotation(imageSelected.getRotation() - 5);
        Log.i(TAG, "RotateLeft " + imageSelected.getRotation());

    }

    public static void handleRotateRight(ImageView imageSelected) {

        imageSelected.setRotation(imageSelected.getRotation() + 5);
        Log.i(TAG, "RotateRight " + imageSelected.getRotation());
    }

    public static void handlePicture(Context context, String uriFile, ImageView imageView) {

        int height = imageView.getHeight();
        int width = imageView.getWidth();

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uriFile, bitmapOptions);

        int photoHeight = bitmapOptions.outHeight;
        int photoWidht = bitmapOptions.outWidth;

        int scalePhoto = Math.min(photoHeight / height, photoWidht / width);

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scalePhoto;

        Bitmap bitmap = BitmapFactory.decodeFile(uriFile);
        Bitmap newBitmap = rotatePictureCall(bitmap, uriFile);

        Glide.with(context).asBitmap().load(newBitmap).into(imageView);

    }

    public static Bitmap rotatePictureCall(Bitmap original, String uriFile) {
        ExifInterface exifInterface;

        try {
            exifInterface = new ExifInterface(uriFile);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotatePicture(original, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotatePicture(original, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotatePicture(original, 270);
                default:
                    return original;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return original;
    }

    private static Bitmap rotatePicture(Bitmap original, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap rotatePicture = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, false);
        original.recycle();
        return rotatePicture;
    }

    public static File createFile() throws IOException {

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File filePicture = File.createTempFile("photicker", ".png", storageDirectory);

        Log.i(TAG, "createFile Temp: " + filePicture.getAbsolutePath());

        return filePicture;
    }

    public static String drawBitmap(ConstraintLayout layout) {

        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache(true);
        File file = null;
        try {
            file = createFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            layout.getDrawingCache(true).compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pathAbsolute = file.getAbsolutePath();
        layout.setDrawingCacheEnabled(false);
        layout.destroyDrawingCache();
        return pathAbsolute;
    }

}