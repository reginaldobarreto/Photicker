package com.devmasterteam.photicker.views.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.views.adapter.PhotickerAdapter;
import com.devmasterteam.photicker.views.utils.EventType;
import com.devmasterteam.photicker.views.utils.ImageUtil;
import com.devmasterteam.photicker.views.utils.SocialUtil;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int CAMERA_REQUEST = 34;
    @BindView(R.id.reciclerView) RecyclerView recyclerView;
    @BindView(R.id.constraintLayoutCenter) ConstraintLayout constraintLayoutCenter;
    @BindView(R.id.linearLayoutBotton) LinearLayout linearLayoutBotton;
    @BindView(R.id.linearLayoutExtra) LinearLayout linearLayoutExtra;
    @BindView(R.id.imageViewTakePicture) ImageView imageViewTakePicture;
    @BindView(R.id.imageViewResult) ImageView imageViewResult;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.imageViewZoomIn) ImageView imageViewZoomIn;
    @BindView(R.id.imageViewZoomOut) ImageView imageViewZoomOut;
    @BindView(R.id.imageViewRotateLeft) ImageView imageViewRotateLeft;
    @BindView(R.id.imageViewRotateRight) ImageView imageViewRotateRight;
    @BindView(R.id.imageViewConfirm) ImageView imageViewConfirm;
    @BindView(R.id.imageViewRemove) ImageView imageViewRemove;
    @BindView(R.id.imageViewInstagram) ImageView imageViewInstagram;
    @BindView(R.id.imageViewTwitter) ImageView imageViewTwitter;
    @BindView(R.id.imageViewFacebook) ImageView imageViewFacebook;
    @BindView(R.id.imageViewWhatsApp) ImageView imageViewWhatsapp;
    private List<Integer> list = ImageUtil.getImage();
    private ImageView imageSelected;
    private Boolean controlPainelImage = false;
    private Boolean runnableStart = false;
    private Handler handler = new Handler();
    private EventType mEventType;
    private String absoluteImageLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Listener();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        PhotickerAdapter adapter = new PhotickerAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemAdapter(new PhotickerAdapter.OnItemAdapter() {
            @Override
            public void itemClick(int position) {

                final ImageView imageView = new ImageView(MainActivity.this);
                imageSelected = imageView;

                constraintLayoutCenter.addView(imageView);
                Glide
                        .with(MainActivity.this)
                        .asBitmap()
                        .load(list.get(position))
                        .into(imageView);

                controlPainelImage();

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        float  x, y;
                        imageSelected = imageView;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                controlPainelImage();
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int coords[] = {0,0};
                                constraintLayoutCenter.getLocationOnScreen(coords);
                                x = event.getRawX() - imageSelected.getWidth()/2;
                                y = event.getRawY() - (coords[1]+imageSelected.getHeight()/2);
                                imageView.setY(y);
                                imageView.setX(x);
                                break;
                        }

                        return true;
                    }
                });

            }
        });

    }

    private void Listener() {
        imageViewZoomIn.setOnClickListener(this);
        imageViewZoomOut.setOnClickListener(this);
        imageViewRotateLeft.setOnClickListener(this);
        imageViewRotateRight.setOnClickListener(this);
        imageViewConfirm.setOnClickListener(this);
        imageViewRemove.setOnClickListener(this);
        imageViewInstagram.setOnClickListener(this);
        imageViewTwitter.setOnClickListener(this);
        imageViewFacebook.setOnClickListener(this);
        imageViewWhatsapp.setOnClickListener(this);
        imageViewTakePicture.setOnClickListener(this);

        imageViewZoomIn.setOnLongClickListener(this);
        imageViewZoomOut.setOnLongClickListener(this);
        imageViewRotateLeft.setOnLongClickListener(this);
        imageViewRotateRight.setOnLongClickListener(this);

        imageViewZoomIn.setOnTouchListener(this);
        imageViewZoomOut.setOnTouchListener(this);
        imageViewRotateLeft.setOnTouchListener(this);
        imageViewRotateRight.setOnTouchListener(this);

    }

    private void controlPainelImage() {

        if(!controlPainelImage){
            linearLayoutBotton.setVisibility(View.GONE);
            linearLayoutExtra.setVisibility(View.VISIBLE);
            controlPainelImage = true;
        }else{
            linearLayoutBotton.setVisibility(View.VISIBLE);
            linearLayoutExtra.setVisibility(View.GONE);
            controlPainelImage = false;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageViewZoomIn:
                ImageUtil.handleZoomIn(imageSelected);
                break;

            case R.id.imageViewZoomOut:
                ImageUtil.handleZoomOut(imageSelected);
                break;

            case R.id.imageViewRotateLeft:
                ImageUtil.handleRotateLeft(imageSelected);
                break;

            case R.id.imageViewRotateRight:
                ImageUtil.handleRotateRight(imageSelected);
                break;

            case R.id.imageViewConfirm:
                controlPainelImage();
                break;

            case R.id.imageViewRemove:
                constraintLayoutCenter.removeView(imageSelected);
                break;

            case R.id.imageViewInstagram:
                SocialUtil.shareWithTwitterOrInstagram(this, v ,constraintLayoutCenter,this,"com.instagram.android");
                break;

            case R.id.imageViewTwitter:
                SocialUtil.shareWithTwitterOrInstagram(this,v,constraintLayoutCenter,this,"com.twitter.android");
                break;

            case R.id.imageViewFacebook:
                SocialUtil.shareWithFacebook(this,constraintLayoutCenter);
                break;

            case R.id.imageViewWhatsApp:
                SocialUtil.shareWithWhatsapp(MainActivity.this, constraintLayoutCenter, v);
                break;

            case R.id.imageViewTakePicture:
                takePicture();
                break;
        }
    }

    private void takePicture() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);

        }else{
            Intent cameraIntent = new Intent();
            cameraIntent.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = ImageUtil.createFile();
                absoluteImageLocation = photoFile.getAbsolutePath();
                Log.i(TAG, "takePicture: " + photoFile.getPath());

            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                String authority = getBaseContext().getPackageName() + ".fileprovider";
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }else{
                String authority = getBaseContext().getPackageName() + ".fileprovider";
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(this,authority,photoFile));
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            ImageUtil.handlePicture(this,absoluteImageLocation,imageViewResult);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int id = v.getId();

        if(     id == imageViewZoomIn.getId() ||
                id == imageViewZoomOut.getId() ||
                id == imageViewRotateLeft.getId() ||
                id == imageViewRotateRight.getId() ){

            if(event.getAction() == MotionEvent.ACTION_UP && runnableStart){
                Log.i(TAG,"OnTouch ACTION_UP");
                runnableStart = false;
                mEventType = null;
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {

        int id = v.getId();
        if(id == imageViewZoomOut.getId()) mEventType = EventType.ZOOM_OUT;
        if(id == imageViewZoomIn.getId()) mEventType = EventType.ZOOM_IN;
        if(id == imageViewRotateLeft.getId()) mEventType = EventType.ROTATE_LEFT;
        if(id == imageViewRotateRight.getId()) mEventType = EventType.ROTATE_RIGHT;
        runnableStart = true;
        new ThreadButton().run();
        Log.i(TAG,"OnLongClick " + id);

        return false;
    }

    private class ThreadButton implements Runnable {

        @Override
        public void run() {
            if(runnableStart){
                handler.postDelayed(new ThreadButton(),50);

                if(mEventType != null){
                    switch (mEventType) {
                        case ZOOM_OUT:
                            ImageUtil.handleZoomOut(imageSelected);
                            break;
                        case ZOOM_IN:
                            ImageUtil.handleZoomIn(imageSelected);
                            break;
                        case ROTATE_LEFT:
                            ImageUtil.handleRotateLeft(imageSelected);
                            break;
                        case ROTATE_RIGHT:
                            ImageUtil.handleRotateRight(imageSelected);
                            break;
                    }
                }

            }
        }
    }

}