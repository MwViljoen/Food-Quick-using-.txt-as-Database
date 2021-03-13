/*                                                  NOTE
*   i removed notes from previous capstone, if needed we can go back and look there if one does not understand something
*   i will only be commenting on the new code
*   The city entered in prompt needs to be same as in driver.txt
*   There will be an original driver.txt file outside of project.
* */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*; // included this

public class Delivery {

    public static void main(String[] args) {
        String[] restaurants = {"Pizzario", "Spaghetna", "Fat Burger", "I Sea Food"};
        String[] pizzario = {"Hawaiian", "120", "Mexican", "132", "Chilli Chicken", "110", "Chicken Mayo", "105"};
        String[] spaghetna = {"Bolognese", "60", "Mac & Cheeze", "40", "Chicken A La King", "70", "Lasagna", "99"};
        String[] fatBurger = {"FatCheezy", "110", "DoubleFatty", "150", "Fat-BLT", "110", "ChickenFatty", "95"};
        String[] iSeaFood = {"Surf & Turf", "165", "Hake & Chips", "90", "Sushi Special", "200", "Calamari & Chips", "110"};
        ArrayList<Integer> mealOrderInfo = new ArrayList<>();
        ArrayList<String> customerOrderArray = new ArrayList<>();
        ArrayList<String> locationArray = new ArrayList<>();// gets all locations from the list and saves them here
        ArrayList<String> customerLocationArray = new ArrayList<>();//  contains the customers and their location
        String driverName, updatedDriverLoadList;// updateddriverlist is the new list with updated load
        int mealCount, whichRestaurant, mealType, orderNumber = 0;
        boolean customerSelectionState, stateMain, anotherItem; // changed another item to bool was int in task7
        // added do while to be able to have multiple customers
        do {
            mealOrderInfo.clear();// clearing meal order info for new customer
            Customer customer1 = new Customer(inputDescription("Customer Name: "),
                    inputDescription("Customer Number: "),
                    inputDescription("Customer Address: "),
                    inputDescription("Customer City: "),
                    inputDescription("Customer Email: "),
                    inputDescription("Customer Special Instructions: "));

            whichRestaurant = selection(restaurants);

            customerSelectionState = true; // some variables were renamed for better reading of code
            while (customerSelectionState) {
                mealType = mealSelect(whichRestaurant, pizzario, spaghetna, fatBurger, iSeaFood);
                mealCount = testMealQuantity();
                mealOrderInfo.add(mealType);
                mealOrderInfo.add(mealCount);
                // another item is now returned as bool since i wrote a new method that works better for defensive prog.
                anotherItem = testInputString("Would you like to add another meal? y/n: ");

                if (!anotherItem) {
                    customerSelectionState = false;
                }
            }

            orderNumber++;// will now increment for each user added

            Restaurant restaurant1 = new Restaurant(menuToClass(whichRestaurant, pizzario, spaghetna, fatBurger, iSeaFood),
                    restaurants[whichRestaurant - 1],
                    customer1.city,
                    inputDescription("Restaurant Contact: "),
                    mealOrderInfo,
                    orderNumber);

            driverName = readFile(restaurant1.location);
            writeInvoiceFile(driverName, customer1, restaurant1);
            // customerList is used to create 2 new arraylists for new text file information
            customerList(customerOrderArray, customerLocationArray, customer1, restaurant1);
            updatedDriverLoadList = driverAddLoad(driverName);// used to update list with new load
            writeCustomerOrderFile(updatedDriverLoadList);// writes changes to file driver.txt
            stateMain = testInputString("Would you like to add a customer? y/n: ");// for new customers
        } while (stateMain);// as long as a new customer wants to be added this will loop

        Collections.sort(customerOrderArray);// sorts array alphabetically
        writeCustomerOrderFile(customerOrderArray);// then writes new sorted array to file
        readNamesLocation(locationArray);// get all possible city names from driver.txt will be used later
        // writes the .txt file with customer names and location and grouping them together by location
        writeNamesLocation(locationArray,  customerLocationArray);
    }

    //methods
    //method for testing a string input // basically defensive programming
    static boolean testInputString(String testString){
        String temp = inputDescription(testString).toLowerCase();// in case user enters capitals
            if(temp.equals("y")){
                return true;
            }
            else if(temp.equals("n")){
                return false;
            }
            else{ // defensive programming
                System.out.println("You must enter a letter \"y\" (yes) or \"n\" (no) ");
                return testInputString(testString);
            }
    }

    // method for printing and returning input from scanner
    static String inputDescription(String message) {
        Scanner input = new Scanner(System.in);
        String placeholder;
        System.out.print(message);
        placeholder = input.nextLine();
        return placeholder;
    }

