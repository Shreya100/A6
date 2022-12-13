package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This interface represents the signature of a model class that can perform various I/O operations
 * related to a stock portfolio.
 * The various operations include, but are not limited to, 'creating a portfolio', 'examining a
 * portfolio', and 'getting a portfolio's value for a given date'.
 * This model represents the stock portfolios for a single user.
 */
public interface IModel {

  /**
   * Create a new stock portfolio from the given CSV file.
   * Each line in the input CSV file must be in the following format:
   * stock name, stock amount, stock purchase date.
   * The given portfolio name must be unique, i.e. no other portfolios with same name can exist.
   *
   * @param portfolioName the name of portfolio to be created
   * @param file          the file containing stock information for creating the new portfolio
   * @throws FileNotFoundException      if the given file does not exist
   * @throws FileAlreadyExistsException if the given portfolio already exists
   * @throws IOException                if there was an I/O error while creating the
   *                                    portfolio storage directory
   * @throws IllegalArgumentException   if the given stock symbol is not accepted, or if the
   *                                    purchase date of the stock is not in the correct format,
   *                                    or the stock quantity is not a whole number
   */
  void createPortfolio(String portfolioName, File file)
          throws FileNotFoundException,
          FileAlreadyExistsException,
          IOException,
          IllegalArgumentException,
          InvalidObjectException;

  /**
   * This method adds stock to the existing portfolio.
   *
   * @param portfolioName portfolio name
   * @param stockName     stock to be added
   * @param stockQuantity stock quantity
   * @param purchaseDate  purchase date
   */
  void addStockToPortfolio(String portfolioName,
                           String stockName,
                           float stockQuantity,
                           String purchaseDate);

  /**
   * Get a portfolio's entire value for a given date in the stock market.
   *
   * @param portfolioName the sequence number of portfolio in the list of all existing portfolios
   * @param date          the market date for which the portfolio's value is to be calculated
   * @return the value of portfolio on the given date as double precision value
   * @throws IndexOutOfBoundsException if the given portfolioNumber is out of bounds of the
   *                                   existing portfolios list
   */
  double getPortfolioValue(String portfolioName, LocalDate date)
          throws IndexOutOfBoundsException;

  /**
   * Examine a specific portfolio from the list of existing portfolios by simply returning
   * the list of stocks present in the portfolio.
   *
   * @param portfolioName the sequence number of portfolio in the list of all existing portfolios
   * @return a list of IStock containing the information of all the stocks present in the
   * @throws IndexOutOfBoundsException if the given portfolioNumber is out of bounds of the
   *
   */
  List<IStock> examinePortfolioForADate(String portfolioName, LocalDate compositionDate)
          throws IndexOutOfBoundsException;


  /**
   * Get the list of all stocks supported by the portfolio model.
   *
   * @return a list of string containing the supported stocks' ticket symbols.
   */
  List<String> getListOfSupportedStocks();

  /**
   * Get the names of all existing portfolios created in the model.
   *
   * @return a list of string containing the names all created portfolios
   */
  List<String> getPortfolioNames();

  /**
   * Delete all portfolios stored in the model.
   */
  void deleteAllPortfolioFiles();

  /**
   * Check if the model does not contain any portfolios.
   *
   * @return true is portfolio list is empty, false otherwise
   */
  boolean isPortfolioListEmpty();

  /**
   * This method retrieves the portfolios at the start of the application.
   */
  void retrieveExistingPortfolios();

  /**
   * This method sets the initial commission fee and reduction percentage.
   *
   * @param commissionFee          initial commission fee
   * @param feeReductionPercentage the amount by which commission fee needs to be reduced
   * @throws IllegalArgumentException if commission fee is less than 0 or fee reduction percentage
   *                                  is not between 0 and 100
   */
  void setCommissionFee(float commissionFee, float feeReductionPercentage)
          throws IllegalArgumentException;

  /**
   * This method gets the cost basis for a portfolio on a specific date.
   *
   * @param portfolioName the sequence number of portfolio in the list of all existing portfolios
   * @param costBasisDate date on which the cost basis needs to be calculated
   * @return cost basis value of portfolio on a specific date
   * @throws IllegalArgumentException if the provided date is a future date
   */
  double getCostBasis(String portfolioName, LocalDate costBasisDate)
          throws IllegalArgumentException;

