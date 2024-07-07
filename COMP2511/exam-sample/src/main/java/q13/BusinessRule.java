package q13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface BusinessRule {
    public boolean evaluate(Map<String, Object> values);
}
