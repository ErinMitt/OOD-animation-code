package cs3500.animator.controller;

/**
 * This class represents all of the functionality that an EditorAnimationView requires.
 */
public interface Features {
  /**
   * Play the animation if paused, pause the animation if playing.
   */
  void togglePlay();

  /**
   * Loop the animation if it's not looping, unloop the animation if it's looping.
   */
  void toggleLoop();

  /**
   * Rewind the animation to the start.
   */
  void rewind();

  /**
   * Move one framw forward.
   */
  void stepForward();

  /**
   * Move one frame backward.
   */
  void stepBackwards();
}
