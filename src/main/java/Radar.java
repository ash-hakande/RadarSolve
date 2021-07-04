import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Radar {

    private final List<Field> fieldList;
    private final List<FamilyRule> familyRuleList;

    public Radar(String charge) {
        RadarParser radarParser = new RadarPaserImpl(charge);

        fieldList = radarParser.getTransactionDetails();
        familyRuleList = radarParser.getValidations();

    }

    private Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<>();
        for (Field field : fieldList) {
            fieldMap.put(field.fieldName, field.value);
        }
        return fieldMap;
    }

    private boolean isAllowed(Rule rule, Object field, RuleType ruleType) {
        boolean allowed = true;
        switch (rule.operand) {
            case EQUALTO -> allowed = field.equals(rule.value);
            case LESSTHAN -> {
                if (field instanceof Integer)
                    allowed = (Integer) field < (Integer) rule.value;
                else
                    allowed = field.toString().compareTo(rule.value.toString()) < 0;
            }
            case GREATERTHAN -> {
                if (field instanceof Integer)
                    allowed = (Integer) field > (Integer) rule.value;
                else allowed = field.toString().compareTo(rule.value.toString()) > 0;
            }
            case LESSTHANEQUALTO -> {
                if (field instanceof Integer)
                    allowed = (Integer) field <= (Integer) rule.value;
                else allowed = field.toString().compareTo(rule.value.toString()) <= 0;
            }
            case GREATERTHANEQUALTO -> {
                if (field instanceof Integer)
                    allowed = (Integer) field >= (Integer) rule.value;
                else allowed = field.toString().compareTo(rule.value.toString()) >= 0;
            }
            case NOTEQUALTO -> {
                if (field instanceof Integer)
                    allowed = field != rule.value;
            }
        }
        if (ruleType.equals(RuleType.ALLOW))
            return allowed;
        else return !allowed;
    }

    public boolean validate() {
        Map<String, Object> fieldMap = getFieldMap();
        boolean valid = true;
        for (FamilyRule familyRule : familyRuleList) {
            if (familyRule.ruleOperand == RuleOperand.AND) {
                valid = ((familyRule.rule1 != null && isAllowed(familyRule.rule1, fieldMap.get(familyRule.rule1.field), familyRule.ruleType))
                        && familyRule.rule2 != null && isAllowed(familyRule.rule2, fieldMap.get(familyRule.rule2.field), familyRule.ruleType));
            } else {
                valid = ((familyRule.rule1 != null && isAllowed(familyRule.rule1, fieldMap.get(familyRule.rule1.field), familyRule.ruleType))
                        || familyRule.rule2 != null && isAllowed(familyRule.rule2, fieldMap.get(familyRule.rule2.field), familyRule.ruleType));
            }
            if (!valid)
                return false;
        }
        return valid;
    }
}