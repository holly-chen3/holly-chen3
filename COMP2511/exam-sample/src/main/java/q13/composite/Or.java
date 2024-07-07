package main.java.q13;

import java.util.Map;

import q13.BusinessRule;

public class Or implements BusinessRule{
    private BusinessRule e1;
    private BusinessRule e2;

    public Or(BusinessRule e1, BusinessRule e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        return e1.evaluate(values) || e2.evaluate(values);
    }
    
}
