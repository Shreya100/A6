package guiview;

import java.time.LocalDate;
import java.util.List;

import guicontroller.Features;
import model.IPortfolioPerformanceData;
import model.IStock;

/**
 * This is an interface for GUI view which has methods to
 * show components on the scree, get user inputs from drop-down,
 * text fields, etc.
 */
public interface IGUIView {

  /**
   * This method adds the action listeners for a respective
   * AWT components and calls the required methods from Features.
   *
   * @param features Feature interface object
   */
  void addFeatures(Features features);

  /**
   * This method shows the retrieved portfolios
   * list on the screen.
   *
   * @param portfolios retrieved portfolio list
   */
  void showRetrievedPortfolios(List<String> portfolios);

  /**
   * This method prompts to select the protfolio
   * CSV file.
   */
  void selectPortfolioFile();

  /**
   * This method shows the menu message for a selected
   * menu item.
   */
  void showMenuMessage();

  /**
   * This method shows the dialog box for errors
   * or messages.
   *
   * @param s message that needs to be shown
   */
  void showDialogBox(String s);

  /**
   * This method return the portfolio file name from
   * the text field.
   *
   * @return portfolio file
   */
  String getPortfolioFilenameFromTextField();

  /**
   * This method return the portfolio name from
   * the text field.
   *
   * @return portfolio name
   */
  String getPortfolioNameFromTextField();

  /**
   * This method displays the portfolio created message.
   *
   * @param portfolioName portfolio that got created
   */
  void showPortfolioCreatedMessage(String portfolioName);

  /**
   * This method displays the date drop-down.
   */
  void showDateInputDialogBox();

  /**
   * This method displays the portfolio list in a drop-down
   * fashion.
   *
   * @param portfolio portfolio names
   */
  void showPortfolioList(List<String> portfolio);

  /**
   * This method gets the year from combo-box.
   *
   * @return year value
   */
  String getYearFromDropDown();

  /**
   * This method gets the month from combo-box.
   *
   * @return month value
   */
  String getMonthFromDropDown();

  /**
   * This method gets the days from combo-box.
   *
   * @return days value
   */
  String getDayFromDropDown();

  /**
   * This method displays the portfolio composition.
   *
   * @param portfolioData data needs to be displayed
   */
  void showExaminePortfolioData(List<IStock> portfolioData);

  /**
   * This method displays the file dialog to select
   * portfolio file.
   */
  void showFileDialogForPortfolioFile();

  /**
   * This method displays the message for menu item.
   *
   * @param labelMessage menu item message
   */
  void showLabelForMenuItem(String labelMessage);

  /**
   * This method displays the text on the screen.
   *
   * @param message output message
   */
  void showMessage(String message);

  /**
   * This method displays the button for date input
   * for examining portfolio.
   */
  void showExamineForADateButton();

  /**
   * This method displays the cost basis button.
   */
  void showCostBasisButton();

  /**
   * This method gets the portfolio name from drop-down.
   *
   * @return portfolio name
   */
  String getPortfolioNameFromDropDown();

  /**
   * This method displays the cost basis message.
   *
   * @param s output message
   */
  void showCostBasisValueMessage(String s);

  /**
   * This method displays examine portfolio button.
   */
  void showExamineButton();

  /**
   * This method displays get portfolio value button.
   */
  void showPortfolioValueButton();

  /**
   * This method shows the portfolio value on the screen.
   *
   * @param s portfolio value
   */
  void showPortfolioValueMessage(String s);

  /**
   * This method shows message to add stock to portfolio.
   *
   * @param s output message
   */
  void showMessageForStock(String s);

  /**
   * This method shows stock quantity message for add stock.
   *
   * @param s output message
   */
  void showQuantityMessage(String s);

  /**
   * This method displays the add stock button
   * for adding stocks to existing portfolio.
   */
  void showAddStockButton();

  /**
   * This method shows the list fo supported stocks.
   *
   * @param supportedStock stock list
   */
  void showStockListDropDown(List<String> supportedStock);

  /**
   * This method displays the spinner component to
   * get stock quantity input.
   */
  void showQuantitySpinner();

  /**
   * This method gets stock name from supported stock list.
   *
   * @return selected stock name
   */
  String getStockName();

  /**
   * This method gets the value of spinner component.
   *
   * @return the selected stock's quantity
   */
  Object getStockQuantity();

  /**
   * This method displays a message if a stock is added successfully.
   *
   * @param s output message
   */
  void showStockAddedMessage(String s);

