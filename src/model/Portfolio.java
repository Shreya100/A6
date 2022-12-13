package model;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import model.stockdatastore.IStockDataStore;

/**
 * The Portfolio class is an implementation of the IPortfolio interface.
 * This class represents a stock portfolio, and provides operations on a stock portfolio like
 * 'getting the portfolio's name', 'adding a stock to the portfolio', and 'getting the portfolio's
 * market value on a given date'.
 */
public class Portfolio implements IPortfolio {
  private final List<IStock> stockList;
  private final String portfolioName;
  private HashMap<LocalDate, Double> costBasisMap;

  /**
   * Create a new Portfolio initialized with the given portfolio name and an empty list of stocks.
   *
   * @param portfolioName the name of the portfolio to be created
   */
  public Portfolio(String portfolioName) {
    this.portfolioName = portfolioName;
    this.stockList = new ArrayList<>();
    this.costBasisMap = new HashMap<>();
  }

  @Override
  public double getPortfolioValue(IStockDataStore dataStore, LocalDate date)
          throws NoSuchElementException {
    double portfolioValue = 0;
    List<IStock> filteredStocks = stockList
            .stream()
            .filter(s -> {
              String purchaseDateString = s.getPurchaseDate();
              LocalDate purchaseDate = LocalDate.parse(purchaseDateString,
                      DateTimeFormatter.ofPattern(Stock.STOCK_PURCHASE_DATE_FORMAT));
              return purchaseDate.isBefore(date) || purchaseDate.isEqual(date);
            })
            .collect(Collectors.toList());

    try {
      for (IStock stock : filteredStocks) {
        double stockValue = dataStore.getStockValue(stock.getStockName(), date);
        portfolioValue += (stockValue * stock.getStockQuantity());
      }
    } catch (NoSuchElementException exception) {
      throw new NoSuchElementException(exception.getMessage());
    }
    return portfolioValue;
  }

  private double getPortfolioValueForAllStocks(IStockDataStore dataStore, LocalDate date)
          throws NoSuchElementException {
    double portfolioValue = 0;
    try {
      for (IStock stock : stockList) {
        double stockValue = dataStore.getStockValue(stock.getStockName(), date);
        portfolioValue += (stockValue * stock.getStockQuantity());
      }
    } catch (NoSuchElementException exception) {
      throw new NoSuchElementException(exception.getMessage());
    }
    return portfolioValue;
  }

  @Override
  public void addStock(IStockDataStore dataStore, IStock stock, float commissionFee) {
    // Check if the stock is supported by the stock data store
    if (!isSupportedStock(dataStore, stock.getStockName())) {
      throw new IllegalArgumentException("The program does not support the "
              + "provided stock name '" + stock.getStockName() + "'");
    }

    IStock existingStock = getDuplicateStock(stock);
    if (existingStock != null) {
      stockList.set(stockList.indexOf(existingStock),
              addNewStockQuantity(existingStock, stock.getStockQuantity()));
    } else {
      //Calculate cost basis for a stock
      LocalDate purchaseDate = LocalDate.parse(stock.getPurchaseDate(),
              DateTimeFormatter.ofPattern(Stock.STOCK_PURCHASE_DATE_FORMAT));
      double stockValue = 0;
      try {
        stockValue = dataStore.getStockValue(stock.getStockName(), purchaseDate);
        stockList.add(stock);
      } catch (NoSuchElementException e) {
        throw e;
      }
      stockValue += commissionFee;

      if (costBasisMap.containsKey(purchaseDate)) {
        double existingCostBasisValue = costBasisMap.get(purchaseDate);
        existingCostBasisValue += stockValue;
        costBasisMap.put(purchaseDate, existingCostBasisValue);
      } else {
        costBasisMap.put(purchaseDate, stockValue);
      }
    }
  }

  @Override
  public HashMap<LocalDate, Double> getCostBasisMap() {
    return this.costBasisMap;
  }

  @Override
  public void setCostBasisMap(HashMap<LocalDate, Double> costBasisMap) {
    this.costBasisMap = costBasisMap;
  }

  @Override
  public void updateStock(IStockDataStore dataStore, Stock stock, IStock existingStock) {
    stockList.set(stockList.indexOf(existingStock), stock);
  }

