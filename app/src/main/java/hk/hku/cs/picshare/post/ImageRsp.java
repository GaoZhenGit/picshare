package hk.hku.cs.picshare.post;

public class ImageRsp {
    String result;
    String failReason;
    String urlSuffix;

    @Override
    public String toString() {
        return "ImageRsp{" +
                "result='" + result + '\'' +
                ", failReason='" + failReason + '\'' +
                ", urlSuffix='" + urlSuffix + '\'' +
                '}';
    }
}
