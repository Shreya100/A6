package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import model.stockdatastore.IStockDataStore;

/**
 * This interface represents a stock portfolio class. A portfolio can have basic properties like
 * a portfolio name, and a list of stocks in the portfolio.
 * A portfolio can have methods for getting its value in the stock market on a given date, or
 * adding a stock to the portfolio, or getting the list of stocks present in the portfolio.
 */
public interface IPortfolio {
  /**
   * Get this portfolio's stock market value on a given date.
   *
   * @param dataStore the data store containing all the stock price information
   * @param date      the market date for which the portfolio's value is to be calculated
   * @return the value of portfolio on the given date as double precision value
   */
  double getPortfolioValue(IStockDataStore dataStore, LocalDate date) throws NoSuchElementException;

  /**
   * Get the name of this portfolio.
   *
   * @return the name of this portfolio as a string
   */
  String getPortfolioName();

  /**
   * Add a stock to this portfolio.
   * If there is an existing stock with same ticker symbol and purchase date, the new stock to be
   * added is considered a duplicate. In this case, the quantity of this duplicate stock
   * will be added to the existing stock list of the portfolio.
   *
   * @param stock the stock to be added to the portfolio
   */
  void addStock(IStockDataStore dataStore, IStock stock, float commissionFee);

  /**
   * Get the list of stocks in this portfolio.
   *
   * @return a list of IStock containing all the stocks present in this portfolio
   */
  List<IStock> getStocksList();

  /**
   * This method gets the stock list on and before a specific date for a
   * portfolio.
   *
   * @param compositionDate date on which user wants to examine a portfolio.
   * @return list of stock from a portfolio
   */
  List<IStock> getStockListOnAndBeforeCompositionDate(LocalDate compositionDate);

  /**
   * This method gets the cost basis of a portfolio on a specific date.
   *
   * @param costBasisDate date on which cost basis needs to be calculated
   * @return cost basis value of a portfolio
   */
  double getCostBasis(LocalDate costBasisDate);

  /**
   * This method gets the cost basis map containing cost basis
   * value of a stock on a particular date of a portfolio.
   *
   * @return cost basis map
   */
  HashMap<LocalDate, Double> getCostBasisMap();

  /**
   * This methods sets the cost basis map for a particular portfolio.
   *
   * @param costBasisMap cost basis map that needs to be updated
   */
  void setCostBasisMap(HashMap<LocalDate, Double> costBasisMap);

  /**
   * This method update the stock values in a portfolio.
   *
   * @param dataStore     the data store containing all the stock price information
   * @param stock         new stock values
   * @param existingStock stock that needs to be updated
   */
  void updateStock(IStockDataStore dataStore, Stock stock, IStock existingStock);

  /**
   * This method deletes the stock from a portfolio.
   *
   * @param stock stock that needs to be deleted
   */
  void deleteStock(IStock stock);

  /**
   * Get the portfolio's performance data over a given time span, given the start date and the end
   * date of the performance duration. The data store specifies the repository/database of the stock
   * value data.
   *
   * @param startDate the start date calculating the portfolio's performance
   * @param endDate the end date calculating the portfolio's performance
   * @param dataStore the data store containing all the stock price information
   * @return the portfolio's performance consolidated for the given time span
   */
  IPortfolioPerformanceData getPerformanceData(LocalDate startDate,
                                               LocalDate endDate,
                                               IStockDataStore dataStore);
}
