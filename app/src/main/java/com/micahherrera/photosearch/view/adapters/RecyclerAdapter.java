package com.micahherrera.photosearch.view.adapters;

import android.content.pm.ActivityInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.micahherrera.photosearch.R;
import com.micahherrera.photosearch.model.Photo_;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyRecyclerViewHolder>  implements ItemTouchHelperAdapter{
    LayoutInflater inflater;
    List<Photo_> photoList;
    int width;
    private PhotoClickListener toplistener;

    public RecyclerAdapter(List<Photo_> photoList, int width, PhotoClickListener listener){
        this.toplistener = listener;
        this.photoList = photoList;
        this.width = width;
    }

    @Override
    public RecyclerAdapter.MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.item_photo_recycler, parent, false);
        RecyclerAdapter.MyRecyclerViewHolder holder = new RecyclerAdapter.MyRecyclerViewHolder(v, toplistener);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyRecyclerViewHolder holder, int position) {
        Photo_ photo = photoList.get(position);
        holder.putThePhoto(Integer.toString(photo.getFarm()), photo.getServer(),
                photo.getId(), photo.getSecret());
        holder.photo = photo;
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        photoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(photoList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(photoList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        int widthSize;
        private PhotoClickListener mlistener;
        Photo_ photo;

        public MyRecyclerViewHolder(View itemView, PhotoClickListener listener) {
            super(itemView);

            this.mlistener = listener;
            imageView = (ImageView)itemView.findViewById(R.id.photoView);
            if(itemView.getContext().getResources().getConfiguration().orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                widthSize = width/2;
                imageView.setLayoutParams(new CardView.LayoutParams(widthSize, widthSize));
            } else {
                widthSize = width/6;
                imageView.setLayoutParams(new CardView.LayoutParams(widthSize, widthSize));
            }

            itemView.setOnClickListener(this);

        }

        public void putThePhoto(String farm, String server, String photoId, String secret){
            String url = String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                    farm,
                    server,
                    photoId,
                    secret);

            Picasso.with(itemView.getContext())
                    .load(url)
                    .centerCrop()
                    .resize(widthSize-1, widthSize-1)
                    .into(imageView);
        }


        @Override
        public void onClick(View v) {
                mlistener.onPhotoClick(photo);
    }



}

    public interface PhotoClickListener {
        void onPhotoClick(Photo_ photo);
    }
}
