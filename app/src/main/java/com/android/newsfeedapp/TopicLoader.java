package com.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class TopicLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = TopicLoader.class.getName();
    private String mUrl;

    public TopicLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Topic> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<Topic> topics = QueryUtils.fetchTopicsData(mUrl);
        return topics;
    }
}
