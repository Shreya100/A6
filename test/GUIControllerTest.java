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

public class GUIControllerTest {

  class MockGUIView implements IGUIView {

    StringBuilder log;

    public MockGUIView(StringBuilder log) {
      this.log = log;
    }


    @Override
    public void addFeatures(Features features) {

    }

    @Override
    public void showRetrievedPortfolios(List<String> portfolios) {

    }

    @Override
    public void selectPortfolioFile() {

    }

    @Override
    public void showMenuMessage() {

    }

    @Override
    public void showPortfolioReBalanceMessage(String portfolioName) {

    }

    @Override
    public void displayStocks(String stockNames) {

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

    }

    @Override
    public void showDateInputDialogBox() {

    }

    @Override
    public void showPortfolioList(List<String> portfolio) {

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

    }

    @Override
    public void showFileDialogForPortfolioFile() {

    }

    @Override
    public void showLabelForMenuItem(String labelMessage) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showExamineForADateButton() {

    }

    @Override
    public void showReBalanceForADateButton() {

    }

    @Override
    public void showCostBasisButton() {

    }

    @Override
    public String getPortfolioNameFromDropDown() {
      return null;
    }

    @Override
    public void showCostBasisValueMessage(String s) {

    }

    @Override
    public void showExamineButton() {

    }

    @Override
    public void showReBalanceButton() {

    }

    @Override
    public void showPortfolioValueButton() {

    }

    @Override
    public void showPortfolioValueMessage(String s) {

    }

    @Override
    public void showMessageForStock(String s) {

    }

    @Override
    public void showQuantityMessage(String s) {

    }

    @Override
    public void showAddStockButton() {

    }

    @Override
    public void showStockListDropDown(List<String> supportedStock) {

    }

    @Override
    public void showQuantitySpinner() {

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

    }

    @Override
    public void stockSymbolFromPortfolio(List<IStock> stockNames, String portfolioName) {

    }

    @Override
    public String getStockFromDropDown() {
      return null;
    }

    @Override
    public void showDatesForSelling(String selectedStockForSell, List<IStock> stockNames) {

    }

    @Override
    public String getSelectedDateForSell() {
      return null;
    }

    @Override
    public void showStockQuantityForSelling(String selectedDateForSell, String selectedStockForSell, List<IStock> stockNames) {

    }

    @Override
    public void showStockContinueButton() {

    }

    @Override
    public void showStocksSoldMessage(String s) {

    }

    @Override
    public void showPerformanceOverTimeData(IPortfolioPerformanceData performanceData) {

    }

    @Override
    public void showPerformanceStartDateInputDialogBox() {

    }

    @Override
    public void showPerformanceOverTimeButton() {

    }

    @Override
    public LocalDate getPerformanceOverTimeStartDate() {
      return null;
    }

    @Override
    public void showEndDateDialogBox() {

    }

    @Override
    public LocalDate getPerformanceOverTimeEndDate() {
      return null;
    }

    @Override
    public void showInvestInPortfolioButton(String portfolioName) {

    }

    @Override
    public void showTextFieldForInvestmentAmount(String portfolioName) {

    }

    @Override
    public String getValueFromInvestmentTextField() {
      return null;
    }

    @Override
    public void showSelectStocksForInvestmentButton() {

    }

    @Override
    public void showStocksListForInvestment(String amount, List<String> stockList) {

    }

    @Override
    public void showInvestMoreButton() {

    }

    @Override
    public void clearScreen() {

    }

    @Override
    public void showList() {

    }

    @Override
    public void getCommissionFee() {

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

    }

    @Override
    public void showTextFieldsForInputs() {

    }

    @Override
    public void showTextFieldsForInputsRebalance() {

    }

    @Override
    public void showStocksButton() {

    }

    @Override
    public void removePreviousMessage() {

    }

    @Override
    public void showSupportedStockList(List<String> stockList, String stockName) {

    }

    @Override
    public void addCreatePortfolioButton() {

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
    assertEquals("Portfolio name not found. Enter valid portfolio Name", viewLog.toString());
  }

  @Test
  public void getStocksOfPortfolioEmptyPortfolioName() {
    controller.getStocksOfPortfolio("");
    assertEquals("Portfolio name cannot be empty!", viewLog.toString());
  }

}