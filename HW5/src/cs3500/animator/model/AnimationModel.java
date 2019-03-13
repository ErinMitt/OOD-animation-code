package cs3500.animator.model;

/**
 * An interface that represents an animation.
 * It allows the user to add and remove shapes (ellipses and rectangles)
 * and control their movements (location, size, color, timing).
 */
public interface AnimationModel extends ReadOnlyModel {
  /**
   * Set the top left corner and size of the canvas on which the animation is to be displayed.
   * @param x the leftmost x value
   * @param y the topmost y value
   * @param width the canvas width
   * @param height the canvas height
   * @throws IllegalArgumentException if the width or height is invalid
   */
  void setBounds(int x, int y, int width, int height);

  /**
   * Add a new ellipse that can be animated.
   *
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addEllipse(String name);

  /**
   * Add a new rectangle that can be animated.
   *
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addRectangle(String name);

  /**
   * Remove the shape with the given name from the animation, along with all of its motions.
   * @throws IllegalArgumentException if there is no shape with the given name
   */
  void deleteShape(String shapeName);

  /**
   * Add a new motion to the given shape.
   *
   * @param shapeName the name of the shape to which a new motion will be added
   * @param time the time of the new motion
   * @param x the new motion's x coordinate
   * @param y the new motion's y coordinate
   * @param width the new motion's width
   * @param height the new motion's height
   * @param red the new motion's color's R component
   * @param green the new motion's color's B component
   * @param blue the new motion's color's G component
   * @throws IllegalArgumentException if there is no shape with the given name, if the time is less
   *                                  than 1, or if the time is before that shape's last movement
   *                                  ends.
   */
  void addMotion(String shapeName, int time, int x, int y, int width,
                 int height, int red, int green, int blue);

  /**
   * Tell the given shape to remain motionless until the given time.
   *
   * @param shape the name of the shape to have its last movement extended
   * @param time the time until when the shape should stay still
   * @throws IllegalArgumentException if there is no such shape or if the time occurs before the
   *                                  shape's last motion
   * @throws IllegalStateException    if the given shape has no motions
   */
  void extend(String shape, int time);

  /**
   * Remove the last motion of the given shape.
   *
   * @param shapeName the shape from which the last motion will be deleted
   * @throws IllegalArgumentException if the shape does not exist
   * @throws IllegalStateException    if the shape has no motions
   */
  void deleteLastMotion(String shapeName);
}
