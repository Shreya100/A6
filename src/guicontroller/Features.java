package guicontroller;

import java.util.Map;

/**
 * This interface represents the features or operations that
 * GUI will perform.
 * The interface is implemented by the controller.
 */

public interface Features {

  /**
   * This method is called when an action is performed by the user to
   * retrieve existing portfolio.
   */
  void retrieveExistingPortfolio();

  /**
   * This method is called when an action is performed by the user to
   * not retrieve existing portfolio.
   */
  void notRetrievingPortfolio();

  /**
   * This method is called when the suer selects create portfolio
   * option from the main menu.
   */
  void inputsForCreatingNewPortfolio();

  /**
   * This method is called when user clicks on the create portfolio button.
   */
  void createPortfolio();

  /**
   * This method examines portfolio on a specific date.
   */
  void examinePortfolioOnASpecificDate();

  void reBalancePortfolioOnASpecificDate();

  /**
   * This method examines the portfolio with no specific date.
   */
  void examinePortfolioWithDate();

  void reBalancePortfolioWithDate(Map<String, Double> stocks);

  /**
   * This method takes the CSV file input from user.
   */
  void portfolioFileInput();

  /**
   * This method selects the portfolio for calculating cost basis.
   */
  void selectPortfolioForCostBasis();

  /**
   * This method calculates the cost basis.
   */
  void calculateCostBasis();

  /**
   * This method is executed when the user clicks examine portfolio button.
   */
  void examinePortfolio();

  /**
   * This method is executed when the user clicks examine button for a
   * specific date.
   */
  void examinePortfolioWithoutDate();

  /**
   * This method gets the input for calculating portfolio value.
   */
  void getInputsForPortfolioValue();

  /**
   * This method is executed when the user clicks get portfolio value btoon.
   */
  void getPortfolioValue();

  /**
   * This method gets the input for adding stock to the portfolio.
   */
  void getInputsForAddStock();

  /**
   * This method add stocks to the portfolio.
   */
  void addStockToPortfolio();

  /**
   * This method gets the input for selling stock from the portfolio.
   */
  void inputsForSellingStock();

  /**
   * This method gets the stock-to-sold after clicking the button.
   */
  void getStockToSell();

  /**
   * This method gets the selling date after clicking the button.
   */
  void getDateInputForSell();

  /**
   * This method gets the selling quantity after clicking the button.
   */
  void getQuantityInputForSell();

  /**
   * This method sells the stock from the portfolio.
   */
  void sellStock();

  void inputsForPerformanceOverTime();

  void getPortfolioPerformanceOverTime();

  /**
   * This method gets the user input to invest in existing portfolio.
   */
  void getPortfolioInput();

  /**
   * This methods gets the investment amount inputs.
   */
  void getInvestmentAmountInputs();

  /**
   * This method gets the stock to be invested.
   */
  void getStockInputToInvest();

  /**
   * This method invests in the existing portfolio.
   */
  void investInExistingPortfolio();

  void createPortfolioWithDollarCostAveraging();

  /**
   * This method shows the list of supported stocks.
   */
  void showSupportedStocks();

  /**
   * This method sets the commission fee.
   */
  void setCommissionFee();

  /**
   * Display fields for getting input for dollar cost averaging.
   */
  void getInputsForDollarCostAveraging();

  /**
   * Show a dialog box, asking the user if they want to enter and end date for dollar cost
   * averaging.
   */
  void showEndDateDialogBox();

  /**
   * Display the button for adding stocks for creating the new portfolio for dollar cost
   * averaging.
   */
  void showAddStocksButton();

  /**
   * Get the percentage/proportion input for the stock to be added to the new portfolio
   * for dollar cost averaging.
   *
   * @param stockName the ticker symbol of stock to be added to portfolio
   */
  void getStockInputsForDollarCost(String stockName);

  /**
   * Create a map of stock-percentages to be used for the portfolio's dollar cost averaging.
   */
  void createMapForDollarCost();

  void getStocksOfPortfolio(String portfolioName);
}
