import guicontroller.GUIController;
import guiview.GUIView;
import guiview.IGUIView;
import model.IModel;
import model.PortfolioModel;

/**
 * This is starter class of the application. The sole purpose of this class is to initialize
 * the controller, model and view, and start the controller in the main method.
 */
public class StocksStarter {
  /**
   * The main method which is called when the program starts. This method contains the logic
   * for initializing the controller, model and view, and calling the starter method of the
   * controller to start the application.
   *
   * @param args the command line arguments required for the application's execution
   */
  public static void main(String[] args) {
    IModel model = new PortfolioModel();
//    IView view = new PortfolioView(System.out);
//    IController controller = new PortfolioController(model, view, System.in);
//    controller.start();

    GUIController controller = new GUIController(model);
    IGUIView view = new GUIView("Portfolio");
    controller.setView(view);
  }
}
