package q14.gitlab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class GitlabGroup extends GitlabPermissionsNode {

    private List<GitlabPermissionsNode> subgroups = new ArrayList<GitlabPermissionsNode>();

    public GitlabGroup (String name, User creator) {
        setName(name);
        addOwner(creator);
    }

    public List<String> getUsersOfPermissionLevel(PermissionsLevel level) {
        return getMembers().keySet()
                .stream()
                .filter(e -> getMembers().get(e).equals(level))
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    @Override
    public GitlabGroup createSubgroup(String name, User user) throws GitlabAuthorisationException {
        authorise(user, PermissionsLevel.MAINTAINER);

        GitlabGroup group = new GitlabGroup(name, user);
        group.setMembers(getMembers());
        subgroups.add(group);
        return group;
    }

    @Override
    public GitlabProject createProject(String name, User user) throws GitlabAuthorisationException {
        authorise(user, PermissionsLevel.DEVELOPER);
        
        GitlabProject project = new GitlabProject(name, user);
        project.setMembers(getMembers());
        subgroups.add(project);
        return project;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "group");
        json.put("name", getName());

        JSONArray subgroupJSON = new JSONArray(
            subgroups.stream()
                     .map(GitlabPermissionsNode::toJSON)
                     .collect(Collectors.toList())
        );

        json.put("subgroups", subgroupJSON);

        return json;
    }
}