package com.micahherrera.photosearch;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.micahherrera.photosearch.Remote.FlickrService;
import com.micahherrera.photosearch.Remote.RetrofitFactory;
import com.micahherrera.photosearch.model.Photo;
import com.micahherrera.photosearch.model.Photo_;
import com.micahherrera.photosearch.view.adapters.RecyclerAdapter;
import com.micahherrera.photosearch.view.helpers.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerAdapter.PhotoClickListener{
    public static final String PHOTO_LIST = "photoList";
    public static String TAG = "tag";

    public FlickrService service;
    public ArrayList<Photo_> photoList;
    public RecyclerAdapter adapter;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public LinearLayout rl;
    public CardView cardView;
    public EditText editText;
    public Button searchButton;
    public ActionBar ab;
    public RecyclerAdapter.PhotoClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener = this;
        ab = getSupportActionBar();
        rl = (LinearLayout)findViewById(R.id.activity_main);
        service = RetrofitFactory.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        cardView = (CardView)findViewById(R.id.card_view);
        editText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button)findViewById(R.id.searchButton);

        searchButton.setOnClickListener(MainActivity.this);

        if(savedInstanceState!=null){
            ab.setTitle(savedInstanceState.getString("ab"));
            photoList = savedInstanceState.getParcelableArrayList(PHOTO_LIST);
            loadPhotos(photoList);
        }

    }

    private void getPhotos(String searchText){
        service.getSearchPhotos(searchText).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                photoList = new ArrayList<Photo_>(response.body().getPhotos().getPhoto());
                loadPhotos(photoList);

            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.d(TAG, "onFailure: fail!!");
            }
        });

    }

    public void loadPhotos(List<Photo_> photoLister){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        adapter = new RecyclerAdapter(photoLister, metrics.widthPixels, listener);
        recyclerView.setAdapter(adapter);

        if(getResources().getConfiguration().orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(MainActivity.this, 2);
        } else {
            layoutManager = new GridLayoutManager(MainActivity.this, 6);
        }
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onClick(View view) {
        getPhotos(editText.getText().toString());
        ab.setTitle(editText.getText().toString());
        editText.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelableArrayList(PHOTO_LIST, photoList);
        savedInstanceState.putString("ab", ab.getTitle().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPhotoClick(Photo_ photo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("photo", photo);
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
