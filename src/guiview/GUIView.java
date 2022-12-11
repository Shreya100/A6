package guiview;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import guicontroller.Features;
import model.IPortfolioPerformanceData;
import model.IStock;
import model.PortfolioPerformanceData;

/**
 * This class represents the GUI view and implements the methods
 * to add, modify components to the frame.
 * This class extends JFrame class and implements IGUIView interface.
 */

public class GUIView extends JFrame implements IGUIView {
  private final JMenuItem createNewPortfolio;
  private final JMenuItem examinePortfolioOnASpecificDate;
  private final JMenuItem examinePortfolio;
  private final JMenuItem portfolioValue;
  private final JMenuItem addStock;
  private final JMenuItem sellStock;
  private final JMenuItem portfolioPerformanceOverTime;
  private final JMenuItem investInExistingPortfolio;
  private final JMenuItem showListOfSupportedStocks;
  private final JMenuItem exit;
  private final JMenuItem createNewPortfolioWithCostAveraging;
  private JTextField portfolioFileName;
  private JLabel displayMessage;
  private JTextField textInput;
  private JTextField investmentAmountTextField;
  private final JButton yes;
  private final JButton no;
  private final JLabel labelForGettingPortfolioName;
  private JPanel panel;
  private final JButton createPortfolioButton;
  private final JButton examinePortfolioForADate;
  private final JButton examinePortfolioButton;
  private final JButton costBasisButton;
  private final JButton selectPortfolioFileButton;
  private final JButton getPortfolioValueButton;
  private final JButton addStockButton;
  private final JButton sellStockButton;
  private final JButton selectStockToSellButton;
  private final JButton addMore;
  private final JButton selectDateToSellButton;
  private final JButton selectQuantityToSellButton;
  private final JButton portfolioPerformanceOverTimeButton;
  private final JButton investInPortfolioButton;
  private final JButton selectStocksButton;
  private final JButton investMoreButton;
  private final JButton yesEndDate;
  private final JButton noEndDate;
  private final JButton addStockToNewPortfolioButton;
  private final JButton createPfWithCostAveragingButton;
  private JComboBox yearDropDown;
  private JComboBox monthDropDown;
  private JComboBox dayDropDown;
  private JComboBox performanceStartYear;
  private JComboBox performanceStartMonth;
  private JComboBox performanceStartDay;
  private JComboBox performanceEndYear;
  private JComboBox performanceEndMonth;
  private JComboBox performanceEndDay;
  private JComboBox supportedStockDropDown;
  private JComboBox portfolioListDropDown;
  private JComboBox stockFromPortfolioDropDown;
  private JComboBox supportedDatesDropDown;
  private final JMenuItem getCostBasis;
  private SpinnerModel model;
  private JSpinner spinner;
  private JSpinner intervalDaysSpinner;
  private JLabel investmentAmountLabel;
  private JLabel selectStockLabel;
  private JTextField commissionTextField;
  private JTextField commissionFeePercentageTextField;
  private final JButton submitButton;


  /**
   * This is a public constructor that initializes JFrame and
   * all the components used by the program.
   *
   * @param caption title of the JFrame
   */

