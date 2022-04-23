package hk.hku.cs.picshare.post;

public class ImageRsp {
    public String result;
    public String failReason;
    public String urlSuffix;

    @Override
    public String toString() {
        return "ImageRsp{" +
                "result='" + result + '\'' +
                ", failReason='" + failReason + '\'' +
                ", urlSuffix='" + urlSuffix + '\'' +
                '}';
    }
}
