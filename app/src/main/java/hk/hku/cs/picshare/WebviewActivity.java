package hk.hku.cs.picshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import hk.hku.cs.picshare.base.BaseActivity;

public class WebviewActivity extends BaseActivity {
    public static final String PARAM_URL = "PARAM_URL";
    private WebView mWebview;
    private TextView mTitle;
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebview = findViewById(R.id.webview);
        mTitle = findViewById(R.id.webview_title);
        mWebview.setWebViewClient(webViewClient);
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title != null) {
                    mTitle.setText(title);
                }
            }
        });
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        String url = intent.getStringExtra(PARAM_URL);
        mWebview.loadUrl(url);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }
}