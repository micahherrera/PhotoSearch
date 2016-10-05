package com.micahherrera.photosearch.Remote;

import com.micahherrera.photosearch.model.Photo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by micahherrera on 9/29/16.
 */

public interface FlickrService {

    @GET("?method=flickr.photos.search&api_key=05a745d64a8596d7fbeebb7bf3f95c40&format=json&nojsoncallback=1")
    Call<Photo> getSearchPhotos(@Query("tags") String tag);
}
