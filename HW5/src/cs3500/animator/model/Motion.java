package cs3500.animator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A class representing the status of a shape at a moment in time.
 * Each Motion describes the shape's location, size, and color.
 * Motions are immutable.
 */
public class Motion {
  public static final int START_TICK = 0;

  private final int time;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int red;
  private final int green;
  private final int blue;
  private final int rotation;

  /**
   * Build a moment. Rotation defaults to 0.
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
  public Motion(int time, int x, int y, int width, int height, int red, int green, int blue) {
    this(time, x, y, width, height, red, green, blue, 0);
  }

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
   * @param rotation the angle of rotation
   */
  public Motion(int time, int x, int y, int width, int height,
                int red, int green, int blue, int rotation) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive nonzero integers, "
              + "given " + width + " & " + height);
    }
    if (time < START_TICK) {
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
    this.rotation = rotation;
  }

  /**
   * Build a "default" motion at the given time - a valid Motion at the given time.
   * @param time the time of the default motion
   * @return the new default motion
   * @throws IllegalArgumentException if the time is invalid
   */
  public static Motion defaultMotion(int time) {
    return new Motion(time, 0, 0, 1, 1, 0, 0, 0, 0);
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
  public String display() {
    ArrayList<Integer> values = new ArrayList<>(
            Arrays.asList(time, x, y, width, height, red, green, blue));
    return values.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" "));
  }

  /**
   * Create a new Motion identical to this one except that it occurs at a later time. This method is
   * meant to create a Motion after this one but is capable of creating Motions that have an earlier
   * or identical time.
   *
   * @param time the time for the new Motion
   * @return the new Motion created
   */
  public Motion extend(int time) {
    return new Motion(time, x, y, width, height, red, green, blue, rotation);
  }

  /**
   * Return the time at which the motion occurs.
   *
   * @return time
   */
  public int getTime() {
    return time;
  }

  /**
   * Getter for x.
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Getter for y.
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * Getter for width.
   * @return width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Getter for height.
   * @return height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Getter for red.
   * @return red
   */
  public int getRed() {
    return red;
  }

  /**
   * Getter for green.
   * @return green
   */
  public int getGreen() {
    return green;
  }

  /**
   * Getter for blue.
   * @return blue
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Getter for rotation.
   * @return rotation
   */
  public int getRotation() {
    return rotation;
  }
}
