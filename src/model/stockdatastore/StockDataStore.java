package model.stockdatastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import model.Stock;

/**
 * The StockDataStore class represents a data store repository for stock price data.
 * This class provides methods for querying data from the stock data store.
 * StockDataStore implements the IStockDataStore interface.
 */
public class StockDataStore implements IStockDataStore {
  private final String alphaVantageApiKey = "2ZZHRYGHDR5SZH9R";

  private final String alphaVantageApi = "https://www.alphavantage"
          + ".co/query?function=TIME_SERIES_DAILY";
  private final String dataStoreFileFormat = ".csv";
  private final String dataStoreFileFormatDelimiter = ",";
  private List<String> supportedStocksList;
  private final String stockDataFilesDirectory = "StockDataFiles";
  private final String supportedStocksListFile = Paths.get(stockDataFilesDirectory,
          "supported-stocks-list.txt").toString();
  private Map<String, List<IStockPrice>> stockData;

  /**
   * Create a new StockDataStore initialized with the stock data stored in
   * the store's local storage. Initializing the store will also initialize the list of
   * supported stocks in the store.
   */
  public StockDataStore() {
    stockData = new HashMap<>();
    try {
      supportedStocksList = populateSupportedStocksList();
    } catch (FileNotFoundException e) {
      supportedStocksList = new ArrayList<>();
    }
  }

  @Override
  public List<String> getSupportedStocksList() {
    return supportedStocksList;
  }

  @Override
  public double getStockValue(String stockName, LocalDate date) throws NoSuchElementException {
    // if stock data is not present for a stock, fetch it and add it
    if (!stockData.containsKey(stockName)) {
      List<IStockPrice> stockPrices = null;
      try {
        stockPrices = getStockPricesList(stockName, StockDataSource.API);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
      stockData.put(stockName, stockPrices);
    }

    List<IStockPrice> stockPriceList = stockData.get(stockName);
    double stockPrice;

    try {
      stockPrice = stockPriceList
              .stream()
              .filter(s -> s.getMarketDate().equals(date))
              .findFirst()
              .get()
              .getMarketPrice();
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("Stock price for stock " + stockName + " is not present for "
              + date.toString() + ". Please enter a valid date.");
    }
    return stockPrice;
  }

  @Override
  public boolean isValidStockMarketDate(LocalDate date) {
    try {
      double stockValue = getStockValue(this.supportedStocksList.get(0), date);
    } catch (NoSuchElementException e) {
      return false;
    }
    return true;
  }

  @Override
  public LocalDate getNextValidMarketDate(LocalDate date) {
    LocalDate currentDate = date;
    while (!isValidStockMarketDate(currentDate)) {
      if (currentDate.isAfter(LocalDate.now().minusDays(1))) {
        break;
      } else {
        currentDate = currentDate.plusDays(1);
      }
    }
    return currentDate;
  }

  @Override
  public boolean isAcceptableStock(String stockName) {
    return supportedStocksList.contains(stockName);
  }

  private List<String> populateSupportedStocksList() throws FileNotFoundException {
    List<String> supportedStocksList = new ArrayList<>();
    try {
      File f = new File(supportedStocksListFile);
      if (f.exists()) {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
          supportedStocksList.add(line.replace("\n", ""));
        }
      } else {
        throw new FileNotFoundException("The supported stocks list file: "
                + supportedStocksListFile
                + " does not exist.");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return supportedStocksList;
  }

  private List<IStockPrice> getStockPricesListFromCsv(File csvDataFile) {
    List<IStockPrice> stockPrices = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(csvDataFile));
      String line;

      line = br.readLine(); // read first line but don't do anything with it
      while ((line = br.readLine()) != null) {
        IStockPrice stockPrice = readStringAsStockPrice(line);
        stockPrices.add(stockPrice);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return stockPrices;
  }

  private boolean areStockDataFilesPresent(String dir) {
    File stockDataFilesDir = new File(dir);
    if (stockDataFilesDir.exists()) {
      return stockDataFilesDir.listFiles().length > 0;
    } else {
      return false;
    }
  }

  private Map<String, List<IStockPrice>> getStockData(List<String> stockSymbols)
          throws FileNotFoundException {
    if (stockSymbols.isEmpty()) {
      throw new IllegalArgumentException("The provided list of stock symbols cannot be null"
              + " or empty");
    }

    Map<String, List<IStockPrice>> stockData = new HashMap<>();
    for (String stockSymbol : stockSymbols) {
      List<IStockPrice> stockPrices = getStockPricesList(stockSymbol, StockDataSource.API);
      stockData.put(stockSymbol, stockPrices);
    }
    return stockData;
  }

  private List<IStockPrice> getStockPricesList(String stockSymbol, StockDataSource dataSource)
          throws FileNotFoundException {

    if (dataSource.equals(StockDataSource.LocalDataFiles)) {
      if (!areStockDataFilesPresent(stockDataFilesDirectory)) {
        throw new FileNotFoundException("The stock data files directory does not exist.");
      }

      File stockDataFile = new File(Paths.get(stockDataFilesDirectory,
              stockSymbol + dataStoreFileFormat).toUri());
      if (stockDataFile.exists() && stockDataFile.isFile()) {
        return getStockPricesListFromCsv(stockDataFile);
      } else {
        throw new FileNotFoundException("The stock data file for stock"
                + stockSymbol
                + " does not exist.");
      }
    } else if (dataSource.equals(StockDataSource.API)) {
      try {
        return getStockPricesListFromApi(stockSymbol);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return null;
  }

  private List<IStockPrice> getStockPricesListFromApi(String stockSymbol) throws IOException {
    StringBuilder apiOutput;

    try {
      apiOutput = getStockDataFromAlphaVantageApi(stockSymbol);
    } catch (IOException e) {
      throw e;
    }

    List<IStockPrice> stockPrices = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new StringReader(apiOutput.toString()));
      String line;

      line = br.readLine(); // read first line but don't do anything with it
      while ((line = br.readLine()) != null) {
        IStockPrice stockPrice = readStringAsStockPrice(line);
        stockPrices.add(stockPrice);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return stockPrices;
  }

  private IStockPrice readStringAsStockPrice(String line) {
    String[] values = line.split(dataStoreFileFormatDelimiter);
    LocalDate marketDate = LocalDate.parse(values[0],
            DateTimeFormatter.ofPattern(Stock.STOCK_PURCHASE_DATE_FORMAT));
    double marketPrice = Double.parseDouble(values[4]);

    IStockPrice stockPrice = new StockPrice(marketDate, marketPrice);
    return stockPrice;
  }

  private void generateStockDataFiles(List<String> stockSymbols) throws IOException {
    for (String stockSymbol : stockSymbols) {
      StringBuilder apiOutput;

      try {
        apiOutput = getStockDataFromAlphaVantageApi(stockSymbol);
      } catch (IOException e) {
        throw e;
      }

      // write api output to file
      try {
        String stockDataFileName = stockSymbol + dataStoreFileFormat;
        File f = new File(Paths.get(stockDataFilesDirectory, stockDataFileName).toUri());
        if (!f.exists()) {
          f.createNewFile();
        }

        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(apiOutput.toString());
        bw.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private StringBuilder getStockDataFromAlphaVantageApi(String stockSymbol) throws IOException {
    URL url = null;

    try {
      url = new URL(alphaVantageApi
              + "&outputsize=full"
              + "&symbol=" + stockSymbol
              + "&apikey=" + alphaVantageApiKey
              + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("The AlphaVantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
    return output;
  }
}
