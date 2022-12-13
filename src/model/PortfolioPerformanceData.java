package model;

import java.time.LocalDate;
import java.util.List;

/**
 * This class implements the interface IPortfolioPerformanceData, and represents a
 * portfolio's performance by considering the portfolio's value over a range of time.
 */
public class PortfolioPerformanceData implements IPortfolioPerformanceData {
  private final String portfolioName;
  private final List<PerformanceDataFrame> performanceData;
  LocalDate startDate;
  LocalDate endDate;

  /**
   * Create a new PortfolioPerformanceData object, given the portfolio name, the performance data
   * list, the start date of the performance time frame, and the end date of the performance
   * time frame.
   *
   * @param portfolioName   the name of the portfolio's whose performance is implied here
   * @param performanceData the performance data of the given portfolio
   * @param startDate       the start date of the performance time frame
   * @param endDate         the end date of the performance time frame
   */
  public PortfolioPerformanceData(String portfolioName,
                                  List<PerformanceDataFrame> performanceData,
                                  LocalDate startDate,
                                  LocalDate endDate) {
    this.portfolioName = portfolioName;
    this.performanceData = performanceData;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public String getPortfolioName() {
    return portfolioName;
  }

  @Override
  public List<PerformanceDataFrame> getPerformanceData() {
    return performanceData;
  }

  @Override
  public LocalDate getEndDate() {
    return endDate;
  }

  @Override
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * This class represents a data frame for a portfolio's performance over time.
   * This class is meant to be used for the IPortfolioPerformanceData objects.
   */
  public static class PerformanceDataFrame {
    String timeFrame;
    Double portfolioValue;

    /**
     * Get the time frame for this data frame.
     * The time frame represents the duration for which this data frame's portfolio value is
     * computed.
     *
     * @return the time frame for this data frame as a string
     */
    public String getTimeFrame() {
      return timeFrame;
    }

    /**
     * Get the portfolio's value for this data frame's time frame.
     *
     * @return the portfolio's value for this data frame's time frame as a double value
     */
    public Double getPortfolioValue() {
      return portfolioValue;
    }

    /**
     * Create a new performance data frame, given the time frame, and the portfolio value for
     * the time frame.
     *
     * @param timeFrame      the time frame for this data frame
     * @param portfolioValue the portfolio's value for this data frame's time frame
     */
    public PerformanceDataFrame(String timeFrame, Double portfolioValue) {
      this.timeFrame = timeFrame;
      this.portfolioValue = portfolioValue;
    }
  }
}
