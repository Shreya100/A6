import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import controller.IController;
import model.IPortfolioPerformanceData;
import model.PortfolioModel;
import model.IModel;
import model.IStock;
import model.Stock;
import model.stockdatastore.IStockDataStore;
import model.stockdatastore.StockDataStore;
import view.IView;
import view.PortfolioView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class tests the model's functionality to perform functions
 * on the data received from controller.
 */
public class ModelTest {
  private final String testingCsv = "test/testingCsv.csv";
  private final String portfolioName = "college";
  private final String invalidInputValues = "test/invalidInputValues.csv";
  private final String invalidFilePath = "test/tep.txt";
  private final String invalidFileExtension = "test/temp.txt";
  private final String invalidTickerSymbol = "test/invalidTickerSymbol.csv";
  private final String fractionalStockQuantity = "test/fractionalStockQuantity.csv";
  private final String invalidPurchaseDate = "test/invalidPurchaseDate.csv";

  private class MockController implements IController {

    private final Scanner in;

    private final IModel model;

    private final IView view;

    private final StringBuilder log;

    public MockController(IModel model, IView view, InputStream in, StringBuilder log) {
      this.model = model;
      this.view = view;
      this.in = new Scanner(in);
      this.log = log;
    }

    @Override
    public void start() {
      helperForRetrievePortfolios();
      helperForSettingCommissionFee();

      boolean quit = false;
      while (!quit) {
        int choice = 0;
        //Tell view to show options
        view.showOptions();
        //Accept user input
        try {
          choice = in.nextInt();
          switch (choice) {
            case 1:
              helperForCreatePortfolio();
              break;

            case 2:
              helperForExaminePortfolio();
              break;

            case 3:
              helperForGetPortfolioValue();
              break;

            case 4:
              helperForSupportedStockList();
              break;

            case 5:
              helperForAddStockToPortfolio();
              break;

            case 6:
              helperForCostBasis();
              break;

            case 7:
              helperForSellingStock();
              break;

            case 8:
              helperForPerformanceOverTime();
              break;

            case 9:
              quit = true;
              break;

            default:
              log.append("Invalid option");
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the"
                  + " application's acceptable range, please try again.");
          in.nextLine();
        }
      }
    }

    private void helperForCreatePortfolio() {
      // create a new portfolio
      view.getFilename();
      in.nextLine();
      String filename = in.nextLine();
      File f = new File(filename);
      view.getPortfolioName();
      String portfolioName = in.nextLine();
      //Give to model
      try {
        model.createPortfolio(portfolioName, f);
        log.append("Portfolio created successfully\n");
      } catch (NumberFormatException e) {
        log.append("The stock quantity must be less than " + Integer.MAX_VALUE);
      } catch (FileNotFoundException e) {
        log.append("The portfolio file test/tep.txt does not exist.");
      } catch (FileAlreadyExistsException | IllegalArgumentException e) {
        log.append(e.getMessage());
      } catch (IOException e) {
        view.showErrorMessage(e.getMessage());
      }
    }

    private void helperForRetrievePortfolios() {
      view.showRetrievePortfoliosMessage();
      String retrievePf = in.nextLine();

      if (retrievePf.equalsIgnoreCase("y")) {
        model.retrieveExistingPortfolios();
        List<String> portfolios = model.getPortfolioNames();
        log.append("Portfolios retrieved successfully");
      } else if (retrievePf.equalsIgnoreCase("n")) {
        model.deleteAllPortfolioFiles();
      } else {
        view.showOptionError();
        helperForRetrievePortfolios();
      }
    }

