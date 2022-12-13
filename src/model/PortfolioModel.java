package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import model.stockdatastore.IStockDataStore;
import model.stockdatastore.StockDataStore;

/**
 * The PortfolioModel class implements the IModel interface.
 * This class represents a model containing the stock portfolios, and various I/O operations on
 * the portfolios.
 * The various operations include, but are not limited to, 'creating a portfolio', 'examining a
 * portfolio', and 'getting a portfolio's value for a given date'.
 */
public class PortfolioModel implements IModel {
  private final String portfoliosDirectory = "Portfolios";
  private final List<IPortfolio> portfolios;
  private final IStockDataStore dataStore;
  private float commissionFee;
  private float commissionFeeReductionPercentage;

  /**
   * Create a new PortfolioModel initialized with an empty list of portfolios.
   */
  public PortfolioModel() {
    dataStore = new StockDataStore();
    portfolios = new ArrayList<>();
  }

  @Override
  public void createPortfolio(String portfolioName, File file)
          throws
          IOException,
          IllegalArgumentException {
    //Check if file exists
    if (!file.exists()) {
      throw new FileNotFoundException("The portfolio file " + file.getAbsolutePath()
              + " does not exist.");
    }

    //Check if file is empty
    if (file.length() == 0) {
      throw new InvalidObjectException("The provided file is empty");
    }

    // If portfolio name already exists
    if (!portfolios.isEmpty()
            && portfolios
            .stream()
            .anyMatch(p -> p.getPortfolioName().equals(portfolioName))) {
      throw new FileAlreadyExistsException("The given portfolio name " + "'"
              + portfolioName + "'" + " cannot be added because " + "it already exists.");
    }

    //Check type of file (extension)
    if (PortfolioModelFileUtility
            .isAcceptableFileExtension(PortfolioModelFileUtility.getFileExtension(file))) {
      //Get the values from portfolio file
      //Create portfolio object
      IPortfolio pf = null;
      try {
        pf = PortfolioModelFileUtility.readCsvAsPortfolio(portfolioName,
                file,
                dataStore,
                commissionFee,
                true);
      } catch (NoSuchElementException e) {
        throw e;
      }

      //Add object to portfolio list
      portfolios.add(pf);

      HashMap<LocalDate, Double> costBasisMap = pf.getCostBasisMap();

      //Write to file storage
      PortfolioModelFileUtility.writePortfolioToFile(pf,
              portfolioName,
              this.portfoliosDirectory,
              true, costBasisMap);

    } else {
      throw new IllegalArgumentException("The given file extension is not supported.");
    }

    updateCommissionFee();
  }

  @Override
  public void addStockToPortfolio(String portfolioName,
                                  String stockName,
                                  float stockQuantity,
                                  String purchaseDate) {
    try {
      IStock stockToBeAdded = new Stock(stockName, stockQuantity, purchaseDate);
      for (IPortfolio selectedPortfolio : portfolios) {
        if (portfolioName.equals(selectedPortfolio.getPortfolioName())) {
          selectedPortfolio.addStock(dataStore, stockToBeAdded, commissionFee);
          HashMap<LocalDate, Double> costBasisMap = selectedPortfolio.getCostBasisMap();
          PortfolioModelFileUtility.writePortfolioToFile(selectedPortfolio,
                  selectedPortfolio.getPortfolioName(),
                  this.portfoliosDirectory,
                  false,
                  costBasisMap);
        }
      }
    } catch (NoSuchElementException | IllegalArgumentException e) {
      throw e;
    }

    updateCommissionFee();
  }

  @Override
  public double getPortfolioValue(String portfolioName, LocalDate date)
          throws IndexOutOfBoundsException {
    double portfolioValue = 0;
    if (LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("The entered date is invalid. Please enter a valid date.");
    }
    // get the portfolio value for the specific portfolio
    try {
      for (IPortfolio pf : portfolios) {
        if (portfolioName.equals(pf.getPortfolioName())) {
          portfolioValue = pf.getPortfolioValue(dataStore, date);
          break;
        }
      }
    } catch (NoSuchElementException exception) {
      throw new NoSuchElementException(exception.getMessage());
    }
    return portfolioValue;
  }

  @Override
  public List<IStock> examinePortfolioForADate(String portfolioName, LocalDate compositionDate)
          throws IndexOutOfBoundsException {
    if (compositionDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Examine date cannot be a future date");
    }

    List<IStock> stockList = null;

    for (IPortfolio portfolio : portfolios) {
      if (portfolioName.equals(portfolio.getPortfolioName())) {
        stockList = portfolio.getStockListOnAndBeforeCompositionDate(compositionDate);
        break;
      }
    }

    return stockList;
  }

  @Override
  public List<String> getListOfSupportedStocks() {
    return dataStore.getSupportedStocksList();
  }

  @Override
  public List<String> getPortfolioNames() {
    return portfolios.stream().map(pf -> pf.getPortfolioName()).collect(Collectors.toList());
  }

