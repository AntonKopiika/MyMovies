package demo.com.mymovies.data;

import android.app.Application;
import android.app.ListActivity;
import android.os.AsyncTask;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieViewModel extends AndroidViewModel {
    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(getApplication());
        movies = database.movieDao().getAllMovies();
        favoriteMovies=database.movieDao().getAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Movie getMovieById(int id) {

        try {
            return new GetMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public FavoriteMovie getFavoriteMovieById(int id) {

        try {
            return new GetFavoriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void deleteMovieFromDB (Movie movie){
        new DeleteMovieFromDBTask().execute(movie);
    }
    public void deleteAllMovies (){
        new DeleteAllMoviesTask().execute();
    }
    public void insertMovie (Movie movie){
        new InsertMovieTask().execute(movie);
    }
    public void deleteFavoriteMovieFromDB (FavoriteMovie favoriteMovie){
        new DeleteFavoriteMovieFromDBTask().execute(favoriteMovie);
    }
    public void insertFavoriteMovie (FavoriteMovie favoriteMovie){
        new InsertFavoriteMovieTask().execute(favoriteMovie);
    }

    private static class GetMovieByIdTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.movieDao().getMovieByID(integers[0]);
            }
            return null;
        }
    }
    private static class GetFavoriteMovieByIdTask extends AsyncTask<Integer, Void, FavoriteMovie> {

        @Override
        protected FavoriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.movieDao().getFavoriteMovieByID(integers[0]);
            }
            return null;
        }
    }

    private static class DeleteMovieFromDBTask extends AsyncTask<Movie, Void,Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies!=null&& movies.length>0){
                database.movieDao().DeleteMovie(movies[0]);
            }
            return null;
        }
    }
    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            database.movieDao().deleteAllFromDB();
            return null;
        }
    }
    private static class InsertMovieTask extends AsyncTask<Movie, Void,Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies!=null&& movies.length>0){
                database.movieDao().insertMovie(movies[0]);
            }
            return null;
        }
    }
    private static class InsertFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void,Void>{

        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies!=null&& movies.length>0){
                database.movieDao().insertFavoriteMovie(movies[0]);
            }
            return null;
        }
    }
    private static class DeleteFavoriteMovieFromDBTask extends AsyncTask<FavoriteMovie, Void,Void> {

        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies!=null&& movies.length>0){
                database.movieDao().DeleteFavoriteMovie(movies[0]);
            }
            return null;
        }
    }
}
