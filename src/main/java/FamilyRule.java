public class FamilyRule {
    Rule rule1;
    Rule rule2;
    RuleOperand ruleOperand;
    RuleType ruleType;

    FamilyRule()
    {
        rule1 = null;
        rule2 = null;
        ruleOperand = RuleOperand.NONE;
    }
    @Override
    public String toString()
    {
        String r1 = rule1 == null ? "" : rule1.toString();
        String r2 = rule2 == null ? "" : rule2.toString();
        String op = ruleOperand == RuleOperand.NONE ? "" : ruleOperand.toString();
        return ruleType.name() + ":" + r1  + op + r2 ;
    }
}
