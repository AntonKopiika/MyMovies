package demo.com.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import demo.com.mymovies.R;
import demo.com.mymovies.data.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private ArrayList<Video> videos;
    private OnPlayClickListener  onPlayClickListener;

    public void setOnPlayClickListener(OnPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }
    public interface OnPlayClickListener{
        void onPlayClick(String url);
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.textViewName.setText(videos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView imageViewPlay;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.textViewName);
            imageViewPlay=itemView.findViewById(R.id.imageViewPlay);
            imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPlayClickListener!=null){
                        onPlayClickListener.onPlayClick(videos.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }



}
