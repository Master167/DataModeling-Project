/**
 * Created by Megan on 4/4/2017.
 */
public class Schema {
    private String name;
    private String dataType;
    private int length;
    private int minMax;
    private int fraction;

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
}
