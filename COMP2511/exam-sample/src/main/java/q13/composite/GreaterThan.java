package main.java.q13;

public class GreaterThan implements BusinessRule{
    private BusinessRule e1;
    private BusinessRule e2;

    public GreaterThan(BusinessRule e1, BusinessRule e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        Object v1 = e1.evaluate(values);
        Object v2 = e2.evaluate(values);
        if (!(v1 instanceof Number) || !(v2 instanceof Number)) {
            throw new BusinessRuleException("Both arguments must be numeric");
        }
        return v1 > v2;
    }
}
