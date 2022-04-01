package hk.hku.cs.picshare.account;

import androidx.annotation.NonNull;

public class User {
    public String name;
    public String email;

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
