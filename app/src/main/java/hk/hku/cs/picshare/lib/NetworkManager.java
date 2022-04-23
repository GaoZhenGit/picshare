package hk.hku.cs.picshare.lib;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.hku.cs.picshare.account.AccountManager;
import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.list.PictureItem;
import hk.hku.cs.picshare.post.ImageRsp;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class NetworkManager {
    public static final String baseUrl = "http://192.168.0.105:8080/";
    private static class InstanceHolder {
        private static NetworkManager instance = new NetworkManager();
    }
    private NetworkService mService;

    public static NetworkManager getInstance() {
        return InstanceHolder.instance;
    }

    public NetworkManager() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(NetworkService.class);
    }

    public void uploadImage(File imageFile, PicCallback<ImageRsp> callback) {
        String fileName = imageFile.getName();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("fileName", fileName, requestFile);
        mService.imageUpload(part).enqueue(new Callback<ImageRsp>() {
            @Override
            public void onResponse(Call<ImageRsp> call, Response<ImageRsp> response) {
                if (response.body().result.equals("success")) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFail(response.body().failReason);
                }
            }

            @Override
            public void onFailure(Call<ImageRsp> call, Throwable t) {
                callback.onFail(t.getMessage());
            }
        });
    }

    public void login(String email, String password, PicCallback<User> callback) {
        //todo mock
        ThreadManager.getInstance().submit(() -> {
            try {
                Thread.sleep(2000);
                if(email.equals("1")&&password.equals("1")) {
                    User user = new User();
                    user.name = email;
                    user.uid = String.valueOf(email.hashCode());
                    user.email = email;
                    user.token = user.uid;
                    callback.onSuccess(user);
                } else {
                    callback.onFail("wrong password!");
                }
            } catch (Exception e) {

            }
        });
    }

    public void signin(String name,String email,String psw,String avatar, PicCallback<Rsp> callback) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("avatar", avatar);
        userMap.put("password", psw);
        userMap.put("desc", "");
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("user", userMap);
        Gson gson = new Gson();
        String reqJson = gson.toJson(reqMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), reqJson);
        Call<Rsp> call = mService.register(requestBody);
        call.enqueue(new Callback<Rsp>() {
            @Override
            public void onResponse(Call<Rsp> call, Response<Rsp> response) {
                if (response.code() != 200) {
                    callback.onFail("code:" + response.code());
                } else if(response.body().result.equals("success")) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFail(response.body().failReason);
                }
            }

            @Override
            public void onFailure(Call<Rsp> call, Throwable t) {
                callback.onFail(t.getMessage());
            }
        });
    }

    public void logout() {
        String uid = AccountManager.getInstance().getUid();

    }

    public void testNetwork(Map<String, String> param, PicCallback<User> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("system", "Android");
        map.put("phoneBrand", Build.BRAND);
        map.put("modelNum", Build.MODEL);
        Gson gson = new Gson();
        String bodyJson = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), bodyJson);
        Call<User> call = mService.test(requestBody);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("NetworkManager", "success " + response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("NetworkManager", "fail");
                callback.onFail(t.getMessage());
            }
        });
    }

    public void getPictureList(String uid, PicCallback<List<PictureItem>> callback) {
        //todo mock
        ThreadManager.getInstance().submit(() -> {
            try {
                Thread.sleep(1000);
                List<PictureItem> mockData = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    PictureItem item = new PictureItem();
                    item.image = "https://i0.hdslb.com/bfs/article/72585c1d8dce989dab9953035e7f92c7c8a46aed.jpg@942w_531h_progressive.webp";
                    item.userName = "User" + i;
                    item.content = "Content " + i + "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
                    item.uid = "uid" + i;
                    for (int j = 0; j < i + 2; j++) {
                        item.tags.add("Tag " + j);
                    }
                    item.avatar = "https://c-ssl.duitang.com/uploads/item/202004/17/20200417125937_bllwk.thumb.1000_0.jpg";
                    mockData.add(item);
                }
                callback.onSuccess(mockData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public interface NetworkService {
        @POST("volley/person_object.json")
        Call<User> test(@Body RequestBody body);
        @POST("imageUpload")
        @Multipart
        Call<ImageRsp> imageUpload(@Part MultipartBody.Part imgs);
        @POST("register")
        Call<Rsp> register(@Body RequestBody body);
    }

    public static class Rsp {
        String result;
        String failReason;
    }

    public interface PicCallback<T> {
        void onSuccess(T data);
        void onFail(String msg);
    }
}
