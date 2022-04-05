package hk.hku.cs.picshare.lib;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.hku.cs.picshare.account.User;
import hk.hku.cs.picshare.list.PictureItem;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class NetworkManager {
    private static final String baseUrl = "http://api.androidhive.info/";
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

    public boolean login(String email, String password) {

        if(email.equals("1")&&password.equals("1"))
            return true;
        else
            return false;

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
                Thread.sleep(5000);
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
    }

    public interface PicCallback<T> {
        void onSuccess(T data);
        void onFail(String msg);
    }
}
