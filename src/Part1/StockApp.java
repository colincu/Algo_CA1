package Part1;
/**
 * App to read in csv file
 * -> create array of stock objects based on csv file
 * -> sort array
 * -> search array
 */

import java.util.Scanner;
import java.io.File;
import java.text.SimpleDateFormat;

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

        //create a new stock catalogue
        Stock[] myStock = new Stock[10000];

        // this will just move us past the header line in the csv file
        myFile.nextLine();

        int i = 0;
        String st = "";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        while (myFile.hasNextLine())  //returns a boolean value
        {
            st = myFile.nextLine();
            String[] data = st.split(",");
            //create stock object from the line read from file and add to array
            myStock[i++] = new Stock(Integer.parseInt(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]), data[3], ft.parse(data[4]), data[5]);

        }
        myFile.close();  //closes the scanner

        // show first 10 objects in array before sorting
        for (i = 0; i < 10; i++) {
            System.out.println(myStock[i]);
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
            System.out.println(myStock[i]);
        }
    }

    //Part1.1 - sorting algorithm - merge sort
    static void merge(Stock myStock[], int lowerB, int mid, int upperB){
        int i = lowerB;
        int j = mid;
        //use temp array to store merged sub-sequence
        Stock temp[] = new Stock[upperB - lowerB];
        int t = 0;
        while(i < mid && j < upperB){
            //compareTo has been overridden to compare based on column 5 (buyDate) and then compare by stockNo if dates
            //are equal
            int compare = myStock[i].compareTo(myStock[j]);
            if(compare < 0){
                temp[t] = myStock[i]; i++; t++;
            }
            else{
                temp[t] = myStock[j]; j++; t++;
            }
        }
        //tag on remaining sequence
        while(i < mid){
            temp[t] = myStock[i]; i++; t++;
        }
        while(j < upperB){
            temp[t] = myStock[j]; j++; t++;
        }
        //copy temp back to f
        i = lowerB;
        t = 0;
        while(t < temp.length){
            myStock[i] = temp[t];
            i++; t++;
        }
    }

    static void mergeSort(Stock[] myStock, int lowerB, int upperB){
        if(lowerB + 1 < upperB){
            int mid = (lowerB + upperB)/2;
            mergeSort(myStock, lowerB, mid);
            mergeSort(myStock, mid, upperB);
            merge(myStock, lowerB, mid, upperB);
        }
    }
}

