/**
 *
 */
class Motion {
  private int time;
  private int x;
  private int y;
  private int width;
  private int height;
  private int red;
  private int green;
  private int blue;

  /**
   *
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
}
