import java.util.List;
import java.util.Map;

public interface RadarParser {

    List<Field> getTransactionDetails();

    List<FamilyRule> getValidations();
}
