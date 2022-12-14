package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import model.stockdatastore.IStockDataStore;

/**
 * Visitor class for PortfolioBalance.
 * @param <T> Generic type.
 */
public class PortfolioBalanceVisitor<T> implements PortfolioVisitor<T> {

  private final LocalDate d;
  private final IStockDataStore ds;
  private final Map<String, Double> balancePercent;
  private final Comparator<IStock> compareByDate;
  private final Function<IPortfolio, T> ff;

  /**
   * Constructor for Balance Portfolio Visitor class.
   * It initializes date, datastore, stock percentages and function.
   * @param d date
   * @param ds datastore
   * @param bp stock percentages
   * @param fp function
   */
  public PortfolioBalanceVisitor(LocalDate d, IStockDataStore ds, Map<String, Double> bp,
                                 Function<IPortfolio, T> fp) {
    this.d = d;
    this.ds = ds;
    this.balancePercent = bp;
    this.ff = fp;
    this.compareByDate = new Comparator<IStock>() {
      public int compare(IStock m1, IStock m2) {
        try {
          return new SimpleDateFormat("yyyy-MM-dd").parse(m1.getPurchaseDate())
                  .compareTo(
                          new SimpleDateFormat("yyyy-MM-dd").parse(m2.getPurchaseDate()));
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public T apply(IPortfolio p) throws ParseException {
    System.out.println("came in this to balance...");
    DateTimeFormatter ff = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println("formatted date.....");

    Map<String, Double> stocks = new HashMap<>();
    for (IStock is : p.getStocksList()) {
      if (LocalDate.parse(is.getPurchaseDate(), ff).compareTo(d) <= 0) {
        if (stocks.containsKey(is.getStockName())) {
          stocks.put(is.getStockName(), stocks.get(is.getStockName()) + is.getStockQuantity());
        } else {
          stocks.put(is.getStockName(), (double) is.getStockQuantity());
        }
      }
    }

    System.out.println("consolidated the stocks...");
    System.out.println(stocks);

    Double totalMoney = 0d;
    Map<String, Double> stockMoney = new HashMap<>();
    for (Map.Entry<String, Double> stock : stocks.entrySet()) {
      double cost = this.ds.getStockValue(stock.getKey(), this.d) * stock.getValue();
      totalMoney += cost;
      stockMoney.put(stock.getKey(), cost);
    }

    System.out.println("converted stocks to money...");
    System.out.println(this.balancePercent);
    System.out.println(totalMoney);
    System.out.println(stockMoney);

    Map<String, Double> opsPerformed = new LinkedHashMap<>();
    List<String> operation = new ArrayList<>();
    for (Map.Entry<String, Double> stock : this.balancePercent.entrySet()) {
      Double stockPercent = (stockMoney.get(stock.getKey()) / totalMoney) * 100;
      System.out.println(stockPercent);
      double worth = 0d;
      if (stockPercent.equals(stock.getValue())) {
        continue;
      } else if (stockPercent < stock.getValue()) {
        worth = ((stock.getValue() - stockPercent) / 100) * totalMoney;
        operation.add("buy");
      } else {
        worth = ((stockPercent - stock.getValue()) / 100) * totalMoney;
        operation.add("sell");
      }
      String vv = String.format("%.2f", worth / this.ds.getStockValue(stock.getKey(), this.d));
      opsPerformed.put(stock.getKey(), Double.valueOf(vv));
    }

    System.out.println("got the operations to be performed");
    System.out.println(opsPerformed);
    System.out.println(operation);


    List<IStock> sts = new ArrayList<>(p.getStocksList());
    List<String> test = new ArrayList<String>(opsPerformed.keySet());


    sts.sort(this.compareByDate);

    for (int i = 0; i < test.size(); i++) {
      if (operation.get(i).equalsIgnoreCase("buy")) {
        IStock temp = new Stock(test.get(i), opsPerformed.get(test.get(i)).floatValue(),
                this.d.format(ff));
        sts.add(temp);
      } else {

        IStock temp = new Stock(test.get(i), -1 * opsPerformed.get(test.get(i))
                .floatValue(), this.d.format(ff));
        sts.add(temp);
      }
    }

    sts.sort(this.compareByDate);


    IPortfolio rv = new Portfolio(p.getPortfolioName());
    for (IStock xx :
            sts) {
      rv.addStock(this.ds, xx, 0);
    }
    return this.ff.apply(rv);

  }
}
