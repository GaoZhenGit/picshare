package hk.hku.cs.picshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.lib.ThreadManager;
import hk.hku.cs.picshare.post.ImagePreviewActivity;
import hk.hku.cs.picshare.post.ImageRsp;

public class SigninActivity extends Activity implements View.OnClickListener {
    EditText SignName,SignNumber,SignPsw,SignPsw2;
    private static final String Tag = "SigninActivity";
    Button SignConfirm;
    NetworkManager network;
    private PicImageView SignPic;
    private static final int SELECT_IMAGE = 1;
    private static final int PREVIEW_IMAGE = 2;
    private static final int REQUIRED_SIZE = 280;
    private Uri mBitmapUri;
    private Bitmap SignBitmap;
    private File mCacheImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        SignName=findViewById(R.id.edit_sign_name);
        SignNumber=findViewById(R.id.edit_sign_phone);
        SignPsw=findViewById(R.id.edit_sign_psw);
        SignPsw2=findViewById(R.id.edit_sign_psw2);
        SignConfirm=findViewById(R.id.btn_sign_confirm);
        SignPic = findViewById(R.id.img_sign_userpic);
        network = NetworkManager.getInstance();

        SignConfirm.setOnClickListener(this);
        SignPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_sign_confirm)
        {
            String strName,strPhone,strPsw,strPsw2;
            strName=SignName.getText().toString();
            strPhone=SignNumber.getText().toString();
            strPsw=SignPsw.getText().toString();
            strPsw2=SignPsw2.getText().toString();
            //这里需要写一个传出图片的函数-->SignBitmap
            if(strName.equals("")||strPhone.equals("")||strPsw.equals("")||strPsw2.equals(""))
            {
                AlertDialog waitDialog = new AlertDialog.Builder(SigninActivity.this)
                        .setTitle("Error")
                        .setMessage("Please input all the information required")
                        .show();
            }
            else
            {
                if(strPsw.equals(strPsw2))
                {
                    NetworkManager.getInstance().uploadImage(mCacheImage, new NetworkManager.PicCallback<ImageRsp>() {
                        @Override
                        public void onSuccess(ImageRsp data) {
                            Log.i(Tag, "upload image success");
                            register(strName, strPhone, strPsw, NetworkManager.baseUrl + data.urlSuffix);
                        }

                        @Override
                        public void onFail(String msg) {
                            Log.i(Tag, "upload image fail:" + msg);
                            Toast.makeText(getBaseContext(), "upload image fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    AlertDialog waitDialog = new AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Please check again your password.")
                            .show();
                }
            }

        }
        else if(view.getId()==R.id.img_sign_userpic)
        {
            if (SignBitmap == null) {
                fetchPictureFromGallery();
            } else {
                Intent intent = new Intent(getBaseContext(), ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.PREVIEW_IMAGE_DATA, mBitmapUri);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        }

    }

    private void fetchPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, SELECT_IMAGE);
    }

    private void cancel() {
        SignPic.setImageResource(R.drawable.add);
        SignBitmap = null;
        mBitmapUri = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK && null != data) {
                mBitmapUri = data.getData();
                try {
                    SignBitmap = decodeUri(mBitmapUri);
                    SignPic.setImageBitmap(SignBitmap);
                    saveImageToDiskCache();
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

    private void saveImageToDiskCache() {
        try {
            mCacheImage = new File(getCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(mCacheImage);
            SignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i(Tag, "saveImageToDiskCache success");
        } catch (Exception e) {
            e.printStackTrace();
            mCacheImage = null;
        }
    }

    private void register(String strName, String strPhone, String strPsw, String avatar) {
        Log.i(Tag, "start register:" + strName + "," + strPhone + "," + strPsw + "," + avatar);
        network.signin(strName, strPhone, strPsw, avatar, new NetworkManager.PicCallback<NetworkManager.Rsp>() {
            @Override
            public void onSuccess(NetworkManager.Rsp data) {
                Log.i(Tag, "register success");
                ThreadManager.getInstance().runOnUiThread(() -> {
                    Toast.makeText(getBaseContext(), "sign in success", Toast.LENGTH_SHORT).show();
                    finish();
                });

            }

            @Override
            public void onFail(String msg) {
                Log.i(Tag, "register fail:" + msg);
                ThreadManager.getInstance().runOnUiThread(() -> {
                    AlertDialog waitDialog = new AlertDialog.Builder(SigninActivity.this)
                            .setTitle("Error")
                            .setMessage(msg)
                            .show();
                });
            }
        });
    }
}
