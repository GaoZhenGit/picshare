package hk.hku.cs.picshare.post;

import java.util.ArrayList;
import java.util.List;

import hk.hku.cs.picshare.account.User;

public class PostReq {
    public User user;
    public String content;
    public String imageUrl;
    public List<Tag> tags = new ArrayList<>();

    public void setTags(List<String> tags) {
        tags.clear();
        for (String t : tags) {
            Tag tag = new Tag();
            tag.name = t;
            this.tags.add(tag);
        }
    }
    public static class Tag {
        public String name;
    }
}
