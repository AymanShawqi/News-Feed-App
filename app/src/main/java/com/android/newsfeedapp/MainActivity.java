package com.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Topic>> {

    private static final int TOPICS_LOADER_ID = 1;
    private static final String TOPICS_REQUEST_URL = "https://content.guardianapis.com/search?q=debates&show-tags=contributor&api-key=1e5d450a-5a45-4b9c-842f-2c75e0161e5d";

    private TopicAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Topic> mTopics;
    private View emptyStateView;
    private TextView noInternetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyStateView = findViewById(R.id.empty_state_view);
        noInternetTextView = (TextView) findViewById(R.id.no_internet);

        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        mTopics = new ArrayList<>();
        mAdapter = new TopicAdapter(this, mTopics);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TOPICS_LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<Topic>> onCreateLoader(int i, Bundle bundle) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return new TopicLoader(this, TOPICS_REQUEST_URL);
        } else {
            noInternetTextView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
            return new TopicLoader(this, null);
        }

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Topic>> loader, ArrayList<Topic> topics) {

        if (topics != null) {
            mTopics.clear();
            mTopics.addAll(topics);
            mAdapter.notifyDataSetChanged();
            emptyStateView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Topic>> loader) {

        mTopics.clear();
        mAdapter.notifyDataSetChanged();
        emptyStateView.setVisibility(View.VISIBLE);
    }

}