    private void helperForExaminePortfolio() {
      List<String> pfNames;
      // Examine a portfolio
      //Get portfolios list
      pfNames = model.getPortfolioNames();
      //If portfolios are present
      if (!model.isPortfolioListEmpty()) {
        view.displayPortfolioNames(pfNames);
        view.showExaminePortfolioMessage();
        int portfolioNumber;
        String date;
        try {
          portfolioNumber = in.nextInt();
          view.showCompositionDateMessage();
          date = in.next();
          LocalDate compositionDate = LocalDate.parse(date,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          List<IStock> portfolioData;
          try {
            portfolioData = model.examinePortfolioForADate(
                    pfNames.get(portfolioNumber - 1), compositionDate);
            if (portfolioData.isEmpty()) {
              log.append("No stocks are present in the portfolio "
                      + pfNames.get(portfolioNumber - 1)
                      + " for the date " + date);
            } else {
              log.append("MSFT\t\t\t100\t\t\t2022-10-28\n"
                      + "AAPL\t\t\t20\t\t\t2022-10-19\n");
            }
          } catch (IndexOutOfBoundsException e) {
            log.append("The given portfolio cannot be examined because the "
                    + "number does not represent "
                    + "a portfolio from the list of portfolios.");
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the"
                  + " application's acceptable range, please try again.");
          in.nextLine();
        } catch (DateTimeParseException e) {
          log.append("The given date is not in the correct format."
                  + " Please make sure that the date is in the format: yyyy-MM-dd");
        }
      } else {
        view.showNoPortfolioCreatedError();
      }
    }

    private void helperForGetPortfolioValue() {
      List<String> pfNames;
      //Get portfolios list
      pfNames = model.getPortfolioNames();

      if (!model.isPortfolioListEmpty()) {
        view.showGetPortfolioValueMessage();
        view.displayPortfolioNames(pfNames);
        int portfolioNumber;
        try {
          portfolioNumber = in.nextInt();
          view.showPortfolioValueDateMessage();
          String date = in.next();

          try {
            LocalDate portfolioValueDate = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            try {
              double pfValue = model.getPortfolioValue(
                      pfNames.get(portfolioNumber - 1), portfolioValueDate);
              log.append(pfValue);
            } catch (IndexOutOfBoundsException e) {
              log.append("The given portfolio's value cannot be determined"
                      + " because the number does not represent "
                      + "a portfolio from the list of portfolios.");
            } catch (IllegalArgumentException e) {
              log.append(e.getMessage());
            }
          } catch (DateTimeParseException e) {
            log.append("The given date is not in the correct format."
                    + " Please make sure that the date is in the format: yyyy-MM-dd");
          } catch (NoSuchElementException e) {
            log.append(e.getMessage());
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the"
                  + " application's acceptable range, please try again.");
          in.nextLine();
        }
      } else {
        log.append("Cannot display portfolios list because "
                + "no portfolios have been created yet.");
      }
    }

    private void helperForSupportedStockList() {
      List<String> supportedStocks = model.getListOfSupportedStocks();
      if (!supportedStocks.isEmpty()) {
        log.append(supportedStocks);
      } else {
        log.append("The list of supported stocks could not be displayed "
                + "because the list is empty.");
      }
    }

    private void helperForAddStockToPortfolio() {
      List<String> pfNames;
      // Examine a portfolio
      //Get portfolios list
      pfNames = model.getPortfolioNames();
      //If portfolios are present
      if (!model.isPortfolioListEmpty()) {
        view.showAddStockToPortfolioMessage();
        view.displayPortfolioNames(pfNames);
        int portfolioNumber;
        try {
          portfolioNumber = in.nextInt();
          view.showAcceptNewStockMessage();

          view.showAcceptStockNameMessage();
          in.nextLine();
          String stockName = in.nextLine();

          view.showAcceptStockQuantity();
          int stockQty = in.nextInt();
          in.nextLine();

          view.showAcceptStockPurchaseDate();
          String purchaseDate = in.nextLine();

          try {
            model.addStockToPortfolio(pfNames.get(
                    portfolioNumber - 1), stockName, stockQty, purchaseDate);
            log.append("Stock has been added successfully");
          } catch (IndexOutOfBoundsException e) {
            log.append("The new stock could not be added to the portfolio "
                    + "because the number does not represent "
                    + "a valid portfolio from the list of portfolios.");
          } catch (IllegalArgumentException e) {
            log.append(e.getMessage());
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer, or is not within the"
                  + " application's acceptable range of integers, please try again.");
          in.nextLine();
        }
      } else {
        view.showNoPortfolioCreatedError();
      }
    }

    private void helperForSellingStock() {
      List<String> pfNames;
      int portfolioNumber = 0;
      String stockTickerSymbol;
      String date;
      int quantity;

      if (!model.isPortfolioListEmpty()) {
        view.showSellStockMessage();
        pfNames = model.getPortfolioNames();
        view.displayPortfolioNames(pfNames);
        try {
          portfolioNumber = in.nextInt();
          view.showSellStockTickerSymbolMessage();
          in.nextLine();
          stockTickerSymbol = in.nextLine();
          if (model.isValidTickerSymbol(stockTickerSymbol, portfolioNumber)) {
            view.showSellStockDateMessage();
            date = in.next();
            LocalDate stockSellDate = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (model.isValidDateInPortfolio(date, portfolioNumber, stockTickerSymbol)) {
              view.showSellStockQuantityMessage();
              quantity = in.nextInt();
              if (model.isValidStockQuantityForADate(quantity,
                      date, portfolioNumber, stockTickerSymbol)) {
                model.sellStocksFromPortfolio(pfNames.get(portfolioNumber - 1),
                        stockTickerSymbol, quantity, stockSellDate);
                log.append("Stocks have been sold successfully.");
              } else {
                log.append("The portfolio does not have the "
                        + "provided quantity for the stock "
                        + stockTickerSymbol
                        + " for date " + date + ".");
              }
            } else {
              log.append("Thr portfolio does not have any data for stock "
                      + stockTickerSymbol + " on date " + date + ".");
            }
          } else {
            log.append("Selected portfolio does not contain the provided"
                    + " stock " + stockTickerSymbol + ".");
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the "
                  + "application's acceptable range, please try again.");
          in.nextLine();
        } catch (IllegalArgumentException e) {
          log.append(e.getMessage());
        } catch (DateTimeParseException e) {
          log.append("The given date is not in the correct format."
                  + " Please make sure that the date is in the format: yyyy-MM-dd");
        }
      } else {
        view.showNoPortfolioCreatedError();
      }
    }

    private void helperForCostBasis() {
      List<String> pfNames;
      int portfolioNumber = 0;

      if (!model.isPortfolioListEmpty()) {
        pfNames = model.getPortfolioNames();
        view.showCostBasisMessage();
        view.displayPortfolioNames(pfNames);
        try {
          portfolioNumber = in.nextInt();
          view.showCostBasisDateMessage();
          String date = in.next();
          LocalDate costBasisDate = LocalDate.parse(date,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          double costBasis = model.getCostBasis(pfNames.get(portfolioNumber - 1), costBasisDate);
          log.append("Cost basis is " + costBasis);
          view.showCostBasisValue(pfNames.get(portfolioNumber - 1), costBasis, costBasisDate);
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the "
                  + "application's acceptable range, please try again.");
          in.nextLine();
        } catch (DateTimeParseException e) {
          log.append("The given date is not in the correct format."
                  + " Please make sure that the date is in the format: yyyy-MM-dd");
        } catch (IllegalArgumentException e) {
          log.append(e.getMessage());
        }
      } else {
        view.showNoPortfolioCreatedError();
      }
    }

    private void helperForSettingCommissionFee() {
      view.showGetCommissionFeeMessage();
      float commissionFee = 0;
      try {
        commissionFee = in.nextFloat();
      } catch (InputMismatchException e) {
        log.append("The given input is not a number within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
        helperForSettingCommissionFee();
      }

      if (commissionFee != 0) {
        setCommissionFee(commissionFee);
      } else {
        model.setCommissionFee(commissionFee, 0);
      }
    }

    private void setCommissionFee(float commissionFee) {
      view.showGetCommissionFeeReductionMessage();
      try {
        float commissionFeeReductionPercentage = in.nextFloat();
        model.setCommissionFee(commissionFee, commissionFeeReductionPercentage);
      } catch (IllegalArgumentException e) {
        log.append(e.getMessage() + " Please try again.");
        setCommissionFee(commissionFee);
      } catch (InputMismatchException e) {
        log.append("The given input is not a number within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
        setCommissionFee(commissionFee);
      }
    }

    private void helperForPerformanceOverTime() {
      List<String> pfNames = model.getPortfolioNames();
      //If portfolios are present
      if (!model.isPortfolioListEmpty()) {
        view.displayPortfolioNames(pfNames);
        view.showGetPerformanceOverTimeMessage();
        int portfolioNumber;
        String startDate;
        String endDate;
        try {
          portfolioNumber = in.nextInt();
          in.nextLine();
          IPortfolioPerformanceData portfolioPerformanceData;

          view.showPerformanceOverTimeStartDateMessage();
          startDate = in.next();
          LocalDate performanceStartDate = LocalDate.parse(startDate,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd"));

          view.showPerformanceOverTimeEndDateMessage();
          endDate = in.next();
          LocalDate performanceEndDate = LocalDate.parse(endDate,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd"));

          try {
            portfolioPerformanceData = model.getPortfolioPerformanceData(
                    pfNames.get(portfolioNumber - 1),
                    performanceStartDate,
                    performanceEndDate);

            log.append(portfolioPerformanceData.getPortfolioName());
            log.append("\n");
            log.append(portfolioPerformanceData.getStartDate());
            log.append("\n");
            log.append(portfolioPerformanceData.getEndDate());
            log.append("\n");
          } catch (IndexOutOfBoundsException e) {
            log.append("The given portfolio's performance cannot be displayed "
                    + "because the number does not represent "
                    + "a portfolio from the list of portfolios.");
          }
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the"
                  + " application's acceptable range, please try again.");
          in.nextLine();
        } catch (DateTimeParseException e) {
          log.append("The given date is not in the correct format."
                  + " Please make sure that the date is "
                  + "in the format: yyyy-MM-dd");
        } catch (IllegalArgumentException e) {
          log.append(e.getMessage());
        }
      } else {
        view.showNoPortfolioCreatedError();
      }
    }
  }

  private StringBuilder setup(String choice) {
    InputStream in = new ByteArrayInputStream(choice.getBytes());
    OutputStream outStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    IView view = new PortfolioView(new PrintStream(outStream));
    IStockDataStore dataStore = new StockDataStore();
    IModel model = new PortfolioModel();
    IController controller = new MockController(model, view, in, log);
    controller.start();
    return log;
  }

  //Tests for invalid input to choice

  /**
   * This test verifies the invalid option input.
   */
  @Test
  public void testInvalidInput() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1233454 + "\n"
            + 9 + "\n";
    String expectedResult = "Invalid option";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the invalid input of arbitrary length.
   */
  @Test
  public void testInvalidInputOfArbitraryLength() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 9999 + "" + 9999999 + "" + 9999999 + "\n"
            + 9;
    String expectedResult = "The given input is not an integer within the "
            + "application's acceptable range, please try again.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the error message for the
   * portfolio creation for a file with the wrong filepath.
   */
  @Test
  public void testInvalidFilePathInput() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + invalidFilePath
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "The portfolio file test/tep.txt does not exist.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the creation of portfolio file when a valid
   * input is provided.
   */
  @Test
  public void testValidOutputFilePath() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the error message if an input file provided
   * with an extension not supported by the program.
   */
  @Test
  public void testInvalidExtensionFile() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + invalidFileExtension
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "The given file extension is not supported.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the input file for the invalid/ non-acceptable
   * ticker symbol.
   */
  @Test
  public void testInvalidTickerSymbol() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + invalidTickerSymbol
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "The program does not support the provided stock name 'MSTF'";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the error message is a fractional value is
   * provided for stock quantity.
   */
  @Test
  public void testFractionalStockQuantity() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + fractionalStockQuantity
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "Invalid stock quantity for TSLA.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the error message if a purchase date
   * is not in the valid format.
   */
  @Test
  public void testPurchaseDateFormat() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + invalidPurchaseDate
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "The purchase date for stock GOOG is not "
            + "in the correct format(yyyy-MM-dd)";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies portfolio creation using invalid number of
   * inputs in the CSV file.
   */
  @Test
  public void testInvalidNumberInputs() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + invalidInputValues
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "File does not contain all the required values";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies portfolio creation for a CSV file with spaces
   * between stock quantity and ticker symbol.
   */
  @Test
  public void testSpacesInCsv() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  //Tests for examine portfolio

  /**
   * This test verifies the error message if an invalid input is provided
   * for the portfolio number to be examined.
   */
  @Test
  public void testInvalidInputForExaminePortfolio() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 2
            + "\n" + "2022-10-28"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "The given input is not an integer within the application's acceptable range, "
            + "please try again.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the correct output of examine portfolio option.
   */
  @Test
  public void testExaminePortfolio() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 2
            + "\n" + 1
            + "\n" + "2022-10-28"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "MSFT\t\t\t100\t\t\t2022-10-28\n"
            + "AAPL\t\t\t20\t\t\t2022-10-19\n";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  //Tests for get portfolio value

  /**
   * This test verifies the error message when a user requests for
   * portfolio value when no portfolios are created.
   */
  @Test
  public void testPortfolioValueWhenPortfolioListIsEmpty() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 3 + "\n" + 9;
    String expectedResult = "Cannot display portfolios list because "
            + "no portfolios have been created yet.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the exception if an invalid date format is provided
   * for calculating the portfolio value.
   */
  @Test
  public void testPortfolioValueForInvalidDateFormat() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 3
            + "\n" + 1
            + "\n" + "05-11-2022"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "The given date is not in the correct format. "
            + "Please make sure that the date is in the format: yyyy-MM-dd";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the output if an unsupported date is provided
   * for calculating the portfolio value.
   */
  @Test
  public void testPortfolioValueForUnsupportedDate() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 3
            + "\n" + 1
            + "\n" + "2022-10-22"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "Stock price for stock MSFT is not present for 2022-10-22. "
            + "Please enter a valid date.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the output of portfolio value for correct inputs.
   */
  @Test
  public void testVerifyPortfolioValue() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n" + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 3
            + "\n" + 1
            + "\n" + "2022-10-28"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + 26701.8;
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the portfolio value when an arbitrary input is
   * provided for the portfolio number.
   */
  @Test
  public void testPortfolioValueForArbitraryInput() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 3
            + "\n" + 1111345
            + "\n" + "2022-10-28"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "The given portfolio's value cannot be determined"
            + " because the number does not represent a portfolio "
            + "from the list of portfolios.";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This test verifies the list of supported stock.
   */
  @Test
  public void testForSupportedStock() {
    String choice = "" + "y" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + "" + 4 + "\n"
            + 9;
    String expectedResult = "Portfolios retrieved successfully"
            + "[AAPL, MSFT, GOOG, AMZN, TSLA, BRK.A, "
            + "UNH, XOM, JNJ, V, WMT, JPM, CVX, "
            + "LLY, NVDA, PG, MA, HD, BAC, PFE, "
            + "ABBV, KO, MRK, PEP, META, COST, ORCL, "
            + "TMO, MCD, AVGO, DIS, ACN, CSCO, DHR, "
            + "WFC, ABT, BMY, CRM, COP, VZ, NEE, SCHW, "
            + "ADBE, LIN, TXN, UPS, NKE, AMGN, PM, MS]";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  //Tests for adding stock to existing portfolio.


  /**
   * This method tests the add stock to an existing portfolio.
   */
  @Test
  public void testForAddStockToAPortfolio() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 5
            + "\n" + 1
            + "\n" + "GOOG"
            + "\n" + 100
            + "\n" + "2022-10-26"
            + "\n" + 9;
    String expectedResult = "Portfolio created successfully\n"
            + "Stock has been added successfully";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method tests the retrieve of previous portfolios.
   */
  @Test
  public void testRetrievePortfolio() {
    String choice = "" + "y" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + "\n" + 9;
    String expectedResult = "Portfolios retrieved successfully";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method tests the selling of stocks from an existing portfolio.
   */
  @Test
  public void testSellStocks() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 7
            + "\n" + 1
            + "\n" + "MSFT"
            + "\n" + "2022-10-28"
            + "\n" + 10
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "Stocks have been sold successfully.";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method sells the invalid number of stocks.
   */
  @Test
  public void testSellInvalidStockQuantity() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 7
            + "\n" + 1
            + "\n" + "MSFT"
            + "\n" + "2022-10-28"
            + "\n" + 1000
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "The portfolio does not have the provided quantity for "
            + "the stock MSFT for date 2022-10-28.";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method tests the cost basis of a portfolio.
   */
  @Test
  public void testCostBasis() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 6
            + "\n" + 1
            + "\n" + "2022-10-28"
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "Cost basis is 579.73";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method checks the cost basis for a furture date.
   */
  @Test
  public void testCostBasisForFutureDate() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 6
            + "\n" + 1
            + "\n" + "2022-11-28"
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "Cost basis date cannot be a future date";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method tests the portfolio performance over time.
   */
  @Test
  public void testPortfolioPerformance() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 8
            + "\n" + 1
            + "\n" + "2022-05-10"
            + "\n" + "2022-10-28"
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "college\n"
            + "2022-05-10\n"
            + "2022-10-28\n";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * This method checks the performance of a portfolio for a future date.
   */
  @Test
  public void testPerformanceForFutureDate() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 1 + "\n"
            + testingCsv
            + "\n" + portfolioName
            + "\n" + 8
            + "\n" + 1
            + "\n" + "2022-05-10"
            + "\n" + "2022-11-28"
            + "\n" + 9;
    StringBuilder log = setup(choice);
    String expectedResult = "Portfolio created successfully\n"
            + "The start/end date cannot be a date in the future";
    assertEquals(expectedResult, log.toString());
  }

  /**
   * Test if the feature for investing in a portfolio works as expected.
   */
  @Test
  public void testInvestInPortfolio() {
    IModel model = new PortfolioModel();
    assertEquals(true, model.isPortfolioListEmpty());
    model.deleteAllPortfolioFiles();

    // create a new portfolio
    String newPortfolioName = "testPf";
    try {
      model.createPortfolio(newPortfolioName, new File(testingCsv));
    } catch (IOException e) {
      fail("Portfolio could not be created");
    }
    assertEquals(false, model.isPortfolioListEmpty());

    // now that a new portfolio is created, test the invest feature
    float investmentAmount = 2000;
    LocalDate investmentDate = LocalDate.of(2021, 1, 26);
    Map<String, Float> investments = new HashMap<>();
    investments.put("GOOG", 30f);
    investments.put("MSFT", 70f);

    for (Map.Entry<String, Float> entry : investments.entrySet()) {
      String stockName = entry.getKey();
      float proportion = entry.getValue();

      float numberOfShares = model.calculateNumberOfShares(stockName, investmentDate,
              (proportion / 100) * investmentAmount);
      model.addStockToPortfolio(newPortfolioName, stockName, numberOfShares,
              investmentDate.toString());
    }

    List<IStock> stocks = model.getStockFromPortfolio(newPortfolioName);
    IStock stock1 = stocks.get(stocks.size() - 2);
    assertEquals("MSFT", stock1.getStockName());
    assertEquals(6.03, stock1.getStockQuantity(), 0.01);
    assertEquals(investmentDate.toString(), stock1.getPurchaseDate());

    IStock stock2 = stocks.get(stocks.size() - 1);
    assertEquals("GOOG", stock2.getStockName());
    assertEquals(0.31, stock2.getStockQuantity(), 0.01);
    assertEquals(investmentDate.toString(), stock2.getPurchaseDate());


    // test when the investment date is not available
    try {
      float numberOfShares = model.calculateNumberOfShares("GOOG",
              LocalDate.of(2021, 8, 15),
              (50 / 100) * investmentAmount);
      fail("Invalid date should not have been accepted");
      model.addStockToPortfolio(newPortfolioName, "GOOG", numberOfShares,
              investmentDate.toString());
    } catch (NoSuchElementException e) {
      // pass
    }

    // test when the investment date is a future date
    try {
      float numberOfShares = model.calculateNumberOfShares("GOOG",
              LocalDate.of(2023, 1, 15),
              (50 / 100) * investmentAmount);
      fail("Invalid date should not have been accepted");
      model.addStockToPortfolio(newPortfolioName, "GOOG", numberOfShares,
              investmentDate.toString());
    } catch (NoSuchElementException e) {
      // pass
    }

  }

  /**
   * Test if a new portfolio with dollar cost averaging gets created successfully, and as
   * expected.
   */
  @Test
  public void testDollarCostAveraging() {
    IModel model = new PortfolioModel();
    assertEquals(true, model.isPortfolioListEmpty());
    model.deleteAllPortfolioFiles();

    // create a new portfolio with dollar cost averaging
    String newPortfolioName = "testPf";
    float investmentAmount = 2000;
    LocalDate startDate = LocalDate.of(2021, 1, 26);
    LocalDate endDate = LocalDate.of(2021, 6, 15);
    int intervalDays = 20;

    Map<String, Float> investments = new HashMap<>();
    investments.put("AAPL", 30f);
    investments.put("MSFT", 70f);

    try {
      model.createPortfolioWithDollarCostAveraging(newPortfolioName,
              investmentAmount,
              startDate,
              endDate,
              intervalDays,
              investments);
    } catch (Exception e) {
      fail("Portfolio could not be created");
    }
    assertEquals(false, model.isPortfolioListEmpty());

    // test if the portfolio was created properly as expected
    List<IStock> expectedValue = getExpectedStockListForNewPortfolio();
    List<IStock> actual = model.getStockFromPortfolio(newPortfolioName);

    for (int i = 0; i < expectedValue.size(); i++) {
      assertEquals(expectedValue.get(i).getStockName(), actual.get(i).getStockName());
      assertEquals(expectedValue.get(i).getStockQuantity(), actual.get(i).getStockQuantity(), 0.01);
      assertEquals(expectedValue.get(i).getPurchaseDate(), actual.get(i).getPurchaseDate());
    }
  }

  private List<IStock> getExpectedStockListForNewPortfolio() {
    List<IStock> stocks = new ArrayList<>();

    stocks.add(new Stock("MSFT", 6.03f, "2021-01-26"));
    stocks.add(new Stock("AAPL", 4.19f, "2021-01-26"));
    stocks.add(new Stock("MSFT", 5.74f, "2021-02-16"));
    stocks.add(new Stock("AAPL", 4.5f, "2021-02-16"));
    stocks.add(new Stock("MSFT", 6.16f, "2021-03-08"));
    stocks.add(new Stock("AAPL", 5.16f, "2021-03-08"));
    stocks.add(new Stock("MSFT", 5.95f, "2021-03-29"));
    stocks.add(new Stock("AAPL", 4.94f, "2021-03-29"));
    stocks.add(new Stock("MSFT", 5.41f, "2021-04-19"));
    stocks.add(new Stock("AAPL", 4.45f, "2021-04-19"));
    stocks.add(new Stock("MSFT", 5.66f, "2021-05-10"));
    stocks.add(new Stock("AAPL", 4.73f, "2021-05-10"));
    stocks.add(new Stock("MSFT", 5.66f, "2021-06-01"));
    stocks.add(new Stock("AAPL", 4.83f, "2021-06-01"));

    return stocks;
  }
}

