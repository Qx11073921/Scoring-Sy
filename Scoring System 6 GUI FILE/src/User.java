import java.util.Iterator;
import java.util.Vector;

public class User {
    private String id;
    private String password;
    private Vector<UserComment> userComments;
    public User(String id, String password){
        this.id = id;
        this.password = password;
        userComments = new Vector<>();
    }
//处理属性
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }
    //Vector
    public Iterator getUserIterator(){
        return userComments.iterator();
    }
    public void addUserComment(UserComment userComment){
        userComments.add(userComment);
    }
    public void removeUserComment(UserComment userComment){
        userComments.remove(userComment);
    }
    public int getNumberOfUserComment(){
        return userComments.size();
    }


}