  /**
   * This method displays the stock available to sell
   * from a portfolio.
   *
   * @param stockNames    stock list
   * @param portfolioName portfolio name
   */
  void stockSymbolFromPortfolio(List<IStock> stockNames, String portfolioName);

  /**
   * This method gets the selected stock from drop down.
   *
   * @return the selected stock name
   */
  String getStockFromDropDown();

  /**
   * This method displays the available selling dates
   * from a portfolio.
   *
   * @param selectedStockForSell selling stock name
   * @param stockNames           supported stocks
   */
  void showDatesForSelling(String selectedStockForSell, List<IStock> stockNames);

  /**
   * This method gets the selected date for selling.
   *
   * @return the date on which the stock is to be sold
   */
  String getSelectedDateForSell();

  /**
   * This method displays stock quantity available to sell.
   *
   * @param selectedDateForSell  selling date
   * @param selectedStockForSell selling stock name
   * @param stockNames           list of supported stocks
   */
  void showStockQuantityForSelling(String selectedDateForSell,
                                   String selectedStockForSell,
                                   List<IStock> stockNames);

  /**
   * This method shows button to sell stocks from portfolio.
   */
  void showStockContinueButton();

  /**
   * This method displays the stock selling message.
   *
   * @param s message needs to be displayed
   */
  void showStocksSoldMessage(String s);

  /**
   * This method displays the performance chart.
   *
   * @param performanceData data needs to be displayed
   */
  void showPerformanceOverTimeData(IPortfolioPerformanceData performanceData);

  /**
   * This method displays the combo-box for start date
   * for performance chart.
   */
  void showPerformanceStartDateInputDialogBox();

  /**
   * This method displays the button to view
   * performance chart.
   */
  void showPerformanceOverTimeButton();

  /**
   * This method gets the start date for performance chart.
   *
   * @return start date
   */
  LocalDate getPerformanceOverTimeStartDate();

  /**
   * This method displays the combo-box to get
   * end date from user.
   */
  void showEndDateDialogBox();

  /**
   * This method gets the date from combo-box.
   *
   * @return end date
   */
  LocalDate getPerformanceOverTimeEndDate();

  /**
   * This method displays the button to invest in the
   * selected protfolio.
   *
   * @param portfolioName selected portfolio
   */
  void showInvestInPortfolioButton(String portfolioName);

  /**
   * This method displays text field to get the investment amount.
   *
   * @param portfolioName selected portfolio name
   */
  void showTextFieldForInvestmentAmount(String portfolioName);

  /**
   * This method gets the amount from the text field.
   *
   * @return value from text field
   */
  String getValueFromInvestmentTextField();

  /**
   * This method displays invest stocks button.
   */
  void showSelectStocksForInvestmentButton();

  /**
   * This method shows the supported stock list
   * for investing in existing portfolio.
   *
   * @param amount    amount to be invested
   * @param stockList supported stock list
   */
  void showStocksListForInvestment(String amount, List<String> stockList);

  /**
   * This method shows invest more button.
   */
  void showInvestMoreButton();

  /**
   * This method removes components from panel.
   */
  void clearScreen();

  /**
   * This method adds the list to panel.
   */
  void showList();

  /**
   * This method calls certain methods to get the input
   * for commission fee form the user.
   */
  void getCommissionFee();

  /**
   * This method gets the commission fee from the text field.
   *
   * @return commission fee
   */
  String getCommissionFeeFromTextField();

  /**
   * This method gets the reduction percentage from
   * text field.
   *
   * @return commission reduction fee
   */
  String getCommissionFeePercentageFromTextField();

  /**
   * This method displays the menu to the frame.
   */
  void addShowMenuToPanel();

  /**
   * This method calls the respective method to get inputs for
   * dollar-cost averaging strategy.
   */
  void showTextFieldsForInputs();

  /**
   * This method shows the button to add more stocks.
   */
  void showStocksButton();

  /**
   * This method removes the previous label.
   */
  void removePreviousMessage();

  /**
   * This method shows the supported stock list for
   * dollar-cost strategy.
   *
   * @param stockList supported stock list
   * @param stockName stock added
   */
  void showSupportedStockList(List<String> stockList, String stockName);

  /**
   * This method adds the create portfolio button for
   * dollar-cost strategy.
   */
  void addCreatePortfolioButton();

  /**
   * Get the number of days as the interval for dollar cost averaging.
   *
   * @return the number of days (interval) as an integer
   */
  Object getIntervalDays();
}
