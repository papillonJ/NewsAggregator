package com.example.newsaggregator_v2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class StoryViewHolder extends RecyclerView.ViewHolder{

    TextView title;
    TextView date;
    TextView author;
    ImageView image;
    TextView description;
    TextView count;

    public StoryViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.storyHeadline);
        date = itemView.findViewById(R.id.storyDate);
        author = itemView.findViewById(R.id.storyAuthor);
        description = itemView.findViewById(R.id.storyDesc);
        image = itemView.findViewById(R.id.imageView);
        count = itemView.findViewById(R.id.storyCount);
    }
}
