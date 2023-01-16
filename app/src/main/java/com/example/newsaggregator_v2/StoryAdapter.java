package com.example.newsaggregator_v2;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<Story> storyList;
    private Picasso picasso;

    public StoryAdapter(MainActivity mainActivity, ArrayList<Story> storyList) {
        this.mainActivity = mainActivity;
        this.storyList =storyList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.story_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

        Story s = storyList.get(position);

        if (s.getTitle() == null || s.getTitle() == "null") {
            holder.title.setVisibility(View.INVISIBLE);
        } else {
            holder.title.setText(s.getTitle());
        }
        if (s.getPublishedAt() == null || s.getPublishedAt() == "null") {
            holder.date.setVisibility(View.INVISIBLE);
        } else {
            holder.date.setText(s.getPublishedAt());
        }
        if (s.getAuthor() == null || s.getAuthor() == "null") {
            holder.author.setVisibility(View.INVISIBLE);
        } else {
            holder.author.setText(s.getAuthor());
        }
        if (s.getDescription() == null || s.getDescription() == "null") {
            holder.description.setVisibility(View.INVISIBLE);
        } else {
            holder.description.setText(s.getDescription());
        }

        holder.count.setText(s.getArticleCount());

        if(s.getUrlToImage() == null) {
            holder.image.setImageResource(R.drawable.noimage);
        } else {
            picasso = Picasso.get();
            picasso.load(s.getUrlToImage())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.loading)
                    .into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

}
