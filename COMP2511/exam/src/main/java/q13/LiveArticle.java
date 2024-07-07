package q13;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LiveArticle {
    private String articleID;
    private LocalDateTime firstCreated;
    private List<Post> posts = new ArrayList<>();

    public LiveArticle(String articleID, LocalDateTime firstCreated) {
        this.articleID = articleID;
        this.firstCreated = firstCreated;
    }

    public void deletePost(String postID) {

    }

    public void addComment(String postID) {
        
    }
}
