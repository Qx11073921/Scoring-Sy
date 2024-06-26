import java.util.Iterator;
import java.util.Vector;

public class Administrator {
    private String id;
    private String password;
    private Vector<ScoringClass> classes;
    private static Administrator administrator;
    private static int administratorNumber = 0;
    private Administrator(/*String id,String password*/){
        //this.id = id;
       // this.password = password;
        classes = new Vector<>();
    }

    public static Administrator getAdministrator() {
        if (administrator == null){
            administrator = new Administrator();
        }
        return administrator;
    }
    public static int getAdministratorNumber(){
        return administratorNumber;
    }
    public int getScoringClassNumber(){
        return classes.size();
    }

    //登录信息
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    //设置登录id和密码，调整计数器为一
    public void setInformation(String id, String password){
        this.id = id;
        this.password = password;
        administratorNumber++;
    }

    //vector 操作
    public Iterator getClassIterator(){
        return classes.iterator();
    }
    public void addClass(ScoringClass scoringClass){
        classes.add(scoringClass);
    }
    public void removeClass(ScoringClass scoringClass){
        classes.remove(scoringClass);
    }
    public int getNumberOfClass(String className){
        return classes.size();
    }
    public ScoringClass getScoringClass(String className){
        for (Iterator i = getClassIterator(); i.hasNext() ; ) {
            ScoringClass scoringClass = (ScoringClass) i.next();
            if(className.equals(scoringClass.getClassName())){
                return scoringClass;
            }
        }
        return null;
    }
}
