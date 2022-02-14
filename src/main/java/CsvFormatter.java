import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CsvFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {

        return record.getSequenceNumber()+  "," + record.getInstant() +
                "," + record.getLevel() + "," + record.getMessage()  + "\n" ;
    }

}
