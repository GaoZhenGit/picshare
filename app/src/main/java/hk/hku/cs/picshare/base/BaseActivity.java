package hk.hku.cs.picshare.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import hk.hku.cs.picshare.R;

public abstract class BaseActivity extends FragmentActivity {
    protected View mBackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mBackBtn = findViewById(R.id.btn_back);
        if (mBackBtn != null) {
            mBackBtn.setOnClickListener(v -> finish());
        }
    }

    protected abstract int getLayoutResId();
}
