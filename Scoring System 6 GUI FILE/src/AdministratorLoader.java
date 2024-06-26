import java.io.FileNotFoundException;
import java.io.IOException;

public interface AdministratorLoader {
    /*
    Administrator loadScoringClass(String fileName)
            throws FileNotFoundException, IOException, DataFormatException;
    ScoringItem loadScoringItem (String fileName)
            throws FileNotFoundException, IOException, DataFormatException;
    UserComment loadUserComment (String fileName)
            throws FileNotFoundException, IOException, DataFormatException;

     */
    Administrator loadAdministrator(String fileName)
            throws FileNotFoundException, IOException, DataFormatException;
}