  public GUIView(String caption) {
    super(caption);

    setSize(700, 300);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Initializing the components that will be used later.
    panel = new JPanel();
    createPortfolioButton = new JButton("Create");
    createPortfolioButton.setForeground(Color.blue);
    createNewPortfolio = new JMenuItem("Create new portfolio");
    selectPortfolioFileButton = new JButton("Select file");
    selectPortfolioFileButton.setForeground(Color.blue);

    examinePortfolioOnASpecificDate = new JMenuItem("Examine "
            + "portfolio on a specific date");
    examinePortfolio = new JMenuItem("Examine portfolio");
    labelForGettingPortfolioName = new JLabel("Enter the portfolio name");
    displayMessage = new JLabel("Do you want to retrieve existing portfolios?");
    examinePortfolioForADate = new JButton("Examine Portfolio");
    examinePortfolioButton = new JButton("Examine Portfolio");
    examinePortfolioForADate.setForeground(Color.blue);
    examinePortfolioButton.setForeground(Color.blue);

    yes = new JButton("Yes");
    no = new JButton("No");
    yes.setForeground(Color.blue);
    no.setForeground(Color.blue);
    submitButton = new JButton("Submit");
    submitButton.setForeground(Color.blue);

    getCostBasis = new JMenuItem("Get cost basis of a portfolio");
    costBasisButton = new JButton("Get Cost Basis");
    costBasisButton.setForeground(Color.blue);

    portfolioValue = new JMenuItem("Get portfolio value");
    getPortfolioValueButton = new JButton("Get value");

    addStock = new JMenuItem("Add stocks to a portfolio");
    addStockButton = new JButton("Add Stock");

    sellStock = new JMenuItem("Sell stocks from a portfolio");
    selectDateToSellButton = new JButton("Select date to sell");
    selectQuantityToSellButton = new JButton("Select quantity to sell");
    selectStockToSellButton = new JButton("Select stock to sell");
    sellStockButton = new JButton("Sell Stocks");

    portfolioPerformanceOverTime = new JMenuItem("View a portfolio's "
            + "performance over time");
    portfolioPerformanceOverTimeButton = new JButton("Show Performance");

    investInExistingPortfolio = new JMenuItem("Invest in an existing portfolio");
    investInPortfolioButton = new JButton("Invest in portfolio");
    selectStocksButton = new JButton("Select stocks");
    investMoreButton = new JButton("Invest");
    selectStockLabel = new JLabel();

    createNewPortfolioWithCostAveraging = new JMenuItem("Create a new portfolio with "
            + "dollar cost averaging");
    yesEndDate = new JButton("Yes");
    noEndDate = new JButton("No");
    addStockToNewPortfolioButton = new JButton("Add stock");
    createPfWithCostAveragingButton = new JButton("Create portfolio");
    addMore = new JButton("Add more");

    showListOfSupportedStocks = new JMenuItem("Show list of supported stocks");

    exit = new JMenuItem("Exit");

    String[] yearRange = new String[24];
    for (int i = 1999, j = 0; i <= 2022; i++, j++) {
      yearRange[j] = String.valueOf(i);
    }
    yearDropDown = new JComboBox(yearRange);
    performanceStartYear = new JComboBox(yearRange);
    performanceEndYear = new JComboBox(yearRange);

    String[] monthRange = new String[12];
    for (int i = 0; i < 12; i++) {
      monthRange[i] = String.valueOf(i + 1);
    }
    monthDropDown = new JComboBox(monthRange);
    performanceStartMonth = new JComboBox(monthRange);
    performanceEndMonth = new JComboBox(monthRange);

    String[] dayRange = new String[31];
    for (int i = 0; i < 31; i++) {
      dayRange[i] = String.valueOf(i + 1);
    }
    dayDropDown = new JComboBox(dayRange);
    performanceStartDay = new JComboBox(dayRange);
    performanceEndDay = new JComboBox(dayRange);

    panel.add(displayMessage);
    panel.add(yes);
    panel.add(no);
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    add(panel);
    setVisible(true);
    setResizable(false);
  }

