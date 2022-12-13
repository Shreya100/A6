package view;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import model.IPortfolioPerformanceData;
import model.IStock;
import model.PortfolioPerformanceData;

/**
 * The PortfolioView class implements the IView interface and provides a
 * text-based UI to the user for performing actions related to the Stock portfolio application.
 */
public class PortfolioView implements IView {
  private final PrintStream out;
  private final String dateFormat = "yyyy-MM-dd";

  /**
   * Create a new PortfolioView initialized with the given PrintStream object.
   *
   * @param out the PrintStream object which will be used for the view's output
   */
  public PortfolioView(PrintStream out) {
    this.out = out;
  }

  @Override
  public void showRebalancePortfolioMessage() {
    out.println("Select a portfolio that you wish to rebalance: ");
  }

  @Override
  public void showRebalanceDateMessage() {
    out.println("Enter the date for which you wish to rebalance the portfolio "
            + "(Date format: " + dateFormat + " )");
  }

  public void displayRebalancePortfolio() {
    out.println("Portfolio rebalanced successfully");
  }


  @Override
  public void showOptions() {
    out.println();
    out.println("Menu: ");
    out.println("1: Create portfolio");
    out.println("2: Examine portfolio composition");
    out.println("3: Get portfolio value for a specific date");
    out.println("4: View list of supported stocks");
    out.println("5: Add a new stock to an existing portfolio");
    out.println("6: Get cost basis of a portfolio on a specific date");
    out.println("7: Sell stocks from an exiting portfolio");
    out.println("8: Get the performance over time for a portfolio");
    out.println("9: Invest in an existing portfolio");
    out.println("10: Quit");
    out.println("11: Re-balance Portfolio");
    out.print("Enter your choice: ");
  }

  @Override
  public void getFilename() {
    out.print("Please enter the path of portfolio data file: ");
  }

  @Override
  public void showOptionError() {
    out.println("Invalid option");
  }

  @Override
  public void getPortfolioName() {
    out.print("Enter portfolio name: ");
  }

  @Override
  public void fileAlreadyExistsError(String exception) {
    out.println(exception);
    out.println("Please provide a different name");
  }

  @Override
  public void fileNotFoundError(String exception) {
    out.println(exception);
    out.println("Please check the provided file path");
  }

  @Override
  public void showErrorMessage(String errorMessage) {
    out.println(errorMessage);
  }

  @Override
  public void displayPortfolioNames(List<String> pfNames) {
    for (int i = 0; i < pfNames.size(); i++) {
      out.println(i + 1 + ". " + pfNames.get(i));
    }
  }

  @Override
  public void displayExaminePortfolio(String portfolioName, List<IStock> portfolioData) {
    out.println("Examining portfolio: " + portfolioName + "\n");
    out.println("Stock Name\tQuantity\tPurchase Date\n");
    String tabs = "\t\t\t";
    for (IStock s : portfolioData) {
      out.print(s.getStockName() + tabs);
      out.print(s.getStockQuantity() + tabs);
      out.print(s.getPurchaseDate() + "\n");
    }
  }

  @Override
  public void showPortfolioCreatedMessage(String portfolioName) {
    out.println("Portfolio created successfully: " + portfolioName);
  }

  @Override
  public void showNoPortfolioCreatedError() {
    out.println("No portfolios created.");
  }

  @Override
  public void showExaminePortfolioMessage() {
    out.println("Select a portfolio that you wish to examine: ");
  }

  @Override
  public void showGetPortfolioValueMessage() {
    out.print("Select the portfolio whose value you wish see: \n");
  }

  @Override
  public void showPortfolioValueDateMessage() {
    String output = MessageFormat.format("Enter the market date for calculating the "
            + "portfolio value (Date format: {0}): ", dateFormat);
    out.print(output);
  }

  @Override
  public void displayPortfolioValue(String portfolioName,
                                    LocalDate portfolioValueDate,
                                    double pfValue) {
    String output = MessageFormat.format("The value of portfolio {0} on date {1} is ${2}",
            portfolioName,
            portfolioValueDate.toString(),
            pfValue);
    out.println(output);
  }

  @Override
  public void showGetSupportedStocks(List<String> supportedStocksList) {
    out.println("The list of stocks supported in this application:");
    String output = String.join(", ", supportedStocksList);
    out.println(output);
  }

  @Override
  public void showAddStockToPortfolioMessage() {
    out.println("Select a portfolio to which you wish to add a new stock: ");
  }

  @Override
  public void showAcceptNewStockMessage() {
    out.println("Enter the details of the stock that you wish to add to the portfolio: ");
  }

  @Override
  public void showAcceptStockNameMessage() {
    out.print("Stock name: ");
  }

  @Override
  public void showAcceptStockQuantity() {
    out.print("Stock quantity: ");
  }

  @Override
  public void showAcceptStockPurchaseDate() {
    out.print("Stock purchase date (format: "
            + dateFormat
            + "): ");
  }

  @Override
  public void showRetrievePortfoliosMessage() {
    out.println("Do you wish to retrieve existing portfolios? (y/n): ");
  }

