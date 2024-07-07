package main.java.q13;

public class IsNotBlank implements BusinessRule{
    private BusinessRule e;

    public IsNotBlank(BusinessRule e) {
        this.e = e;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        Object v = e.evaluate(values);
        if (v == null) {
            return false;
        } 
        if (v.isBlank()) {
            return false;
        }
        return true;
    }
}
