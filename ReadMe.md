## How to run the program
    1. Make sure your directory which has the Assignment4Stocks.jar file must have the folder “StockDataFiles”.
        * StockDataFiles is offline storage for the stock prices on the dates supported by the program. (The list of dates is given at the end of this document.)
        * Before running the Assignment4Stocks.jar file, extract the compressed file named “StockDataFiles.zip”.
        * Now you should have a folder named “StockDataFiles” in the same directory as the Assignment4Stocks.jar file
    2. Use the following command to run the Assignment4Stocks.jar file
       java -jar Assignment4Stocks.jar
    3. The program requires input from the user in the form of a CSV file.
    4. The CSV file should contain the stocks that the user wants to buy.
    5. Format of the CSV file:
        * <ticker symbol>,<quantity>,<date>
            * ticker symbol:- ticker symbol of the stocks a user wants to buy. The supported stock list is provided below.
            * quantity:- The number of shares/stocks a user wants to buy. This should not be a fractional value, if a fractional value is provided then the program does not create the portfolio for the given input. Also, the input quantity must be within the reasonable integer limit.
            * date:- This is the purchase date on which the user wants to buy the stock. The date should be in the “yyyy-MM-dd” format.
        * Example:-
            * MSFT,100,2022-30-10
        * A file can have multiple and duplicate entries of the stocks the user wants to buy. If a duplicate stock (another stock of the same organization purchased on the same date) exists, then this stock’s quantity is simply added to the existing stock, instead of creating a new stock entry in the portfolio.
* To run the program from IntelliJ, right click on the porject and go to project structure-> libraries -> + 
  -> JFreeChart version 1.5.3 -> Ok (This would add the jfreechart for the project).

* To run the console based code, add the following lines in the StoclsStartercode:
  `IModel model = new PortfolioModel();
  IView view = new PortfolioView(System.out);
  IController controller = new PortfolioController(model, view, System.in);
  controller.start();`

## Complete
* We were able to add the rebalance feature to the model, controller and gui/console based view
* Added critique document 

## Summary 
### Model


### Controller
##### Console Based UI
A helper function "helperForRebalancingPortfolio" was added to the controller that 
takes the portfolio name, date and the percentage value of shares and validates 
the values and sends them to the model to re balance the portfolio

#### GUI
3 Features were added to the GUI controller. 
* getStocksOfPortfolio: fetches stocks from the model from the given portfolio name.
* reBalancePortfolioOnASpecificDate: to display the menu to re balance the portfolio.
* reBalancePortfolioWithDate: to validate percentage of stocks and calling the rebalance function 
from the model.

### View
#### Console based view
* showRebalancePortfolioMessage: Show prompt for portfolio rebalance.
* showRebalanceDateMessage: Prompts input for date for portfolio rebalance.
* displayRebalancePortfolio: Shows success message for rebalance of portfolio.

#### GUI
* showPortfolioReBalanceMessage: Shows success message for rebalance of portfolio.
* displayStocks: Displays stocks of portfolio.
* showTextFieldsForInputsRebalance: Shows prompt for portfolio Re-balance.


