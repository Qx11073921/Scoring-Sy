import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileAdministratorLoader implements AdministratorLoader {
    private static final String DELIM = "_";

    @Override
    public Administrator loadAdministrator(String fileName)
            throws FileNotFoundException, IOException, DataFormatException {
        Administrator administrator = Administrator.getAdministrator();
        //读入管理员信息
        String fileAdministrator = "administrator.txt";
        BufferedReader administratorReader = new BufferedReader(new FileReader(fileAdministrator));
        String information = administratorReader.readLine();
        //读入管理员用户名及密码
        if (information != null){
            StringTokenizer tokenizer = new StringTokenizer(information,DELIM);
            if (tokenizer.countTokens() != 2){
                throw new DataFormatException();
            }
            String id = tokenizer.nextToken();
            String passWord = tokenizer.nextToken();
            administrator.setInformation(id,passWord);
        }

        //读入类名
        String fileScoringClass = "scoringClass.txt";
        ScoringClass scoringClass = null;
        //从文件（储存评分大类信息）读入评分大类信息
        BufferedReader fileReader = new BufferedReader(new FileReader(fileScoringClass));
        String scoringClassInformation = fileReader.readLine();//读取一行信息
        while(scoringClassInformation != null) {
            scoringClass = readScoringClass(scoringClassInformation);
            administrator.addClass(scoringClass);//将一个评分大类加入管理员中

            scoringClassInformation = fileReader.readLine();//读取一行信息
        }
       return administrator;
    }
    private ScoringClass readScoringClass (String line)
            throws FileNotFoundException, IOException, DataFormatException{
        ScoringClass scoringClass = null;
        //分隔
        StringTokenizer tokenizer = new StringTokenizer(line,DELIM);
        if (tokenizer.countTokens() != 2){
            throw new DataFormatException();
        }
        //初始化一个大类
        String className = tokenizer.nextToken();
        String classDescription = tokenizer.nextToken();
        ScoringItem scoringItem = null;
        scoringClass = new ScoringClass(className,classDescription);
        //为存储ScoringClass评分大类所属评分对象信息的txt文件命名
        String FileScoringItem = className + ".txt";
        //从文件（储存评分对象信息）读入评分对象信息
        BufferedReader fileReader = new BufferedReader(new FileReader(FileScoringItem));
        String scoringItemInformation = fileReader.readLine();//读取一行信息

        while (scoringItemInformation != null){
            scoringItem = readScoringItem(scoringItemInformation);
            scoringClass.addItem(scoringItem);//加入评分对象

            scoringItemInformation = fileReader.readLine();
        }

        return scoringClass;
    }
    private ScoringItem readScoringItem (String line)
            throws FileNotFoundException, IOException, DataFormatException{
        ScoringItem scoringItem = null;
        //分隔
        StringTokenizer tokenizer = new StringTokenizer(line,DELIM);
        if (tokenizer.countTokens() != 4){
            throw new DataFormatException();
        }
        //初始化一个大类
        String itemName = tokenizer.nextToken();
        String itemDescription = tokenizer.nextToken();
        double itemAverageScore = Double.parseDouble(tokenizer.nextToken());
        int scoringNumber = Integer.parseInt(tokenizer.nextToken());
        scoringItem = new ScoringItem(itemName,itemDescription);
        scoringItem.setAverageScore(itemAverageScore);
        scoringItem.setScoringNumber(scoringNumber);
        //为存储ScoringItem评分对象所属UserComment的txt文件命名
        String FileUserComment = itemName + ".txt";
        UserComment userComment = null;
        //从文件（储存UserComment信息）读入UserComment
        BufferedReader fileReader = new BufferedReader(new FileReader(FileUserComment));
        String userCommentInformation = fileReader.readLine();//读取一行信息
        while (userCommentInformation != null){
            userComment = readUserComment(userCommentInformation);//将该行信息转化为一个userComment对象
            scoringItem.addUserComment(userComment);//加入评分对象

            userCommentInformation = fileReader.readLine();//读取一行信息
        }
        return scoringItem;
    }

    private UserComment readUserComment(String line)
            throws FileNotFoundException, IOException, DataFormatException{
        UserComment userComment = new UserComment();

        //文件中有信息
        StringTokenizer tokenizer = new StringTokenizer(line,DELIM);
        if (tokenizer.countTokens() != 2){
            throw new DataFormatException();
        }
        //初始化一个大类
        String comment = tokenizer.nextToken();
        int score = Integer.parseInt(tokenizer.nextToken());
        userComment.setComment(comment);
        userComment.setScore(score);

        return userComment;
    }

}
