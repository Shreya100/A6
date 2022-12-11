package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.IModel;
import model.IPortfolioPerformanceData;
import model.IStock;
import view.IView;

/**
 * The PortfolioController class implements the IController interface.
 * This class binds the model and view together, and provides the starting point of the
 * application.
 */
public class PortfolioController implements IController {

  private final Scanner in;

  private final IModel model;

  private final IView view;

  /**
   * Create a new controller initialized with the given model, view, in, and out.
   *
   * @param model the model object representing the stock portfolios
   * @param view  the view responsible for displaying the data related to the stock portfolios model
   * @param in    the input stream object
   */
  public PortfolioController(IModel model, IView view, InputStream in) {
    this.model = model;
    this.view = view;
    this.in = new Scanner(in);
  }

  @Override
  public void start() {
    // Provide the option for restoring existing portfolios from storage
    helperForRetrievePortfolios();

    // Get the commission fees
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
            helperForInvestingInExistingPortfolio();
            break;

          case 10:
            quit = true;
            break;

          case 11:
            helperForRebalancingPortfolio();
            break;

          default:
            view.showOptionError();
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
      }
    }
  }

  private void helperForRebalancingPortfolio(){
    List<String> pfNames;

    //Get portfolios list
    pfNames = model.getPortfolioNames();
    //If portfolios are present
    if (!model.isPortfolioListEmpty()) {
      view.displayPortfolioNames(pfNames);
      view.showRebalancePortfolioMessage();
      int portfolioNumber;
      String date;
      try {
        portfolioNumber = in.nextInt();
        if(portfolioNumber < 0 || portfolioNumber > pfNames.size()) {
          throw new IndexOutOfBoundsException("");
        }
        view.showRebalanceDateMessage();
        date = in.next();
        LocalDate compositionDate = LocalDate.parse(date,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
          Map<String, Double> sp = new HashMap<>();
          DateTimeFormatter ff = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          Map<String, Double> stockData = new HashMap<>();
          String stockNames = "";
          for(IStock is : model.getStockFromPortfolio(pfNames.get(portfolioNumber - 1))) {
            if(LocalDate.parse(is.getPurchaseDate(), ff).compareTo(compositionDate) <= 0) {
              if(stockData.containsKey(is.getStockName())) {
                stockData.put(is.getStockName(), stockData.get(is.getStockName())+is.getStockQuantity());
              } else {
                stockData.put(is.getStockName(), (double) is.getStockQuantity());
                stockNames += is.getStockName() + ",";
              }
            }
          }
          stockNames = stockNames.substring(0, stockNames.length()-1);
          System.out.println("Specify the percents of following stocks in comma seperated manner");
          System.out.println(stockNames);
          System.out.println("Specify comma seperated list of percents for above stocks.");
          String percents = in.next();
          if(percents.split(",").length != stockNames.split(",").length) {
            throw new IllegalArgumentException("Percents length should be same as stocks!");
          }
          String[] stockNameArr = stockNames.split(",");
          String[] percentArr = percents.split(",");
          Double pp = 100d;
          for(int kk =0; kk < stockNameArr.length; kk++) {
            Double value = Math.round(Double.valueOf(percentArr[kk]) * 100.0) / 100.0;
            if(value < 0 || value > pp) {
              throw new IllegalArgumentException("Entered Percents are not valid!");
            }
            pp -= value;
            sp.put(stockNameArr[kk], value);
          }
          model.balancePortfolio(pfNames.get(portfolioNumber-1), compositionDate, sp);
          view.displayRebalancePortfolio();
        } catch (IndexOutOfBoundsException e) {
          view.showErrorMessage("The given portfolio cannot be examined because the "
              + "number does not represent "
              + "a portfolio from the list of portfolios.");
        } catch (IllegalArgumentException | ParseException e) {
          view.showErrorMessage(e.getMessage());
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
            + " application's acceptable range, please try again.");
        in.nextLine();
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
            + " Please make sure that the date is "
            + "in the format: yyyy-MM-dd");
      }
    } else {
      view.showNoPortfolioCreatedError();
    }
  }

  private void helperForInvestingInExistingPortfolio() {
    List<String> pfNames = model.getPortfolioNames();
    if (!model.isPortfolioListEmpty()) {
      view.displayPortfolioNames(pfNames);
      view.showInvestInExistingPortfolioMessage();
      int portfolioNumber;
      float amountToBeInvested;
      float totalPercentageAmount = 0;
      List<IStock> portfolioData;
      String date;

      try {
        portfolioNumber = in.nextInt();
        view.showInvestmentDateMessage();
        date = in.next();
        LocalDate purchaseDate = LocalDate.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        view.showAmountToBeInvestedMessage();
        amountToBeInvested = in.nextFloat();
        portfolioData = model.getStockFromPortfolio(pfNames.get(portfolioNumber - 1));
        view.showAmountToBeInvestedInEachStockMessage();
        for (IStock s : portfolioData) {
          String stockName = s.getStockName();
          if (totalPercentageAmount != 100) {
            view.showStockMessage(s.getStockName());
            float percentageAmount = in.nextFloat();
            totalPercentageAmount += percentageAmount;
            float numberOfShares = model.calculateNumberOfShares(stockName,
                    purchaseDate,
                    (percentageAmount / 100) * amountToBeInvested);
            model.addStockToPortfolio(pfNames.get(portfolioNumber - 1),
                    stockName, numberOfShares, date);
            view.displayNewStockAddedMessage(pfNames.get(portfolioNumber - 1), stockName);
          }
        }
        if (totalPercentageAmount != 100) {
          view.showMessageIfHundredPercentNotInvested();
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
                + " Please make sure that the date is "
                + "in the format: yyyy-MM-dd");
      } catch (NoSuchElementException e) {
        view.showErrorMessage(e.getMessage());
      }
    } else {
      view.showNoPortfolioCreatedError();
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

          view.displayPortfolioPerformanceData(portfolioPerformanceData);
        } catch (IndexOutOfBoundsException e) {
          view.showErrorMessage("The given portfolio's performance cannot be displayed "
                  + "because the number does not represent "
                  + "a portfolio from the list of portfolios.");
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
                + " Please make sure that the date is "
                + "in the format: yyyy-MM-dd");
      } catch (IllegalArgumentException e) {
        view.showErrorMessage(e.getMessage());
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
          view.showSellStockQuantityMessage();
          quantity = in.nextInt();
          model.sellStocksFromPortfolio(pfNames.get(portfolioNumber - 1),
              stockTickerSymbol,
              quantity,
              stockSellDate);
          view.showSuccessfulSellStockMessage();
        } else {
          view.showErrorMessage("Selected portfolio does not contain the provided"
              + " stock " + stockTickerSymbol + ".");
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the "
            + "application's acceptable range, please try again.");
        in.nextLine();
      } catch (IllegalArgumentException e) {
        view.showErrorMessage(e.getMessage());
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
            + " Please make sure that the date is "
            + "in the format: yyyy-MM-dd");
      } catch (IndexOutOfBoundsException e) {
        view.showErrorMessage("The cost basis for the given portfolio cannot be "
            + "calculated because the number does not represent "
            + "a portfolio from the list of portfolios.");
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
        view.showErrorMessage("The given input is not an integer within the "
                + "application's acceptable range, please try again.");
        in.nextLine();
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
                + " Please make sure that the date is "
                + "in the format: yyyy-MM-dd");
      } catch (IllegalArgumentException e) {
        view.showErrorMessage(e.getMessage());
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
      view.showErrorMessage("The given input is not a number within the"
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
      view.showErrorMessage(e.getMessage() + " Please try again.");
      setCommissionFee(commissionFee);
    } catch (InputMismatchException e) {
      view.showErrorMessage("The given input is not a number within the"
              + " application's acceptable range, please try again.");
      in.nextLine();
      setCommissionFee(commissionFee);
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
      view.showPortfolioCreatedMessage(portfolioName);
    } catch (FileNotFoundException e) {
      view.fileNotFoundError(e.getMessage());
    } catch (FileAlreadyExistsException e) {
      view.fileAlreadyExistsError(e.getMessage());
    } catch (NumberFormatException e) {
      view.showErrorMessage("The stock quantity must be less than " + Integer.MAX_VALUE);
    } catch (IllegalArgumentException e) {
      view.showErrorMessage(e.getMessage());
    } catch (InvalidObjectException e) {
      view.showErrorMessage(e.getMessage());
    } catch (IOException e) {
      view.showErrorMessage(e.getMessage());
    } catch (NoSuchElementException e) {
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
        view.showDateOrNoDateOption();
        in.nextLine();
        String answer = in.nextLine();
        List<IStock> portfolioData;
        if (answer.equalsIgnoreCase("y")) {
          view.showCompositionDateMessage();
          date = in.next();
          LocalDate compositionDate = LocalDate.parse(date,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          try {
            portfolioData = model.examinePortfolioForADate(
                    pfNames.get(portfolioNumber - 1), compositionDate);
            if (portfolioData.isEmpty()) {
              view.showErrorMessage("No stocks are present in the portfolio "
                      + pfNames.get(portfolioNumber - 1)
                      + " for the date " + date);
            } else {
              view.displayExaminePortfolio(pfNames.get(portfolioNumber - 1), portfolioData);
            }
          } catch (IndexOutOfBoundsException e) {
            view.showErrorMessage("The given portfolio cannot be examined because the "
                    + "number does not represent "
                    + "a portfolio from the list of portfolios.");
          } catch (IllegalArgumentException e) {
            view.showErrorMessage(e.getMessage());
          }
        } else if (answer.equalsIgnoreCase("n")) {
          portfolioData = model.examinePortfolio(pfNames.get(portfolioNumber - 1));
          if (portfolioData.isEmpty()) {
            view.showErrorMessage("No stocks are present in the portfolio "
                    + pfNames.get(portfolioNumber - 1));
          } else {
            view.displayExaminePortfolio(pfNames.get(portfolioNumber - 1), portfolioData);
          }
        } else {
          view.showErrorMessage("Invalid option. Please try again.");
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
      } catch (DateTimeParseException e) {
        view.showErrorMessage("The given date is not in the correct format."
                + " Please make sure that the date is "
                + "in the format: yyyy-MM-dd");
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
            view.displayPortfolioValue(pfNames.get(portfolioNumber - 1),
                    portfolioValueDate,
                    pfValue);
          } catch (IndexOutOfBoundsException e) {
            view.showErrorMessage("The given portfolio's value cannot be determined"
                    + " because the number does not represent "
                    + "a portfolio from the list of portfolios.");
          } catch (IllegalArgumentException e) {
            view.showErrorMessage(e.getMessage());
          }
        } catch (DateTimeParseException e) {
          view.showErrorMessage("The given date is not in the correct format."
                  + " Please make sure that the date is in the format: yyyy-MM-dd");
        } catch (NoSuchElementException e) {
          view.showErrorMessage(e.getMessage());
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer within the"
                + " application's acceptable range, please try again.");
        in.nextLine();
      }
    } else {
      view.showErrorMessage("Cannot display portfolios list because "
              + "no portfolios have been created yet.");
    }
  }

  private void helperForSupportedStockList() {
    List<String> supportedStocks = model.getListOfSupportedStocks();
    if (!supportedStocks.isEmpty()) {
      view.showGetSupportedStocks(supportedStocks);
    } else {
      view.showErrorMessage("The list of supported stocks could not be displayed "
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
          model.addStockToPortfolio(pfNames.get(portfolioNumber - 1), stockName,
                  stockQty, purchaseDate);
          view.displayNewStockAddedMessage(pfNames.get(portfolioNumber - 1), stockName);
        } catch (IndexOutOfBoundsException e) {
          view.showErrorMessage("The new stock could not be added to the portfolio "
                  + "because the number does not represent "
                  + "a valid portfolio from the list of portfolios.");
        } catch (IllegalArgumentException e) {
          view.showErrorMessage(e.getMessage());
        }
      } catch (InputMismatchException e) {
        view.showErrorMessage("The given input is not an integer, or is not within the"
                + " application's acceptable range of integers, "
                + "please try again.");
        in.nextLine();
      }
    } else {
      view.showNoPortfolioCreatedError();
    }
  }
}
