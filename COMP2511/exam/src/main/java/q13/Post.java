package q13;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postID;
    private String content;
    private String postTitle;
    private LocalDateTime postCreated;
    private List<Comment> comments = new ArrayList<>();

    public Post(String content) {
    }

    public void updatePost(String contentString) {

    }

    public void addComment(Comment comment) {
    }
}
