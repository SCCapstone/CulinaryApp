package com.github.CulinaryApp;

import static org.junit.Assert.assertTrue;

import com.github.CulinaryApp.APIClient;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIClientTest {
    private static HttpURLConnection connectionToEndpoint;


    @Test
    public void connectsSuccesfully() throws MalformedURLException, IOException {


        String apiEndpoint = "https://www.themealdb.com/api/json/v1/1/categories.php";
        URL url = new URL(apiEndpoint);
        connectionToEndpoint = (HttpURLConnection) url.openConnection();

        // Setup HTTP Request
        connectionToEndpoint.setRequestMethod("GET");
        connectionToEndpoint.setConnectTimeout(5000); // after 5000 ms - 5 secs, if conn hasn't succeeded, halt attempt
        connectionToEndpoint.setReadTimeout(5000);

        // ensure response code is in the 200's
        int status = connectionToEndpoint.getResponseCode();
        assertTrue(attemptWasSuccessful(status));
}

    private boolean attemptWasSuccessful(int status) {
        return status > 199 || status < 300;
    }
}
