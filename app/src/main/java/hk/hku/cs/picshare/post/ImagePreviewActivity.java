package hk.hku.cs.picshare.post;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

import hk.hku.cs.picshare.R;

public class ImagePreviewActivity extends Activity {
    public static final String PREVIEW_IMAGE_DATA = "PREVIEW_IMAGE_DATA";
    public static final int RESULT_CODE_DELETE = 234;
    private static final String Tag = "ImagePreviewActivity";
    private ImageView imageView;
    private View mDeleteBtn;
    private View mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        initView();
        initData();
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        imageView = findViewById(R.id.preview_img);
        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(v -> finish());
        mDeleteBtn = findViewById(R.id.btn_preview_delete);
        mDeleteBtn.setOnClickListener(v -> {
            setResult(RESULT_CODE_DELETE);
            finish();
        });
    }

    private void initData() {
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(PREVIEW_IMAGE_DATA);
        try {
            Bitmap bitmap = decodeUri(uri);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
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
}