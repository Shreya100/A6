package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import model.stockdatastore.IStockDataStore;

/**
 * The PortfolioModelFileUtility final class provides static helper methods for performing
 * tasks related to PortfolioModel class, and also provides methods for performing file operations.
 * Some examples of the operations that this helper class provides are:
 * "get a file's extension", "read a CSV file as an IPortfolio object", "write an IPortfolio
 * object to file", "delete a directory", and more.
 */
public final class PortfolioModelFileUtility {
  private static final String extensionRegex = "\\.";
  private static final String portfolioFileExtension = ".csv";
  private static final String costBasisExtension = "-costBasis.txt";
  private static final String validStockQuantityPattern = "^[1-9][0-9]*(\\.[0]+)?$";


  /**
   * Get the extension of a given file.
   *
   * @param file the file whose extension is required
   * @return the extension of the given file as a string
   */
  public static String getFileExtension(File file) {
    String[] fileSplit = file.getName().split(extensionRegex);
    return fileSplit[fileSplit.length - 1];
  }

  /**
   * Check if a given file extension is in the acceptable format
   * required for the creation of a portfolio.
   *
   * @param fileExtension the file extension of the file to be tested for acceptance
   * @return true if the given file extension is acceptable, false otherwise
   */
  public static boolean isAcceptableFileExtension(String fileExtension) {
    for (AcceptableFileExtensions acceptableExtension : AcceptableFileExtensions.values()) {
      if (acceptableExtension.name().equals(fileExtension)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Read the given CSV file as an IPortfolio instance.
   *
   * @param portfolioName the name of the portfolio to be created
   * @param file          the CSV file to be parsed for creating the portfolio
   * @param dataStore     the stock data store containing the information of
   *                      stock prices and market dates
   * @return a new IPortfolio instance containing the data provided in the given file
   * @throws IllegalArgumentException if the given file does not contain the required data values,
   *                                  or if the stock quantity for a stock provided in the data
   *                                  file contains fractional value,
   *                                  or if the purchase date for a stock provided in the data file
   *                                  is not in the correct format,
   *                                  or if a stock symbol provided in the data file is not
   *                                  supported by the given stock data store.
   */
  public static IPortfolio readCsvAsPortfolio(String portfolioName,
                                              File file,
                                              IStockDataStore dataStore, float commissionFee,
                                              boolean isNewPortfolio)
          throws IllegalArgumentException {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line;
      IPortfolio pf = new Portfolio(portfolioName);

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        //Check if values length = 3
        if (values.length != 3) {
          throw new IllegalArgumentException("File does not contain all the required values");
        }
        String stockSymbol = values[0].trim();
        String stockQuantity = values[1].trim();
        String stockPurchaseDate = values[2].trim();

        //Check if values quantity is fractional
        if (isNewPortfolio) {
          if (!verifyNonFractionalStockQuantity(stockQuantity)) {
            throw new IllegalArgumentException("Invalid stock quantity for "
                                                       + stockSymbol + ".");
          }
        }

        try {
          IStock stock;
          try {
            stock = new Stock(stockSymbol,
                    Float.parseFloat(stockQuantity),
                    stockPurchaseDate);
          } catch (NumberFormatException e) {
            throw e;
          }

          pf.addStock(dataStore, stock, commissionFee);
        } catch (DateTimeParseException e) {
          throw new IllegalArgumentException("The purchase date for stock "
                  + stockSymbol
                  + " is not in the correct format(" + Stock.STOCK_PURCHASE_DATE_FORMAT + ")");
        } catch (NoSuchElementException e) {
          throw e;
        }
      }
      return pf;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean verifyNonFractionalStockQuantity(String stockQuantity) {
    return Pattern.matches(validStockQuantityPattern, String.valueOf(stockQuantity));
  }

  /**
   * Read the stored cost basis file while retrieving the existing portfolios.
   *
   * @param portfolioFilesDirectory the name of the portfolios directory
   * @param portfolioName           the name of the portfolio to be created
   * @return a new map containing the cost basis values of a portfolio
   * @throws IllegalArgumentException if the given file does not contain the date
   *                                  in a specified format.
   */
  public static HashMap<LocalDate, Double> readCsvForCostBasis(String portfolioFilesDirectory,
                                                               String portfolioName)
          throws IllegalArgumentException {
    HashMap<LocalDate, Double> costBasisMap = new HashMap<>();
    portfolioName += costBasisExtension;
    try {
      File f = new File(Paths.get(portfolioFilesDirectory, portfolioName).toUri());
      BufferedReader br = new BufferedReader(new FileReader(f));
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        LocalDate date = LocalDate.parse(values[0].trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Double costBasisValue = Double.valueOf(values[1].trim());
        costBasisMap.put(date, costBasisValue);
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return costBasisMap;
  }

  /**
   * Write a given IPortfolio object to a file in the PortfolioModelFileUtility class's internal
   * format.
   *
   * @param portfolio               the portfolio object to be written to file
   * @param portfolioName           the name of portfolio to be written to file
   * @param portfolioFilesDirectory the directory where the portfolio's file is to be created
   */
  public static void writePortfolioToFile(IPortfolio portfolio,
                                          String portfolioName,
                                          String portfolioFilesDirectory,
                                          boolean isNewPortfolio,
                                          HashMap<LocalDate, Double> costBasisMap) {
    //Appending cost basis file extension to create new file for cost basis
    String costBasis = portfolioName + costBasisExtension;

    //Appending file extension to portfolio name
    portfolioName += portfolioFileExtension;
    // if portfolios storage directory does not exist, create it
    File portfoliosDir = new File(portfolioFilesDirectory);
    if (!portfoliosDir.exists()) {
      portfoliosDir.mkdirs();
    }

    try {
      File f = new File(Paths.get(portfolioFilesDirectory, portfolioName).toUri());
      File costBasisFile = new File(Paths.get(portfolioFilesDirectory, costBasis).toUri());

      if (f.exists() && isNewPortfolio) {
        throw new FileAlreadyExistsException("The portfolio with same already exists");
      }

      if (isNewPortfolio) {
        f.createNewFile();
        costBasisFile.createNewFile();
      }

      //Writing stock list to portfolio csv file
      List<IStock> stockResult = portfolio.getStocksList();
      StringBuilder result = new StringBuilder();
      for (IStock s : stockResult) {
        result.append(s.getStockDataAsCsv());
        result.append("\n");
      }
      FileWriter fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(result.toString());
      bw.close();

      //Writing cost basis to cost basis csv file
      StringBuilder costBasisResult = new StringBuilder();
      for (LocalDate date : costBasisMap.keySet()) {
        costBasisResult.append(date);
        costBasisResult.append(",");
        costBasisResult.append(costBasisMap.get(date));
        costBasisResult.append("\n");
      }
      fw = new FileWriter(costBasisFile);
      bw = new BufferedWriter(fw);
      bw.write(costBasisResult.toString());
      bw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Delete a given file directory, including the files inside the directory,
   * and the directory itself.
   *
   * @param directoryToBeDeleted the path of directory to be deleted
   * @return true if the directory was deleted successfully, false otherwise
   */
  public static boolean deleteDirectory(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectory(file);
      }
    }
    return directoryToBeDeleted.delete();
  }

  /**
   * Get a list of all CSV files in the given directory.
   *
   * @param directory the directory to be searched
   * @return the list of CSV files present in the directory
   */
  public static List<File> getCsvFilesInDirectory(File directory) {
    if (!directory.exists()) {
      return null;
    }

    File[] allContents = directory.listFiles();
    List<File> csvFiles = Arrays
            .stream(allContents)
            .filter(f -> getFileExtension(f).equals("csv"))
            .collect(Collectors.toList());
    return csvFiles;
  }

  /**
   * Get the filename without its extension.
   *
   * @param file file object whose name needs extracted
   * @return filename without extension
   */
  public static String getFileNameWithoutExtension(File file) {
    if (file.isFile()) {
      String fileName = file.getName();
      int dotIndex = fileName.lastIndexOf('.');
      return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    } else {
      throw new IllegalArgumentException("The given input is not a file.");
    }
  }
}