  @Override
  public void deleteAllPortfolioFiles() {
    PortfolioModelFileUtility.deleteDirectory(new File(portfoliosDirectory));
    this.portfolios.clear();
  }

  @Override
  public boolean isPortfolioListEmpty() {
    return portfolios.size() == 0;
  }

  @Override
  public void retrieveExistingPortfolios() {
    // Get all the CSV files in the portfolios storage directory
    List<File> portfolioFiles = PortfolioModelFileUtility
            .getCsvFilesInDirectory(new File(portfoliosDirectory));

    // loop over all the existing portfolio files and read them
    if (portfolioFiles != null) {
      for (File file : portfolioFiles) {
        IPortfolio storedPortfolio;
        String pfName = PortfolioModelFileUtility.getFileNameWithoutExtension(file);
        storedPortfolio = PortfolioModelFileUtility.readCsvAsPortfolio(pfName,
                file,
                dataStore, commissionFee, false);
        portfolios.add(storedPortfolio);
        HashMap<LocalDate, Double> costBasisMap = PortfolioModelFileUtility
                .readCsvForCostBasis(portfoliosDirectory,
                        pfName);
        storedPortfolio.setCostBasisMap(costBasisMap);
      }
    }
  }

  @Override
  public void setCommissionFee(float commissionFee, float feeReductionPercentage)
          throws IllegalArgumentException {
    if (commissionFee < 0) {
      throw new IllegalArgumentException("The commission fee cannot be a negative value.");
    }

    if (feeReductionPercentage < 0 || feeReductionPercentage > 100) {
      throw new IllegalArgumentException("The commission fee reduction percentage must be "
              + "between 0 and 100.");
    }

    this.commissionFee = commissionFee;
    this.commissionFeeReductionPercentage = feeReductionPercentage;
  }

  @Override
  public double getCostBasis(String portfolioName, LocalDate costBasisDate)
          throws IllegalArgumentException {
    double costBasisValue = 0;
    if (costBasisDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cost basis date cannot be a future date");
    }
    for (IPortfolio selectedPortfolio : portfolios) {
      if (portfolioName.equals(selectedPortfolio.getPortfolioName())) {
        costBasisValue = selectedPortfolio.getCostBasis(costBasisDate);
        break;
      }
    }
    return costBasisValue;
  }

