package hk.hku.cs.picshare.list;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;
import java.util.List;

import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.lib.PicImageView;
import hk.hku.cs.picshare.post.ImagePreviewActivity;

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.PictureListViewHolder> {
    private Fragment fragment;
    private final List<PictureItem> mDatas = new ArrayList<>();
    public PictureListAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PictureListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureListViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureListViewHolder holder, int position) {
        holder.onBindViewHolder(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(List<PictureItem> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    protected class PictureListViewHolder extends RecyclerView.ViewHolder {
        private PicImageView mAvatar;
        private TextView mName;
        private TextView mContent;
        private LabelsView mLabelsView;
        private PicImageView mImage;
        public PictureListViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list_item, parent, false));
            mAvatar = itemView.findViewById(R.id.list_item_avatar);
            mName = itemView.findViewById(R.id.list_item_name);
            mContent = itemView.findViewById(R.id.list_item_content);
            mLabelsView = itemView.findViewById(R.id.list_item_labels);
            mImage = itemView.findViewById(R.id.list_item_image);
        }

        public void onBindViewHolder(PictureItem dataItem) {
            mAvatar.load(dataItem.avatar);
            mName.setText(dataItem.userName);
            mContent.setText(dataItem.content);
            mLabelsView.setLabels(dataItem.tags);
            mImage.load(dataItem.image);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(fragment.getContext(), ImagePreviewActivity.class);
                    intent.putExtra(ImagePreviewActivity.PREVIEW_IMAGE_URL, dataItem.image);
                    fragment.startActivity(intent);
                }
            });
        }
    }
}
