SETUP-README


* How to run the program:-
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




* Run the program to create a portfolio with 3 different stocks
   1. Create a new CSV file with the following data:
MSFT,100,2022-10-20
NVDA,11,2022-9-10
AAPL,20,2022-10-20


      * We have already created a CSV file with the following data in the directory “res/examples” with the name “PortfoliosWith3Stocks.csv”. So, you may use this example file and edit it as requires for testing the application.
   2. Run the program (Assignment4Stocks.jar file)
      * Enter option 1 to create a new portfolio
      * Enter the complete path of the CSV file when the program prompts (you can enter the path of PortfoliosWith3Stocks.csv):- Please enter the path of portfolio data file:
      * Enter the portfolio name when the program prompts:- Enter portfolio name:
         * This name can be any name for your portfolio. For example, college, retirement, etc.
      * If the given CSV file exists and the portfolio with the same name isn’t already present then you’ll see a success message:- Portfolio created successfully + portfolioName (provided in the above step)
      * The program will again display the menu options. Please select option 3 to get the portfolio value on a specific date:- Get portfolio value for a specific date
         * This option displays the available portfolios. Please select the portfolio number you wish to get the value for, after the message:- Select a portfolio whose value you wish see:
         * After selecting the portfolio number, the program asks for a date for which you want to calculate the value:- Enter the market date for calculating the portfolio value (Date format: yyyy-MM-dd): 
            * Enter the stock market date supported by the program. (These are mentioned at the end of this document). If a date that is not supported by the application is entered, the user will be prompted with an error message.
      * The program will again display the menu options. Please select option 6 to get the cost basis of a portfolio:- Get cost basis of a portfolio on a specific date
         * This option displays the available portfolios. Please select the portfolio number you wish to get the value for after the message:- Select a portfolio to which you wish to get the cost basis value:
         * After selecting the portfolio number, the program asks for a date for which you want to calculate the value:- Enter the date for which you wish to get the cost basis of the portfolio (Date format: yyyy-MM-dd )
            * Enter the date supported by the program. 
            * You can enter 2022-10-20 to get the cost basis value of a portfolio provided above (PortfoliosWith3Stocks.csv)
            * You can also try getting the cost basis for 2022-09-10. 




* Run the program to create a portfolio with 2 different stocks
   1. Create a new CSV file with the following data:
GOOG,100,2022-10-20
NVDA,11,2022-9-10


      * We have already created a CSV file with the following data in the directory “res/examples” with the name “PortfoliosWith2Stocks.csv”. So, you may use this example file and edit it as requires for testing the application.
   2. Run the program (jar file)
      * Select option 1 to create a new portfolio
      * Enter the complete path of the CSV file when the program prompts (you can enter the path of PortfoliosWith2Stocks.csv):- Please enter the path of portfolio data file:
      * Enter the portfolio name when the program prompts:- Enter portfolio name:
         * This name can be any name for your portfolio. For example, college, retirement, etc.
         * Make sure you enter a different name than the one used in the above portfolio else the program will prompt an error.
      * If the given CSV file exists and the portfolio with the same isn’t already present then you’ll see a success message:- Portfolio created successfully + portfolioName (provided in the above step)
      * The program will again display the menu options. Please select option 3 to get the portfolio value on a specific date:- Get portfolio value for a specific date
         * This option displays the available portfolios. Please select the portfolio number you wish to get the value for after the message:- Select a portfolio whose value you wish see:
         * After selecting the portfolio number, the program asks for a date for which you want to calculate the value:- Enter the market date for calculating the portfolio value (Date format: yyyy-MM-dd): 
            * Enter the date supported by the program. (These are mentioned at the end of this document). If a date that is not supported by the application is entered, the user will be prompted with an error message.
      * The program will again display the menu options. Please select option 6 to get the cost basis of a portfolio:- Get cost basis of a portfolio on a specific date
         * This option displays the available portfolios. Please select the portfolio number you wish to get the value for after the message:- Select a portfolio to which you wish to get the cost basis value:
         * After selecting the portfolio number, the program asks for a date for which you want to calculate the value:- Enter the date for which you wish to get the cost basis of the portfolio (Date format: yyyy-MM-dd )
            * Enter the date supported by the program. 
            * You can enter 2022-10-20 to get the cost basis value of a portfolio provided above (PortfoliosWith2Stocks.csv)
            * You can also try getting the cost basis for 2022-09-10. 




