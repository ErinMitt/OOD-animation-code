import java.util.List;

/**
 * An interface that represents an animation.
 * It allows the user to add and remove shapes (ellipses and rectangles)
 * and control their movements (location, size, color, timing).
 */
public interface AnimationModel {
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

  /**
   * Return a list of names of all shapes available in the animation.
   *
   * @return the list of shape names.
   */
  List<String> getShapes();

  /**
   * Describe the animation using a human-readable String. The String will follow this format:
   * # declares a rectangle shape named R
   * shape R rectangle
   * # describes the motions of shape R, between two moments of animation:
   * # t == tick
   * # (x,y) == position
   * # (w,h) == dimensions
   * # (r,g,b) == color (with values between 0 and 255)
   * #                  start                           end
   * #        --------------------------    ----------------------------
   * #        t  x   y   w  h   r   g  b    t   x   y   w  h   r   g  b
   * motion R 1  200 200 50 100 255 0  0    10  200 200 50 100 255 0  0
   * motion R 10 200 200 50 100 255 0  0    50  300 300 50 100 255 0  0
   * motion R 50 300 300 50 100 255 0  0    51  300 300 50 100 255 0  0
   * motion R 51 300 300 50 100 255 0  0    70  300 300 25 100 255 0  0
   * motion R 70 300 300 25 100 255 0  0    100 200 200 25 100 255 0  0
   *
   * shape C ellipse
   * motion C 6  440 70 120 60 0 0 255      20  440 70  120 60 0 0 255
   * motion C 20 440 70 120 60 0 0 255      50  440 250 120 60 0 0 255
   * motion C 50 440 250 120 60 0 0 255     70  440 370 120 60 0 170 85
   * motion C 70 440 370 120 60 0 170 85    80  440 370 120 60 0 255  0
   * motion C 80 440 370 120 60 0 255  0    100 440 370 120 60 0 255  0
   * Lines beginning with # are informative and will not be part of the string.
   *
   * @return a string describing the animation
   */
  String displayAnimation();
}
