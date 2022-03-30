package Part1;
/**
 * Instantiable class to represent a particular stock
 */

import java.text.SimpleDateFormat;
import java.util.Date;


public class Stock implements Comparable<Object>{
    //stock variables
    private int stockNo;
    private float productSize;
    private float cost;
    private String productType;
    private Date buyDate;
    private String productName;

    // constructor
    public Stock(int stockNo, float productSize, float cost, String productType, Date buyDate, String productName) {
        super();
        this.stockNo=stockNo;
        this.productSize = productSize;
        this.cost = cost;
        this.productType= productType;
        this.buyDate =buyDate;
        this.productName=productName;
    }

    // setters and getters
    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public float getProductSize() {
        return productSize;
    }

    public void setProductSize(float stockSize) {
        this.productSize = stockSize;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // so the stock objects can be compared when sorting/searching
    // NOTE: this will only allow comparisons based on the title of the film
    @Override
    public int compareTo(Object obj) {

		/*
		Based on second last number of my student number being 3 - search will be done on colum 5 (buyDate)
		*/
        Stock stk = (Stock)obj;
        //check if date is after
        if ( this.buyDate.toInstant().isAfter(stk.getBuyDate().toInstant())){
            return 1;
        }
        //check if date is before
        else if ( this.buyDate.toInstant().isBefore(stk.getBuyDate().toInstant())){
            return -1;
        }
        //if dates are equal, fallback to sorting by stockNo
        else{
            return stockNo - (stk.stockNo);
        }
    }

    @Override
    public String toString() {
        return "Part1.Stock [StockNo=" + stockNo+ ", Part1.Stock size="+productSize+", Cost="+ cost+ ", Product Type="+
                productType + ", Buy date=" + buyDate + ", Product name=" + productName+ "]";
    }

}
