package model.stockdatastore;

import java.time.LocalDate;

/**
 * This interface defines the signature for a class that represents a stock's market price for a
 * specific date. The stock price can represent the properties for any stock.
 * Typically, a stock would have a list of stock prices.
 */
public interface IStockPrice {

  /**
   * Get the market date for stock's price.
   *
   * @return the market date of the stock as LocalDate
   */
  LocalDate getMarketDate();

  /**
   * Get the stock's market price for its stock market date.
   *
   * @return the stock's market price on its market date
   */
  double getMarketPrice();
}
