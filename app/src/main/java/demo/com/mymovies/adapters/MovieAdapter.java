package demo.com.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import demo.com.mymovies.R;
import demo.com.mymovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    public MovieAdapter() {
        movies=new ArrayList<>();
    }
public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
}
    public List<Movie> getMovies() {
        return movies;
    }
    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    public interface OnPosterClickListener{
        void onPosterClick(int position);
    }
    public interface OnReachEndListener{
        void onReachEnd();
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
        return new MovieViewHolder(view);
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies.size()>=20 && position==movies.size()-5&&onReachEndListener!=null){
            onReachEndListener.onReachEnd();
        }
        Movie movie=movies.get(position);
        Picasso.get().load(movie.getPosterPath()).into(holder.smallPoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView smallPoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            smallPoster=itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPosterClickListener!=null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
