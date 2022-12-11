package view;

import java.time.LocalDate;
import java.util.List;

import model.IPortfolioPerformanceData;
import model.IStock;

/**
 * This is an interface for view for the Stocks application.
 */
public interface IView {

  /**
   * This method displays the different option to perform
   * operations on the portfolio.
   */
  void showOptions();

  void showRebalancePortfolioMessage();
  void showRebalanceDateMessage();
  void displayRebalancePortfolio();

  /**
   * This method gets the filename input from the user.
   */
  void getFilename();

  /**
   * This method displays the error message if user inputs
   * an invalid choice.
   */
  void showOptionError();

  /**
   * This method gets the portfolio name from the user.
   */
  void getPortfolioName();

  /**
   * The method shows the error message to the user
   * if a portfolio name provided already exists in the database.
   *
   * @param exception error message
   */
  void fileAlreadyExistsError(String exception);

  /**
   * This method shows the error message to the user
   * if the provided file isn't present.
   *
   * @param exception error message
   */
  void fileNotFoundError(String exception);

  /**
   * This method shows the error message to the user
   * in case of any invalid inputs.
   *
   * @param exception error message
   */
  void showErrorMessage(String exception);

  /**
   * Display the list of all  portfolios.
   *
   * @param pfNames the list of string containing all existing portfolio names
   */
  void displayPortfolioNames(List<String> pfNames);

  /**
   * Display the portfolio date in a user-friendly format.
   *
   * @param portfolioName the name of portfolio being examined
   * @param portfolioData the portfolio data of stock list to be displayed
   */
  void displayExaminePortfolio(String portfolioName, List<IStock> portfolioData);

  /**
   * Display a message after successful creation of a portfolio.
   *
   * @param portfolioName name of the successfully created portfolio
   */
  void showPortfolioCreatedMessage(String portfolioName);

  /**
   * Display a message expressing that there are no existing portfolios created yet.
   */
  void showNoPortfolioCreatedError();

  /**
   * Display a message asking for the user to select the portfolio that they wish to examine.
   */
  void showExaminePortfolioMessage();

  /**
   * Display a message asking for a user input for getting the value of a portfolio.
   */
  void showGetPortfolioValueMessage();

  /**
   * Display a message asking for the date for the portfolio's value is to be calculated.
   */
  void showPortfolioValueDateMessage();

  /**
   * Display the portfolio's stock market value in a user-friendly format.
   *
   * @param portfolioName      the name of the portfolio whose value is being displayed
   * @param portfolioValueDate the market date for which the portfolio's value is being displayed
   * @param pfValue            the stock market price value of the portfolio being displayed
   */
  void displayPortfolioValue(String portfolioName, LocalDate portfolioValueDate, double pfValue);

  /**
   * Display the list of supported stocks in the model.
   *
   * @param supportedStocksList list of string containing the stock ticker symbols of supported
   *                            stocks
   */
  void showGetSupportedStocks(List<String> supportedStocksList);

  /**
   * Display the message to add stock to an existing portfolio.
   */
  void showAddStockToPortfolioMessage();

  /**
   * Display message to take inputs from the user to add new stock.
   */
  void showAcceptNewStockMessage();

  /**
   * Display the message if a new stock is accepted successfully.
   *
   * @param portfolioName portfolio to which a stock is added
   * @param stockName     stock name that was added
   */
  void displayNewStockAddedMessage(String portfolioName, String stockName);

  /**
   * Display message to accept the stock ticker symbol for new stock
   * that needed to be added.
   */
  void showAcceptStockNameMessage();

  /**
   * Display message to accept the stock quantity for new stock
   * that needed to be added.
   */
  void showAcceptStockQuantity();

  /**
   * Display message to accept the stock purchase date for new stock
   * that needed to be added.
   */
  void showAcceptStockPurchaseDate();

  /**
   * Display the message at the start of application to retrieve previously
   * created portfolios.
   */
  void showRetrievePortfoliosMessage();

  /**
   * Display message if the previous portfolios are retrieved successfully.
   *
   * @param portfolios portfolio names that are retrieved
   */
  void showPortfoliosRetrievedMessage(List<String> portfolios);

  /**
   * Display message to get the date for which user wants to examine
   * a portfolio.
   */
  void showCompositionDateMessage();

  /**
   * Display the message to get the commission fee from the user.
   */
  void showGetCommissionFeeMessage();

  /**
   * Display the message to get the percentage by which commission fee
   * will be reduced after each transaction.
   */
  void showGetCommissionFeeReductionMessage();

  /**
   * Display the message to get the cost basis of a portfolio.
   */
  void showCostBasisMessage();

  /**
   * Display the message to get the date for which user wants to get
   * the cost basis of a portfolio.
   */
  void showCostBasisDateMessage();

  /**
   * Display the cost basis value of a portfolio on a specific date.
   *
   * @param portfolioName portfolio whose cost basis value needs to be displayed
   * @param costBasis     cost basis value
   * @param date          date on which cost basis needs to be calculated and displayed.
   */
  void showCostBasisValue(String portfolioName, double costBasis, LocalDate date);

  /**
   * Display message to sell stocks from an existing portfolio.
   */
  void showSellStockMessage();

  /**
   * Display message to get the date for which stocks need to sell.
   */
  void showSellStockDateMessage();

  /**
   * Display message to get the stock name that need to sell.
   */
  void showSellStockTickerSymbolMessage();

  /**
   * Display message to get the stock quantity that need to sell.
   */
  void showSellStockQuantityMessage();

  /**
   * Display message if the stocks are sold successfully.
   */
  void showSuccessfulSellStockMessage();

  /**
   * Display the message to get input from the user if they want to examine
   * portfolio for a specific date or not.
   */
  void showDateOrNoDateOption();

  /**
   * Display the message to get the input from user for getting a portfolio's performance
   * over time by displaying a list of existing portfolios, and asking for a selection.
   */
  void showGetPerformanceOverTimeMessage();

  /**
   * Display the message for asking the user to input the start date for calculating the
   * performance over time.
   */
  void showPerformanceOverTimeStartDateMessage();

  /**
   * Display the message for asking the user to input the end date for calculating the
   * performance over time.
   */
  void showPerformanceOverTimeEndDateMessage();

  /**
   * Display the performance data of a portfolio in a user readable format.
   */
  void displayPortfolioPerformanceData(IPortfolioPerformanceData portfolioPerformanceData);

  void showInvestInExistingPortfolioMessage();

  void showAmountToBeInvestedMessage();

  void showAmountToBeInvestedInEachStockMessage();

  void showInvestmentDateMessage();

  void showStockMessage(String stockName);

  void showMessageIfHundredPercentNotInvested();
}
