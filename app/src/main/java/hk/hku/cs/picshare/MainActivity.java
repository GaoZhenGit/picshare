package hk.hku.cs.picshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.yolov5ncnn.YoloV5Ncnn;

import hk.hku.cs.picshare.post.PostActivity;

public class MainActivity extends Activity {
    private View mPublishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        YoloV5Ncnn ncnn = new YoloV5Ncnn();
        boolean ret = ncnn.Init(getAssets());
        Toast.makeText(this, "init:" + ret, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mPublishBtn = findViewById(R.id.btn_publich);
        mPublishBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
        });
    }
}