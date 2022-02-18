package com.github.CulinaryApp;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.github.CulinaryApp.APIClient;
import com.github.CulinaryApp.viewmodels.Categories;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIClientTest {
    private static HttpURLConnection connectionToEndpoint;


    @Test
    public void connectsSuccesfully() throws MalformedURLException, IOException {

        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(Categories.BASE_URL).newBuilder();
        String url = urlBuilder.build().toString();

        Request aRequest = new Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/categories.php")
                .build();

     //   try (Response response = httpClient.newCall(aRequest).execute())
       //     String responseBody = response.body().string();
         //   Boolean imConnected = httpClient.newCall(aRequest).
         //   assertTrue(imConnected);



}

    private boolean attemptWasSuccessful(int status) {
        return status > 199 || status < 300;
    }
}
