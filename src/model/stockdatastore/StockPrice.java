package model.stockdatastore;

import java.time.LocalDate;

/**
 * The StockPrice class implements the IStockPrice interface.
 * This class provides an implementation that represents a stock's market price for a given date in
 * the market.
 */
public class StockPrice implements IStockPrice {
  private final LocalDate marketDate;
  private final double marketPrice;

  /**
   * Create a new StockPrice object initialized with the stock's market price and market date.
   *
   * @param marketDate  the stock's market date
   * @param marketPrice the stock's market price for the given market date
   */
  public StockPrice(LocalDate marketDate, double marketPrice) {
    this.marketDate = marketDate;
    this.marketPrice = marketPrice;
  }

  public LocalDate getMarketDate() {
    return marketDate;
  }

  public double getMarketPrice() {
    return marketPrice;
  }
}
