import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public interface UserLoader {
    Vector loadUser(String fileName)
            throws FileNotFoundException, IOException, DataFormatException;
}
