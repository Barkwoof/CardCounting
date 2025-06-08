public class Card {

    private String value;
    private String type;

    public Card(String value, String type){
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return value + type;
    }

    public int getCountValue(){
        if("AJQK10".contains(value)){ // ace jack ...
            return 1;
        }
        if("2345".contains(value)){
            return -1;
        }
        else{
            return 0;
        }
    }

    public int getTrueValue(){
        if("AJQK".contains(value)){ // ace jack ...
            if(value == "A"){
               return 11;
               //TODO 10 or 11
            }
            else {
                return 10;
            }
        }
        else { // 2 to 10
            return Integer.parseInt(value);
        }
    }
    public boolean isAce(){
        return value == "A";
    }
    public String getImagePath(){
        return "./PNG/" + value + type + ".png";
    }
}
