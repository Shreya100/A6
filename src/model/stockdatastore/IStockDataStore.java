package model.stockdatastore;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This interface provides the signature of a class which is a data store
 * for storing stock prices for various supported stocks. The store must provide stock prices
 * for a given stock, on a given date.
 * The store can have methods for getting a stock's value on a given date, or
 * getting the list of supported stocks in the store, or
 * checking if a given stock is supported by the store.
 */
public interface IStockDataStore {
  /**
   * Get the list of all the stocks that are supported by the stock data store.
   * The store contains stock price information for only the supported stocks.
   *
   * @return a list of string containing the supported stocks' ticker symbols.
   */
  List<String> getSupportedStocksList();

  /**
   * Check if a given stock is accepted by the stock data store.
   *
   * @param stockName the stock symbol to be checked for acceptance
   * @return true if the given stock is acceptable, false otherwise
   */
  boolean isAcceptableStock(String stockName);

  /**
   * Get the stock value of the given stock ticker symbol, on a given date.
   *
   * @param stockName the ticker symbol for the stock whose price value is required
   * @param date      the date for which stock price is required
   * @return the stock price of the given stock, on the given date as a double precision value
   */
  double getStockValue(String stockName, LocalDate date) throws NoSuchElementException;

  /**
   * Check if the given date is a valid date with stock price data present in the data store.
   *
   * @param date the date to be checked
   * @return true if the given date is a valid stock market date, false otherwise
   */
  boolean isValidStockMarketDate(LocalDate date);

  /**
   * Get the closest valid market date from the given date. If the given date is a valid
   * market date, return the same date.
   *
   * @param date the date from which the closest market date is to be found
   * @return the closest valid stock market date
   */
  LocalDate getNextValidMarketDate(LocalDate date);
}
