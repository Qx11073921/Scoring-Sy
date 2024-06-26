public class UserComment {
    private String comment;
//    private String time;
    private int score;
    public UserComment(){
        this.comment = "";
        this.score = 0;
    }
    public String getComment() {
        return comment;
    }

    public int getScore() {
        return score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "用户评分: "+getScore()+";  用户评价为: "+getComment();
    }
}
