package guicontroller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import guiview.IGUIView;
import model.IModel;
import model.IPortfolioPerformanceData;
import model.IStock;

/**
 * This is the controller class which implements Features interface.
 * The input from the user first comes to the controller and controller
 * then delegates the task to model and view depending upon the input.
 */

public class GUIController implements Features {
  private final IModel model;
  private IGUIView view;
  private String portfolioName;
  private LocalDate compositionDate;
  private List<IStock> stockNames;
  private static float totalAmountInvestedSoFar;
  private HashMap<String, Float> stockNamePercentageAmount = new HashMap();

  public GUIController(IModel m) {
    model = m;
  }

  /**
   * This method sets the view object and calls addFeatures
   * to register the listeners for all AWT components.
   *
   * @param v IGUIView object
   */
  public void setView(IGUIView v) {
    view = v;
    //provide view with all the callbacks
    view.addFeatures(this);
  }

  @Override
  public void retrieveExistingPortfolio() {
    model.retrieveExistingPortfolios();
    List<String> portfolios = model.getPortfolioNames();
    view.showRetrievedPortfolios(portfolios);
    view.getCommissionFee();
  }

  @Override
  public void notRetrievingPortfolio() {
    model.deleteAllPortfolioFiles();
    view.clearScreen();
    view.getCommissionFee();
  }

  @Override
  public void inputsForCreatingNewPortfolio() {
    view.selectPortfolioFile();
  }

  @Override
  public void createPortfolio() {
    String portfolioFilename = view.getPortfolioFilenameFromTextField();
    String portfolioName = view.getPortfolioNameFromTextField();

    if (portfolioFilename.isEmpty()) {
      view.showDialogBox("Portfolio file cannot be empty!");
    } else {
      if (portfolioName.isEmpty()) {
        view.showDialogBox("Portfolio name cannot be empty!");
      } else {
        try {
          model.createPortfolio(portfolioName, new File(portfolioFilename));
          view.showPortfolioCreatedMessage(portfolioName);
        } catch (NumberFormatException e) {
          view.showDialogBox("The stock quantity must be less than " + Integer.MAX_VALUE);
        } catch (IllegalArgumentException | NoSuchElementException | IOException e) {
          view.showDialogBox(e.getMessage());
        }
      }
    }
  }

  @Override
  public void examinePortfolioOnASpecificDate() {
    view.showLabelForMenuItem("Select the portfolio you"
            + " wish to examine.");
    if (showPortfolioList()) {
      view.showMessage("Please select the date for which you want "
              + "to examine the portfolio.");
      view.showDateInputDialogBox();
      view.showExamineForADateButton();
    } else {
      view.showMenuMessage();
    }
  }


  @Override
  public void reBalancePortfolioOnASpecificDate() {
    view.showTextFieldsForInputsRebalance();
  }

  @Override
  public void examinePortfolioWithDate() {
    getDate();
    portfolioName = view.getPortfolioNameFromDropDown();
    try {
      List<IStock> portfolioData = model.examinePortfolioForADate(portfolioName,
              compositionDate);
      if (portfolioData.isEmpty()) {
        view.showDialogBox("No stocks are present in the portfolio "
                + portfolioName
                + " for the date " + compositionDate.toString());
      } else {
        view.showExaminePortfolioData(portfolioData);
      }
    } catch (IllegalArgumentException e) {
      view.showDialogBox(e.getMessage());
    }
  }

