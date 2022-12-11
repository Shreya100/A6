package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The Stock class implements the IStock interface.
 * This class represents a stock in the stock market.
 * A stock can have basic properties like a stock ticker symbol, quantity of stock, and
 * the purchase date of the stock.
 */
public class Stock implements IStock {
  /**
   * The date format accepted as the purchase date of the Stock.
   */
  public static final String STOCK_PURCHASE_DATE_FORMAT = "yyyy-MM-dd";
  private final String stockName;
  private final float stockQuantity;
  private final LocalDate purchaseDate;

  /**
   * Create a new Stock initialized with a stock ticker symbol, stock quantity, and the stock's
   * purchase date.
   *
   * @param stockName     the stock's ticket symbol
   * @param stockQuantity the stock's quantity
   * @param purchaseDate  the stock's purchase date in the format (yyyy-MM-dd)
   * @throws DateTimeParseException if the given stock purchase date is not in the correct format
   */
  public Stock(String stockName, float stockQuantity, String purchaseDate)
          throws DateTimeParseException, IllegalArgumentException {
    LocalDate parsedPurchaseDate;
    try {
      parsedPurchaseDate = LocalDate.parse(purchaseDate,
              DateTimeFormatter.ofPattern(STOCK_PURCHASE_DATE_FORMAT));
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("The purchase date for stock "
              + stockName
              + " is not in the correct format(" + STOCK_PURCHASE_DATE_FORMAT + ")");
    }

    //Check if the purchase date is a future date
    if (!verifyFuturePurchaseDate(parsedPurchaseDate)) {
      throw new IllegalArgumentException("Invalid purchase date for stock "
              + stockName + ". "
              + "A stock's purchase date cannot be a future date.");
    }

    this.stockName = stockName;
    this.stockQuantity = stockQuantity;
    this.purchaseDate = parsedPurchaseDate;
  }

  @Override
  public String getStockName() {
    return this.stockName;
  }

  @Override
  public float getStockQuantity() {
    return this.stockQuantity;
  }

  @Override
  public String getPurchaseDate() {
    return String.valueOf(this.purchaseDate);
  }

  @Override
  public String getStockDataAsCsv() {
    String result = "";
    result += this.stockName;
    result += ",";
    result += this.stockQuantity;
    result += ",";
    result += DateTimeFormatter.ofPattern(STOCK_PURCHASE_DATE_FORMAT).format(this.purchaseDate);

    return result;
  }

  @Override
  public IStock addStockQuantity(float quantityToBeAdded) {
    float newStockQuantity = this.stockQuantity + quantityToBeAdded;
    return new Stock(this.stockName, newStockQuantity, this.purchaseDate.toString());
  }

  private boolean verifyFuturePurchaseDate(LocalDate purchaseDate) {
    return !purchaseDate.isAfter(LocalDate.now());
  }

  @Override
  public String toString() {
    return (this.stockName + ":" + this.stockQuantity + ":" + this.purchaseDate);
  }
}
