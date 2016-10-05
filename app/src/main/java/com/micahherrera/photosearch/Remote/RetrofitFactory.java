package com.micahherrera.photosearch.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by micahherrera on 9/30/16.
 */

public class RetrofitFactory {
    public static String BASE_URL = "https://api.flickr.com/services/rest/";
    public static FlickrService service;

    public static FlickrService getInstance(){
        if(service == null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(FlickrService.class);
            return service;
        } else {
            return service;
        }
    }
}
