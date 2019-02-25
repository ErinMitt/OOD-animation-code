import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing the status of a shape at a moment in time.
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
   * @param time
   * @param x
   * @param y
   * @param width
   * @param height
   * @param red
   * @param green
   * @param blue
   */
  public Motion(int time, int x, int y, int width, int height, int red, int green, int blue) {
    this.time = time;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Create a human-readable String that displays all the information it contains
   * @return
   */
  public String display(String shapeName) {
    ArrayList<Integer> values
            = new ArrayList<>(Arrays.asList(time, x, y, width, height, red, green, blue));
    values.stream().



    return "";
  }
}
