package bool;

public class Not implements BooleanNode{
    private BooleanNode e;

    public Not(BooleanNode e) {
        this.e = e;
    }

    @Override
    public boolean evaluate() {
        return !e.evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(NOT " + e.prettyPrint() + ")";
    }
    
}
