package controller;

/**
 * This interface defines the signature of a controller which uses the model and provides the view
 * with information to be used.
 * The controller acts as a middleware between the model and view.
 */
public interface IController {

  /**
   * Start the execution of the controller, and hence start the application.
   */
  void start();
}
