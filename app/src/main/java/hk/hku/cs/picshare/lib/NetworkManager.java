package hk.hku.cs.picshare.lib;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.hku.cs.picshare.account.AccountManager;
import hk.hku.cs.picshare.account.LoginRsp;
import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.list.PictureItem;
import hk.hku.cs.picshare.list.PictureListRsp;
import hk.hku.cs.picshare.post.ImageRsp;
import hk.hku.cs.picshare.post.PostReq;
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

    public void login(String email, String password, PicCallback<LoginRsp> callback) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("password", password);
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("user", userMap);
        Gson gson = new Gson();
        String reqJson = gson.toJson(reqMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), reqJson);
        Call<LoginRsp> call = mService.login(requestBody);
        call.enqueue(new Callback<LoginRsp>() {
            @Override
            public void onResponse(Call<LoginRsp> call, Response<LoginRsp> response) {
                if (response.code() != 200) {
                    callback.onFail("code:" + response.code());
                } else if(response.body().result.equalsIgnoreCase("success")) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFail(response.body().failReason);
                }
            }

            @Override
            public void onFailure(Call<LoginRsp> call, Throwable t) {
                callback.onFail(t.getMessage());
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
                } else if(response.body().result.equalsIgnoreCase("success")) {
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

    public void post(String content, String imageUrl, List<String> tagList, PicCallback<Rsp> callback) {
        PostReq req = new PostReq();
        req.user = AccountManager.getInstance().getUser();
        req.imageUrl = imageUrl;
        req.content = content;
        req.setTagList(tagList);
        Gson gson = new Gson();
        String bodyJson = gson.toJson(req);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), bodyJson);
        Call<Rsp> call = mService.post(requestBody);
        call.enqueue(new Callback<Rsp>() {
            @Override
            public void onResponse(Call<Rsp> call, Response<Rsp> response) {
                if (response.code() != 200) {
                    callback.onFail("code:" + response.code());
                } else if(response.body().result.equalsIgnoreCase("success")) {
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
        Map<String, Object> map = new HashMap<>();
        map.put("user", AccountManager.getInstance().getUser());
        Gson gson = new Gson();
        String bodyJson = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), bodyJson);
        Call<PictureListRsp> call = mService.getList(requestBody);
        call.enqueue(new Callback<PictureListRsp>() {
            @Override
            public void onResponse(Call<PictureListRsp> call, Response<PictureListRsp> response) {
                if (response.code() != 200) {
                    callback.onFail("code:" + response.code());
                } else if(response.body().result.equalsIgnoreCase("success")) {
                    callback.onSuccess(response.body().getItems());
                } else {
                    callback.onFail(response.body().failReason);
                }
            }

            @Override
            public void onFailure(Call<PictureListRsp> call, Throwable t) {
                Log.i("NetworkManager", "fail");
                callback.onFail(t.getMessage());
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
        @POST("login")
        Call<LoginRsp> login(@Body RequestBody body);
        @POST("post")
        Call<Rsp> post(@Body RequestBody body);
        @POST("getList")
        Call<PictureListRsp> getList(@Body RequestBody body);
    }

    public static class Rsp {
        public String result;
        public String failReason;
    }

    public interface PicCallback<T> {
        void onSuccess(T data);
        void onFail(String msg);
    }
}
