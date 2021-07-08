package com.demo.softexperttest;

import android.graphics.Movie;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<HashMap<String, Object>> getCars(@Url String url);
}
