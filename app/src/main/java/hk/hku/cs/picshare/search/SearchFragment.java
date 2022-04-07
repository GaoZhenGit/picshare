package hk.hku.cs.picshare.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.account.AccountManager;
import hk.hku.cs.picshare.base.BaseFragment;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.lib.ThreadManager;
import hk.hku.cs.picshare.list.PictureItem;
import hk.hku.cs.picshare.list.PictureListAdapter;
import hk.hku.cs.picshare.post.PostActivity;

public class SearchFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initView();
        loadData();
        return mRoot;
    }

    private void initView() {
        mRecyclerView = mRoot.findViewById(R.id.search_list_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new SearchListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        NetworkManager.getInstance().getPictureList(AccountManager.getInstance().getUid(), new NetworkManager.PicCallback<List<PictureItem>>() {
            @Override
            public void onSuccess(List<PictureItem> data) {
                ThreadManager.getInstance().runOnUiThread(() -> mAdapter.setData(data));
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
