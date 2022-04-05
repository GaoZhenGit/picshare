package hk.hku.cs.picshare.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.R;

public class PicImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Drawable mDefaultDrawable;
    private boolean mIsSquare = false;
    public PicImageView(Context context) {
        super(context);
        initView(null);
    }

    public PicImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PicImageView);
            int defaultImageId = ta.getResourceId(R.styleable.PicImageView_defaultImageRes, 0);
            if (defaultImageId > 0) {
                mDefaultDrawable = getContext().getDrawable(defaultImageId);
                Glide.with(getContext()).load(mDefaultDrawable).into(this);
            }
            mIsSquare = ta.getBoolean(R.styleable.PicImageView_square, false);
            ta.recycle();
            postInvalidate();
        }
    }

    public void load(String url) {
        Context context = getContext();
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(mDefaultDrawable)
                .into(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsSquare) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
