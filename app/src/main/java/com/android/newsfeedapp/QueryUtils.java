package com.android.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

/*
    private static URL createURL(String requestURL){
        if (TextUtils.isEmpty(requestURL)){
            Log.e(LOG_TAG,"null url string");
            return null;
        }
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG,"Error in create URL",exception);
        }
        return url;
    }

    private static InputStream makeHttpRequest(URL url){
        if (url==null){
            Log.e(LOG_TAG,"null url");
            return null;
        }
        InputStream inputStream=null;
        HttpURLConnection urlConnection=null;
        try {
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
            }
            else {
                Log.e(LOG_TAG,"Response Code Error "+urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG,"Error in make HTTP connection",exception);
        }finally {
            if (urlConnection != null){}
                //urlConnection.disconnect();
        }
        return inputStream;
    }

    private static String readFromInputStream(InputStream inputStream){
        if (inputStream==null){
            Log.e(LOG_TAG,"null inputStream");
           return null;
        }
        String jsonResponse=null;
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            String line = reader.readLine();
            while (line != null){
                line = reader.readLine();
                builder.append(line);
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG,"Error in Reading Stream",exception);
        }finally {
            try {
               // inputStream.close();
            } catch (Exception exception) {
                Log.e(LOG_TAG,"Error in closing inputStream",exception);
            }
        }
        jsonResponse = builder.toString();
        return jsonResponse;
    }

    private static ArrayList<Topic> extractDataFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Topic> topics = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray resultsArray = root.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String sectionName = resultObject.getString("sectionName");
                String webTitle = resultObject.getString("webTitle");
                String webPublicationDate = resultObject.optString("webPublicationDate", "");
                String webUrl = resultObject.getString("webUrl");
                JSONArray tags = resultObject.optJSONArray("tags");
                String author = "";
                if (tags.length() != 0) {
                    author = tags.getString(2);
                }

                topics.add(new Topic("jsonResponse", "Science", "webPublicationDate", "author", "webUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



            return topics;
        }

        */

    private static URL createURl(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error in creating url", exception);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException exception) {
                Log.e(LOG_TAG, "Error in reading stream", exception);
            }
        }

        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response code Error" + urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error in make Http Connection", exception);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }


    private static ArrayList<Topic> extractDataFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse))
            return null;
        ArrayList<Topic> topics = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);

                String title = result.getString("webTitle");
                String sectionName = result.getString("sectionName");
                String publicationDate = formateDate(result.optString("webPublicationDate", ""));
                String webUrl = result.getString("webUrl");
                JSONArray tags = result.getJSONArray("tags");

                String authors = "";
                if (tags.length() > 0) {
                    ArrayList<String> authorList = new ArrayList<>();
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tag = tags.getJSONObject(j);
                        authorList.add(tag.getString("webTitle"));
                    }
                    authors = joinAuthors(authorList);
                }

                topics.add(new Topic(title, sectionName, publicationDate, authors, webUrl));
            }

            //}
        } catch (Exception exception) {
            Log.e(LOG_TAG, "Error in json extraction");
        }
        return topics;
    }

    private static String joinAuthors(ArrayList<String> authorList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < authorList.size() - 2; i++) {
            builder.append(authorList.get(i)).append(", ");
        }
        builder.append(authorList.get(authorList.size() - 1));
        return builder.toString();
    }

    private static String formateDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return date;
        }
        String[] dateArray = date.split("T");
        return dateArray[0];
    }


    public static ArrayList<Topic> fetchTopicsData(String requestURL) {
        URL url = createURl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Topic> topics = extractDataFromJson(jsonResponse);
        return topics;
    }

}