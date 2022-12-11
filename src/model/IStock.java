package model;

/**
 * This interface represents a stock in the stock market.
 * A stock can have basic properties like a stock ticker symbol, quantity of stock, and
 * the purchase date of the stock.
 * A stock can have methods for getting its basic properties, or updating its existing values.
 */
public interface IStock {
  /**
   * Get the stock's name as its stock ticker symbol in the stock market.
   *
   * @return the stock's ticker symbol as a string
   */
  String getStockName();

  /**
   * Get the quantity of the stock.
   *
   * @return the quantity of stock as a long integer
   */
  float getStockQuantity();

  /**
   * Get the purchase date of the stock.
   *
   * @return the purchase date of the stock as string
   */
  String getPurchaseDate();

  /**
   * Get the stock data in CSV format as a string. The format is as follows:
   * stock name, stock quantity, purchase date.
   *
   * @return the stock data formatted in CSV format as a string
   */
  String getStockDataAsCsv();

  /**
   * Add a stock quantity to this stock's current quantity.
   *
   * @param quantityToBeAdded the new stock quantity to be added to this stock
   * @return a new IStock containing the updated stock quantity
   */
  IStock addStockQuantity(float quantityToBeAdded);
}
