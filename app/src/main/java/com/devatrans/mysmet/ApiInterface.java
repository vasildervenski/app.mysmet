package com.devatrans.mysmet;

import com.google.firebase.firestore.auth.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {


    //@POST("client-token")
    //Call<com.devatrans.mysmet.User> sendToken(@Query("user_id") Integer user_id, @Query("token") String token);

    @Multipart
    @POST("client-token")
    Call<ResponseBody> sendToken(@PartMap() Map<String, RequestBody> partMap);


}
