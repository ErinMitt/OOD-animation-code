package cs3500.animator.model;

/**
 * An interface that represents an animation.
 * It allows the user to add and remove shapes (ellipses and rectangles)
 * and control their movements (location, size, color, timing).
 */
public interface AnimationModel extends ReadOnlyModel {
  /**
   * Set the top left corner and size of the canvas on which the animation is to be displayed.
   * @param x the left padding
   * @param y the top padding
   * @param width the canvas width
   * @param height the canvas height
   * @throws IllegalArgumentException if the width or height is invalid
   */
  void setBounds(int x, int y, int width, int height);

  /**
   * Add a layer by the given name.
   * @param layerName the new layer's name.
   * @throws IllegalArgumentException if there is already a layer by the given name.
   */
  void addLayer(String layerName);

  /**
   * Delete the layer of the given name.
   *
   * @param layerName the name of the layer to be deleted
   * @throws IllegalArgumentException if there is no layer by the given name
   */
  void deleteLayer(String layerName);

  /**
   * Move the layer of the given name from its current location to a new position.
   * @param layerName the layer to be moved
   * @param position the new position
   * @throws IllegalArgumentException if there is no layer from the given name
   *     or if the new position is invalid
   */
  void moveLayer(String layerName, int position);

  /**
   * Add a new ellipse that can be animated.
   *
   * @param layer the layer on which the shape is found
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addEllipse(String layer, String name);

  /**
   * Add a new rectangle that can be animated.
   *
   * @param layer the layer on which the shape is found
   * @param name the shape's name
   * @throws IllegalArgumentException if the shape doesn't have a unique name
   */
  void addRectangle(String layer, String name);

  /**
   * Remove the shape with the given name from the animation, along with all of its motions.
   * @param layer the layer on which the shape is found
   * @param shapeName the shape's name
   * @throws IllegalArgumentException if there is no shape with the given name
   */
  void deleteShape(String layer, String shapeName);

  /**
   * Add a new motion to the given shape. Rotation defaults to 0.
   *
   * @param layer the layer on which the shape is found
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
  void addMotion(String layer, String shapeName, int time, int x, int y, int width,
                 int height, int red, int green, int blue);

  /**
   * Add a new motion to the given shape.
   *
   * @param layer the layer on which the shape is found
   * @param shapeName the name of the shape to which a new motion will be added
   * @param time the time of the new motion
   * @param x the new motion's x coordinate
   * @param y the new motion's y coordinate
   * @param width the new motion's width
   * @param height the new motion's height
   * @param red the new motion's color's R component
   * @param green the new motion's color's B component
   * @param blue the new motion's color's G component
   * @param rotation the new motion's rotation
   * @throws IllegalArgumentException if there is no shape with the given name, if the time is less
   *                                  than 1, or if the time is before that shape's last movement
   *                                  ends.
   */
  void addMotion(String layer, String shapeName, int time, int x, int y, int width,
                 int height, int red, int green, int blue, int rotation);

  /**
   * Replace the motion at the given time with a motion of the given parameters.
   * Rotation defaults to 0.
   * @param layer the layer on which the shape is found
   * @param shape the shape name
   * @param time the time
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   * @param red the r component of the color
   * @param green the g component of the color
   * @param blue the b component of the color
   * @throws IllegalArgumentException if the shape does not exist, if there is no motion at the
   *     given time, or if the given motion parameters are invalid
   */
  void editMotion(String layer, String shape, int time, int x, int y, int width, int height,
                  int red, int green, int blue);

  /**
   * Replace the motion at the given time with a motion of the given parameters.
   * @param layer the layer on which the shape is found
   * @param shape the shape name
   * @param time the time
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the width
   * @param height the height
   * @param red the r component of the color
   * @param green the g component of the color
   * @param blue the b component of the color
   * @param rotation the rotation
   * @throws IllegalArgumentException if the shape does not exist, if there is no motion at the
   *     given time, or if the given motion parameters are invalid
   */
  void editMotion(String layer, String shape, int time, int x, int y, int width, int height,
                  int red, int green, int blue, int rotation);

  /**
   * Delete the motion of the given shape at the given time.
   * @param layer the layer on which the shape is found
   * @param shape the shape name
   * @param time the time of the motion
   * @throws IllegalArgumentException if there is no shape by the given name or if there is no
   *     motion at the given time
   */
  void deleteMotion(String layer, String shape, int time);

  /**
   * Remove the last motion of the given shape.
   *
   * @param layer the layer on which the shape is found
   * @param shapeName the shape from which the last motion will be deleted
   * @throws IllegalArgumentException if the shape does not exist
   * @throws IllegalStateException    if the shape has no motions
   */
  void deleteLastMotion(String layer, String shapeName);
}
