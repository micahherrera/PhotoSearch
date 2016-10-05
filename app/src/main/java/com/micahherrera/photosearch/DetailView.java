package com.micahherrera.photosearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.micahherrera.photosearch.model.Photo_;
import com.squareup.picasso.Picasso;

public class DetailView extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Photo_ photo = getIntent().getBundleExtra("bundle").getParcelable("photo");


        imageView = (ImageView)findViewById(R.id.photoDetail);
        textView = (TextView)findViewById(R.id.photoText);

        textView.setText(photo.getTitle().toString());
        Log.d("TAG", "onCreate: "+photo.getTitle().toString());

        String url = String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg",
                Integer.toString(photo.getFarm()),
                photo.getServer(),
                photo.getId(),
                photo.getSecret());

        Log.d("TAG", "onCreate: "+ url);

        Picasso.with(this).load(url).into(imageView);


    }
}