  @Override
  public boolean isValidTickerSymbol(String stockTickerSymbol, int portfolioNumber) {
    IPortfolio selectedPortfolio = portfolios.get(portfolioNumber - 1);
    List<IStock> stockList = selectedPortfolio.getStocksList();

    for (IStock stock : stockList) {
      if (stock.getStockName().equals(stockTickerSymbol)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isValidDateInPortfolio(String date,
                                        int portfolioNumber,
                                        String stockTickerSymbol) {
    IPortfolio selectedPortfolio = portfolios.get(portfolioNumber - 1);
    List<IStock> stockList = selectedPortfolio.getStocksList();

    for (IStock stock : stockList) {
      if (stock.getPurchaseDate().equals(date)
              && stock.getStockName().equals(stockTickerSymbol)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isValidStockQuantityForADate(int quantity, String date,
                                              int portfolioNumber, String stockTickerSymbol) {
    IPortfolio selectedPortfolio = portfolios.get(portfolioNumber - 1);
    List<IStock> stockList = selectedPortfolio.getStocksList();

    for (IStock stock : stockList) {
      if (stock.getPurchaseDate().equals(date)
              && (quantity <= stock.getStockQuantity())
              && stock.getStockName().equals(stockTickerSymbol)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void sellStocksFromPortfolio(String portfolioName,
                                      String stockTickerSymbol,
                                      double quantity,
                                      LocalDate stockSellDate) {
    for (IPortfolio selectedPortfolio : portfolios) {
      if (selectedPortfolio.getPortfolioName().equals(portfolioName)) {

        double totalStockQuantity = selectedPortfolio.getStocksList()
                .stream()
                .filter(s -> s.getStockName().equals(stockTickerSymbol)
                        && (!LocalDate.parse(s.getPurchaseDate())
                        .isAfter(stockSellDate)))
                .mapToDouble(s -> s.getStockQuantity())
                .sum();
        if (totalStockQuantity <= 0) {
          throw new IllegalArgumentException("Stocks need to be "
                  + "purchased before they can be sold.");
        }
        if (quantity > totalStockQuantity) {
          throw new IllegalArgumentException("Please enter valid stock quantity.");
        }

        //double updatedStockQuantity = totalStockQuantity - quantity;
        selectedPortfolio.addStock(dataStore,
                new Stock(stockTickerSymbol,
                        -(float) quantity, stockSellDate.toString()),
                commissionFee);
        updateCommissionFee();

        PortfolioModelFileUtility.writePortfolioToFile(selectedPortfolio,
                selectedPortfolio.getPortfolioName(),
                portfoliosDirectory,
                false,
                selectedPortfolio.getCostBasisMap());
      }
      break;
    }
  }

  @Override
  public List<IStock> examinePortfolio(String portfolioName) {
    List<IStock> stockList = null;

    for (IPortfolio portfolio : portfolios) {
      if (portfolioName.equals(portfolio.getPortfolioName())) {
        stockList = portfolio.getStocksList();
        return stockList;
      }
    }
    return null;
  }

  @Override
  public IPortfolioPerformanceData getPortfolioPerformanceData(String portfolioName,
                                                               LocalDate startDate,
                                                               LocalDate endDate)
          throws IllegalArgumentException {
    // if end date < start date, throw error
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("The end date cannot come before start date for computing"
              + " the portfolio's performance");
    }

    // either of the dates is in the future
    if (LocalDate.now().isBefore(startDate) || LocalDate.now().isBefore(endDate)) {
      throw new IllegalArgumentException("The start/end date cannot be a date in the future");
    }

    // either of the dates is before the year 2000
    if (startDate.getYear() < 2000 || endDate.getYear() < 2000) {
      throw new IllegalArgumentException("The start/end date cannot be before the year 2000");
    }

    // if start date is today's date
    if (LocalDate.now().isEqual(startDate)) {
      throw new IllegalArgumentException("The start date cannot be today's date");
    }

    IPortfolio selectedPortfolio = portfolios
            .stream()
            .filter(p -> p.getPortfolioName().equals(portfolioName))
            .findFirst()
            .orElse(null);
    if (selectedPortfolio != null) {
      return selectedPortfolio.getPerformanceData(startDate, endDate, this.dataStore);
    } else {
      throw new IllegalArgumentException("The given portfolio does not exist.");
    }
  }

  @Override
  public List<IStock> getStockFromPortfolio(String pfName) {
    List<IStock> stockNames = null;
    for (IPortfolio pf : portfolios) {
      if (pfName.equals(pf.getPortfolioName())) {
        stockNames = pf.getStocksList();
        break;
      }
    }
    return stockNames;
  }

  @Override
  public float calculateNumberOfShares(String stockName, LocalDate purchaseDate,
                                       float amountToBeInvestedForStock) {
    double stockValueOnADate = dataStore.getStockValue(stockName, purchaseDate);
    float numberOfShares = (float) (amountToBeInvestedForStock / stockValueOnADate);
    return (float) (Math.round(numberOfShares * 100.0) / 100.0);
  }

  @Override
  public void createPortfolioWithDollarCostAveraging(String portfolioName,
                                                     float amountToBeInvested,
                                                     LocalDate startDate,
                                                     LocalDate endDate,
                                                     int numberOfDays,
                                                     Map<String, Float> stockProportions) {
    if (startDate.isAfter(endDate) || startDate.equals(endDate)) {
      throw new IllegalArgumentException("The start date cannot be on or after the end date.");
    }

    IPortfolio newPortfolio = new Portfolio(portfolioName);
    this.portfolios.add(newPortfolio);

    // iterate over the time period intervals
    LocalDate currentInvestmentDate = dataStore.getNextValidMarketDate(startDate);
    while (currentInvestmentDate.isBefore(endDate) || currentInvestmentDate.isEqual(endDate)) {
      for (String stock : stockProportions.keySet()) {
        float numberOfShares = calculateNumberOfShares(stock, currentInvestmentDate,
                ((stockProportions.get(stock) / 100) * amountToBeInvested));
        addStockToPortfolio(portfolioName, stock, numberOfShares, currentInvestmentDate.toString());
      }

      currentInvestmentDate = dataStore.getNextValidMarketDate(
              currentInvestmentDate.plusDays(numberOfDays));
    }

    HashMap<LocalDate, Double> costBasisMap = newPortfolio.getCostBasisMap();

    //Write to file storage
    PortfolioModelFileUtility.writePortfolioToFile(newPortfolio,
            portfolioName,
            this.portfoliosDirectory,
            false, costBasisMap);
  }

  @Override
  public void balancePortfolio(String pfName, LocalDate d, Map<String, Double> ds)
          throws ParseException {
    int pfIndex = -1;
    for (int i = 0; i < this.portfolios.size(); i++) {
      if (Objects.equals(this.portfolios.get(i).getPortfolioName(), pfName)) {
        pfIndex = i;
        break;
      }
    }
    if (pfIndex == -1) {
      throw new IllegalArgumentException("No Such portfolio exists!");
    }
    PortfolioVisitor<IPortfolio> visitor = new PortfolioBalanceVisitor<>(d,
            this.dataStore, ds, x -> x);
    this.portfolios.set(pfIndex, visitor.apply(this.portfolios.get(pfIndex)));
  }

  private void updateCommissionFee() {
    this.commissionFee = this.commissionFee * (1 - (this.commissionFeeReductionPercentage / 100));
  }
}
