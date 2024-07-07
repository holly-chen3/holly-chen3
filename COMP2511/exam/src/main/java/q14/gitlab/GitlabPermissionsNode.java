package q14.gitlab;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public abstract class GitlabPermissionsNode {
    private String name;
    private Map<User, PermissionsLevel> members = new HashMap<User, PermissionsLevel>();

    public String getName() {
        return name;
    }

    public Map<User, PermissionsLevel> getMembers() {
        return members;
    }

    public void setMembers(Map<User, PermissionsLevel> members) {
        this.members = members;
    }

    public void addOwner(User user) {
        members.put(user, PermissionsLevel.OWNER);
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void authorise(User user, PermissionsLevel requiredPermissionsLevel) throws GitlabAuthorisationException {
        int perms = getUserPermissions(user).ordinal();
        int requiredPerms = requiredPermissionsLevel.ordinal();
        if (perms > requiredPerms) {
            throw new GitlabAuthorisationException("User is not authorised");
        }
    }

    public PermissionsLevel getUserPermissions(User user) {
        return members.get(user);
    }

    public void updateUserPermissions(User userToUpdate, PermissionsLevel permissions, User updatingUser) throws GitlabAuthorisationException {
        authorise(updatingUser, PermissionsLevel.OWNER);
        if (members.containsKey(userToUpdate)) {
            throw new GitlabAuthorisationException("already have permissions");
        }
        members.put(userToUpdate, permissions);
    }

    public abstract GitlabGroup createSubgroup(String name, User creator) throws GitlabAuthorisationException;
    
    public abstract GitlabProject createProject(String name, User creator) throws GitlabAuthorisationException;

    public abstract JSONObject toJSON();


}