package model;

import java.text.ParseException;

/**
 * Visitor for Portfolio.
 * @param <T> Generic Type.
 */
public interface PortfolioVisitor<T> {

  /**
   * Helper method to apply function.
   * @param p Portfolio.
   * @return function applied returns a generic type.
   * @throws ParseException error on parsing date.
   */
  T apply(IPortfolio p) throws ParseException;

}
