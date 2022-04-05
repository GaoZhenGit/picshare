package hk.hku.cs.picshare.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;
import java.util.List;

import hk.hku.cs.picshare.R;
import hk.hku.cs.picshare.lib.PicImageView;

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.PictureListViewHolder> {
    private final List<PictureItem> mDatas = new ArrayList<>();

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

    protected static class PictureListViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
