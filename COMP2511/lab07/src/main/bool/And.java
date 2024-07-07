package bool;

public class And implements BooleanNode{
    private BooleanNode e1;
    private BooleanNode e2;

    public And(BooleanNode e1, BooleanNode e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean evaluate() {
        return e1.evaluate() && e2.evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(AND " + e1.prettyPrint() + " " + e2.prettyPrint() + ")";
    }
    
}
