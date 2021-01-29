package demo.com.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    public static final String BASE_URL="https://api.themoviedb.org/3/discover/movie";
    public static final String VIDEO_URL="https://api.themoviedb.org/3/movie/%s/videos";
    public static final String REVIEWS_URL="https://api.themoviedb.org/3/movie/%s/reviews";
    private static final String PARAMS_API_KEY="api_key";
    private static final String PARAMS_LANGUAGE="language";
    private static final String PARAMS_SORT_BY="sort_by";
    private static final String PARAMS_PAGE="page";
    private static final String PARAMS_MIN_VOTE_COUNT="vote_count.gte";

    private static final String API_KEY="3bf65997f0b7fc7963fe9cc0151c4023";
    private static final String SORT_BY_POPULARITY="popularity.desc";
    private static final String SORT_BY_RATE="vote_average.desc";
    private static final String MIN_VOTE_COUNT_VALUE="1000";

    public static final int POPULARITY= 1;
    public static final int TOP_RATED=0;

    public static URL createURL(int sort_by,int page,String lang){
        URL result=null;
        String sorted;
        if (sort_by==POPULARITY){
            sorted=SORT_BY_POPULARITY;
        }else {
            sorted=SORT_BY_RATE;
        }
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE,lang)
                .appendQueryParameter(PARAMS_SORT_BY,sorted)
                .appendQueryParameter(PARAMS_PAGE,Integer.toString(page))
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT,MIN_VOTE_COUNT_VALUE)
                .build();
        try {
            result =new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static URL createVideoURL(int movieId, String lang){
        URL url=null;
        String base=String.format(VIDEO_URL,movieId);
        Uri uri=Uri.parse(base).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE,lang).build();
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL createReviewURL(int movieId){
        URL url=null;
        String base=String.format(REVIEWS_URL,movieId);
        Uri uri=Uri.parse(base).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .build();
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static JSONObject getJSONReviewFromNetwork(int movieId){
        URL url=createReviewURL(movieId);
        JSONObject result=null;
        try {
            result=new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JSONObject getJSONVideoFromNetwork(int movieId,String lang){
        URL url=createVideoURL(movieId,lang);
        JSONObject result=null;
        try {
            result=new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JSONObject getJSONFromNetwork(int sort_by, int page,String lang){
       URL url=createURL(sort_by,page,lang);
       JSONObject result=null;
        try {
            result=new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        private OnStartLoadingListener onStartLoadingListener;
        private Bundle bundle;
        public interface OnStartLoadingListener{
            void onStartLoading();
        }
        public JSONLoader(@NonNull Context context,Bundle bundle) {
            super(context);
            this.bundle=bundle;
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;

        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener!=null){
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle==null){
                return null;
            }
            String urlAsString=bundle.getString("url");
            URL url=null;
            try {
                url=new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject=null;
            if (url==null){
                return jsonObject;
            }
            HttpURLConnection httpURLConnection=null;
            try {
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream stream=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(stream);
                BufferedReader bufferedReader=new BufferedReader(reader);
                StringBuilder result=new StringBuilder();
                String line=bufferedReader.readLine();
                while(line!=null){
                    result.append(line);
                    line=bufferedReader.readLine();
                }
                jsonObject=new JSONObject(result.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            }
            return jsonObject;
        }
    }
    private static class JSONLoadTask extends AsyncTask<URL,Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject jsonObject=null;
            if (urls==null&&urls.length==0){
                return jsonObject;
            }
            HttpURLConnection httpURLConnection=null;
            URL url=null;
            url=urls[0];

            try {
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream stream=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(stream);
                BufferedReader bufferedReader=new BufferedReader(reader);
                StringBuilder result=new StringBuilder();
                String line=bufferedReader.readLine();
                while(line!=null){
                    result.append(line);
                    line=bufferedReader.readLine();
                }
                jsonObject=new JSONObject(result.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            }
        return jsonObject;
        }
    }
}
