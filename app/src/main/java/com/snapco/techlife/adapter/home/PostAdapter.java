package com.snapco.techlife.adapter.home;

import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import com.snapco.techlife.data.model.home.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> modelList;
    private OnPostActionListener onPostActionListener;

    public PostAdapter(List<Post> modelList, OnPostActionListener onPostActionListener) {
        this.modelList = modelList;
        this.onPostActionListener = onPostActionListener;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.techlife_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post model = modelList.get(position);
        holder.setItems(model.getUserImageUrl(), model.getUserName(), model.getCaption(),
                model.getImageUrl(), model.getLikesCount(), model.getCommentsCount(), model.getCreatedAt());

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

        ImageView userProfile, imageView, icDotMenu;
        TextView username_id, status_id, likes_count_id, comment_count_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.userProfile_id);
            imageView = itemView.findViewById(R.id.imageView);
            username_id = itemView.findViewById(R.id.username_id);
            status_id = itemView.findViewById(R.id.status_id);
            likes_count_id = itemView.findViewById(R.id.likes_count_id);
            comment_count_id = itemView.findViewById(R.id.comment_count_id);
            icDotMenu = itemView.findViewById(R.id.ic_dot_menu);

            icDotMenu.setOnClickListener(this::showPopupMenu);
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.action_edit) {
                    if (onPostActionListener != null) {
                        onPostActionListener.onEditPost(getAdapterPosition());
                    }
                    return true;
                } else if (itemId == R.id.action_delete) {
                    if (onPostActionListener != null) {
                        onPostActionListener.onDeletePost(getAdapterPosition());
                    }
                    return true;
                } else if (itemId == R.id.action_share) {
                    Toast.makeText(view.getContext(), "Share clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        }

        public void setItems(String userImageUrl, String username, String caption, String imageUrl,
                             int likesCount, int commentsCount, String createdAt) {

            Glide.with(itemView.getContext())
                    .load(userImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userProfile);

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(imageView);

            username_id.setText(username);
            status_id.setText(caption);
            likes_count_id.setText(likesCount + " likes");
            comment_count_id.setText("View all " + commentsCount + " comments");
        }
    }

    public interface OnPostActionListener {
        void onPostLongClicked(int position);
        void onEditPost(int position);
        void onDeletePost(int position);
    }
}
