package hk.hku.cs.picshare.search;

import android.graphics.drawable.Drawable;

import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.base.BaseFragment;

public class SearchFragment extends BaseFragment {
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search;
    }

    @Override
    public Drawable getOnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_search_select);
    }

    @Override
    public Drawable getOnUnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_search_unselect);
    }
}
