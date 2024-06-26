import java.util.Iterator;
import java.util.Vector;

public class ScoringItem {
    String ItemName;
    String ItemDescription;
    double AverageScore;
    int ScoringNumber;
    ItemPicture picture;
    Vector<UserComment> userComments;
    public ScoringItem(String itemName, String itemDescription){
        ItemName = itemName;
        ItemDescription = itemDescription;
        AverageScore = 0.0;
        ScoringNumber = 0;
        picture = new ItemPicture();
        userComments = new Vector<>();
    }
//处理属性
    public String getItemName() {
        return ItemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public double getAverageScore() {
        return AverageScore;
    }

    public int getScoringNumber() {
        return ScoringNumber;
    }

    public void setAverageScore(double averageScore) {
        AverageScore = averageScore;
    }

    public void setScoringNumber(int scoringNumber) {
        ScoringNumber = scoringNumber;
    }

    //vector
    public Iterator getUserCommentIterate(){
        return userComments.iterator();
    }
    public void addUserComment(UserComment userComment){
        userComments.add(userComment);
    }
    public void removeComment(UserComment userComment){
        userComments.remove(userComment);
    }
    public double calculateAverageScore(){
        int totalScore = 0;
        Iterator i = getUserCommentIterate();
        if (!i.hasNext()){
            return 0.0;
        }
        for(;i.hasNext();){
            UserComment userComment = (UserComment) i.next();
            totalScore += userComment.getScore();
        }
        return (double) totalScore/calculateScoringNumber();
    }

    //计算评分人数
    public int calculateScoringNumber(){
        this.ScoringNumber = userComments.size();
        return userComments.size();
    }

    @Override
    public String toString() {
        return "评分对象:  "+getItemName()+"\n"+"对象描述:  "+getItemDescription()+"\n"+
                "平均分为: "+calculateAverageScore()+"\n"
                +calculateScoringNumber()+"条评论";
    }
}
