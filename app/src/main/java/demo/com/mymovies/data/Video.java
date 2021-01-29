package demo.com.mymovies.data;

public class Video {
    private String name;
    private String key;

    public Video(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
