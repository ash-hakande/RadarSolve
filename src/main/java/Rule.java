public class Rule<E> {
    String field;
    Operand operand;
    E value ;

    @Override
    public String toString()
    {
        return "[" + field + operand.getOperand() + value +"]";
    }
}
