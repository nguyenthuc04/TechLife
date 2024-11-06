package com.snapco.techlife.adapter.home;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.snapco.techlife.R;
import com.snapco.techlife.data.model.home.Feed;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    List<Feed> modelList;
    private OnPostActionListener onPostActionListener;

    public FeedAdapter(List<Feed> modelList, OnPostActionListener onPostActionListener) {
        this.modelList = modelList;
        this.onPostActionListener = onPostActionListener;
    }

    public void updatePost(Feed updatedPost, int position) {
        modelList.set(position, updatedPost);
        notifyItemChanged(position);
    }

    public void deletePost(int position) {
        modelList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnPostActionListener {
        void onPostLongClicked(int position);
        void onEditPost(int position);  // Added method for Edit action
        void onDeletePost(int position);  // Added method for Delete action
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
        holder.setItems(model.getImage(), model.getUsername(), model.getLocation(),
                model.getFeed_img(), model.getStatus(), model.getComment_count(), model.getDate());

        holder.itemView.setOnLongClickListener(view -> {
            if (onPostActionListener != null) {
                onPostActionListener.onPostLongClicked(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userProfile, userProfile2, imageView, icDotMenu;
        TextView username_id, location_id, status_id, comment_count_id, date_id;

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

            // Initialize the three-dot menu icon
            icDotMenu = itemView.findViewById(R.id.ic_dot_menu); // Use your actual ID here

            // Set click listener to show the PopupMenu when three-dot icon is clicked
            icDotMenu.setOnClickListener(view -> showPopupMenu(view));
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.action_edit) {
                    // Notify the listener (fragment) to edit the post
                    if (onPostActionListener != null) {
                        onPostActionListener.onEditPost(getAdapterPosition());
                    }
                    return true;
                } else if (itemId == R.id.action_delete) {
                    // Notify the listener (fragment) to delete the post
                    if (onPostActionListener != null) {
                        onPostActionListener.onDeletePost(getAdapterPosition());
                    }
                    return true;
                } else if (itemId == R.id.action_share) {
                    Toast.makeText(view.getContext(), "Share clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            });

            popup.show();
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
