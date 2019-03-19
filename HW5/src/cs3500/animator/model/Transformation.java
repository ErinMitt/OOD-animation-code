package cs3500.animator.model;

/**
 * A class representing a shape's smooth transition
 * from it's state at the start to its state at the end.
 */
public class Transformation {
  private final Motion start;
  private final Motion end;

  /**
   * Build a transformation representing a smooth movement of a shape from one state to another.
   * @param start the start position
   * @param end the end position
   */
  public Transformation(Motion start, Motion end) {
    if (start.getTime() > end.getTime()) {
      throw new IllegalArgumentException(
              "The time at the start must not come after the time at the end");
    }
    this.start = start;
    this.end = end;
  }

  /**
   * Calculate the shape's state at the given time and return that information
   * packaged as a Motion.
   * @param tick the time to be tested
   * @return the state at time = tick
   */
  public Motion getStateAt(int tick) {
    return new Motion(tick,
            stateAtTime(tick, start.getX(), end.getX()),
            stateAtTime(tick, start.getY(), end.getY()),
            stateAtTime(tick, start.getWidth(), end.getWidth()),
            stateAtTime(tick, start.getHeight(), end.getHeight()),
            stateAtTime(tick, start.getRed(), end.getRed()),
            stateAtTime(tick, start.getGreen(), end.getGreen()),
            stateAtTime(tick, start.getBlue(), end.getBlue()));
  }

  /**
   * Determine the state of an integer at time tick if it switches smoothly between
   * initial (at the time of start's tick) and last (at the time of end's tick).
   * @param tick the time for which the state should be calculated
   * @param initial the initial state
   * @param last the final state
   * @return the state at the given time
   * @throws IllegalArgumentException if the given time is outside the transformation's bounds
   */
  private int stateAtTime(int tick, int initial, int last) {
    if (tick < start.getTime() || tick > end.getTime()) {
      throw new IllegalArgumentException(
              "Cannot calculate state outside of the transformation's boundaries");
    }
    // if there is no change in time between the two motions, return the initial state
    if (start.getTime() == end.getTime()) {
      return initial;
    }
    return (int) Math.round(
            (double) (initial * ((end.getTime() - tick)
                    / (double) (end.getTime() - start.getTime())))
            + ((double) last * ((tick - start.getTime())
                    / (double) (end.getTime() - start.getTime()))));
  }
}