  @Override
  public void addFeatures(Features features) {
    yes.addActionListener(e -> features.retrieveExistingPortfolio());
    no.addActionListener(e -> features.notRetrievingPortfolio());
    exit.addActionListener(e -> System.exit(0));
    createNewPortfolio.addActionListener(e -> features.inputsForCreatingNewPortfolio());
    createPortfolioButton.addActionListener(e -> features.createPortfolio());
    selectPortfolioFileButton.addActionListener(e -> features.portfolioFileInput());
    examinePortfolioOnASpecificDate.addActionListener(e ->
            features.examinePortfolioOnASpecificDate());
    examinePortfolio.addActionListener(e -> features.examinePortfolio());
    examinePortfolioForADate.addActionListener(e -> features.examinePortfolioWithDate());
    examinePortfolioButton.addActionListener(e -> features.examinePortfolioWithoutDate());
    getCostBasis.addActionListener(e -> features.selectPortfolioForCostBasis());
    costBasisButton.addActionListener(e -> features.calculateCostBasis());
    portfolioValue.addActionListener(e -> features.getInputsForPortfolioValue());
    getPortfolioValueButton.addActionListener(e -> features.getPortfolioValue());
    addStock.addActionListener(e -> features.getInputsForAddStock());
    addStockButton.addActionListener(e -> features.addStockToPortfolio());
    sellStock.addActionListener(e -> features.inputsForSellingStock());
    selectStockToSellButton.addActionListener(e -> features.getStockToSell());
    selectDateToSellButton.addActionListener(e -> features.getDateInputForSell());
    selectQuantityToSellButton.addActionListener(e -> features.getQuantityInputForSell());
    sellStockButton.addActionListener(e -> features.sellStock());
    portfolioPerformanceOverTime.addActionListener(e -> features.inputsForPerformanceOverTime());
    portfolioPerformanceOverTimeButton.addActionListener(e ->
            features.getPortfolioPerformanceOverTime());
    investInExistingPortfolio.addActionListener(e -> features.getPortfolioInput());
    investInPortfolioButton.addActionListener(e -> features.getInvestmentAmountInputs());
    selectStocksButton.addActionListener(e -> features.getStockInputToInvest());
    investMoreButton.addActionListener(e -> features.investInExistingPortfolio());
    showListOfSupportedStocks.addActionListener(e -> features.showSupportedStocks());
    submitButton.addActionListener(e -> features.setCommissionFee());
    createNewPortfolioWithCostAveraging.addActionListener(e ->
            features.getInputsForDollarCostAveraging());
    yesEndDate.addActionListener(e -> features.showEndDateDialogBox());
    noEndDate.addActionListener(e -> features.showAddStocksButton());
    addStockToNewPortfolioButton.addActionListener(e -> features.getStockInputsForDollarCost(""));
    createPfWithCostAveragingButton.addActionListener(e ->
            features.createPortfolioWithDollarCostAveraging());
    addMore.addActionListener(e -> features.createMapForDollarCost());
  }

  @Override
  public void showRetrievedPortfolios(List<String> portfolios) {
    if (portfolios.size() > 0) {
      String retrievedPortfolios = String.join(",", portfolios);
      displayMessage.setText("The following portfolios have been retrieved successfully: \n"
              + retrievedPortfolios + "\n");
    } else {
      displayMessage.setText("No portfolios could be retrieved because no files were found.\n");
    }
    panel.removeAll();
    panel.add(displayMessage);

    add(panel);
    setVisible(true);
  }

  @Override
  public void showMenuMessage() {
    panel.removeAll();
    displayMessage.setText("Please select an option from the menu.");
    revalidate();
    repaint();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
  }

