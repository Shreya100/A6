package model;

import java.text.ParseException;

public interface PortfolioVisitor<T> {

  T apply(IPortfolio p) throws ParseException;

}
