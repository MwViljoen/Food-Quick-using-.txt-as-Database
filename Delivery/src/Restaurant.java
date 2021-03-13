import java.util.ArrayList;

public class Restaurant {
    String [] menu;
    String name, location, contact;
    ArrayList<Integer> mealOrderInfo;
    ArrayList<String> mealList = new ArrayList<>();
    int total = 0,orderNumber;

    public Restaurant(String [] menu, String name, String location, String contact, ArrayList<Integer> mealOrderInfo, int orderNumber){
        this.menu = menu;
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.mealOrderInfo = mealOrderInfo;
        this.orderNumber = orderNumber;

        list();
    }
    // getting all data and assigning it to variable
    public void list(){
        int meal;
        String mealString = "", mealPrice = "",mealAmount = "";
        for (int i = 0; i < mealOrderInfo.size(); i++){
            meal = mealOrderInfo.get(i);
            mealAmount = String.valueOf(mealOrderInfo.get(i + 1));

            if(meal == 1){ // if meal 1 from menu then get name and price as string
                mealString = menu[0];
                mealPrice = menu[1];
            }
            if(meal == 2){// if meal 2 from menu then get name and price as string
                mealString = menu[2];
                mealPrice = menu[3];
            }
            if(meal == 3){
                mealString = menu[4];
                mealPrice = menu[5];
            }
            if(meal == 4){
                mealString = menu[6];
                mealPrice = menu[7];
            }
            i++;// incrementing in 2's
            mealList.add(mealAmount);
            mealList.add(mealString);
            mealList.add(mealPrice);

            total += Integer.parseInt(mealPrice) * Integer.parseInt(mealAmount); // calculating total
        }
    }
}
