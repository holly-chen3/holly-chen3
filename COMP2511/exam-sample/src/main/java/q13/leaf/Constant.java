package main.java.q13;

public class Constant implements BusinessRuleValue{
    private Integer e;

    public Constant(Integer e) {
        this.e = e;
    }

    @Override
    public Object evaluate(Map<String, Object> values) {
        return e;
    }
}
