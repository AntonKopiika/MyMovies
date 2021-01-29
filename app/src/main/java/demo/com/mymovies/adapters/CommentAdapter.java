package demo.com.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import demo.com.mymovies.R;
import demo.com.mymovies.data.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> comments;

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public CommentAdapter() {
        comments=new ArrayList<>();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.textViewAuthor.setText(comments.get(position).getAuthor());
        holder.textViewContent.setText(comments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewAuthor;
        private TextView textViewContent;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor=itemView.findViewById(R.id.textViewAuthor);
            textViewContent=itemView.findViewById(R.id.textViewContent);
        }
    }
}
