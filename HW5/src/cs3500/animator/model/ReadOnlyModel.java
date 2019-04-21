package cs3500.animator.model;

import java.util.List;

/**
 * A class representing a Model with only getters that cannot be mutated.
 */
public interface ReadOnlyModel {
  /**
   * Return a list of names of all layers in the order of their
   *
   * @return the list of layer names.
   */
  List<String> getLayers();

  /**
   * Return a list of names of all shapes in the given layer.
   *
   * @param layer the name of the layer
   * @return the list of shape names
   * @throws IllegalArgumentException if no such layer exists
   */
  List<String> getShapes(String layer);

  /**
   * Describe the animation using a human-readable String. The String will follow this format:
   * # declares a layer named layer1
   * layer layer1
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
   * [empty line]
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

  /**
   * Get the leftmost x value of the canvas.
   * @return x
   */
  int getX();

  /**
   * Get the topmost y value of the canvas.
   * @return y
   */
  int getY();

  /**
   * Get the canvas width.
   * @return the canvas width
   */
  int getWidth();

  /**
   * Get the canvas height.
   * @return the canvas height
   */
  int getHeight();

  /**
   * Find the transformation of the shape in progress at the given tick.
   * @param layer the layer on which the shape is found
   * @param shapeName the shape's name
   * @param tick the tick of the desired Transformation
   * @return the transformation
   * @throws IllegalArgumentException if there is no such shape name ir if the shape is not
   *     present on the screen during the given tick
   */
  Transformation getTransformationAt(String layer, String shapeName, int tick);

  /**
   * Return a list of all of the given shape's motions.
   * @param layer the layer on which the shape is found
   * @param shapeName the shape's name
   * @return a list of all keyframes of the shape
   * @throws IllegalArgumentException if there is no such shape
   */
  List<Motion> getMotions(String layer, String shapeName);

  /**
   * Return a string (either ellipse or rectangle) representing the shape type.
   * @param layer the layer on which the shape is found
   * @param shapeName the name of the shape
   * @return the shape type
   */
  String getShapeType(String layer, String shapeName);
}
