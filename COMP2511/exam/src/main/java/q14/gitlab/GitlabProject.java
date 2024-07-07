package q14.gitlab;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GitlabProject extends GitlabPermissionsNode {

    public GitlabProject(String name, User user) {
        setName(name);
    }

    @Override
    public GitlabGroup createSubgroup(String name, User user) {
        return null;
    }

    @Override
    public GitlabProject createProject(String name, User user) throws GitlabAuthorisationException {
        return null;
    }

    public void runPipeline(Runnable runnable) {
        GitlabRunner runner = GitlabRunner.getInstance();
        runner.run(runnable);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "project");
        json.put("name", getName());

        return json;
    }
}