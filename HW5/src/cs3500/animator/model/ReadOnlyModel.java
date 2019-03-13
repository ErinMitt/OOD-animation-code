package cs3500.animator.model;

import java.util.List;

public interface ReadOnlyModel {

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
   * @return thw canvas width
   */
  int getWidth();

  /**
   * Get the canvas height.
   * @return the canvas height
   */
  int getHeight();
}
