import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class RadarPaserImpl implements RadarParser{

    private final String chargeString;
    private final String allowString ;
    private final String blockString;

    private List<Field> fields = new ArrayList<>();
    private List<FamilyRule> allowRules = new ArrayList<>();
    private List<FamilyRule> blockRules = new ArrayList<>();

    RadarPaserImpl(String charge)
    {
        String[] items = charge.split(",");
        if(items.length != 3)
            throw new IllegalArgumentException("Please ensure 3 comma separated strings are passed to Radar Parser");
        chargeString = items[0];
        allowString = items[1];
        blockString = items[2];
        parseTrasactionDetails();
        parseRules(RuleType.ALLOW);
        parseRules(RuleType.BLOCK);
    }

    private void parseRules(RuleType ruleType)
    {
        List<FamilyRule> rules ;
        String ruleStr ;
        if(ruleType == RuleType.ALLOW) {
            rules = allowRules;
            ruleStr= allowString;
        }
        else{
            rules = blockRules;
            ruleStr = blockString;
        }

        String[] items ;
        RuleOperand ruleOperand = RuleOperand.NONE;
        if( ruleStr.contains("AND")) {
            items = ruleStr.split("AND");
            ruleOperand = RuleOperand.AND;
        }
        else if(ruleStr.contains("OR")) {
            items = ruleStr.split("OR");
            ruleOperand = RuleOperand.OR;
        }
        else
            items = new String[]{ruleStr};


        FamilyRule familyRule = new FamilyRule();
        for(String item : items)
        {
            Rule rule;
            Matcher operandMatcher = Pattern.compile("(<|>|<=|>=|==|!=)").matcher(item);
            Operand operand = null;
            while(operandMatcher.find())
            {
                String op = operandMatcher.group();
                operand = Operand.get(op);
            }
            if( operand == null) throw new IllegalArgumentException("Not a valid operand");

            String[] fielditems = item.split("<|>|<=|>=|==|!=");

            Object fieldValue = getValue(StringUtils.removePattern(fielditems[1],"\".*"));
            if(fieldValue instanceof Integer)
            {
                rule = new Rule<Integer>();
            }
            else
            {
                rule = new Rule<String>();
            }
            rule.value = fieldValue;
            if(fielditems[0].trim().contains(":"))
                rule.field = fielditems[0].trim().split(":")[1];
            else
                rule.field = fielditems[0].trim();
            rule.operand = operand;
            if( familyRule.rule1 == null) familyRule.rule1 = rule;
            else familyRule.rule2 = rule;

            familyRule.ruleType = ruleType;
            familyRule.ruleOperand = ruleOperand;
            rules.add(familyRule);
        }
    }

    private Object getValue(String fieldStr)
    {
        try {
            return Integer.parseInt(fieldStr);
        }
        catch(Exception e)
        {
            return fieldStr.trim();
        }
    }


    private void parseTrasactionDetails()
    {
        Matcher p = Pattern.compile("(?<=&|:).*?=.*?(?=&|\")").matcher(chargeString);
        while(p.find())
        {
            String fieldStr = p.group();

            String fieldName = fieldStr.split("=")[0].trim();
            String fieldValueStr = fieldStr.split("=")[1].trim();
            Object fieldValue = getValue(fieldValueStr);
            Field field;

            if(fieldValue instanceof Integer){
                field = new Field<Integer>();
            }
            else {
                field = new Field<String>();
            }
            field.fieldName = fieldName;
            field.value = fieldValue;
            fields.add(field);
        }
    }

    @Override
    public List<Field> getTransactionDetails() {
        return fields;
    }



    @Override
    public List<FamilyRule> getValidations() {
        List<FamilyRule> rules = new ArrayList<>();
        rules.addAll(allowRules);
        rules.addAll(blockRules);
        return rules;
    }
}
