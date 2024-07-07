package q14.gitlab;

public class GitlabRunner {
    private static GitlabRunner instance = null;

    private GitlabRunner() {
    
    }

    public static GitlabRunner getInstance() {
        if (instance == null) {
            instance = new GitlabRunner();
        }
        return instance;
    }
    
    public void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {}
    }
}