    // prompts and shows restaurant names, restaurant is selected with number of 1 - 4 since we have 4 restaurants
    static int selection(String[] restaurants) {
        Scanner input = new Scanner(System.in);
        int number = 0;

        System.out.println("\nSelect Restaurant by entering a number 1 - 4:\n");//
        System.out.println("1 : " + restaurants[0] + "\n" +
                "2 : " + restaurants[1] + "\n" +
                "3 : " + restaurants[2] + "\n" +
                "4 : " + restaurants[3]);

        do {// defensive programming with try catch method
            try {
                number = Integer.parseInt(input.next());
                if (number <= 0 || number > 4) {
                    System.out.println("Wrong Number entered Try again!");
                    System.out.println("Select Restaurant by entering a number 1 - 4:\n");
                }
            }
            catch (Exception e){
                System.out.println("invalid entry, enter a number not a character or word");
                System.out.println("Select Restaurant by entering a number 1 - 4:\n");
            }
        } while (number <= 0 || number > 4);

        System.out.println(restaurants[number - 1] + " :" + "\n");

        return number;
    }

    // selecting meal from menu
    static int mealSelect(int whichRestaurant, String[] pizzario, String[] spaghetna, String[] FatBurger, String[] iSeaFood) {
        int mealType = 0;
        switch (whichRestaurant) {
            case 1:
                mealType = menu(pizzario);
                break;
            case 2:
                mealType = menu(spaghetna);
                break;
            case 3:
                mealType = menu(FatBurger);
                break;
            case 4:
                mealType = menu(iSeaFood);
                break;
            default:
                // check this later
                break;
        }
        return mealType;
    }
    // testing whether correct value was entered// towards defensive programming
    static int testMealQuantity(){
        int temp = 0;
        try { // in case a string is entered and not a number
            // if a string is entered try catch will take control if wrong number entered the if statement will control
            temp = Integer.parseInt(inputDescription("how many of this meal? "));
            if(temp <= 0 || temp > 100){
                System.out.println("Invalid number entered, enter a number from 1 to 100");
                return testMealQuantity();
            }
        }
        catch (Exception e){
            System.out.println("invalid entry, enter a number not a character or word");
            testMealQuantity();// recursive for user to attempt the correct input
        }
        return temp;
    }

    static int menu(String[] restaurantMenu) {
        Scanner input = new Scanner(System.in);
        int foodItemNr = 1, selectedNr = 0;
        for (int i = 0; i < restaurantMenu.length; i++) {
            System.out.println(foodItemNr + " : " + restaurantMenu[i] + " " + restaurantMenu[i + 1]);
            i++;
            foodItemNr++;
        }
        do { // defensive programming will not explain again since it was explained above
            try{
                System.out.println("Select meal form numbers:");
                selectedNr = Integer.parseInt(input.next());
                if (selectedNr < 1 || selectedNr > 4) {
                    System.out.println("wrong number entered, try again!");
                }
            }
            catch (Exception e){
                System.out.println("invalid entry! enter a number not a character or a word, try again!");
            }
        } while (selectedNr < 1 || selectedNr > 4);

        return selectedNr;
    }

    // used to return selected menu array for use in class
    static String[] menuToClass(int whichRestaurant, String[] pizzario, String[] spaghetna, String[] FatBurger, String[] iSeaFood) {
        String[] placeholder = pizzario;
        switch (whichRestaurant) {
            case 1:
                placeholder = pizzario;
                break;
            case 2:
                placeholder = spaghetna;
                break;
            case 3:
                placeholder = FatBurger;
                break;
            case 4:
                placeholder = iSeaFood;
                break;
            default:
                break;
        }
        return placeholder;
    }

