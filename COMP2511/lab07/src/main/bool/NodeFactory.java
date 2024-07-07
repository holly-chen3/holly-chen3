package bool;

import org.json.JSONObject;

public abstract class NodeFactory {
    public BooleanNode loadNode(JSONObject nodeObject) {
        // recursive method
        // end condition if subgoals is null
        String node = nodeObject.get("node").toString();
        switch (node) {
            case "and":
                BooleanNode and1 = loadNode(nodeObject.getJSONObject("subnode1"));
                BooleanNode and2 = loadNode(nodeObject.getJSONObject("subnode2"));
                return new And(and1, and2);
            case "or":
                BooleanNode or1 = loadNode(nodeObject.getJSONObject("subnode1"));
                BooleanNode or2 = loadNode(nodeObject.getJSONObject("subnode2"));
                return new Or(or1, or2);
            case "not":
                BooleanNode not = loadNode(nodeObject.getJSONObject("subnode1"));
                return new Not(not);
            case "value":
                String value = nodeObject.getJSONObject("value").toString();
                return new BooleanLeaf(value == "true" ? true : false);
            default:
                return null;
        }
    } 

}