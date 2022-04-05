package hk.hku.cs.picshare.account;

import androidx.annotation.NonNull;

public class User {
    public String name;
    public String email;
    public String uid;
    public String token;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
