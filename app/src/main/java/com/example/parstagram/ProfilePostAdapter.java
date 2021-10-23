package com.example.parstagram;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public ProfilePostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfileImg;
        private TextView tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimetamp);
            ivProfileImg = itemView.findViewById(R.id.ivProfileImg);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(Post post) {
            // Bind the post data the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            String timestamp = getRelativeTimestamp(post.getCreatedAt().toString());
            tvTimestamp.setText(timestamp);
            ParseFile image = post.getImage();
            ParseFile profileImg = post.getUser().getParseFile("profilePic");

            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            if (profileImg != null) {
                Glide.with(context).load(profileImg.getUrl()).circleCrop().into(ivProfileImg);
            }
        }
    }
    // Calculate relative timestamp
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getRelativeTimestamp(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
