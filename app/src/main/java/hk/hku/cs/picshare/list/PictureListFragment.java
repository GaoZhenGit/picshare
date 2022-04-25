package hk.hku.cs.picshare.list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.account.AccountManager;
import hk.hku.cs.picshare.base.BaseFragment;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.lib.ThreadManager;
import hk.hku.cs.picshare.post.PostActivity;

public class PictureListFragment extends BaseFragment {
    private static final String Tag = "PictureListFragment";
    private View mStartPostBtn;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private PictureListAdapter mAdapter;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picture_list;
    }

    @Override
    public Drawable getOnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_pic_list_select);
    }

    @Override
    public Drawable getOnUnSelectTabIcon() {
        return PicApplication.getInstance().getDrawable(R.drawable.tab_pic_list_unselect);
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
        mStartPostBtn = mRoot.findViewById(R.id.btn_start_post);
        mStartPostBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });
        mRecyclerView = mRoot.findViewById(R.id.pic_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PictureListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout = mRoot.findViewById(R.id.swipe_layout);
        mRefreshLayout.setOnRefreshListener(() -> loadData());
    }

    private void loadData() {
        Log.i(Tag, "loadData");
        NetworkManager.getInstance().getPictureList(AccountManager.getInstance().getUid(), new NetworkManager.PicCallback<List<PictureItem>>() {
            @Override
            public void onSuccess(List<PictureItem> data) {
                Log.i(Tag, "loadData success");
                ThreadManager.getInstance().runOnUiThread(() -> {
                    mAdapter.setData(data);
                    mRefreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onFail(String msg) {
                Log.i(Tag, "loadData fail:" + msg);
                ThreadManager.getInstance().runOnUiThread(() -> {
                    mRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
}
