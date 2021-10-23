package com.example.parstagram.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.ProfilePostAdapter;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.example.parstagram.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private RecyclerView rvProfilePost;
    private Button btnLogout;
    private ImageView ivProfileImage;
    private TextView ivUsername;
    private List<Post> allPosts;
    public static final String TAG = "ProfileFragment";
    private ProfilePostAdapter adapter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        rvProfilePost = view.findViewById(R.id.rvProfilePost);
        ivUsername = view.findViewById(R.id.ivUsername);
        queryUser();

        allPosts = new ArrayList<>();
        adapter = new ProfilePostAdapter(getContext(), allPosts);
        rvProfilePost.setAdapter(adapter);
        rvProfilePost.setLayoutManager(new LinearLayoutManager(getContext()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick logout button");
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goLoginActivity();
            }
        });
        queryPosts();
    }
    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    public void queryUser(){
        ParseQuery <User> query = ParseQuery.getQuery(User.class);
            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> objects, ParseException e) {
                    if (e != null){
                        Log.e(TAG, "Issue with getting post");
                        return;
                    }
                    ivUsername.setText(User.getCurrentUser().getUsername());
                    Glide.with(getContext())
                            .load(objects.get(0).getProfileImage().getUrl())
                            .circleCrop()
                            .into(ivProfileImage);
                }
            });
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER,ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.clear();
                // Now we call setRefreshing(false) to signal refresh has finished
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}