  @Override
  public void reBalancePortfolioWithDate(Map<String, Double> stocks) {
    getDate();
    portfolioName = view.getPortfolioNameFromTextField();
    Double pp = 100d;
    for (Map.Entry<String, Double> stock : stocks.entrySet()) {
      Double value = Math.round(Double.valueOf(stock.getValue() * 100.0)) / 100.0;
      if (value < 0 || value > pp) {
        view.showDialogBox("Entered Percents are not valid!");
        return;
      }
      pp -= value;
      stocks.put(stock.getKey(), value);
    }
    if (pp > 0) {
      view.showDialogBox("Entered Percents are not valid!");
      return;
    }
    try {
      model.balancePortfolio(portfolioName,
              compositionDate, stocks);
      view.showPortfolioReBalanceMessage(portfolioName);
    } catch (IllegalArgumentException e) {
      view.showDialogBox(e.getMessage());
    } catch (ParseException e) {
      view.showDialogBox(e.getMessage());
    } catch (Exception e) {
      view.showDialogBox("The given date is a holiday. Provide a different date");
    }
  }

  @Override
  public void portfolioFileInput() {
    view.showFileDialogForPortfolioFile();
  }

  @Override
  public void selectPortfolioForCostBasis() {
    view.showLabelForMenuItem("Select the portfolio you wish "
            + "to get the cost basis for");
    if (showPortfolioList()) {
      view.showMessage("Select the date for which you want "
              + "to get the cost basis.");
      view.showDateInputDialogBox();
      view.showCostBasisButton();
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void calculateCostBasis() {
    getDate();
    portfolioName = view.getPortfolioNameFromDropDown();
    double costBasisValue = model.getCostBasis(portfolioName, compositionDate);
    view.showCostBasisValueMessage("Cost basis of portfolio " + portfolioName
            + " on " + compositionDate.toString()
            + " is $" + costBasisValue);
  }

  @Override
  public void examinePortfolio() {
    view.showLabelForMenuItem("Select the portfolio you"
            + " wish to examine.");
    if (showPortfolioList()) {
      view.showExamineButton();
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void examinePortfolioWithoutDate() {
    String portfolioName = view.getPortfolioNameFromDropDown();
    List<IStock> portfolioData = model.examinePortfolio(portfolioName);
    view.showExaminePortfolioData(portfolioData);
  }

  @Override
  public void getInputsForPortfolioValue() {
    view.showLabelForMenuItem("Select the portfolio you"
            + " wish to get the value for.");
    if (showPortfolioList()) {
      view.showMessage("Select the date for which you want "
              + "to get the portfolio value.");
      view.showDateInputDialogBox();
      view.showPortfolioValueButton();
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void getPortfolioValue() {
    try {
      getDate();
      portfolioName = view.getPortfolioNameFromDropDown();
      double portfolioValue = model.getPortfolioValue(portfolioName, compositionDate);
      view.showPortfolioValueMessage("The value of portfolio " + portfolioName
              + " on " + compositionDate.toString()
              + " is $"
              + Math.round(portfolioValue * 100.0) / 100.0);
    } catch (IllegalArgumentException | NoSuchElementException e) {
      view.showDialogBox(e.getMessage());
    }
  }

  @Override
  public void getInputsForAddStock() {
    view.showLabelForMenuItem("Select the portfolio to which "
            + "you wish to add stocks");
    if (showPortfolioList()) {
      view.showMessageForStock("Select the stock you wish to purchase");
      List<String> supportedStock = model.getListOfSupportedStocks();
      view.showStockListDropDown(supportedStock);
      view.showQuantityMessage("Select the number of stocks you wish to purchase");
      view.showQuantitySpinner();
      view.showMessage("Select the date for which you want "
              + "to purchase stock.");
      view.showDateInputDialogBox();
      view.showAddStockButton();
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void addStockToPortfolio() {
    String stockName = view.getStockName();
    int stockQuantity = (int) view.getStockQuantity();
    if (stockQuantity == 0) {
      view.showDialogBox("Stock quantity cannot be 0");
    } else {
      getDate();
      portfolioName = view.getPortfolioNameFromDropDown();
      try {
        model.addStockToPortfolio(portfolioName, stockName, (float) stockQuantity,
                compositionDate.toString());
        view.showStockAddedMessage("Stock added successfully to the portfolio "
                + portfolioName);
      } catch (NoSuchElementException | IllegalArgumentException e) {
        view.showDialogBox(e.getMessage());
      }
    }
  }

  @Override
  public void inputsForSellingStock() {
    view.showLabelForMenuItem("Select the portfolio from which you wish"
            + " to sell stocks");
    if (!showPortfolioList()) {
      view.showMenuMessage();
    } else {
      view.showStockContinueButton();
    }
  }

  @Override
  public void getStockToSell() {
    String pfName = view.getPortfolioNameFromDropDown();
    stockNames = model.getStockFromPortfolio(pfName);
    view.stockSymbolFromPortfolio(stockNames, pfName);
  }

  @Override
  public void getDateInputForSell() {
    String selectedStockForSell = view.getStockFromDropDown();
    view.showDatesForSelling(selectedStockForSell, stockNames);
  }

  @Override
  public void getQuantityInputForSell() {
    String selectedStockForSell = view.getStockFromDropDown();
    //String selectedDateForSell = view.getSelectedDateForSell();
    String selectedDateForSell = String.valueOf(view.getPerformanceOverTimeEndDate());
    if (selectedDateForSell == null) {
      view.showDialogBox("Please select a valid date");
    } else {
      view.showStockQuantityForSelling(selectedDateForSell,
              selectedStockForSell, stockNames);
    }
  }

  @Override
  public void sellStock() {
    String stockName = view.getStockFromDropDown();
    LocalDate date = view.getPerformanceOverTimeEndDate();
    double quantity = (double) view.getStockQuantity();
    String portfolioName = view.getPortfolioNameFromDropDown();
    /*LocalDate compositionDate = LocalDate.parse(date,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));*/
    if (quantity == 0) {
      view.showDialogBox("Please enter valid stock quantity");
    } else {
      try {
        model.sellStocksFromPortfolio(portfolioName, stockName,
                quantity, date);
        view.showStocksSoldMessage("Stocks from "
                + portfolioName + " sold successfully");
      } catch (IllegalArgumentException | NoSuchElementException e) {
        view.showDialogBox(e.getMessage());
      }
    }
  }

  @Override
  public void inputsForPerformanceOverTime() {
    view.showLabelForMenuItem("Select the portfolio for which you wish"
            + " to view performance over time");
    if (showPortfolioList()) {
      view.showMessage("Select the start date for computing the "
              + "portfolio's performance over time");
      view.showPerformanceStartDateInputDialogBox();
      view.showMessage("Select the end date for computing the "
              + "portfolio's performance over time");
      view.showEndDateDialogBox();
      view.showPerformanceOverTimeButton();
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void getPortfolioInput() {
    view.showLabelForMenuItem("Select the portfolio from which you wish"
            + " to sell stocks");
    if (showPortfolioList()) {
      view.showInvestInPortfolioButton(portfolioName);
    } else {
      view.showMenuMessage();
    }
  }

  @Override
  public void getPortfolioPerformanceOverTime() {
    LocalDate performanceStartDate = view.getPerformanceOverTimeStartDate();
    LocalDate performanceEndDate = view.getPerformanceOverTimeEndDate();

    IPortfolioPerformanceData performanceData = model.getPortfolioPerformanceData(
            view.getPortfolioNameFromDropDown(),
            performanceStartDate,
            performanceEndDate);
    view.showPerformanceOverTimeData(performanceData);
  }

  @Override
  public void getInvestmentAmountInputs() {
    String portfolioName = view.getPortfolioNameFromDropDown();
    view.showTextFieldForInvestmentAmount(portfolioName);
    view.showSelectStocksForInvestmentButton();
  }

  @Override
  public void getStockInputToInvest() {
    String amount = view.getValueFromInvestmentTextField();
    List<String> stockList = model.getListOfSupportedStocks();
    view.showStocksListForInvestment(amount, stockList);
    view.showInvestMoreButton();
  }

  @Override
  public void investInExistingPortfolio() {
    double stockQuantity = (double) view.getStockQuantity();
    String stockName = view.getStockFromDropDown();
    String amount = view.getValueFromInvestmentTextField();
    getDate();
    portfolioName = view.getPortfolioNameFromDropDown();
    try {
      totalAmountInvestedSoFar += stockQuantity;

      if (totalAmountInvestedSoFar > 100) {
        view.showDialogBox("Please enter the valid number of percentage amount");
        totalAmountInvestedSoFar -= stockQuantity;
      } else {
        float numberOfShares = model.calculateNumberOfShares(stockName, compositionDate,
                (float) ((stockQuantity / 100) * Float.parseFloat(amount)));
        model.addStockToPortfolio(portfolioName, stockName, numberOfShares,
                compositionDate.toString());
        if (totalAmountInvestedSoFar != 100) {
          view.clearScreen();
          view.showDialogBox("Amount invested successfully");
          getStockInputToInvest();
        } else {
          view.showDialogBox("The amount has been invested successfully");
          view.showMenuMessage();
          totalAmountInvestedSoFar = 0;
        }
      }
    } catch (NoSuchElementException e) {
      view.showDialogBox(e.getMessage());
      totalAmountInvestedSoFar -= stockQuantity;
    }
  }

  @Override
  public void createPortfolioWithDollarCostAveraging() {
    String portfolioName = view.getPortfolioNameFromTextField();
    float investmentAmount = Float.parseFloat(view.getValueFromInvestmentTextField());
    LocalDate endDate;
    getDate();
    endDate = view.getPerformanceOverTimeEndDate();
    if (endDate.equals(LocalDate.of(1999, 1, 1))) {
      endDate = LocalDate.now().minusDays(1);
    }

    int intervalDays = (int) view.getIntervalDays();

    if (portfolioName.isEmpty()) {
      view.showDialogBox("Portfolio name cannot be empty");
    } else if (investmentAmount == 0) {
      view.showDialogBox("Please enter valid investment amount");
    } else if (intervalDays == 0) {
      view.showDialogBox("Please enter valid interval days");
    } else {
      try {
        model.createPortfolioWithDollarCostAveraging(portfolioName,
                investmentAmount,
                compositionDate,
                endDate,
                intervalDays,
                stockNamePercentageAmount);
        view.showDialogBox("Portfolio " + portfolioName + " created successfully");
        stockNamePercentageAmount = new HashMap<>();
        view.addShowMenuToPanel();
      } catch (IllegalArgumentException e) {
        view.showDialogBox(e.getMessage());
      }
    }
  }

  @Override
  public void showSupportedStocks() {
    List<String> supportedStocks = model.getListOfSupportedStocks();
    view.clearScreen();
    view.showStockListDropDown(supportedStocks);
    view.showList();
  }

  @Override
  public void setCommissionFee() {
    String commissionFee = view.getCommissionFeeFromTextField();
    String commissionFeePercentage = view.getCommissionFeePercentageFromTextField();
    try {
      if (commissionFeePercentage.isEmpty() || commissionFee.isEmpty()) {
        view.showDialogBox("Please enter valid input values");
      } else {
        if (commissionFee.equals("0")) {
          model.setCommissionFee(Float.parseFloat(commissionFee), 0);
        } else {
          model.setCommissionFee(Float.parseFloat(commissionFee),
                  Float.parseFloat(commissionFeePercentage));
          view.addShowMenuToPanel();
        }
      }
    } catch (NumberFormatException e) {
      view.showDialogBox("Please enter valid input values");
    } catch (IllegalArgumentException e) {
      view.showDialogBox(e.getMessage());
    }
  }

  @Override
  public void getInputsForDollarCostAveraging() {
    view.showTextFieldsForInputs();
  }

  @Override
  public void showEndDateDialogBox() {
    view.removePreviousMessage();
    view.showEndDateDialogBox();
    view.showStocksButton();
  }

  @Override
  public void showAddStocksButton() {
    try {
      String portfolioName = view.getPortfolioNameFromTextField();
      float investmentAmount = Float.parseFloat(view.getValueFromInvestmentTextField());
      int intervalDays = (int) view.getIntervalDays();
      if (portfolioName.isEmpty()) {
        view.showDialogBox("Portfolio name cannot be empty");
      } else if (investmentAmount == 0) {
        view.showDialogBox("Please enter valid investment amount");
      } else if (intervalDays == 0) {
        view.showDialogBox("Please enter valid interval days");
      } else {
        view.clearScreen();
        view.showStocksButton();
      }
    } catch (NumberFormatException e) {
      view.showDialogBox("Please enter valid input values " + e.getMessage());
    }
  }

  @Override
  public void getStockInputsForDollarCost(String stockName) {
    List<String> stockList = model.getListOfSupportedStocks();
    view.showSupportedStockList(stockList, stockName);
  }

  @Override
  public void createMapForDollarCost() {
    double percentageAmountForStock = (double) view.getStockQuantity();
    String stockName = view.getStockName();
    if (percentageAmountForStock == 0) {
      view.showDialogBox("Please enter valid number of stocks");
    } else {
      if (totalAmountInvestedSoFar > 100) {
        view.showDialogBox("Please enter the valid number of percentage amount");
        totalAmountInvestedSoFar -= percentageAmountForStock;
      } else {
        stockNamePercentageAmount.put(stockName, (float) percentageAmountForStock);
        totalAmountInvestedSoFar += percentageAmountForStock;
        if (totalAmountInvestedSoFar != 100) {
          getStockInputsForDollarCost(stockName);
        } else {
          totalAmountInvestedSoFar = 0;
          view.addCreatePortfolioButton();
        }
      }
    }
  }

  @Override
  public void getStocksOfPortfolio(String portfolioName) {

    if (portfolioName.isEmpty()) {
      view.showDialogBox("Portfolio name cannot be empty!");
      return;
    }
    List<String> portfolio = model.getPortfolioNames();
    if (!portfolio.contains(portfolioName)) {
      view.showDialogBox("Portfolio name not found. Enter valid portfolio Name");
      return;
    }
    Map<String, Double> stockData = new HashMap<>();
    String stockNames = "";
    DateTimeFormatter ff = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    getDate();
    if (compositionDate.compareTo(LocalDate.now()) > 0) {
      view.showDialogBox("Date cannot be in the future");
      return;
    }
    for (IStock is : model.getStockFromPortfolio(portfolioName)) {
      if (LocalDate.parse(is.getPurchaseDate(), ff).compareTo(compositionDate) <= 0) {
        if (stockData.containsKey(is.getStockName())) {
          stockData.put(is.getStockName(), stockData.get(is.getStockName()) + is.getStockQuantity());
        } else {
          stockData.put(is.getStockName(), (double) is.getStockQuantity());
          stockNames += is.getStockName() + ",";
        }
      }
    }
    if (stockNames.isEmpty()) {
      view.showDialogBox("No stocks exists in the portfolio on the given date");
      return;
    }
    view.displayStocks(stockNames);
  }

  private boolean showPortfolioList() {
    List<String> portfolio = model.getPortfolioNames();
    if (portfolio.isEmpty()) {
      view.showDialogBox("No portfolios created!");
      return false;
    } else {
      view.showPortfolioList(portfolio);
      return true;
    }
  }

  private void getDate() {
    String year = view.getYearFromDropDown();
    String month = view.getMonthFromDropDown();
    String formattedMonth = String.format("%02d", Integer.valueOf(month));
    String day = view.getDayFromDropDown();
    String formattedDay = String.format("%02d", Integer.valueOf(day));
    String date = year + "-" + formattedMonth + "-" + formattedDay;
    compositionDate = LocalDate.parse(date,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
