package demo.com.mymovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import demo.com.mymovies.data.Comment;
import demo.com.mymovies.data.Movie;
import demo.com.mymovies.data.Video;

public class JSONUtils {

    private static final String KEY_RESULTS = "results";

    //comments
    private static final String KEY_AUTHOR="author";
    private static final String KEY_CONTENT="content";

    //video
    private static final String KEY_VIDEO_KEY="key";
    private static final String KEY_NAME="name";
    private static final String BASE_VIDEO_URL="https://www.youtube.com/watch?v=";

    //review
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ID = "id";
    //poster
    private static final String POSTER_BASE_URL="https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE="w185";
    private static final String BIG_POSTER_SIZE="w780";
    private static final String BACKDROP_SIZE="w780";

    public static ArrayList<Comment> getCommentsFromJSON(JSONObject jsonObject){
        ArrayList<Comment> comments = new ArrayList<>();
        if (jsonObject == null) {
            return comments;
        }
        try {
            JSONArray result = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObjectComment = result.getJSONObject(i);
                String author=jsonObjectComment.getString(KEY_AUTHOR);
                String content=jsonObjectComment.getString(KEY_CONTENT);
                comments.add(new Comment(author,content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }
    public static ArrayList<Video> getVideoFromJSON(JSONObject jsonObject){
        ArrayList<Video> videos = new ArrayList<>();
        if (jsonObject == null) {
            return videos;
        }
        try {
            JSONArray result = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObjectComment = result.getJSONObject(i);
                String name=jsonObjectComment.getString(KEY_NAME);
                String key=BASE_VIDEO_URL+jsonObjectComment.getString(KEY_VIDEO_KEY);
                videos.add(new Video(name,key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }
    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (jsonObject == null) {
            return movies;
        }
        try {
            JSONArray result = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObjectMovie = result.getJSONObject(i);
                int id=jsonObjectMovie.getInt(KEY_ID);
                int vote_count=jsonObjectMovie.getInt(KEY_VOTE_COUNT);
                String title=jsonObjectMovie.getString(KEY_TITLE);
                String originalTitle=jsonObjectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview=jsonObjectMovie.getString(KEY_OVERVIEW);
                String posterPath=POSTER_BASE_URL+SMALL_POSTER_SIZE+jsonObjectMovie.getString(KEY_POSTER_PATH);
                String bigPosterPath=POSTER_BASE_URL+BIG_POSTER_SIZE+jsonObjectMovie.getString(KEY_POSTER_PATH);
                String backdropPath=POSTER_BASE_URL+BACKDROP_SIZE+jsonObjectMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage=jsonObjectMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate=jsonObjectMovie.getString(KEY_RELEASE_DATE);
                Movie movie=new Movie(id,vote_count,title,originalTitle,overview,posterPath,bigPosterPath,backdropPath,voteAverage,releaseDate);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }


}
