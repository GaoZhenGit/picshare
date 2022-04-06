package hk.hku.cs.picshare.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import hk.hku.cs.picshare.LoginActivity;
import hk.hku.cs.picshare.PicApplication;
import hk.hku.cs.picshare.lib.NetworkManager;

public class AccountManager {
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String KEY_UID = "KEY_UID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_TOKEN = "KEY_TOKEN";

    private static class InstanceHolder {
        private static AccountManager instance = new AccountManager();
    }

    public static AccountManager getInstance() {
        return InstanceHolder.instance;
    }

    public boolean isLogin() {
        if (!TextUtils.isEmpty(getToken()) && !TextUtils.isEmpty(getUserName()) && !TextUtils.isEmpty(getUid())) {
            return true;
        } else {
            return false;
        }
    }

    public String getToken() {
        return getPreference().getString(KEY_TOKEN, "");
    }

    public String getUserName() {
        return getPreference().getString(KEY_USER_NAME, "");
    }

    public String getUid() {
        return getPreference().getString(KEY_UID, "");
    }

    private SharedPreferences getPreference() {
        return PicApplication.getInstance().getSharedPreferences(KEY_ACCOUNT, Context.MODE_PRIVATE);
    }

    public void setLocalUserInfo(String uid, String userName, String token) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void clearUserInfo() {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(KEY_UID, "");
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_TOKEN, "");
        editor.commit();
    }
}
