package hk.hku.cs.picshare.lib;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class PicImageView extends androidx.appcompat.widget.AppCompatImageView {
    public PicImageView(Context context) {
        super(context);
    }

    public PicImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void load(String url) {
        Context context = getContext();
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(this);
    }
}
