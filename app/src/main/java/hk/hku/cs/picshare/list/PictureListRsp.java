package hk.hku.cs.picshare.list;

import java.util.ArrayList;
import java.util.List;

import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.lib.NetworkManager;
import hk.hku.cs.picshare.post.PostReq;

public class PictureListRsp extends NetworkManager.Rsp {
    public List<FormRspItem> form;
    public static class FormRspItem {
        public String id;
        public String content;
        public String imageUrl;
        public User user;
        public List<PostReq.Tag> tags = new ArrayList<>();
    }

    public List<PictureItem> getItems() {
        List<PictureItem> pictureItems = new ArrayList<>();
        for (FormRspItem formRspItem : form) {
            PictureItem item = new PictureItem();
            item.image = formRspItem.imageUrl;
            item.avatar = formRspItem.user.avatar;
            item.itemId = formRspItem.id;
            item.content = formRspItem.content;
            item.userName = formRspItem.user.name;
            item.tags = new ArrayList<>();
            for (PostReq.Tag tag : formRspItem.tags){
                item.tags.add(tag.name);
            }
            item.uid = formRspItem.user.id;
            pictureItems.add(item);
        }
        return pictureItems;
    }
}
