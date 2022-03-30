package Part1;
/**
 * App to read in csv file
 * -> create array of stock objects based on csv file
 * -> sort array
 * -> search array
 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//Part2.3- exception handling allowed values
enum AllowedTypes{
    food, furniture, clothing
}

public class StockApp {

    public static void main(String[] args) throws Exception {
        //variables for time complexity analysis
        long startTime;
        long endTime;
        long totalTime;

        //csv path
        File dir = new File("/Users/colin.cumings/IdeaProjects/Algo_CA1/Part1/");
        String fileName = dir.getAbsolutePath() + "/Stock.csv";
        Scanner myFile = new Scanner(new File(fileName));

        //create a new stock catalogue - used arraylist so we can add new objects in part2.1
        ArrayList<Stock> myStock = new ArrayList<Stock>(10000);

        // this will just move us past the header line in the csv file
        myFile.nextLine();

        int i = 0;
        String st;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        while (myFile.hasNextLine())  //returns a boolean value
        {
            st = myFile.nextLine();
            String[] data = st.split(",");
            //create stock object from the line read from file and add to array
            Stock stock = new Stock(Integer.parseInt(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]), data[3], ft.parse(data[4]), data[5]);
            myStock.add(stock);
            i++;

        }
        myFile.close();  //closes the scanner

        // show first 10 objects in array before sorting
        for (i = 0; i < 10; i++) {
            System.out.println(myStock.get(i));
        }
        //Part1.1 - merge sort
        startTime = System.nanoTime();  //record start time just before we execute the sorting algorithm
        mergeSort(myStock, 0, 10000);  //myStock.length can be substituted with 10, 100, 1000, 5000 for time complex analysis
        endTime = System.nanoTime();  //record end time just after sorting algorithm runs

        //Part1.2 - time complexity measure in microseconds for visibility
        totalTime = (endTime - startTime)/1000;
        System.out.println("Sorting the data took: " + totalTime);

        // show first 10 objects in array after merge sorting
        for (i = 0; i < 10; i++) {
            System.out.println(myStock.get(i));
        }

        //Part1.3 - binary search by date
        Scanner myDateInput = new Scanner(System.in);
        System.out.println("Please enter date to search in the following format: yyyy-mm-dd ");
        String dateToSearch = myDateInput.nextLine();
        int firstResult;
        startTime = System.nanoTime();  //record start time just before we execute the searching algorithm
        firstResult = searchFirst(myStock, dateToSearch);
        endTime = System.nanoTime();  //record end time just after searching algorithm runs
        if(firstResult == -1){
            System.out.println("Not an existing buy_date!");
        }
        else{
            int lastResult = searchLast(myStock, dateToSearch);
            System.out.println("Date first found at index: " + firstResult);
            System.out.println("Date last found at index: " + lastResult);
            System.out.println("Below is the list of stock bought on: " + dateToSearch);
            for(int num = firstResult; num <= lastResult; num++ ){
                System.out.println(myStock.get(num));
            }
        }

        //Part1.4 - time complexity measure for search algorithm in microseconds for visibility
        totalTime = (endTime - startTime)/1000;
        System.out.println("Searching the first occurrence of the date  took: " + totalTime);

        //Part2.1 - accept new record and add it at the end of the arraylist
        //Example Input for stock: (12.2, 120.3, “furniture”, 2015-01-10, ”L-Sofa”)
        //product_size,cost,product_type,buy_date,product_name

        //get user input
        Scanner myInput = new Scanner(System.in);
        System.out.println("Entry of new stock item....");
        System.out.println("Please enter the product size: ");
        float size = myInput.nextFloat();
        System.out.println("Please enter the product cost: ");
        float cost = myInput.nextFloat();
        //bool to flag when to exit while loop
        boolean validType = false;
        String type = "";
        //loop until user enter correct type
        while(validType == false) {
            System.out.println("Please enter the product type: ");
            type = myInput.next();
            //Part2.3- exception handling if invalid input entered
            try {
                AllowedTypes.valueOf(type);
                validType = true;
            } catch (IllegalArgumentException err) {
                System.out.println(err);
                System.out.println("Product type can only be one of : furniture | clothing | food");
            }
        }
        System.out.println("Please enter the date the product was bought: ");
        String buyDate = myInput.next();
        //Part2.2 - date validation on user input
        if (checkDate(buyDate)){
            System.out.println("Please enter the product name: ");
            String name = myInput.next();

            //generate the next available stock number
            int stockNum = myStock.size() +1;
            //create stock object from the user Input data
            Stock userAddedStock = new Stock(stockNum, size, cost, type, ft.parse(buyDate), name);
            //add new stock entry to arraylist
            myStock.add(userAddedStock);

            //check if new entry added
            // show first 10 objects in array after merge sorting
            for (i = 9995; i < myStock.size(); i++) {
                System.out.println(myStock.get(i));
            }
        }
        else {
            System.out.println("Please enter a valid date between 2014-05-01 and today's date: ");
        }



    }

    //Part1.1 - sorting algorithm - merge sort
    static void merge(ArrayList<Stock> myStock, int lowerB, int mid, int upperB){
        int i = lowerB;
        int j = mid;
        //use temp array to store merged sub-sequence
        int t = 0;
        Stock[] temp = new Stock[upperB - lowerB];
        while(i < mid && j < upperB){
            //compareTo has been overridden to compare based on column 5 (buyDate) and then compare by stockNo if dates
            //are equal
            int compare = myStock.get(i).compareTo(myStock.get(j));
            if(compare < 0){
                temp[t] = myStock.get(i); i++; t++;
            }
            else{
                temp[t] = myStock.get(j); j++; t++;
            }
        }
        //tag on remaining sequence
        while(i < mid){
            temp[t] = myStock.get(i); i++; t++;
        }
        while(j < upperB){
            temp[t] = myStock.get(j); j++; t++;
        }
        //copy temp back to f
        i = lowerB;
        t = 0;
        while(t < temp.length){
            myStock.set(i, temp[t]);
            i++; t++;
        }
    }

    static void mergeSort(ArrayList<Stock> myStock, int lowerB, int upperB){
        if(lowerB + 1 < upperB){
            int mid = (lowerB + upperB)/2;
            mergeSort(myStock, lowerB, mid);
            mergeSort(myStock, mid, upperB);
            merge(myStock, lowerB, mid, upperB);
        }
    }

    /**
     *
     * Part1.3 - searching algorithm - binary search
     * As our array is sorted and we need to return all stock bought on a particular date:
     * - we will use binary search to find the first item bought on this date and the last item
     * - then we will display all items between first and last occurrence
     */
    static int searchFirst(ArrayList<Stock> myStock, String date) throws ParseException {
        // convert search date string to Date
        SimpleDateFormat enteredDate = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = enteredDate.parse(date);
        //System.out.println("Searching for date: " + searchDate);
        int first = 0;
        int last = myStock.size() -1;
        //for analysing time complexity we can adjust the amount on entries we search by adjusting this variable
        //int last = 10;
        int result = -1;
        while(first <= last){
            int middle = (first +last)/2;
            Date stockDate = myStock.get(middle).getBuyDate();
            //System.out.println("Checking if " + stockDate + " is after " + searchDate );
            if (stockDate.before(searchDate)){
                //System.out.println("Confirmed that " + stockDate + " is after " + searchDate );
                first = middle +1;
            }
            else if (stockDate.after(searchDate)){
                //System.out.println("Confirmed that " + stockDate + " is before " + searchDate );
                last = middle -1;
            }
            else{
                result = middle;
                //if middle is the same as searchDate then move left to find first occurrence
                last = middle -1;
            }
        }
        return result;
    }

    //return the last index of the
    static int searchLast(ArrayList<Stock> myStock, String date) throws ParseException {
        // convert search date string to Date
        SimpleDateFormat enteredDate = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = enteredDate.parse(date);
        //System.out.println("Searching for date: " + searchDate);
        int first = 0;
        int last = myStock.size() -1;
        int result = -1;
        while(first <= last){
            int middle = (first +last)/2;
            Date stockDate = myStock.get(middle).getBuyDate();
            //System.out.println("Checking if " + stockDate + " is after " + searchDate );
            if (stockDate.before(searchDate)){
                //System.out.println("Confirmed that " + stockDate + " is after " + searchDate );
                first = middle +1;
            }
            else if (stockDate.after(searchDate)){
                //System.out.println("Confirmed that " + stockDate + " is before " + searchDate );
                last = middle -1;
            }
            else{
                result = middle;
                //if middle is the same as searchDate then move right to find first occurrence
                first = middle +1;
            }
        }
        return result;
    }

    ////Part2.2 - method to validate date input
    static boolean checkDate(String date) throws ParseException {
        //get current date
        Date currentDate = new Date();
        //convert input string to date object
        Date inputDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        //check date is not after current date
        if(inputDate.after(currentDate)){
            return false;
        }

        //check date is not before start of company
        String start = "2014-05-01";
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        if(inputDate.before(startDate)){
            return false;
        }
        return true;

    }
}

