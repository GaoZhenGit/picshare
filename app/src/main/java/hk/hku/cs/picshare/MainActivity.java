package hk.hku.cs.picshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.yolov5ncnn.YoloV5Ncnn;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YoloV5Ncnn ncnn = new YoloV5Ncnn();
        boolean ret = ncnn.Init(getAssets());
        Toast.makeText(this, "init:" + ret, Toast.LENGTH_SHORT).show();
    }
}