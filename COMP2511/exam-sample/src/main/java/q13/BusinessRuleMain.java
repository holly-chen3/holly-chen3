package q13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import main.java.q13.Constant;
import main.java.q13.GreaterThan;
import main.java.q13.IsNotBlank;
import main.java.q13.LookUp;

public class BusinessRuleMain {

    /**
     * Loads a resource file given a certain path that is relative to resources/
     * for example `/dungeons/maze.json`. Will add a `/` prefix to path if it's not
     * specified.
     * 
     * @precondiction path exists as a file
     * @param path Relative to resources/ will add an implicit `/` prefix if not
     *             given.
     * @return The textual content of the given file.
     * @throws IOException If some other IO exception.
     */
    public static String loadResourceFile(String path) throws IOException {
        if (!path.startsWith("/"))
            path = "/" + path;
        return new String(BusinessRuleMain.class.getResourceAsStream(path).readAllBytes());
    }

    public static BusinessRule generateRule(String inputBusinessRule) {
        return null;
    }

    public static BusinessRule generateRuleLoadJson(JSONObject obj) {
        // recursive method
        // end condition if subgoals is null
        String node = obj.getString("Operator");
        if (node.equals("AND")) {
            JSONArray arguments = obj.getJSONArray("Args");
            return new And(generateRuleLoadJson(arguments.get(0)), generateRuleLoadJson(arguments.get(1)));
        } else if (node.equals("OR")) {
            JSONArray arguments = obj.getJSONArray("Args");
            return new Or(generateRuleLoadJson(arguments.get(0)), generateRuleLoadJson(arguments.get(1)));
        } else if (node.equals("GREATER THAN")) {
            JSONArray arguments = obj.getJSONArray("Args");
            return new GreaterThan(generateRuleLoadJson(arguments.get(0)), generateRuleLoadJson(arguments.get(1)));
        } else if (node.equals("NOT BLANK")) {
            JSONObject argument = obj.getJSONObject("Arg");
            return new IsNotBlank(generateRuleLoadJson(argument));
        }
        return null;
    }

    public static BusinessRule generateRuleLoadValueJson(JSONObject obj) {
        String node = obj.getString("Operator");
        Object argument = obj.get("Arg");
        switch (node) {
            case "LOOKUP":
                return new LookUp((String)argument);
            case "CONSTANT":
                return new Constant((Integer)argument);
            default:
                return null;
        }
    }

}
