import java.util.List;
import org.junit.jupiter.api.Test;

public class RadarParserTest {

    String charge_allow = "[\"CHARGE:card_country=US&currency=USD&amount=250&ip_country=CA\",\"ALLOW:amount<500ANDcurrency==USD\", \"BLOCK:card_country==JP\"]";
    String charge_block = "[\"CHARGE:card_country=US&currency=USD&amount=250&ip_country=CA\",\"ALLOW:amount<500ANDcurrency==CNY\", \"BLOCK:card_country==JP\"]";

    RadarParser radarParser = new RadarPaserImpl(charge_allow);

    @Test
    public void testGetTrasactionDetails()
    {
        List<Field> details = radarParser.getTransactionDetails();
        List<FamilyRule> rules = radarParser.getValidations();
        for( Field field : details)
        {
            System.out.println("["+field.fieldName + "," + field.value+"]" );
        }
        for( FamilyRule rule : rules)
        {
            System.out.println(rule);
        }
    }

    @Test
    public void testRadar()
    {
        Radar radar_allow = new Radar(charge_allow);
        Radar radar_block = new Radar(charge_block);
        assert radar_allow.validate() ;
        assert !radar_block.validate();
    }
}
