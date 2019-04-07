package cs3500.animator.provider.view;

import java.util.Map;

/**
 * Interface of the Model for our Animation.
 */

public interface IModel {

  /**
   * Adds a new Shape.
   *
   * @param name name of the shape
   * @param type type of the shape (ellipse, rectangle, etc.)
   */

  void addShape(String name, ShapeType type);

  /**
   * Makes the change to any parameters of the shape in the specific time frame.
   *
   * @param name name of the shape
   * @param t1   starting time of the shape
   * @param x1   starting x-position of the shape
   * @param y1   starting y-position of the shape
   * @param w1   starting width of the shape
   * @param h1   starting height of the shape
   * @param r1   starting red color-value of the shape
   * @param g1   starting green color-value of the shape
   * @param b1   starting blue color-value of the shape
   * @param t2   ending time of the shape
   * @param x2   ending x-position of the shape
   * @param y2   ending y-position of the shape
   * @param w2   ending width of the shape
   * @param h2   ending height of the shape
   * @param r2   ending red color-value of the shape
   * @param g2   ending green color-value of the shape
   * @param b2   ending blue color-value of the shape
   */

  void makeChange(String name, int t1, int x1, int y1, int w1, int h1,
                  int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2,
                  int g2, int b2);

  /**
   * Gets the Map consisting of the shapes and the names of them.
   *
   * @return the shapes
   */

  Map<String, Shape> getShapes();

  /**
   * Gets the array of the parameters of the screen dimensions.
   *
   * @return the array of parameters
   */

  int[] getScreenDimensions();

  /**
   * Updates the parameters of the canvas.
   *
   * @param dim array of the dimensions of the canvas
   */

  void updateCanvas(int[] dim);
}
