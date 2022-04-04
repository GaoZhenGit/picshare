package hk.hku.cs.picshare.post;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.donkingliang.labels.LabelsView;
import com.tencent.yolov5ncnn.YoloV5Ncnn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hk.hku.cs.picshare.base.BaseActivity;
import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.lib.ThreadManager;

public class PostActivity extends BaseActivity {
    private static final String Tag = "PostActivity";
    private static final int SELECT_IMAGE = 1;
    private static final int PREVIEW_IMAGE = 2;
    private static final int REQUIRED_SIZE = 280;

    private View mPublishBtn;
    private PicImageView mImageFirst;
    private LabelsView mLabelsView;

    private YoloV5Ncnn ncnn;
    private Bitmap mBitmapFirst;
    private Uri mBitmapUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initYolo();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_post;
    }

    private void initView() {
        mPublishBtn = findViewById(R.id.btn_post);
        mPublishBtn.setOnClickListener(v -> publish());
        mImageFirst = findViewById(R.id.post_img_first);
        mImageFirst.setOnClickListener(v -> {
            if (mBitmapFirst == null) {
                fetchPictureFromGallery();
            } else {
                Intent intent = new Intent(getBaseContext(), ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.PREVIEW_IMAGE_DATA, mBitmapUri);
                startActivityForResult(intent, PREVIEW_IMAGE);
            }
        });

        mLabelsView = findViewById(R.id.labels);
        mLabelsView.clearAllSelect();
        mLabelsView.setSelectType(LabelsView.SelectType.MULTI);
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
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK && null != data) {
                mBitmapUri = data.getData();
                try {
                    mBitmapFirst = decodeUri(mBitmapUri);
                    mImageFirst.setImageBitmap(mBitmapFirst);
                    mLabelsView.setLabels(null);
                    ThreadManager.getInstance().submit(this::detect);
                } catch (Exception e) {
                    Log.e(Tag, e.getMessage(), e);
                }
            }
        } else if (requestCode == PREVIEW_IMAGE) {
            if (resultCode == ImagePreviewActivity.RESULT_CODE_DELETE) {
                cancel();
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
        int[] selectIndex = new int[labels.size()];
        for (int i = 0; i < selectIndex.length; i++) {
            selectIndex[i] = i;
        }
        Log.i(Tag, "yolo detect:" + labels);
        ThreadManager.getInstance().runOnUiThread(() -> {
            mLabelsView.setLabels(labels);
            if (labels.size() > 0) {
                mLabelsView.setSelects(selectIndex);
            }
        });
    }

    private void cancel() {
        mImageFirst.setImageResource(R.drawable.add);
        mBitmapFirst = null;
        mBitmapUri = null;
        mLabelsView.setLabels(null);
    }
}