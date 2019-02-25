import java.util.List;

/**
 *
 */
public interface AnimationModel {
  /**
   * Add a new ellipse that can be animated.
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addEllipse(String name);

  /**
   * Add a new rectangle that can be animated.
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addRectangle(String name);

  /**
   *
   * @param shapeName
   * @throws IllegalArgumentException if there is no shape with the given name
   */
  void deleteShape(String shapeName);

  /**
   *
   * @param shapeName
   * @param time
   * @param x
   * @param y
   * @param width
   * @param height
   * @param red
   * @param green
   * @param blue
   * @throws IllegalArgumentException if there is no shape with the given name, or if the time is
   * before that shape's last movement ends.
   */
  void addMotion(String shapeName, int time, int x, int y, int width,
                 int height, int red, int green, int blue);

  /**
   *
   * @param shapeName
   */
  void deleteLastMotion(String shapeName);

  /**
   * Return a list of names of all shapes available in the animation.
   * @return the list of shape names.
   */
  List<String> getShapes();
}