  @Override
  public void showPortfoliosRetrievedMessage(List<String> portfolios) {
    if (portfolios.size() > 0) {
      String retrievedPortfolios = String.join(",", portfolios);
      out.println("The following portfolios have been retrieved successfully: "
              + retrievedPortfolios);
    } else {
      out.println("No portfolios could be retrieved because no files were found.");
    }
  }

  @Override
  public void showCompositionDateMessage() {
    out.println("Enter the date for which you wish to examine the portfolio "
            + "(Date format: " + dateFormat + " )");
  }

  @Override
  public void showGetCommissionFeeMessage() {
    out.print("Please enter the starting value of the commission fee (in $) "
            + "to be used for every transaction (The fee cannot be less than 0): ");
  }

  @Override
  public void showGetCommissionFeeReductionMessage() {
    out.print("Please enter the percentage by which the commission fee will be "
            + "reduced after every transaction: ");
  }

  @Override
  public void showCostBasisMessage() {
    out.println("Select a portfolio to which you wish to get the cost basis value:");
  }

  @Override
  public void showCostBasisDateMessage() {
    out.println("Enter the date for which you wish to get the"
            + " cost basis of the portfolio (Date format: " + dateFormat + " )");
  }

  @Override
  public void showCostBasisValue(String portfolioName, double costBasis, LocalDate date) {
    out.println("The cost basis of portfolio "
            + portfolioName
            + " on date "
            + date
            + " is $" + costBasis
            + ".");
  }

  @Override
  public void showSellStockMessage() {
    out.println("Select the portfolio from which you wish sell stocks.");
  }

  @Override
  public void showSellStockDateMessage() {
    out.println("Enter the date for which you wish to sell stocks "
            + "(Date format: " + dateFormat + " )");
  }

  @Override
  public void showSellStockTickerSymbolMessage() {
    out.println("Enter the stock name that you wish to sell: ");
  }

  @Override
  public void showSellStockQuantityMessage() {
    out.println("Enter the stock quantity that you wish to sell: ");
  }

  @Override
  public void showSuccessfulSellStockMessage() {
    out.println("The stocks have been sold successfully.");
  }

  @Override
  public void showDateOrNoDateOption() {
    out.println("Do you wish to examine the selected portfolio for a specific date? (y/n): ");
  }

  @Override
  public void showGetPerformanceOverTimeMessage() {
    out.println("Select the portfolio whose performance over time you wish to see: ");
  }

  @Override
  public void showPerformanceOverTimeStartDateMessage() {
    out.print("Enter the start date for the performance range"
            + " (Date format: " + dateFormat + " ): ");
  }

  @Override
  public void showPerformanceOverTimeEndDateMessage() {
    out.print("Enter the end date for the performance range"
            + " (Date format: " + dateFormat + " ): ");
  }

  @Override
  public void displayPortfolioPerformanceData(IPortfolioPerformanceData portfolioPerformanceData) {
    out.println();
    String heading = MessageFormat.format(
            "Performance of portfolio \"{0}\" from {1} to {2}",
            portfolioPerformanceData.getPortfolioName(),
            portfolioPerformanceData.getStartDate(),
            portfolioPerformanceData.getEndDate());
    out.println(heading);
    out.println();

    long dataScale = getPerformanceDataScale(portfolioPerformanceData);

    for (PortfolioPerformanceData.PerformanceDataFrame dataFrame :
            portfolioPerformanceData
                    .getPerformanceData()) {
      int chartDataPoints = (int) (dataFrame.getPortfolioValue() / dataScale);
      String dataChart = "";
      for (int i = 0; i < chartDataPoints; i++) {
        dataChart += "*";
      }

      out.println(dataFrame.getTimeFrame() + ": " + dataChart);
    }
    out.println();

    out.println("Scale: * = $" + dataScale);
  }

  @Override
  public void showInvestInExistingPortfolioMessage() {
    out.println("Please select the portfolio to which you wish to invest: ");
  }

  @Override
  public void showAmountToBeInvestedMessage() {
    out.println("Enter the amount that you wish to invest: ");
  }

  @Override
  public void showAmountToBeInvestedInEachStockMessage() {
    out.println("Please enter the percentage amount that you wish to invest "
            + "in each stocks");
  }

  @Override
  public void showInvestmentDateMessage() {
    out.println("Please enter the date on which you wish to investment "
            + "(date format:- yyyy-MM-dd) :- ");
  }

  @Override
  public void showStockMessage(String stockName) {
    out.println("Enter the percentage amount for " + stockName + ":- ");
  }

  @Override
  public void showMessageIfHundredPercentNotInvested() {
    out.println("You have not invested total 100% of the amount in the portfolio. "
            + "Please enter the correct percentage amounts for each stock.");
  }

  private long getPerformanceDataScale(IPortfolioPerformanceData portfolioPerformanceData) {
    double maxPortfolioValue = portfolioPerformanceData
            .getPerformanceData()
            .stream()
            .max(Comparator.comparingDouble(value -> value.getPortfolioValue()))
            .get()
            .getPortfolioValue();

    long dataScale = (long) ((maxPortfolioValue) / 50);
    return dataScale;
  }

  @Override
  public void displayNewStockAddedMessage(String portfolioName, String stockName) {
    out.println("The new stock "
            + stockName
            + " has been added to the portfolio "
            + portfolioName
            + " successfully");
  }
}
