package hk.hku.cs.picshare.account;

import hk.hku.cs.picshare.lib.NetworkManager;

public class LoginRsp extends NetworkManager.Rsp {
    public User user;

    @Override
    public String toString() {
        return "LoginRsp{" +
                "user=" + user +
                ", result='" + result + '\'' +
                ", failReason='" + failReason + '\'' +
                '}';
    }
}
