package demo.com.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import demo.com.mymovies.adapters.MovieAdapter;
import demo.com.mymovies.data.FavoriteMovie;
import demo.com.mymovies.data.Movie;
import demo.com.mymovies.data.MovieViewModel;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavoriteMovies;
    private MovieAdapter adapter;
    private MovieViewModel viewModel;
    private LiveData<List<FavoriteMovie>> favoriteMovies;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private int getColumnsCount() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.itemMain:
                Intent intentMain=new Intent(this,MainActivity.class);
                startActivity(intentMain);
                break;
            case R.id.itemFavorite:
                Intent intentFavorite=new Intent(this, FavoriteActivity.class);
                startActivity(intentFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        adapter=new MovieAdapter();
        recyclerViewFavoriteMovies=findViewById(R.id.recyclerViewFavoriteMovies);
        recyclerViewFavoriteMovies.setLayoutManager(new GridLayoutManager(this,getColumnsCount()));
        recyclerViewFavoriteMovies.setAdapter(adapter);
        viewModel= ViewModelProviders.of(this).get(MovieViewModel.class);
        favoriteMovies=viewModel.getFavoriteMovies();
        favoriteMovies.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                List<Movie> movies=new ArrayList<>();
                if (favoriteMovies!=null){
                movies.addAll(favoriteMovies);
                adapter.setMovies(movies);
                }
            }
        });
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                Movie movie = adapter.getMovies().get(position);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });
    }

}