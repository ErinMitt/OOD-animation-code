package cs3500.animator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A class representing the status of a shape at a moment in time.
 * Each Motion describes the shape's location, size, and color.
 * Motions are immutable.
 */
class Motion {
  private final int time;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int red;
  private final int green;
  private final int blue;

  /**
   * Build a moment.
   *
   * @param time the moment's time
   * @param x the moment's x coordinate
   * @param y the moment's y coordinate
   * @param width the moment's width
   * @param height the moment's height
   * @param red the R component of the moment's color
   * @param green the G component of the moment's color
   * @param blue the B component of the moment's color
   */
  Motion(int time, int x, int y, int width, int height, int red, int green, int blue) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive nonzero integers, "
              + "given " + width + " & " + height);
    }
    if (time < 1) {
      throw new IllegalArgumentException("Time must be a positive integer, given " + time);
    }
    this.time = time;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.red = regularizeColor(red);
    this.green = regularizeColor(green);
    this.blue = regularizeColor(blue);
  }

  /**
   * If a given integer is not usable as a color (greater than 255 or less than 0), then return the
   * closest possible int that is a legal color.
   *
   * @param color the uncorrected color
   * @return the corrected color
   */
  private int regularizeColor(int color) {
    return Math.max(0, Math.min(255, color));
  }

  /**
   * Create a human-readable String that displays all the information the motion contains. The
   * resulting string will be in the format: "[time] [x] [y] [width] [height] [red] [green]
   * [blue]".
   *
   * @return the display string
   */
  String display() {
    ArrayList<Integer> values
            = new ArrayList<>(Arrays.asList(time, x, y, width, height, red, green, blue));
    return values.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" "));
  }

  /**
   * Return the time at which the motion occurs.
   *
   * @return time
   */
  int getTime() {
    return time;
  }

  /**
   * Create a new Motion identical to this one except that it occurs at a later time. This method is
   * meant to create a Motion after this one but is capable of creating Motions that have an earlier
   * or identical time.
   *
   * @param time the time for the new Motion
   * @return the new Motion created
   */
  Motion extend(int time) {
    return new Motion(time, x, y, width, height, red, green, blue);
  }
}
