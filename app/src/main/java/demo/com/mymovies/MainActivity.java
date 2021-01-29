package demo.com.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import demo.com.mymovies.adapters.MovieAdapter;
import demo.com.mymovies.data.Movie;
import demo.com.mymovies.data.MovieViewModel;
import demo.com.mymovies.utils.JSONUtils;
import demo.com.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private TextView textViewTopRated;
    private TextView textViewPopular;
    private Switch switchSort;
    private MovieAdapter adapter;
    private RecyclerView recyclerViewPosters;
    private MovieViewModel viewModel;
    private static int page = 1;
    private static boolean isLoading = false;
    private static int methodOfSort;
    private ProgressBar progressBarLoading;
    private static final int LOADER_ID = 145;
    private LoaderManager loaderManager;
    private static String lang;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;
            case R.id.itemFavorite:
                Intent intentFavorite = new Intent(this, FavoriteActivity.class);
                startActivity(intentFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getColumnsCount() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lang= Locale.getDefault().getLanguage();
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopular = findViewById(R.id.textViewPopularity);
        switchSort = findViewById(R.id.switchSort);
        adapter = new MovieAdapter();
        progressBarLoading = findViewById(R.id.progressBarLoading);
        loaderManager = LoaderManager.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getColumnsCount()));
        recyclerViewPosters.setAdapter(adapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    methodOfSort = NetworkUtils.TOP_RATED;
                } else {
                    methodOfSort = NetworkUtils.POPULARITY;
                }
                downloadData(methodOfSort, 1);
                page = 1;
            }
        });
        switchSort.setChecked(false);
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                Movie movie = adapter.getMovies().get(position);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });
        adapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadData(methodOfSort, page);
                }
            }
        });
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1) {
                    adapter.setMovies(movies);
                }
            }
        });
    }

    private void downloadData(int methodOfSort, int page) {
        URL url = NetworkUtils.createURL(methodOfSort, page,lang);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    public void onClickSetPopular(View view) {
        switchSort.setChecked(false);
        textViewTopRated.setTextColor(getResources().getColor((R.color.colorWhite)));
        textViewPopular.setTextColor(getResources().getColor((R.color.colorAccent)));
    }

    public void onClickSetTopRated(View view) {
        switchSort.setChecked(true);
        textViewTopRated.setTextColor(getResources().getColor((R.color.colorAccent)));
        textViewPopular.setTextColor(getResources().getColor((R.color.colorWhite)));

    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(getApplicationContext(), args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                adapter.clear();

            }

            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            adapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}