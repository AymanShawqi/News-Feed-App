package com.android.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    Context context;
    List<Topic> topicsList;

    public TopicAdapter(@NonNull Context context, @NonNull List<Topic> topicsList) {
        this.context = context;
        this.topicsList = topicsList;
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = topicsList.get(position);

        String sectionName = topic.getSectionName();
        String title = topic.getTitle();
        String author = topic.getAuthor();
        String publishDate = topic.getPublishDate();
        final String webUrl = topic.getWebUrl();

        holder.sectionName.setText(sectionName);
        holder.titleTextView.setText(title);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(publishDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                context.startActivity(intent);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sectionName;
        TextView titleTextView;
        TextView authorTextView;
        TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_name);
            titleTextView = itemView.findViewById(R.id.title);
            authorTextView = itemView.findViewById(R.id.author);
            dateTextView = itemView.findViewById(R.id.publish_data);
        }
    }
}
