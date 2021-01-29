package demo.com.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavoriteMovie>> getAllFavoriteMovies();

    @Query("SELECT * FROM movies WHERE id== :ID")
    Movie getMovieByID(int ID);

    @Query("SELECT * FROM favorite_movies WHERE id== :ID")
    FavoriteMovie getFavoriteMovieByID(int ID);

    @Query("DELETE FROM movies")
    void deleteAllFromDB();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void DeleteMovie(Movie movie);

    @Insert
    void insertFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    void DeleteFavoriteMovie(FavoriteMovie favoriteMovie);
}
