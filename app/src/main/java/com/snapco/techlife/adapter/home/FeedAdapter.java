package com.snapco.techlife.adapter.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.snapco.techlife.R;
import com.snapco.techlife.data.model.home.Feed;


import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    List<Feed> modelList;

    public FeedAdapter(List<Feed> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.techlife_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        Feed model = modelList.get(position);

        int image = model.getImage();
        String username = model.getUsername();
        String  location = model.getLocation();
        String feed_img = model.getFeed_img();
        String status = model.getStatus();
        String comment_count = model.getComment_count();
        String  date = model.getDate();

        holder.setItems(image, username,location ,feed_img ,status,comment_count,date );

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView userProfile,userProfile2,imageView;
        TextView username_id,location_id,status_id,comment_count_id,date_id;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            userProfile = itemView.findViewById(R.id.userProfile_id);
            userProfile2 = itemView.findViewById(R.id.userProfile2_id);
            username_id = itemView.findViewById(R.id.username_id);
            location_id = itemView.findViewById(R.id.location_id);
            status_id = itemView.findViewById(R.id.status_id);
            comment_count_id = itemView.findViewById(R.id.comment_count_id);
            date_id = itemView.findViewById(R.id.date_id);


        }

        public void setItems(int image, String username, String location, String feed_img,
                             String status, String comment_count, String date) {

            // Load user profile images as circular images
            Glide.with(itemView.getContext())
                    .load(image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userProfile);

            Glide.with(itemView.getContext())
                    .load(image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userProfile2);

            // Load feed image
            Glide.with(itemView.getContext())
                    .load(feed_img)
                    .into(imageView);

            // Set text fields
            username_id.setText(username.trim());
            location_id.setText(location.trim());
            status_id.setText(status.trim());
            date_id.setText(date.trim());
            comment_count_id.setText("View all " + comment_count + " comments");
        }
    }
}
