package com.github.CulinaryApp;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIClient {


    /* 2 WAYS TO SEND HTTP GET & PARSING JSON RESPONSE INTO JAVA OBJECT */


    /**
     * METHOD 1: using java.netHttpURLConnection, response is an input stream
     *
     *   Step 1: define connection
     *   Step 2: define URL using endpoint (for categories in this case), handle MalFormedURLException
     *   Step 4: open connection to the API endpoint, handle IOException
     *   Step 5: Setup the request
     *   Step 6: Get Response code to see if connection was succesful
     *   Step 7: Define an BufferedReader wrapped inside InputStreamReader
     *   Step 8: build response
     *   Step 9: close connection
     */

    private static HttpURLConnection connectionToEndpoint;

    public static void main(String[] args) {


        String apiEndpoint = "https://www.themealdb.com/api/json/v1/1/categories.php";
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer(); // appends each line read from buffer to build response

        try {
            URL url = new URL(apiEndpoint);
            connectionToEndpoint = (HttpURLConnection) url.openConnection();

            // Setup HTTP Request
            connectionToEndpoint.setRequestMethod("GET");
            connectionToEndpoint.setConnectTimeout(5000); // after 5000 ms - 5 secs, if conn hasn't succeeded, halt attempt
            connectionToEndpoint.setReadTimeout(5000);

            // get response codes...
            // ... read response & build content based on response code
            int status = connectionToEndpoint.getResponseCode();
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connectionToEndpoint.getErrorStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connectionToEndpoint.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            System.out.println(responseContent.toString());
        } catch (MalformedURLException e) { // thrown when trying to connect to url but program can't parse url correctly
            e.printStackTrace();
            System.out.println("\nProgram cannot parse URL for some reason.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connectionToEndpoint.disconnect();
        }

    }
}
