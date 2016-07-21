package cn.edu.hit.itsmobile.service;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.hit.itsmobile.model.Customer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class FileUploadManager {

    public static final String ENDPOINT = "http://192.168.191.1:9123";//192.168.1.118:8389/192.168.191.1:8080
    private static String TAG = FileUploadManager.class.getSimpleName();

    public interface FileUploadService {

        @Multipart
        @POST("/customer")
        Call<String> uploadMessage(@Part("description") String description);

        @Multipart
        @POST("/upload")
//        @Headers({"Accept: application/json", "User-Agent: " + "GPCS-ANDROID-V1"})
        Call<String> uploadImage(@Part("description") String description, @PartMap
        Map<String, RequestBody> imgs1);

        @GET("/customer")
        Call<String> getData(@Part("description") String description);
    }

    private static final Retrofit sRetrofit = new Retrofit .Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final FileUploadService apiManager = sRetrofit.create(FileUploadService.class);

    public static void uploadToServer(String customerMessage){
        Call<String> stringCall = apiManager.uploadMessage(customerMessage);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public static void uploadMany(ArrayList<String> paths, String desp, String fileImage){
        Map<String,RequestBody> photos = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//设置日期格式
        String date = df.format(new Date());
        if (paths.size()>0) {
            for (int i=0;i<paths.size() - 1;i++) {
                photos.put("photos\"; filename=\""+fileImage+i+".jpg",  RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i))));
            }
        }
        Call<String> stringCall = apiManager.uploadImage(desp, photos);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    public static void pushData(final String message){
        new Thread(){
            @Override
            public void run() {
                super.run();
//                uploadToServer(message);
                ArrayList<String> paths = new ArrayList<String>();
                uploadMany(paths,message,"");
            }
        }.start();
    }
    public static String getData(String jobStr){
        Call<String> stringCall = apiManager.getData(jobStr);
        final String[] dataStr = {""};
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
                dataStr[0] = response.toString();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
        return dataStr[0];
    }
}
