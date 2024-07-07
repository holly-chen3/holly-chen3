package bool;

public class BooleanLeaf implements BooleanNode{
    private boolean bool;
    
    public BooleanLeaf(boolean bool) {
        this.bool = bool;
    }

    @Override
    public boolean evaluate() {
        return bool;
    }

    @Override
    public String prettyPrint() {
        return bool ? "true" : "false";
    }
}
