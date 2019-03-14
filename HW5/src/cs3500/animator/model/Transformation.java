package cs3500.animator.model;

public class Transformation {
  private final Motion start;
  private final Motion end;

  public Transformation(Motion start, Motion end) {
    if (start.getTime() > end.getTime()) {
      throw new IllegalArgumentException(
              "The time at the start must not come after the time at the end");
    }
    this.start = start;
    this.end = end;
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
    return (initial * ((end.getTime() - tick) / (end.getTime() - start.getTime())))
            + (last * ((tick - start.getTime()) / (end.getTime() - start.getTime())));
  }
}
