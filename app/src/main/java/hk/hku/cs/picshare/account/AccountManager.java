package hk.hku.cs.picshare.account;

import hk.hku.cs.picshare.lib.NetworkManager;

public class AccountManager {
    private static class InstanceHolder {
        private static AccountManager instance = new AccountManager();
    }

    public static AccountManager getInstance() {
        return InstanceHolder.instance;
    }

    private String uid;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
