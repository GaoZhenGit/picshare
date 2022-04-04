package hk.hku.cs.picshare.post;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tencent.yolov5ncnn.YoloV5Ncnn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.lib.ThreadManager;

public class PostActivity extends Activity {
    private static final String Tag = "PostActivity";
    private static final int SELECT_IMAGE = 1;
    private static final int REQUIRED_SIZE = 280;

    private View mBackBtn;
    private View mPublishBtn;
    private PicImageView mImageFirst;

    private YoloV5Ncnn ncnn;
    private Bitmap mBitmapFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
        initYolo();
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(v -> finish());
        mPublishBtn = findViewById(R.id.btn_post);
        mPublishBtn.setOnClickListener(v -> publish());
        mImageFirst = findViewById(R.id.post_img_first);
        mImageFirst.setOnClickListener(v -> fetchPictureFromGallery());
    }

    private void initYolo() {
        ThreadManager.getInstance().submit(() -> {
            ncnn = new YoloV5Ncnn();
            boolean ret = ncnn.Init(getAssets());
            Log.i(Tag, "ncnn init finish:" + ret);
        });
    }

    private void publish() {

    }

    private void fetchPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                if (requestCode == SELECT_IMAGE) {
                    mBitmapFirst = decodeUri(selectedImage);
//                    yourSelectedImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    mImageFirst.setImageBitmap(mBitmapFirst);
                    ThreadManager.getInstance().submit(() -> detect());
                }
            } catch (Exception e) {
                Log.e(Tag, e.getMessage(), e);
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

        // Rotate according to EXIF
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(selectedImage));
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            Log.e(Tag, e.getMessage(), e);
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void detect() {
        Bitmap bitmap = mBitmapFirst.copy(Bitmap.Config.ARGB_8888, true);
        YoloV5Ncnn.Obj[] objs = ncnn.Detect(bitmap, false);
        List<String> labels = Arrays.stream(objs).map(obj -> obj.label).distinct().collect(Collectors.toList());
        Log.i(Tag, "yolo detect:" + labels);
    }
}