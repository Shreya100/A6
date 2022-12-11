package model;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface represents a portfolio's performance data for a given date range.
 * This interface provides methods for getting the start and end dates of the performance rate,
 * and also has a getter for the list of performance data of the portfolio.
 */
public interface IPortfolioPerformanceData {
  /**
   * Get the name of portfolio whose performance data is contained in this object.
   *
   * @return the portfolio name related to this performance data
   */
  String getPortfolioName();

  /**
   * Get the performance data of the portfolio specified by this object.
   * The data is represented as a list of PerformanceDataFrame.
   *
   * @return a list of PerformanceDataFrame
   */
  List<PortfolioPerformanceData.PerformanceDataFrame> getPerformanceData();

  /**
   * Get the start date of this performance's time frame.
   *
   * @return the start date of this performance's time frame
   */
  LocalDate getStartDate();

  /**
   * Get the end date of this performance's time frame.
   *
   * @return the end date of this performance's time frame
   */
  LocalDate getEndDate();
}
