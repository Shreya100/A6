import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import controller.IController;
import model.IModel;
import model.IPortfolioPerformanceData;
import model.IStock;
import model.PortfolioModel;
import view.IView;

import static org.junit.Assert.assertEquals;

/**
 * This test class is meant for testing the PortfolioView class.
 * This class tests that the view displays the expected results to the user.
 */
public class ViewTest {
  private class MockView implements IView {
    private final StringBuilder log;


    protected MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void showOptions() {
      log.append("Menu: ");
      log.append("1: Create portfolio");
      log.append("2: Examine portfolio composition");
      log.append("3: Get portfolio value for a specific date");
      log.append("4: View list of supported stocks");
      log.append("5: Quit");
      log.append("Enter your choice: ");
    }

    @Override
    public void getFilename() {
      // do nothing here
    }

    @Override
    public void showOptionError() {
      log.append("Invalid option");
    }

    @Override
    public void getPortfolioName() {
      // do nothing here
    }

    @Override
    public void fileAlreadyExistsError(String exception) {
      // do nothing here
    }

    @Override
    public void fileNotFoundError(String exception) {
      // do nothing here
    }

    @Override
    public void showErrorMessage(String exception) {
      log.append(exception);
    }

    @Override
    public void displayPortfolioNames(List<String> pfNames) {
      //do nothing here
    }

    @Override
    public void displayExaminePortfolio(String portfolioName, List<IStock> portfolioData) {
      // do nothing here
    }

    @Override
    public void showPortfolioCreatedMessage(String portfolioName) {
      // do nothing here
    }

    @Override
    public void showNoPortfolioCreatedError() {
      log.append("No portfolios created.");
    }

    @Override
    public void showExaminePortfolioMessage() {
      // do nothing here
    }

    @Override
    public void showGetPortfolioValueMessage() {
      // do nothing here
    }

    @Override
    public void showPortfolioValueDateMessage() {
      // do nothing here
    }

    @Override
    public void displayPortfolioValue(String portfolioName,
                                      LocalDate portfolioValueDate,
                                      double pfValue) {
      // do nothing here
    }

    @Override
    public void showGetSupportedStocks(List<String> supportedStocksList) {
      // do nothing here
    }

    @Override
    public void showAddStockToPortfolioMessage() {
      // do nothing here
    }

    @Override
    public void showAcceptNewStockMessage() {
      // do nothing here
    }

    @Override
    public void displayNewStockAddedMessage(String portfolioName, String stockName) {
      // do nothing here
    }

    @Override
    public void showAcceptStockNameMessage() {
      // do nothing here
    }

    @Override
    public void showAcceptStockQuantity() {
      // do nothing here
    }

    @Override
    public void showAcceptStockPurchaseDate() {
      // do nothing here
    }

    @Override
    public void showRetrievePortfoliosMessage() {
      // do nothing here
    }

    @Override
    public void showPortfoliosRetrievedMessage(List<String> portfolios) {
      // do nothing here
    }

    @Override
    public void showCompositionDateMessage() {
      // do nothing here
    }

    @Override
    public void showGetCommissionFeeMessage() {
      // do nothing here
    }

    @Override
    public void showGetCommissionFeeReductionMessage() {
      // do nothing here
    }

    @Override
    public void showCostBasisMessage() {
      // do nothing here
    }

    @Override
    public void showCostBasisDateMessage() {
      // do nothing here
    }

    @Override
    public void showCostBasisValue(String portfolioName, double costBasis, LocalDate date) {
      // do nothing here
    }

    @Override
    public void showSellStockMessage() {
      // do nothing here
    }

    @Override
    public void showSellStockDateMessage() {
      // do nothing here
    }

    @Override
    public void showSellStockTickerSymbolMessage() {
      // do nothing here
    }

    @Override
    public void showSellStockQuantityMessage() {
      // do nothing here
    }

    @Override
    public void showSuccessfulSellStockMessage() {
      // do nothing here
    }

    @Override
    public void showDateOrNoDateOption() {
      //do nothing here
    }

    @Override
    public void showGetPerformanceOverTimeMessage() {
      //do nothing here
    }

    @Override
    public void showPerformanceOverTimeStartDateMessage() {
      //do nothing here
    }

    @Override
    public void showPerformanceOverTimeEndDateMessage() {
      //do nothing here
    }

    @Override
    public void displayPortfolioPerformanceData(
            IPortfolioPerformanceData portfolioPerformanceData) {
      //do nothing here
    }

    @Override
    public void showInvestInExistingPortfolioMessage() {
      // do nothing here
    }

    @Override
    public void showAmountToBeInvestedMessage() {
      // do nothing here
    }

    @Override
    public void showAmountToBeInvestedInEachStockMessage() {
      // do nothing here
    }

    @Override
    public void showInvestmentDateMessage() {
      // do nothing here
    }

    @Override
    public void showStockMessage(String stockName) {
      // do nothing here
    }

    @Override
    public void showMessageIfHundredPercentNotInvested() {
      // do nothing here
    }
  }

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
        view.showPortfoliosRetrievedMessage(portfolios);
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
            model.addStockToPortfolio(
                    pfNames.get(portfolioNumber - 1), stockName, stockQty, purchaseDate);
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
                view.showSuccessfulSellStockMessage();
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
          view.showCostBasisValue(pfNames.get(portfolioNumber - 1), costBasis, costBasisDate);
        } catch (InputMismatchException e) {
          log.append("The given input is not an integer within the "
                  + "application's acceptable range, please try again.");
          in.nextLine();
        } catch (DateTimeParseException e) {
          log.append("The given date is not in the correct format."
                  + " Please make sure that the date is in the format: yyyy-MM-dd");
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
  }

  private StringBuilder setup(String choice) {
    IModel model = new PortfolioModel();
    InputStream in = new ByteArrayInputStream(choice.getBytes());
    StringBuilder log = new StringBuilder();
    IView view = new MockView(log);
    IController controller = new MockController(model, view, in, log);
    controller.start();
    return log;
  }

  /**
   * This test checks the output of showOptions method.
   */
  @Test
  public void testShowOptions() {
    String choice = "" + "n" + "\n"
            + 100 + "\n"
            + 1 + "\n"
            + 8 + "\n";
    String expectedResult = "Menu: 1: Create portfolio"
            + "2: Examine portfolio composition"
            + "3: Get portfolio value for a specific date"
            + "4: View list of supported stocks"
            + "5: QuitEnter your choice: ";
    StringBuilder log = setup(choice);
    assertEquals(expectedResult, log.toString());
  }
}
