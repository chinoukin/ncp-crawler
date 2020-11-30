package com.wisea;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class PostmanHttpTest {

    public static void main(String[] args) throws UnirestException, IOException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://corona-virus-stats.herokuapp.com/api/v1/cases/general-stats")
                .asString();

        System.out.println(response.getBody());


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://corona-virus-stats.herokuapp.com/api/v1/cases/general-stats")
                .method("GET", null)
                .build();
        Response response1 = client.newCall(request).execute();
        System.out.println(response1.body().string());
    }
}