  /**
   * This method checks if the provided stock symbol is present in the given portfolio.
   *
   * @param stockTickerSymbol stock to be deleted
   * @param portfolioNumber   the sequence number of portfolio in the list of
   *                          all existing portfolios
   * @return true if the stock symbol is present in the portfolio, false otherwise
   */
  boolean isValidTickerSymbol(String stockTickerSymbol, int portfolioNumber);

  /**
   * This method checks if the provided date is present in the portfolio
   * for a given stock.
   *
   * @param date              sell date
   * @param portfolioNumber   the sequence number of portfolio in the list of
   *                          all existing portfolios
   * @param stockTickerSymbol stock name
   * @return true if the date is present, false otherwise
   */
  boolean isValidDateInPortfolio(String date, int portfolioNumber, String stockTickerSymbol);

  /**
   * This method checks if the given stock quantity if less than or equal
   * to the stock quantity in the given portfolio for a given date.
   *
   * @param quantity          stock sell quantity
   * @param date              sell date
   * @param portfolioNumber   the sequence number of portfolio in the list of
   *                          all existing portfolios
   * @param stockTickerSymbol stock name
   * @return true if the stock quantity is valid, false otherwise
   */
  boolean isValidStockQuantityForADate(int quantity, String date,
                                       int portfolioNumber,
                                       String stockTickerSymbol);

  /**
   * This method sells the stocks from a given portfolio.
   *
   * @param portfolioName     the sequence number of portfolio in the list of
   *                          all existing portfolios
   * @param stockTickerSymbol stock name
   * @param quantity          stock sell quantity
   * @param stockSellDate     sell date
   */
  void sellStocksFromPortfolio(String portfolioName,
                               String stockTickerSymbol,
                               double quantity,
                               LocalDate stockSellDate);

  /**
   * This method displays the portfolio composition.
   *
   * @param portfolioName the sequence number of portfolio in the list of all existing portfolios
   * @return portfolio data
   */
  List<IStock> examinePortfolio(String portfolioName);

  /**
   * Get a given portfolio's performance data over a given time span, given the start date and the
   * end date of the performance duration.
   *
   * @param portfolioName the sequence number of portfolio in the list of all existing portfolios
   * @param startDate     the start date calculating the portfolio's performance
   * @param endDate       the end date calculating the portfolio's performance
   * @return the portfolio's performance consolidated for the given time span
   * @throws IllegalArgumentException if the given start or end date is a future date,
   *                                  or if the given start date comes after the end date,
   *                                  or if the start/end date is not supported by the application
   */
  IPortfolioPerformanceData getPortfolioPerformanceData(String portfolioName,
                                                        LocalDate startDate,
                                                        LocalDate endDate)
          throws IllegalArgumentException;

  /**
   * Get the list of all stocks in a given portfolio.
   *
   * @param pfName name of portfolio whose stock list is required
   * @return list of stocks in the given portfolio
   */
  List<IStock> getStockFromPortfolio(String pfName);

  /**
   * Calculate the number of shares of a stock corresponding to the stock's total value on a given
   * purchase date in the stock market.
   *
   * @param stockName                  stock's ticker symbol
   * @param purchaseDate               the purchase date of the stock
   * @param amountToBeInvestedForStock the total money amount equivalent of the stock.
   * @return the number of shares which correspond to the stock's total amount for the given date
   */
  float calculateNumberOfShares(String stockName,
                                LocalDate purchaseDate,
                                float amountToBeInvestedForStock);

  /**
   * Create a new portfolio initialized with dollar cost averaging over a given period of time, at
   * given intervals. This feature creates a portfolio and invests in it periodically, based on the
   * stock proportions provided.
   *
   * @param portfolioName      the name of the portfolio to be created
   * @param amountToBeInvested the amount to be invested in the portfolio as given intervals
   * @param startDate          the start date of dollar cost averaging
   * @param endDate            the end date of dollar cost averaging
   * @param numberOfDays       the number of days (interval) after which money is to be invested
   * @param stockProportions   the map containing the percentages/proportions of each stock to be
   *                           invested into the portfolio over the intervals
   */
  void createPortfolioWithDollarCostAveraging(String portfolioName,
                                              float amountToBeInvested,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              int numberOfDays,
                                              Map<String, Float> stockProportions);

  void balancePortfolio(String pfName, LocalDate d, Map<String, Double> ds) throws ParseException;

}
