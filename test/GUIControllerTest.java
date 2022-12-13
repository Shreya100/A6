import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import guicontroller.Features;
import guicontroller.GUIController;
import guiview.IGUIView;
import model.IModel;
import model.IPortfolioPerformanceData;
import model.IStock;

import static org.junit.Assert.assertEquals;

/**
 * Junit tests for GUI Controller .
 */
public class GUIControllerTest {

  /**
   * Mock Model for GUI Controller.
   */
  class MockGUIView implements IGUIView {

    StringBuilder log;

    /**
     * Constructor that initializes the log.
     * @param log logging object.
     */
    public MockGUIView(StringBuilder log) {
      this.log = log;
    }


    @Override
    public void addFeatures(Features features) {
      // do nothing.
    }

    @Override
    public void showRetrievedPortfolios(List<String> portfolios) {
      // do nothing.
    }

    @Override
    public void selectPortfolioFile() {
      // do nothing.
    }

    @Override
    public void showMenuMessage() {
      // do nothing.
    }

    @Override
    public void showPortfolioReBalanceMessage(String portfolioName) {
      // do nothing.
    }

    @Override
    public void displayStocks(String stockNames) {
      // do nothing.
    }

    @Override
    public void showDialogBox(String s) {
      log.append(s);
    }

    @Override
    public String getPortfolioFilenameFromTextField() {
      return null;
    }

    @Override
    public String getPortfolioNameFromTextField() {
      return null;
    }

    @Override
    public void showPortfolioCreatedMessage(String portfolioName) {
      // do nothing.
    }

    @Override
    public void showDateInputDialogBox() {
      // do nothing.
    }

    @Override
    public void showPortfolioList(List<String> portfolio) {
      // do nothing.
    }

    @Override
    public String getYearFromDropDown() {
      return "2022";
    }

    @Override
    public String getMonthFromDropDown() {
      return "12";
    }

    @Override
    public String getDayFromDropDown() {
      return "30";
    }

    @Override
    public void showExaminePortfolioData(List<IStock> portfolioData) {
      // do nothing.
    }

    @Override
    public void showFileDialogForPortfolioFile() {
      // do nothing.
    }

    @Override
    public void showLabelForMenuItem(String labelMessage) {
      // do nothing.
    }

    @Override
    public void showMessage(String message) {
      // do nothing.
    }

    @Override
    public void showExamineForADateButton() {
      // do nothing.
    }

    @Override
    public void showReBalanceForADateButton() {
      // do nothing.
    }

    @Override
    public void showCostBasisButton() {
      // do nothing.
    }

    @Override
    public String getPortfolioNameFromDropDown() {
      return null;
    }

    @Override
    public void showCostBasisValueMessage(String s) {
      // do nothing.
    }

    @Override
    public void showExamineButton() {
      // do nothing.
    }

    @Override
    public void showReBalanceButton() {
      // do nothing.
    }

    @Override
    public void showPortfolioValueButton() {
      // do nothing.
    }

    @Override
    public void showPortfolioValueMessage(String s) {
      // do nothing.
    }

    @Override
    public void showMessageForStock(String s) {
      // do nothing.
    }

    @Override
    public void showQuantityMessage(String s) {
      // do nothing.
    }

    @Override
    public void showAddStockButton() {
      // do nothing.
    }

    @Override
    public void showStockListDropDown(List<String> supportedStock) {
      // do nothing.
    }

    @Override
    public void showQuantitySpinner() {
      // do nothing.
    }

    @Override
    public String getStockName() {
      return null;
    }

    @Override
    public Object getStockQuantity() {
      return null;
    }

    @Override
    public void showStockAddedMessage(String s) {
      // do nothing.
    }

    @Override
    public void stockSymbolFromPortfolio(List<IStock> stockNames, String portfolioName) {
      // do nothing.
    }

    @Override
    public String getStockFromDropDown() {
      return null;
    }

    @Override
    public void showDatesForSelling(String selectedStockForSell, List<IStock> stockNames) {
      // do nothing.
    }

    @Override
    public String getSelectedDateForSell() {
      return null;
    }