  @Override
  public void deleteStock(IStock stock) {
    stockList.remove(stock);
  }

  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  @Override
  public List<IStock> getStocksList() {
    return this.stockList;
  }

  @Override
  public List<IStock> getStockListOnAndBeforeCompositionDate(LocalDate compositionDate) {
    List<IStock> compositionStockList = new ArrayList<>();
    for (IStock stock : this.stockList) {
      LocalDate stockPurchaseDate = LocalDate.parse(stock.getPurchaseDate(),
              DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      if (!stockPurchaseDate.isAfter(compositionDate)) {
        compositionStockList.add(stock);
      }
    }
    return compositionStockList;
  }

  @Override
  public double getCostBasis(LocalDate costBasisDate) {
    double costBasis = 0;

    for (LocalDate date : costBasisMap.keySet()) {
      if (!date.isAfter(costBasisDate)) {
        costBasis += costBasisMap.get(date);
      }
    }

    return costBasis;
  }

  @Override
  public IPortfolioPerformanceData getPerformanceData(LocalDate startDate,
                                                      LocalDate endDate,
                                                      IStockDataStore dataStore) {
    List<PortfolioPerformanceData.PerformanceDataFrame> performanceDataList = new ArrayList<>();
    long monthsInRange = ChronoUnit.MONTHS.between(startDate, endDate);

    if (monthsInRange > 90) {
      // if months in range > 90, compute data for every year
      populateYearlyPerformanceData(startDate, performanceDataList, monthsInRange, dataStore);
    } else if (monthsInRange > 30 && monthsInRange <= 90) {
      // if months in range > 30 and <= 90, compute data for every 3 months
      populateTriMonthlyPerformanceData(startDate,
              endDate,
              performanceDataList,
              monthsInRange,
              dataStore);
    } else if (monthsInRange > 1 && monthsInRange <= 30) {
      // if months in range < 30 and > 1, compute data for every month
      populateMonthlyPerformanceData(startDate, performanceDataList, monthsInRange, dataStore);
    } else if (monthsInRange <= 1) {
      // if months in range < 1, compute data for every day
      long daysInRange = getNumberOfWeekdaysInRange(startDate, endDate);
      populateDailyPerformanceData(startDate, performanceDataList, daysInRange, dataStore);
    }

    IPortfolioPerformanceData performanceData = new PortfolioPerformanceData(
            this.getPortfolioName(),
            performanceDataList,
            startDate,
            endDate
    );

    return performanceData;
  }

  private void populateDailyPerformanceData(LocalDate startDate,
                                            List<PortfolioPerformanceData
                                                    .PerformanceDataFrame> performanceDataList,
                                            long weekDaysInRange,
                                            IStockDataStore dataStore) {
    int dataFrames = Math.toIntExact(weekDaysInRange) + 1;
    dataFrames = dataFrames < 5 ? 5 : dataFrames; // there should be at least 5 data frames
    LocalDate currentFrameDate = startDate;

    for (int i = 0; i < dataFrames; i++) {
      double portfolioValue = 0;
      try {
        portfolioValue = getPortfolioValueForAllStocks(dataStore, currentFrameDate);
      } catch (NoSuchElementException e) {
        currentFrameDate = getNextClosestWeekday(currentFrameDate);
        portfolioValue = getPortfolioValueForAllStocks(dataStore, currentFrameDate);
      }

      // convert the time frame to string format
      String dataFrameDurationAsString = MessageFormat.format(
              "{0}",
              currentFrameDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));

      // Enter the data for this frame into the map
      performanceDataList.add(new PortfolioPerformanceData.PerformanceDataFrame(
              dataFrameDurationAsString,
              portfolioValue));

      currentFrameDate = getNextClosestWeekday(currentFrameDate);
    }
  }

