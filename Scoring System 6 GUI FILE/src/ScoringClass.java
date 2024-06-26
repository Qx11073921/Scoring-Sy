import java.util.Iterator;
import java.util.Vector;

public class ScoringClass {
    private String className;
    private String ClassDescription;
    private Vector<ScoringItem> items;
    public ScoringClass(String className, String classDescription){
        this.ClassDescription = classDescription;
        this.className = className;
        items = new Vector<>();
    }
//名称及描述操作
    public String getClassName() {
        return className;
    }

    public String getClassDescription() {
        return ClassDescription;
    }

    public void setClassDescription(String classDescription) {
        ClassDescription = classDescription;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    //vector操作
    public Iterator getItemIterator(){
        return items.iterator();
    }
    public void addItem(ScoringItem scoringItem){
        items.add(scoringItem);
    }
    public void removeItem(ScoringItem scoringItem){
        items.remove(scoringItem);
    }
    public int getNumberOfItem(){
        return items.size();
    }
    public ScoringItem getScoringItem(String itemName){
        for(Iterator i = getItemIterator(); i.hasNext();){
            ScoringItem scoringItem =(ScoringItem) i.next();
            if(scoringItem.getItemName().equals(itemName)){
                return scoringItem;
            }
        }
        return null;
    }
    //toSting
    @Override
    public String toString() {
        return getClassName()+" "+getClassDescription();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof  ScoringClass &&
                getClassName().equals(((ScoringClass) obj).getClassName());
    }
}