    // reading driver file to select closest driver and returns driver name only
    static String readFile(String rLocation) {
        String inputString, dName = "none";
        String[] parts;
        int loadPlaceholder, dLoad = 99999;
        try {// used mainly for if file not present
            File x = new File("drivers.txt");
            Scanner scanner = new Scanner(x);
            while (scanner.hasNextLine()) {
                inputString = scanner.nextLine();

                parts = inputString.split(", ");
                if (Arrays.asList(parts).contains(rLocation)) {
                    if (parts.length < 3) {
                        dName = parts[0];
                        dLoad = 0;
                    } else {
                        loadPlaceholder = Integer.parseInt(parts[2]);
                        if (loadPlaceholder < dLoad) {
                            dName = parts[0];
                            dLoad = loadPlaceholder;
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
        return dName;
    }


    static void readNamesLocation (ArrayList<String> locationArray) {
        String inputString;
        String[] parts;
        try {// used mainly for if file not present
            File x = new File("drivers.txt");
            Scanner scanner = new Scanner(x);
            while (scanner.hasNextLine()) {
                inputString = scanner.nextLine();

                parts = inputString.split(", ");
                if(!locationArray.contains(parts[1])){
                    locationArray.add(parts[1]);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }
    // used to reconstruct driver.txt with added driver load
    // if a driver was selected the load of that driver will increment
    // then we need to update the .txt file for as soon as a new customer is added
    static String driverAddLoad (String dName) {
        String inputString, updatedDriverList = "";
        String[] parts;
        int tempNumber;
        try {
            File x = new File("drivers.txt");
            Scanner scanner = new Scanner(x);
            while (scanner.hasNextLine()) {
                inputString = scanner.nextLine();
                parts = inputString.split(", ");
                // in case load is null and no load was given to driver.txt list
                if(parts[0].equals(dName) && parts.length < 3){
                    updatedDriverList += parts[0] + ", " + parts[1] + ", " + 1 + "\r\n";
                }
                // else if driver name is same as nextline in driver.txt the load will be incremented
                else if(parts[0].equals(dName)){
                    tempNumber = (Integer.parseInt(parts[2]) + 1);
                    updatedDriverList +=  parts[0] + ", " + parts[1] + ", " + tempNumber + "\r\n";
                }
                // else no changes will be made
                else {
                    updatedDriverList += parts[0] + ", " + parts[1] + ", " + parts[2] + "\r\n";
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
        return updatedDriverList;
    }

    static void writeInvoiceFile(String driverName, Customer customer1, Restaurant restaurant1) {
        String invoice, list = "";
        try {// mainly used for write failure
            FileWriter newTextCypher = new FileWriter("invoice.txt");
            if (!driverName.equals("none")) {
                for (int i = 0; i < restaurant1.mealList.size() - 1; i++) {
                    list += restaurant1.mealList.get(i) + " x " + restaurant1.mealList.get(i + 1) + " (R" + restaurant1.mealList.get(i + 2) + ")\r\n";
                    i++;
                    i++;
                }

                invoice = "Order Number: " + restaurant1.orderNumber + "\r\n"
                        + "Customer: " + customer1.name + "\r\n"
                        + "Email: " + customer1.email + "\r\n"
                        + "Phone Number: " + customer1.number + "\r\n"
                        + "Location: " + customer1.city + "\r\n\r\n"
                        + "You have ordered the following from " + restaurant1.name + " in " + restaurant1.location + ":\r\n\r\n"
                        + list + "\r\n"
                        + "Total: R" + restaurant1.total + "\r\n\r\n"
                        + driverName + " is nearest to the restaurant and so he will be delivering your\n\r order to you at:\r\n\r\n"
                        + customer1.address + "\r\n\r\n"
                        + "If you need to contact the restaurant, their number is " + restaurant1.contact;
                newTextCypher.write(invoice);
            } else {
                newTextCypher.write("\"Sorry! Our drivers are too far away from you to be able to\r\n"
                        + "deliver to your location.\"");
            }

            newTextCypher.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    // method for assembling the string to print the sorted customer with order number array
    static void writeCustomerOrderFile(ArrayList<String> customerOrderArray) {
        String customerAndOrder = "";
        try {
            FileWriter customerOrderNumber = new FileWriter("Customer&OrderNumber.txt");
            for (String s : customerOrderArray) {
                customerAndOrder += s; // concatenating array to string
            }
            customerOrderNumber.write(customerAndOrder);// writing to file
            customerOrderNumber.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    // method used to sort names by location, uses the location array which contains all the cities in driver.txt
    // loops through all cities and for each city loops through customers and appends accordingly
    static void writeNamesLocation(ArrayList<String> locationArray, ArrayList<String> customerLocationArray){
        String groupedNamesAndLocations = "";
        for(int i = 0; i < locationArray.size(); i++){
            for(int e = 0; e < customerLocationArray.size(); e++){
                if(customerLocationArray.get(e).contains(locationArray.get(i))){
                    groupedNamesAndLocations += (customerLocationArray.get(e) + "\r\n");
                }
            }
        }
        try { // in the end writes string to file
            FileWriter file = new FileWriter("Customer&Location.txt");
            file.write(groupedNamesAndLocations);
            file.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    // receives updated driver load list and prints to .txt file
    static void writeCustomerOrderFile(String updatedDriverList) {
        try {
            FileWriter driverList = new FileWriter("drivers.txt");
            driverList.write(updatedDriverList);
            driverList.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    // used to create 2 new arraylist for two new .txt files
    static void customerList(ArrayList<String> customerOrderArray, ArrayList<String> customerLocationArray, Customer customer1, Restaurant restaurant1){
        customerOrderArray.add(customer1.name + ": Order Number " + restaurant1.orderNumber + "\r\n");
        customerLocationArray.add(customer1.name + " " + customer1.city);
    }
}