  private void populateMonthlyPerformanceData(LocalDate startDate,
                                              List<PortfolioPerformanceData
                                                      .PerformanceDataFrame> performanceDataList,
                                              long monthsInRange,
                                              IStockDataStore dataStore) {
    int dataFrames = Math.toIntExact(monthsInRange) + 1;
    dataFrames = dataFrames < 5 ? 5 : dataFrames; // there should be at least 5 data frames
    LocalDate currentFrameDate = startDate;

    for (int i = 0; i < dataFrames; i++) {
      LocalDate lastWorkingDayOfMonth = getLastWorkingDateOfMonth(currentFrameDate);
      double portfolioValue = 0;
      try {
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfMonth);
      } catch (NoSuchElementException e) {
        lastWorkingDayOfMonth = getLastRecentWeekday(lastWorkingDayOfMonth);
        lastWorkingDayOfMonth = dataStore.getNextValidMarketDate(lastWorkingDayOfMonth);
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfMonth);
      }


      // convert the time frame to string format
      String dataFrameDurationAsString = MessageFormat.format(
              "{0} {1}",
              currentFrameDate.getMonth().toString().substring(0, 3),
              Integer.toString(currentFrameDate.getYear()));

      // Enter the data for this frame into the map
      performanceDataList.add(new PortfolioPerformanceData.PerformanceDataFrame(
              dataFrameDurationAsString,
              portfolioValue));

