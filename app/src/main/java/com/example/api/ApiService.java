package com.example.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("https://randomuser.me/api/")
    Call<JsonObject> getRandomData();
}
