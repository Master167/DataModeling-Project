/**
 * Created by Megan on 4/4/2017.
 */
public class Schema {
    private String name;
    private String dataType;
    private int length;
    private int min;//Cannot be null?
    private int fraction;
    private String tableName;
    private String date;
    public Schema(){

    }
    public void setName(String value){
        name = value;
    }
    public String getName(){
        return name;
    }
    public void setDataType(String value){
        //removes xsd: from data type
        int cut = value.indexOf(":")+1;
        value = value.substring(cut, value.length());
        dataType = value;
    }
    public String getDataType(){
        return dataType;
    }
    public void setLength(String value){
        length = Integer.parseInt(value);
    }
    public int getLength() {
        return length;
    }
    public void setMin(String value){
        min = Integer.parseInt(value);
    }
    public int getMin(){
        return min;
    }
    public void setFraction(String value){
        fraction = Integer.parseInt(value);
    }
    public int getFraction(){
        return fraction;
    }
    public void setTableName(String value){
        tableName = value;
    }
    public String getTableName(){
        return tableName;
    }
    public void setDate(String value){
            date = value;


    }
    public String getDate(){
        return date;
    }
}