      currentFrameDate = currentFrameDate.plusMonths(1);
    }
  }

  private void populateYearlyPerformanceData(LocalDate startDate,
                                             List<PortfolioPerformanceData
                                                     .PerformanceDataFrame> performanceDataList,
                                             long monthsInRange,
                                             IStockDataStore dataStore) {
    int dataFrames = Math.toIntExact((long) Math.floor(monthsInRange / 12)) + 1;
    dataFrames = dataFrames < 5 ? 5 : dataFrames; // there should be at least 5 data frames
    LocalDate currentFrameDate = startDate;

    for (int i = 0; i < dataFrames; i++) {
      LocalDate lastWorkingDayOfYear = getLastWorkingDateOfYear(currentFrameDate);
      double portfolioValue = 0;
      try {
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfYear);
      } catch (NoSuchElementException e) {
        lastWorkingDayOfYear = getLastRecentWeekday(lastWorkingDayOfYear);
        lastWorkingDayOfYear = dataStore.getNextValidMarketDate(lastWorkingDayOfYear);
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfYear);
      }

      // convert the time frame to string format
      String dataFrameDurationAsString = MessageFormat.format(
              "{0}",
              Integer.toString(currentFrameDate.getYear()));

      // Enter the data for this frame into the map
      performanceDataList.add(new PortfolioPerformanceData.PerformanceDataFrame(
              dataFrameDurationAsString,
              portfolioValue));

      currentFrameDate = currentFrameDate.plusYears(1);
    }
  }

  private void populateTriMonthlyPerformanceData(LocalDate startDate,
                                                 LocalDate endDate,
                                                 List<PortfolioPerformanceData
                                                         .PerformanceDataFrame> performanceDataList,
                                                 long monthsInRange,
                                                 IStockDataStore dataStore) {
    int dataFrames = Math.toIntExact((long) Math.floor(monthsInRange / 3)) + 1;
    dataFrames = dataFrames < 5 ? 5 : dataFrames; // there should be at least 5 data frames
    LocalDate currentFrameStartDate = startDate;
    LocalDate currentFrameEndDate = startDate;
    for (int i = 0; i < dataFrames; i++) {
      // calculate the portfolio's value at the last working day of the time frame
      currentFrameEndDate = currentFrameStartDate.plusMonths(2);
      if (currentFrameStartDate.plusMonths(2).isAfter(endDate)
              &&
              currentFrameStartDate
                      .plusMonths(2)
                      .getMonth()
                      != endDate.getMonth()
      ) {
        currentFrameEndDate = currentFrameStartDate.plusMonths(1);
      }
      if (currentFrameStartDate.plusMonths(1).isAfter(endDate)
              &&
              currentFrameStartDate
                      .plusMonths(1)
                      .getMonth()
                      != endDate.getMonth()) {
        currentFrameEndDate = currentFrameStartDate;
      }

      LocalDate lastWorkingDayOfMonth;
      if (currentFrameEndDate.getYear() == endDate.getYear()
              &&
              currentFrameEndDate.getMonth() == endDate.getMonth()) {
        lastWorkingDayOfMonth = getLastRecentWeekday(endDate);
      } else {
        lastWorkingDayOfMonth = getLastWorkingDateOfMonth(currentFrameEndDate);
      }
      double portfolioValue = 0;
      try {
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfMonth);
      } catch (NoSuchElementException e) {
        lastWorkingDayOfMonth = getLastRecentWeekday(lastWorkingDayOfMonth.minusDays(1));
        lastWorkingDayOfMonth = dataStore.getNextValidMarketDate(lastWorkingDayOfMonth);
        portfolioValue = getPortfolioValueForAllStocks(dataStore, lastWorkingDayOfMonth);
      }

      // convert the time frame to string format
      String dataFrameDurationAsString = MessageFormat.format(
              "{0} {1} - {2} {3}",
              currentFrameStartDate.getMonth().toString().substring(0, 3),
              Integer.toString(currentFrameStartDate.getYear()),
              currentFrameEndDate.getMonth().toString().substring(0, 3),
              Integer.toString(currentFrameEndDate.getYear()));

      // Enter the data for this frame into the map
      performanceDataList.add(new PortfolioPerformanceData.PerformanceDataFrame(
              dataFrameDurationAsString,
              portfolioValue));

      // update the start date for the next time frame
      currentFrameStartDate = currentFrameEndDate.plusMonths(1);
    }
  }

  private LocalDate getLastWorkingDateOfMonth(LocalDate date) {
    LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
    LocalDate lastWorkingDayOfMonth = getLastRecentWeekday(lastDay);

    // if last working of month is after today, return last Friday closest to Today
    LocalDate today = LocalDate.now().minusDays(1);
    if (lastWorkingDayOfMonth.isAfter(LocalDate.now())) {
      lastWorkingDayOfMonth = getLastRecentWeekday(today);
    }

    return lastWorkingDayOfMonth;
  }

  private LocalDate getLastWorkingDateOfYear(LocalDate date) {
    LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfYear());
    LocalDate lastWorkingDayOfMonth = getLastRecentWeekday(lastDay);

    // if last working of month is after today, return last Friday closest to Today
    LocalDate today = LocalDate.now().minusDays(1);
    if (lastWorkingDayOfMonth.isAfter(LocalDate.now())) {
      lastWorkingDayOfMonth = getLastRecentWeekday(today);
    }

    return lastWorkingDayOfMonth;
  }

  private LocalDate getLastRecentWeekday(LocalDate date) {
    LocalDate lastWeekDay;
    switch (DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK))) {
      case SATURDAY:
        lastWeekDay = date.minusDays(1);
        break;
      case SUNDAY:
        lastWeekDay = date.minusDays(2);
        break;
      default:
        lastWeekDay = date;
    }
    return lastWeekDay;
  }

  private LocalDate getNextClosestWeekday(LocalDate date) {
    LocalDate nextWeekDay = date.plusDays(1);
    switch (DayOfWeek.of(nextWeekDay.get(ChronoField.DAY_OF_WEEK))) {
      case SATURDAY:
        nextWeekDay = nextWeekDay.plusDays(2);
        break;
      case SUNDAY:
        nextWeekDay = nextWeekDay.plusDays(1);
        break;
      default:
        break;
    }
    return nextWeekDay;
  }

  private long getNumberOfWeekdaysInRange(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("The start date cannot be after the end date");
    }

    LocalDate currentDate = startDate.plusDays(1);
    long weekDays = 0;
    while (currentDate.isBefore(endDate)) {
      var dayOfWeek = DayOfWeek.of(currentDate.get(ChronoField.DAY_OF_WEEK));
      if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
        weekDays += 1;
      }
      currentDate = currentDate.plusDays(1);
    }

    return weekDays;
  }

  private IStock getDuplicateStock(IStock stock) {
    IStock duplicateStock = this.stockList
            .stream()
            .filter(s -> s.getStockName().equals(stock.getStockName())
                    && s.getPurchaseDate().equals(stock.getPurchaseDate()))
            .findFirst()
            .orElse(null);
    return duplicateStock;
  }

  private IStock addNewStockQuantity(IStock existingStock, float quantityToBeAdded) {
    return existingStock.addStockQuantity(quantityToBeAdded);
  }

  private boolean isSupportedStock(IStockDataStore dataStore, String stockSymbol) {
    return dataStore.isAcceptableStock(stockSymbol);
  }
}
