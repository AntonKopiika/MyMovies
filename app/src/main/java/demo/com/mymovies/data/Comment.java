package demo.com.mymovies.data;

import androidx.recyclerview.widget.RecyclerView;

public class Comment {
    private String author;
    private String content;

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
