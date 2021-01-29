package demo.com.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import demo.com.mymovies.adapters.CommentAdapter;
import demo.com.mymovies.adapters.VideoAdapter;
import demo.com.mymovies.data.Comment;
import demo.com.mymovies.data.FavoriteMovie;
import demo.com.mymovies.data.Movie;
import demo.com.mymovies.data.MovieViewModel;
import demo.com.mymovies.data.Video;
import demo.com.mymovies.utils.JSONUtils;
import demo.com.mymovies.utils.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewOverview;
    private TextView textViewOriginalTitle;
    private TextView textViewRate;
    private TextView textViewReleaseDate;
    private ImageView imageViewBigPoster;
    private int id;
    private MovieViewModel viewModel;
    private Movie selectedMovie;
    private ImageView imageViewAddToFavorite;
    private FavoriteMovie favoriteMovie;
    private static boolean isFavorite;
    private static String lang;

    private RecyclerView recyclerViewComments;
    private RecyclerView recyclerViewVideos;
    private CommentAdapter commentAdapter;
    private VideoAdapter videoAdapter;
    private ArrayList<Comment> comments;
    private ArrayList<Video> videos;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
        setContentView(R.layout.activity_details);
        lang= Locale.getDefault().getLanguage();
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRate = findViewById(R.id.textViewRate);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        recyclerViewComments=findViewById(R.id.recyclerViewComments);
        recyclerViewVideos=findViewById(R.id.recyclerViewVideos);
        final Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id",-1);
            viewModel= ViewModelProviders.of(this).get(MovieViewModel.class);
            selectedMovie=viewModel.getFavoriteMovieById(id);
            if (selectedMovie==null){
                selectedMovie=viewModel.getMovieById(id);
            }
            String title=selectedMovie.getTitle();
            String originalTitle = selectedMovie.getOriginalTitle();
            String overview = selectedMovie.getOverview();
            String bigPosterPath = selectedMovie.getBigPosterPath();
            double voteAverage = selectedMovie.getVoteAverage();
            String releaseDate = selectedMovie.getReleaseDate();
            imageViewAddToFavorite=findViewById(R.id.imageButtonAddToFavorite);
            textViewTitle.setText(title);
            textViewOriginalTitle.setText(originalTitle);
            textViewOverview.setText(overview);
            textViewRate.setText(String.format("%s", voteAverage));
            textViewReleaseDate.setText(releaseDate);
            Picasso.get().load(bigPosterPath).placeholder(R.drawable.placeholder_cinema).into(imageViewBigPoster);
            setFavorite();

            videoAdapter=new VideoAdapter();
            commentAdapter=new CommentAdapter();
            videoAdapter.setOnPlayClickListener(new VideoAdapter.OnPlayClickListener() {
                @Override
                public void onPlayClick(String url) {
                    Intent intentToTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intentToTrailer);
                }
            });
            recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewVideos.setAdapter(videoAdapter);
            recyclerViewComments.setAdapter(commentAdapter);
            videos= JSONUtils.getVideoFromJSON(NetworkUtils.getJSONVideoFromNetwork(id,lang));
            comments= JSONUtils.getCommentsFromJSON(NetworkUtils.getJSONReviewFromNetwork(id));

            videoAdapter.setVideos(videos);
            commentAdapter.setComments(comments);
        }
        else {
            finish();
        }
    }

    public void onClickChangeFavoriteMovie(View view) {
        if (favoriteMovie==null){
            viewModel.insertFavoriteMovie(new FavoriteMovie(selectedMovie));
            Toast.makeText(this, R.string.add_to_favorite,Toast.LENGTH_SHORT).show();

        }
        else {
            viewModel.deleteFavoriteMovieFromDB(new FavoriteMovie(selectedMovie));
            Toast.makeText(this, R.string.remove_from_favorite,Toast.LENGTH_SHORT).show();
        }
        setFavorite();
    }
    private void setFavorite(){
        favoriteMovie=viewModel.getFavoriteMovieById(id);
        if (favoriteMovie==null){
            imageViewAddToFavorite.setImageResource(R.drawable.add_to_favorite);
            isFavorite=false;
        }
        else {
            imageViewAddToFavorite.setImageResource(R.drawable.delete_from_favorite);
            isFavorite=true;
        }
    }
}