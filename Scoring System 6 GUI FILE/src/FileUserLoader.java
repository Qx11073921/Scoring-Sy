import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class FileUserLoader implements UserLoader{
    String DELIM = "_";
    @Override
    public Vector loadUser(String fileName) throws FileNotFoundException, IOException, DataFormatException {
        Vector users = new Vector<>();
        //读入用户信息
        String fileUser = "user.txt";
        BufferedReader userReader = new BufferedReader(new FileReader(fileUser));
        String information = userReader.readLine();
        while (information!=null){
            StringTokenizer tokenizer = new StringTokenizer(information,DELIM);
            if (tokenizer.countTokens() != 2){
                throw new DataFormatException();
            }
            String id = tokenizer.nextToken();
            String passWord = tokenizer.nextToken();
            User user = new User(id,passWord);
            users.add(user);

            information = userReader.readLine();
        }

        return users;
    }
}