    @Override
    public void showStockQuantityForSelling(String selectedDateForSell,
                                            String selectedStockForSell, List<IStock> stockNames) {
      // do nothing.
    }

    @Override
    public void showStockContinueButton() {
      // do nothing.
    }

    @Override
    public void showStocksSoldMessage(String s) {
      // do nothing.
    }

    @Override
    public void showPerformanceOverTimeData(IPortfolioPerformanceData performanceData) {
      // do nothing.
    }

    @Override
    public void showPerformanceStartDateInputDialogBox() {
      // do nothing.
    }

    @Override
    public void showPerformanceOverTimeButton() {
      // do nothing.
    }

    @Override
    public LocalDate getPerformanceOverTimeStartDate() {
      return null;
    }

    @Override
    public void showEndDateDialogBox() {
      // do nothing.
    }

    @Override
    public LocalDate getPerformanceOverTimeEndDate() {
      return null;
    }

    @Override
    public void showInvestInPortfolioButton(String portfolioName) {
      // do nothing.
    }

    @Override
    public void showTextFieldForInvestmentAmount(String portfolioName) {
      // do nothing.
    }

    @Override
    public String getValueFromInvestmentTextField() {
      return null;
    }

    @Override
    public void showSelectStocksForInvestmentButton() {
      // do nothing.
    }

    @Override
    public void showStocksListForInvestment(String amount, List<String> stockList) {
      // do nothing.
    }

    @Override
    public void showInvestMoreButton() {
      // do nothing.
    }

    @Override
    public void clearScreen() {
      // do nothing.
    }

    @Override
    public void showList() {
      // do nothing.
    }

    @Override
    public void getCommissionFee() {
      // do nothing.
    }

    @Override
    public String getCommissionFeeFromTextField() {
      return null;
    }

    @Override
    public String getCommissionFeePercentageFromTextField() {
      return null;
    }

    @Override
    public void addShowMenuToPanel() {
      // do nothing.
    }

    @Override
    public void showTextFieldsForInputs() {
      // do nothing.
    }

    @Override
    public void showTextFieldsForInputsRebalance() {
      // do nothing.
    }

    @Override
    public void showStocksButton() {
      // do nothing.
    }

    @Override
    public void removePreviousMessage() {
      // do nothing.
    }

    @Override
    public void showSupportedStockList(List<String> stockList, String stockName) {
      // do nothing.
    }

    @Override
    public void addCreatePortfolioButton() {
      // do nothing.
    }

    @Override
    public Object getIntervalDays() {
      return null;
    }
  }

  StringBuilder viewLog = new StringBuilder();
  StringBuilder modelLog = new StringBuilder();

  IModel model;
  IGUIView view;
  GUIController controller;

  @Before
  public void setUp() throws Exception {
    model = new ControllerTest.MockModel(modelLog);
    view = new MockGUIView(viewLog);
    controller = new GUIController(model);
    controller.setView(view);
  }


  @Test
  public void reBalancePortfolioWithDate() {

    Map<String, Double> stocks = new HashMap<String, Double>();
    stocks.put("Stock1", 90.0);
    stocks.put("Stock2", 10.0);
    controller.reBalancePortfolioWithDate(stocks);
    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("Portfolio Name: nullDate: 2022-12-30");
    for (Map.Entry<String, Double> stock : stocks.entrySet()) {
      expectedOutput.append(stock.getKey() + " :" + stock.getValue());
    }

    assertEquals(expectedOutput.toString(), modelLog.toString());

  }

  @Test
  public void getStocksOfPortfolio() {
    controller.getStocksOfPortfolio("portfolio1");
    assertEquals("Portfolio name: portfolio1", modelLog.toString());
  }

  @Test
  public void getStocksOfPortfolioInvalidPortfolioName() {
    controller.getStocksOfPortfolio("portfolio3");
    assertEquals("Portfolio name not found. Enter valid portfolio Name",
            viewLog.toString());
  }

  @Test
  public void getStocksOfPortfolioEmptyPortfolioName() {
    controller.getStocksOfPortfolio("");
    assertEquals("Portfolio name cannot be empty!", viewLog.toString());
  }

}