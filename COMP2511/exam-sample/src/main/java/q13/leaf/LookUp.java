package main.java.q13;

public class LookUp implements BusinessRuleValue{
    private String e;

    public Constant(String e) {
        this.e = e;
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return e;
    }
}