* Invalid inputs
   1. Input file extension
      * The program supports CSV files only. If a file with an unsupported extension is provided, an error is displayed. 
   2. Fractional inputs
      * The program does not accept fractional input for stock quantity. 
      * An error message is shown if a fractional input is provided. 
   3. Arbitrarily large stock quantities
      * The stock quantity for a share must be less than Integer.MAX_VALUE (2147483647). 
   4. Date format for calculating portfolio value
      * If the date for getting a portfolio value isn’t provided in the specified format then the program shows an error message. 
   5. Invalid ticker symbol
      * The program supports 50 ticker symbols, if an invalid/ unsupported symbol is provided then the program gives an error. 




* List of stocks the program supports


S. No.
	Company Name
	Stock Ticker Symbol
	1
	APPLE INC.
	AAPL
	2
	MICROSOFT CORPORATION
	MSFT
	3
	ALPHABET INC.
	GOOG
	4
	AMAZON.COM, INC.
	AMZN
	5
	TESLA, INC.
	TSLA
	6
	BERKSHIRE HATHAWAY INC.,
	BRK.A
	7
	UNITEDHEALTH GROUP INCORPORATED
	UNH
	8
	EXXON MOBIL CORPORATION
	XOM
	9
	JOHNSON & JOHNSON
	JNJ
	10
	VISA INC.
	V
	11
	WALMART INC.
	WMT
	12
	JPMORGAN CHASE & CO.
	JPM
	13
	CHEVRON CORPORATION
	CVX
	14
	ELI LILLY AND COMPANY
	LLY
	15
	NVIDIA CORPORATION
	NVDA
	16
	THE PROCTER & GAMBLE COMPANY
	PG
	17
	MASTERCARD INCORPORATED.
	MA
	18
	THE HOME DEPOT, INC.
	HD
	19
	BANK OF AMERICA CORPORATION
	BAC
	20
	PFIZER INC.
	PFE
	21
	ABBVIE INC.
	ABBV
	22
	THE COCA-COLA COMPANY
	KO
	23
	MERCK & CO., INC.
	MRK
	24
	PEPSICO, INC.
	PEP
	25
	Meta Platforms, Inc.
	META
	26
	COSTCO WHOLESALE CORPORATION
	COST
	27
	ORACLE CORPORATION
	ORCL
	28
	THERMO FISHER SCIENTIFIC INC.
	TMO
	29
	MCDONALD'S CORPORATION
	MCD
	30
	Broadcom Inc.
	AVGO
	31
	THE WALT DISNEY COMPANY
	DIS
	32
	ACCENTURE PUBLIC LIMITED COMPANY
	ACN
	33
	CISCO SYSTEMS, INC.
	CSCO
	34
	DANAHER CORPORATION
	DHR
	35
	WELLS FARGO & COMPANY
	WFC
	36
	ABBOTT LABORATORIES
	ABT
	37
	BRISTOL-MYERS SQUIBB COMPANY
	BMY
	38
	SALESFORCE, INC.
	CRM
	39
	CONOCOPHILLIPS
	COP
	40
	VERIZON COMMUNICATIONS INC.
	VZ
	41
	NEXTERA ENERGY, INC.
	NEE
	42
	THE CHARLES SCHWAB CORPORATION
	SCHW
	43
	ADOBE INC.
	ADBE
	44
	LINDE PUBLIC LIMITED COMPANY
	LIN
	45
	TEXAS INSTRUMENTS INCORPORATED
	TXN
	46
	UNITED PARCEL SERVICE, INC.
	UPS
	47
	NIKE, INC.
	NKE
	48
	AMGEN INC.
	AMGN
	49
	Philip Morris International Inc.
	PM
	50
	MORGAN STANLEY
	MS
	



* Dates on which the portfolio value can be determined
   * The program supports dates in the following range. 
      * Year 2000 to Current Date
      * These dates don’t include Holidays, Saturdays, and Sundays.