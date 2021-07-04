import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Operand {
    GREATERTHAN(">"),
    LESSTHAN("<"),
    GREATERTHANEQUALTO(">="),
    LESSTHANEQUALTO("<="),
    EQUALTO("=="),
    NOTEQUALTO("!=");

    private String operand ;

    Operand(String operand)
    {
        this.operand = operand;
    }

    public String getOperand(){
        return operand;
    }
    private static final Map<String, Operand> LOOKUP = new HashMap<>();

    static {
        for( Operand operand1 : EnumSet.allOf(Operand.class))
                LOOKUP.put(operand1.getOperand(), operand1);
    }

    public static Operand get(String s)
    {
        return LOOKUP.get(s);
    }
}