  @Override
  public void selectPortfolioFile() {
    resetComponents();
    JLabel selectPortfolioFileMessage = new JLabel("Select CSV file for portfolio");
    panel.removeAll();
    repaint();
    panel.add(selectPortfolioFileMessage);
    panel.add(selectPortfolioFileButton);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showFileDialogForPortfolioFile() {
    portfolioFileName = new JTextField(20);
    panel.removeAll();
    getContentPane().removeAll();
    FileDialog fileDialogForGettingPortfolioFile = new FileDialog(this,
            "Select portfolio file", FileDialog.LOAD);
    fileDialogForGettingPortfolioFile.setVisible(true);
    String filename = fileDialogForGettingPortfolioFile.getFile();
    if (filename != null) {
      displayMessage.setText("Selected portfolio file is:- ");
      portfolioFileName.setText(fileDialogForGettingPortfolioFile.getDirectory()
              + fileDialogForGettingPortfolioFile.getFile());
      portfolioFileName.setEditable(false);
      panel.add(displayMessage);
      panel.add(portfolioFileName);
      add(panel);
      setVisible(true);
      getPortfolioName();
    } else {
      selectPortfolioFile();
    }
  }

  @Override
  public void showLabelForMenuItem(String labelMessage) {
    resetComponents();
    panel.removeAll();
    repaint();
    JLabel portfolioExamineMessage = new JLabel(labelMessage);
    panel.add(portfolioExamineMessage);
    add(panel);
    setVisible(true);
  }

  private String getPortfolioName() {
    textInput = new JTextField(10);
    panel.add(labelForGettingPortfolioName);
    panel.add(textInput);
    panel.add(createPortfolioButton);
    setVisible(true);
    setSize(500, 300);
    return textInput.getText();
  }

  @Override
  public void showPortfolioCreatedMessage(String portfolioName) {
    displayMessage.setText("Portfolio created successfully");
    panel.removeAll();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
    setSize(700, 300);
  }

  @Override
  public void showDialogBox(String s) {
    JOptionPane.showMessageDialog(panel, s);
    setVisible(true);
  }

  @Override
  public void showPortfolioList(List<String> portfolioNames) {
    String[] portfolioList = new String[portfolioNames.size()];
    int i = 0;
    for (String portfolioName : portfolioNames) {
      portfolioList[i] = portfolioName;
      i++;
    }
    portfolioListDropDown = new JComboBox(portfolioList);
    panel.add(portfolioListDropDown);
    add(panel);
    setResizable(false);
    setVisible(true);
    setSize(450, 300);
  }

  @Override
  public void showDateInputDialogBox() {
    JLabel year = new JLabel("Year");
    JLabel month = new JLabel("Month");
    JLabel day = new JLabel("Day");

    panel.add(displayMessage);
    panel.add(year);
    panel.add(yearDropDown);
    panel.add(month);
    panel.add(monthDropDown);
    panel.add(day);
    panel.add(dayDropDown);

    add(panel);
    setSize(450, 400);
  }


  @Override
  public void showExamineForADateButton() {
    panel.add(examinePortfolioForADate);
  }

  @Override
  public void showCostBasisButton() {
    panel.add(costBasisButton);
  }

  @Override
  public String getPortfolioNameFromDropDown() {
    return (String) portfolioListDropDown.getSelectedItem();
  }

  @Override
  public void showCostBasisValueMessage(String s) {
    displayMessage.setText(s);
    panel.removeAll();
    repaint();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showExamineButton() {
    panel.add(examinePortfolioButton);
  }

  @Override
  public void showPortfolioValueButton() {
    panel.add(getPortfolioValueButton);
  }

  @Override
  public void showPortfolioValueMessage(String s) {
    displayMessage.setText(s);
    panel.removeAll();
    repaint();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showMessageForStock(String s) {
    JLabel label = new JLabel(s);
    panel.add(label);
  }

  @Override
  public void showQuantityMessage(String s) {
    JLabel label = new JLabel(s);
    panel.add(label);
  }

  @Override
  public void showAddStockButton() {
    panel.add(addStockButton);
  }

  @Override
  public void showStockListDropDown(List<String> supportedStock) {
    String[] supportedStockList = new String[supportedStock.size()];
    int i = 0;
    for (String stock : supportedStock) {
      supportedStockList[i] = stock;
      i++;
    }
    supportedStockDropDown = new JComboBox(supportedStockList);
    panel.add(supportedStockDropDown);
  }

  @Override
  public void showQuantitySpinner() {
    model = new SpinnerNumberModel(0, 0, 200, 1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(new Dimension(50, 30));
    panel.add(spinner);
  }

  @Override
  public String getStockName() {
    return (String) supportedStockDropDown.getSelectedItem();
  }

  @Override
  public Object getStockQuantity() {
    return spinner.getValue();
  }

  @Override
  public void showStockAddedMessage(String s) {
    displayMessage.setText(s);
    panel.removeAll();
    repaint();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
  }

  @Override
  public void stockSymbolFromPortfolio(List<IStock> stockNames, String portfolioName) {
    panel.removeAll();
    repaint();
    JLabel label = new JLabel("Selected portfolio is " + portfolioName);
    panel.add(label);
    String[] stockList = new String[stockNames.size()];
    int i = 0;
    for (IStock stock : stockNames) {
      if (Arrays.stream(stockList).noneMatch(stock.getStockName()::equals)) {
        stockList[i] = stock.getStockName();
        i++;
      }
    }

    stockFromPortfolioDropDown = new JComboBox(stockList);
    panel.add(stockFromPortfolioDropDown);
    panel.add(selectDateToSellButton);
    add(panel);
    setVisible(true);
    setSize(300, 400);
  }

  @Override
  public String getStockFromDropDown() {
    return (String) stockFromPortfolioDropDown.getSelectedItem();
  }

  @Override
  public void showDatesForSelling(String selectedStockForSell, List<IStock> stockNames) {
    JLabel label = new JLabel("Selected stock for sell:- " + selectedStockForSell);
    panel.remove(stockFromPortfolioDropDown);
    panel.remove(selectStockToSellButton);
    panel.remove(selectDateToSellButton);
    repaint();
    panel.add(label);

    /*String[] supportedDates = new String[stockNames.size()];
    int i = 0;
    for (IStock stock : stockNames) {
      if (stock.getStockName().equals(selectedStockForSell)) {
        supportedDates[i] = stock.getPurchaseDate();
        i++;
      }
    }*/

    //supportedDatesDropDown = new JComboBox(supportedDates);
    //panel.add(supportedDatesDropDown);
    showEndDateDialogBox();
    panel.add(selectQuantityToSellButton);
    add(panel);
    setVisible(true);
    setSize(300, 400);
  }

  @Override
  public String getSelectedDateForSell() {
    return (String) supportedDatesDropDown.getSelectedItem();
  }

  @Override
  public void showStockQuantityForSelling(String selectedDateForSell,
      String selectedStockForSell, List<IStock> stockNames) {
    float stockQuantity = 0;

    JLabel label = new JLabel("Selected date for sell:- " + selectedDateForSell);
    //panel.remove(supportedDatesDropDown);
    panel.remove(selectDateToSellButton);
    panel.remove(selectQuantityToSellButton);
    repaint();
    panel.add(label);
    for (IStock stock : stockNames) {
      if (stock.getStockName().equals(selectedStockForSell)) {
        stockQuantity += stock.getStockQuantity();
      }
    }

    model = new SpinnerNumberModel(0, 0, stockQuantity, 0.1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(new Dimension(50, 30));
    panel.add(spinner);
    panel.add(sellStockButton);
    add(panel);
    setVisible(true);
    setSize(300, 400);
  }

  @Override
  public void showStockContinueButton() {
    panel.add(selectStockToSellButton);
  }

  @Override
  public void showStocksSoldMessage(String s) {
    displayMessage.setText(s);
    panel.removeAll();
    repaint();
    panel.add(displayMessage);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showPerformanceOverTimeData(IPortfolioPerformanceData performanceData) {
    panel.removeAll();
    repaint();
    String heading = MessageFormat.format(
            "Performance of portfolio \"{0}\" from {1} to {2}",
            performanceData.getPortfolioName(),
            performanceData.getStartDate(),
            performanceData.getEndDate());

    JLabel headingLabel = new JLabel(heading);
    panel.add(headingLabel);

    // Create dataset
    DefaultCategoryDataset dataset = createPerformanceDataset(performanceData);

    // Create chart
    JFreeChart chart = ChartFactory.createLineChart(
            "Performance Over Time", // Chart title
            "Performance Duration", // X-Axis Label
            "Portfolio Value", // Y-Axis Label
            dataset,
        PlotOrientation.VERTICAL,
        false,
        true,
        false
    );

    ChartPanel chartPanel = new ChartPanel(chart);
    panel.add(chartPanel);

    add(panel);
    setVisible(true);
    setSize(1200, 900);
  }

  @Override
  public void showPerformanceStartDateInputDialogBox() {
    JLabel year = new JLabel("Year");
    JLabel month = new JLabel("Month");
    JLabel day = new JLabel("Day");

    panel.add(new JLabel("Select the start date for "
            + "calculating the performance over time"));
    panel.add(year);
    panel.add(performanceStartYear);
    panel.add(month);
    panel.add(performanceStartMonth);
    panel.add(day);
    panel.add(performanceStartDay);

    add(panel);
    setSize(450, 400);
  }

  @Override
  public void showEndDateDialogBox() {
    JLabel year = new JLabel("Year");
    JLabel month = new JLabel("Month");
    JLabel day = new JLabel("Day");

    panel.add(year);
    panel.add(performanceEndYear);
    panel.add(month);
    panel.add(performanceEndMonth);
    panel.add(day);
    panel.add(performanceEndDay);

    add(panel);
    setSize(450, 400);
  }

  @Override
  public void showPerformanceOverTimeButton() {
    panel.add(portfolioPerformanceOverTimeButton);
  }

  @Override
  public LocalDate getPerformanceOverTimeStartDate() {
    String year = String.format("%02d",
            Integer.valueOf((String) performanceStartYear.getSelectedItem()));
    String month = String.format("%02d",
            Integer.valueOf((String) performanceStartMonth.getSelectedItem()));
    String day = String.format("%02d",
            Integer.valueOf((String) performanceStartDay.getSelectedItem()));
    String startDateString = MessageFormat.format("{0}-{1}-{2}", year, month, day);
    LocalDate startDate = LocalDate.parse(startDateString,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return startDate;
  }

  @Override
  public LocalDate getPerformanceOverTimeEndDate() {
    String year = String.format("%02d",
            Integer.valueOf((String) performanceEndYear.getSelectedItem()));
    String month = String.format("%02d",
            Integer.valueOf((String) performanceEndMonth.getSelectedItem()));
    String day = String.format("%02d",
            Integer.valueOf((String) performanceEndDay.getSelectedItem()));
    String endDateString = MessageFormat.format("{0}-{1}-{2}", year, month, day);
    LocalDate endDate = LocalDate.parse(endDateString,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return endDate;
  }

  private DefaultCategoryDataset createPerformanceDataset(
          IPortfolioPerformanceData performanceData) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (PortfolioPerformanceData.PerformanceDataFrame dataFrame :
            performanceData.getPerformanceData()) {
      dataset.addValue(dataFrame.getPortfolioValue(),
              "Portfolio Value",
              dataFrame.getTimeFrame());
    }

    return dataset;
  }

  @Override
  public void showInvestInPortfolioButton(String portfolioName) {
    panel.add(investInPortfolioButton);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showTextFieldForInvestmentAmount(String portfolioName) {
    JLabel label = new JLabel("Selected portfolio is " + portfolioName);
    investmentAmountTextField = new JTextField(5);
    investmentAmountLabel = new JLabel("Enter the amount you wish to invest");
    panel.removeAll();
    repaint();
    panel.add(label);
    panel.add(investmentAmountLabel);
    panel.add(investmentAmountTextField);
    add(panel);
    setSize(320, 500);
    setVisible(true);
  }

  @Override
  public String getValueFromInvestmentTextField() {
    return investmentAmountTextField.getText();
  }

  @Override
  public void showSelectStocksForInvestmentButton() {
    panel.remove(investInPortfolioButton);
    repaint();
    panel.add(selectStocksButton);
    add(panel);
    setVisible(true);
  }

  @Override
  public void showStocksListForInvestment(String amount, List<String> stockList) {
    panel.remove(selectStocksButton);
    panel.remove(investmentAmountTextField);
    panel.remove(investmentAmountLabel);
    repaint();
    if (amount.isEmpty()) {
      showDialogBox("Please enter the amount to be invested");
    } else {
      JLabel label = new JLabel("The amount to be invested:- $" + amount);
      panel.add(label);
      showInputsForInvestment(stockList);
      showDateInputDialogBox();
      add(panel);
      setVisible(true);
      setSize(300, 400);
    }
  }

  @Override
  public void showInvestMoreButton() {
    panel.add(investMoreButton);
    add(panel);
    setVisible(true);
    setSize(350, 400);
  }

  @Override
  public void clearScreen() {
    panel.removeAll();
    repaint();
  }

  @Override
  public void showList() {
    add(panel);
    setVisible(true);
  }

  @Override
  public void getCommissionFee() {
    JLabel commissionFee = new JLabel("Please enter the starting value of the "
            + "commission fee (in $) to be used for every transaction "
            + "(The fee cannot be less than 0): ");
    JLabel commissionFeePercentage = new JLabel("Please enter the percentage by which "
            + "the commission fee will be "
            + "reduced after every transaction: ");
    commissionTextField = new JTextField(5);
    commissionFeePercentageTextField = new JTextField(5);
    panel.add(commissionFee);
    panel.add(commissionTextField);
    panel.add(commissionFeePercentage);
    panel.add(commissionFeePercentageTextField);
    panel.add(submitButton);
    add(panel);
    setVisible(true);
    setSize(900, 400);
  }

  @Override
  public String getCommissionFeeFromTextField() {
    return commissionTextField.getText();
  }

  @Override
  public String getCommissionFeePercentageFromTextField() {
    return commissionFeePercentageTextField.getText();
  }

  private void showInputsForInvestment(List<String> stockList) {
    JLabel percentageLabel = new JLabel("Enter the percentage amount");
    selectStockLabel = new JLabel("Select the stock");
    String[] stock = new String[stockList.size()];
    int i = 0;
    for (String s : stockList) {
      if (Arrays.stream(stock).noneMatch(s::equals)) {
        stock[i] = s;
        i++;
      }
    }
    model = new SpinnerNumberModel(0, 0, 100, 0.1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(new Dimension(50, 30));
    stockFromPortfolioDropDown = new JComboBox(stock);
    panel.add(selectStockLabel);
    panel.add(stockFromPortfolioDropDown);
    panel.add(percentageLabel);
    panel.add(spinner);
  }

  @Override
  public void showMessage(String message) {
    displayMessage.setText(message);
  }

  @Override
  public String getYearFromDropDown() {
    return (String) yearDropDown.getSelectedItem();
  }

  @Override
  public String getMonthFromDropDown() {
    return (String) monthDropDown.getSelectedItem();
  }

  @Override
  public String getDayFromDropDown() {
    return (String) dayDropDown.getSelectedItem();
  }

  @Override
  public void showExaminePortfolioData(List<IStock> portfolioData) {
    String[] columns = {"Stock Name", "Quantity", "Purchase Date"};
    Object[][] portfolio = new Object[portfolioData.size()][3];
    int row = 0;

    for (IStock stock : portfolioData) {
      portfolio[row][0] = stock.getStockName();
      portfolio[row][1] = stock.getStockQuantity();
      portfolio[row][2] = stock.getPurchaseDate();
      row++;
    }

    JTable examineTable = new JTable(portfolio, columns);
    examineTable.setRowHeight(20);
    JScrollPane scrollPane = new JScrollPane(examineTable);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    panel.removeAll();
    repaint();
    panel.add(scrollPane);
    examineTable.setEnabled(false);
    add(panel);
    setVisible(true);
    setResizable(false);
  }

  @Override
  public void addShowMenuToPanel() {
    panel.removeAll();
    repaint();
    JMenu mainMenu = new JMenu("Main Menu");
    JMenuBar menuBar = new JMenuBar();

    mainMenu.add(createNewPortfolio);
    mainMenu.add(createNewPortfolioWithCostAveraging);
    mainMenu.add(examinePortfolio);
    mainMenu.add(examinePortfolioOnASpecificDate);
    mainMenu.add(getCostBasis);
    mainMenu.add(portfolioValue);
    mainMenu.add(addStock);
    mainMenu.add(sellStock);
    mainMenu.add(portfolioPerformanceOverTime);
    mainMenu.add(investInExistingPortfolio);
    mainMenu.add(showListOfSupportedStocks);
    mainMenu.add(exit);

    menuBar.add(mainMenu);

    displayMessage = new JLabel();
    textInput = new JTextField(20);

    displayMessage.setText("Please select an option from the menu.");
    panel.add(displayMessage);

    setJMenuBar(menuBar);

    add(panel);
    setVisible(true);
  }

  @Override
  public void showTextFieldsForInputs() {
    panel.removeAll();
    repaint();
    JLabel label = new JLabel("Enter the portfolio name");
    investmentAmountTextField = new JTextField(5);
    JLabel label2 = new JLabel("Enter the amount to be invested");
    JLabel label3 = new JLabel("Enter the start date");
    JLabel label4 = new JLabel("Enter the interval days");
    displayMessage = new JLabel("Do you want to enter the end date?");
    model = new SpinnerNumberModel(0, 0, 400, 1);
    intervalDaysSpinner = new JSpinner(model);
    textInput = new JTextField(20);
    intervalDaysSpinner.setPreferredSize(new Dimension(50, 30));
    panel.add(label);
    panel.add(textInput);
    panel.add(label2);
    panel.add(investmentAmountTextField);
    panel.add(label4);
    panel.add(intervalDaysSpinner);
    panel.add(label3);
    showDateInputDialogBox();
    panel.add(displayMessage);
    panel.add(yesEndDate);
    panel.add(noEndDate);
    add(panel);
    setVisible(true);
    setSize(600, 400);
  }

  @Override
  public void showStocksButton() {
    panel.add(addStockToNewPortfolioButton);
    add(panel);
    setVisible(true);
    setSize(600, 400);
  }

  @Override
  public void removePreviousMessage() {
    panel.remove(noEndDate);
    panel.remove(yesEndDate);
    panel.remove(displayMessage);
    panel.add(new JLabel("Enter end date"));
    add(panel);
    setVisible(true);
  }

  @Override
  public void showSupportedStockList(List<String> stockList, String stockName) {
    clearScreen();
    String[] stocks = new String[stockList.size()];
    int i = 0;
    for (String s : stockList) {
      stocks[i] = s;
      i++;
    }

    model = new SpinnerNumberModel(0, 0, 100, 0.1);
    spinner = new JSpinner(model);
    spinner.setPreferredSize(new Dimension(50, 30));

    supportedStockDropDown = new JComboBox(stocks);
    panel.add(new JLabel("Last selected stock:- " + stockName));
    panel.add(new JLabel("Select the stock"));
    panel.add(supportedStockDropDown);
    panel.add(new JLabel("Enter the percentage amount"));
    panel.add(spinner);
    panel.add(addMore);
    add(panel);
    setVisible(true);
  }

  @Override
  public void addCreatePortfolioButton() {
    clearScreen();
    panel.add(createPfWithCostAveragingButton);
    add(panel);
    setVisible(true);
  }

  @Override
  public Object getIntervalDays() {
    return intervalDaysSpinner.getValue();
  }

  @Override
  public String getPortfolioFilenameFromTextField() {
    return portfolioFileName.getText();
  }

  @Override
  public String getPortfolioNameFromTextField() {
    return textInput.getText();
  }

  private void resetComponents() {
    commissionFeePercentageTextField = new JTextField();
    commissionTextField = new JTextField();
    investmentAmountTextField = new JTextField();
    textInput = new JTextField();

    String[] yearRange = new String[24];
    for (int i = 1999, j = 0; i <= 2022; i++, j++) {
      yearRange[j] = String.valueOf(i);
    }
    yearDropDown = new JComboBox(yearRange);
    performanceStartYear = new JComboBox(yearRange);
    performanceEndYear = new JComboBox(yearRange);

    String[] monthRange = new String[12];
    for (int i = 0; i < 12; i++) {
      monthRange[i] = String.valueOf(i + 1);
    }
    monthDropDown = new JComboBox(monthRange);
    performanceStartMonth = new JComboBox(monthRange);
    performanceEndMonth = new JComboBox(monthRange);

    String[] dayRange = new String[31];
    for (int i = 0; i < 31; i++) {
      dayRange[i] = String.valueOf(i + 1);
    }
    dayDropDown = new JComboBox(dayRange);
    performanceStartDay = new JComboBox(dayRange);
    performanceEndDay = new JComboBox(dayRange);
  }
}
